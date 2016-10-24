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

import java.util.List;

/**
 * author: EwenQin
 * since : 2016/10/23 下午7:12.
 */
public interface WebViewJavascriptBridge {

    void send(String data);

    void send(String data,WJCallbacks callbacks);

    void setStartupMessages(List<WJMessage> messages);

    void loadUrl(String jsUrl,WJCallbacks callbacks);

    void callHandler(String handlerName,String data,WJCallbacks callbacks);

    void registerHandler(String handlerName,WJBridgeHandler handler);

    void setDefaultHandler(WJBridgeHandler handler);
}
