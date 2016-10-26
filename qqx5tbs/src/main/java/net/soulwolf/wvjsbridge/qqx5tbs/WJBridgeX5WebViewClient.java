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


import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import net.soulwolf.wvjsbridge.WJBridgeProvider;

/**
 * author: EwenQin
 * since : 2016/10/23 下午12:59.
 */
public class WJBridgeX5WebViewClient extends WebViewClient {

    private static final String TAG = "WJBridgeWebViewClient";

    private WJBridgeProvider mProvider;

    public WJBridgeX5WebViewClient(WJBridgeProvider provider){
        this.mProvider = provider;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return mProvider.shouldOverrideUrlLoading(mProvider.getLoader(), url)
                || super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        this.mProvider.onPageFinished();
    }
}
