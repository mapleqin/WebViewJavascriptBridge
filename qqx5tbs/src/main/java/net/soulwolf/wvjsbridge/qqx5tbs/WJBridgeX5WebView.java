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
package net.soulwolf.wvjsbridge.qqx5tbs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import net.soulwolf.wvjsbridge.WJBridgeHandler;
import net.soulwolf.wvjsbridge.WJBridgeProvider;
import net.soulwolf.wvjsbridge.WJCallbacks;
import net.soulwolf.wvjsbridge.WJMessage;
import net.soulwolf.wvjsbridge.WJWebLoader;
import net.soulwolf.wvjsbridge.WebViewJavascriptBridge;

import java.util.List;
import java.util.Map;

/**
 * author: EwenQin
 * since : 2016/10/26 上午11:14.
 */
public class WJBridgeX5WebView extends WebView implements WebViewJavascriptBridge,WJWebLoader{

    private WJBridgeProvider mProvider;

    public WJBridgeX5WebView(Context context) {
        super(context);
        this._init();
    }

    public WJBridgeX5WebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this._init();
    }

    public WJBridgeX5WebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this._init();
    }

    public WJBridgeX5WebView(Context context, AttributeSet attributeSet, int i, boolean b) {
        super(context, attributeSet, i, b);
        this._init();
    }

    public WJBridgeX5WebView(Context context, AttributeSet attributeSet, int i, Map<String, Object> map, boolean b) {
        super(context, attributeSet, i, map, b);
        this._init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void _init(){
        this.mProvider = WJBridgeProvider.newInstance(this);
        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
        this.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            android.webkit.WebView.setWebContentsDebuggingEnabled(true);
        }
        this.setWebViewClient(onCreateWebViewClient(mProvider));
    }

    protected WebViewClient onCreateWebViewClient(WJBridgeProvider provider){
        return new WJBridgeX5WebViewClient(provider);
    }

    @Override
    public void send(String data) {
        this.mProvider.send(data);
    }

    @Override
    public void send(String data, WJCallbacks callbacks) {
        this.mProvider.send(data,callbacks);
    }

    @Override
    public void setStartupMessages(List<WJMessage> messages) {
        this.mProvider.setStartupMessages(messages);
    }

    @Override
    public void loadUrl(String jsUrl, WJCallbacks callbacks) {
        this.mProvider.loadUrl(jsUrl,callbacks);
    }

    @Override
    public void callHandler(String handlerName, String data, WJCallbacks callbacks) {
        this.mProvider.callHandler(handlerName,data,callbacks);
    }

    @Override
    public void registerHandler(String handlerName, WJBridgeHandler handler) {
        this.mProvider.registerHandler(handlerName,handler);
    }

    @Override
    public void setDefaultHandler(WJBridgeHandler handler) {
        this.mProvider.setDefaultHandler(handler);
    }

    @Override
    public void destroy() {
        super.destroy();
        this.mProvider.destroy();
    }
}
