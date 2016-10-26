/*
 * Copyright 2015 Soulwolf Ching
 * Copyright 2015 The Android Open Source Project for WebViewJavascriptBridge
 * 
 * Licens ed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.soulwolf.wvjsbridge;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * author: EwenQin
 * since : 2016/10/23 下午1:02.
 */
public final class WJBridgeProvider implements WebViewJavascriptBridge {

    public static WJBridgeProvider newInstance(WJWebLoader webLoader) {
        return new WJBridgeProvider(WJBridgeUtils.checkNoNull(webLoader, "WJWebLoader not NULL!"));
    }

    public static final String SCRIPT_NAME = "WebViewJavascriptBridge.js";

    private WeakReference<WJWebLoader> mWebLoader;

    private final AtomicLong mUniqueId = new AtomicLong();

    private Map<String, WJCallbacks> mResponseCallbacks = new HashMap<>();
    private Map<String, WJBridgeHandler> mMessageHandlers = new HashMap<>();

    private WJBridgeHandler mDefaultHandler = new WJDefaultHandler();

    private List<WJMessage> mStartupMessages = new ArrayList<>();

    private WJBridgeProvider(@NonNull WJWebLoader webLoader) {
        this.mWebLoader = new WeakReference<WJWebLoader>(webLoader);
    }

    public boolean shouldOverrideUrlLoading(WJWebLoader loader, String url){
        url = WJBridgeUtils.decodeUrl(url);
        if(url.startsWith(WJBridgeUtils.WJ_BRIDGE_LOADED)){
            WJBridgeUtils.webViewLoadLocalJs(loader,SCRIPT_NAME);
            return true;
        }else if(url.startsWith(WJBridgeUtils.WJ_RETURN_DATA)){
            this.handlerReturnData(url);
            return true;
        }else if(url.startsWith(WJBridgeUtils.WJ_PROTOCOL_SCHEME)){
            this.flushMessageQueue();
            return true;
        }
        return false;
    }

    public void onPageFinished(){
        if(mStartupMessages != null){
            for(WJMessage message:mStartupMessages){
                this.dispatchMessage(message);
            }
            this.setStartupMessages(null);
        }
    }

    private void handlerReturnData(String url) {
        String functionName = WJBridgeUtils.getFuncNameFromUrl(url);
        WJCallbacks f = mResponseCallbacks.remove(functionName);
        String data = WJBridgeUtils.getReturnDataFromUrl(url);
        if (f != null) {
            f.onCallback(data);
        }
    }

    @Override
    public void send(String data) {
        this.send(data, null);
    }

    @Override
    public void send(String data, WJCallbacks callbacks) {
        this._pushMessage(null, data, callbacks);
    }

    public List<WJMessage> startupMessages() {
        return mStartupMessages;
    }

    @Override
    public void setStartupMessages(List<WJMessage> messages) {
        this.mStartupMessages = messages;
    }

    @Override
    public void loadUrl(String jsUrl, WJCallbacks callbacks) {
        this.mResponseCallbacks.put(WJBridgeUtils.parseFuncName(jsUrl), callbacks);
        this._loadUrl(jsUrl);
    }

    private void _loadUrl(String url) {
        if (mWebLoader.get() != null) {
            this.mWebLoader.get().loadUrl(url);
        }
    }

    private void _pushMessage(String handlerName, String data, WJCallbacks callbacks) {
        WJMessage message = new WJMessage();
        message.data = data;
        message.handlerName = handlerName;
        if (callbacks != null) {
            String callbackId = WJBridgeUtils.formatCallbackId(mUniqueId.incrementAndGet());
            message.callbackId = callbackId;
            this.mResponseCallbacks.put(callbackId, callbacks);
        }
        this.queueMessage(message);
    }

    private void queueMessage(WJMessage message) {
        if(mStartupMessages != null){
            this.mStartupMessages.add(message);
        }else {
            this.dispatchMessage(message);
        }
    }

    private void dispatchMessage(WJMessage message) {
        String messageJson = message.toJson();
        //escape special characters for json string
        messageJson = messageJson.replaceAll("(\\\\)([^utrn])", "\\\\\\\\$1$2")
                .replaceAll("(?<=[^\\\\])(\")", "\\\\\"");
        String javascriptCommand = WJBridgeUtils.formatJsDispatchMessage(messageJson);
        if(Thread.currentThread() == Looper.getMainLooper().getThread()){
            this._loadUrl(javascriptCommand);
        }
        L.i("WJBridge send message.[%s]", javascriptCommand);
    }

    private void flushMessageQueue(){
        if(Thread.currentThread() != Looper.getMainLooper().getThread()){
            L.i("Call flushMessageQueue no in main thread!");
            return;
        }
        this.loadUrl(WJBridgeUtils.JS_FETCH_QUEUE, new WJCallbacks() {
            @Override
            public void onCallback(String data) {
                List<WJMessage> messages = WJMessage.ofJsonArray(data);
                if (messages == null || messages.isEmpty()) {
                    return;
                }
                for (WJMessage message : messages) {
                    if (!TextUtils.isEmpty(message.responseId)) {
                        WJCallbacks callbacks = mResponseCallbacks.remove(message.responseId);
                        if (callbacks != null) {
                            callbacks.onCallback(message.responseData);
                        }
                    } else {
                        WJCallbacks callbacks = new SimpleCallbacks();
                        final String callbackId = message.callbackId;
                        if (!TextUtils.isEmpty(callbackId)) {
                            callbacks = new WJCallbacks() {
                                @Override
                                public void onCallback(String data) {
                                    WJMessage m = new WJMessage();
                                    m.responseId = callbackId;
                                    m.responseData = data;
                                    queueMessage(m);
                                }
                            };
                        }
                        WJBridgeHandler handler = mMessageHandlers.get(message.handlerName);
                        if (handler == null) {
                            handler = mDefaultHandler;
                        }
                        if (handler != null) {
                            handler.handler(message.data, callbacks);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void callHandler(String handlerName, String data, WJCallbacks callbacks) {
        this._pushMessage(handlerName, data, callbacks);
    }

    @Override
    public void registerHandler(String handlerName, WJBridgeHandler handler) {
        if (handler != null) {
            this.mMessageHandlers.put(handlerName, handler);
        }
    }

    @Override
    public void setDefaultHandler(WJBridgeHandler handler) {
        if (handler != null) {
            this.mDefaultHandler = handler;
        }
    }

    public WJWebLoader getLoader(){
        return mWebLoader.get();
    }

    public void destroy(){
        this.mDefaultHandler = null;
        this.mResponseCallbacks.clear();
        this.mResponseCallbacks = null;
        this.mMessageHandlers.clear();
        this.mMessageHandlers = null;
        this.mWebLoader = null;
    }
}
