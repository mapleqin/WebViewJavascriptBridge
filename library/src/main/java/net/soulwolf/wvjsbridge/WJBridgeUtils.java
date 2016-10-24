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

import android.content.Context;
import android.os.SystemClock;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;

/**
 * author: EwenQin
 * since : 2016/10/24 下午1:28.
 */
public final class WJBridgeUtils {

    private static final String ENCODING = "UTF-8";

    static final String WJ_PROTOCOL_SCHEME = "wvjbscheme://";
    static final String WJ_BRIDGE_LOADED = WJ_PROTOCOL_SCHEME + "__BRIDGE_LOADED__";
    static final String WJ_RETURN_DATA = WJ_PROTOCOL_SCHEME + "return/";
    static final String WJ_FETCH_QUEUE_MESSAGE = WJ_RETURN_DATA + "_fetchQueue/";
    static final String WJ_EMPTY_STR = "";
    static final String WJ_SPLIT_MARK = "/";

    static final String CALLBACK_ID_FORMAT = "native_cb_%s_%s";
    static final String JS_DISPATCH_MESSAGE = "javascript:WebViewJavascriptBridge._handleMessageFromNative('%s');";
    static final String JS_FETCH_QUEUE = "javascript:WebViewJavascriptBridge._fetchQueue();";

    private WJBridgeUtils() {
        throw new AssertionError("no instance!");
    }

    public static <T> T checkNoNull(T t, String msg) {
        if (t == null) {
            throw new NullPointerException(msg);
        }
        return t;
    }

    public static String formatCallbackId(long uniqueId) {
        return String.format(CALLBACK_ID_FORMAT, uniqueId, SystemClock.currentThreadTimeMillis());
    }

    public static String formatJsDispatchMessage(String messageJson) {
        return String.format(JS_DISPATCH_MESSAGE, messageJson);
    }

    public static String parseFuncName(String jsUrl) {
        return jsUrl.replace("javascript:WebViewJavascriptBridge.", "").replaceAll("\\(.*\\);", "");
    }

    public static String getFuncNameFromUrl(String url) {
        String temp = url.replace(WJ_RETURN_DATA, WJ_EMPTY_STR);
        String[] functionAndData = temp.split(WJ_SPLIT_MARK);
        if (functionAndData.length >= 1) {
            return functionAndData[0];
        }
        return null;
    }

    public static String getReturnDataFromUrl(String url) {
        if (url.startsWith(WJ_FETCH_QUEUE_MESSAGE)) {
            return url.replace(WJ_FETCH_QUEUE_MESSAGE, WJ_EMPTY_STR);
        }

        String temp = url.replace(WJ_RETURN_DATA, WJ_EMPTY_STR);
        String[] functionAndData = temp.split(WJ_SPLIT_MARK);

        if (functionAndData.length >= 2) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < functionAndData.length; i++) {
                sb.append(functionAndData[i]);
            }
            return sb.toString();
        }
        return null;
    }

    public static String decodeUrl(String url) {
        try {
            return URLDecoder.decode(url, ENCODING);
        } catch (Exception e) {
            return url;
        }
    }

    public static void webViewLoadJs(WebView view, String url) {
        String js = "var newscript = document.createElement(\"script\");";
        js += "newscript.src=\"" + url + "\";";
        js += "document.scripts[0].parentNode.insertBefore(newscript,document.scripts[0]);";
        view.loadUrl("javascript:" + js);
    }

    public static void webViewLoadLocalJs(WebView view, String path) {
        String jsContent = assetFile2Str(view.getContext(), path);
        view.loadUrl("javascript:" + jsContent);
    }

    public static String assetFile2Str(Context context, String urlStr) {
        InputStream is = null;
        try {
            is = context.getAssets().open(urlStr);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder sb = new StringBuilder();
            do {
                line = bufferedReader.readLine();
                if (line != null && !line.matches("^\\s*\\/\\/.*")) {
                    sb.append(line);
                }
            } while (line != null);
            WJBridgeUtils.close(bufferedReader);
            return sb.toString();
        } catch (Exception e) {
            L.e("assetFile2Str.[%s]",new Object[]{urlStr},e);
        } finally {
            WJBridgeUtils.close(is);
        }
        return null;
    }

    public static void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            try {
                if(closeable != null){
                    closeable.close();
                }
            }catch (Exception e){
                L.e("close.[%s]",new Object[]{closeable},e);
            }
        }
    }
}
