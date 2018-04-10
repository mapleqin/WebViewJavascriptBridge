# WebViewJavascriptBridge[ ![Download](https://api.bintray.com/packages/soulwolf/maven/jsbridge/images/download.svg) ](https://bintray.com/soulwolf/maven/jsbridge/_latestVersion)
This is a communication between Android applications and Web Javascript to establish a bridge between the call support each other

<a href="https://github.com/marcuswestin/WebViewJavascriptBridge">WebViewJavascriptBridge</a> for Android Implement!

## Sample
init
```javascript
function setupWebViewJavascriptBridge(callback) {
    if (window.WebViewJavascriptBridge) { return callback(WebViewJavascriptBridge); }
    if (window.WVJBCallbacks) { return window.WVJBCallbacks.push(callback); }
    window.WVJBCallbacks = [callback];
    var WVJBIframe = document.createElement('iframe');
    WVJBIframe.style.display = 'none';
    WVJBIframe.src = 'wvjbscheme://__BRIDGE_LOADED__';
    document.documentElement.appendChild(WVJBIframe);
    setTimeout(function() { document.documentElement.removeChild(WVJBIframe) }, 0)
}

setupWebViewJavascriptBridge(function(bridge) {

    /* Initialize your app here */

    bridge.registerHandler('callJs', function(data, responseCallback) {
        console.log(data);
        responseCallback('callback:');
    })
})
````


```java
webView.registerHandler("callNative", new WJBridgeHandler() {
        @Override
        public void handler(String data, WJCallbacks callbacks) {
            Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
            callbacks.onCallback("callNative response data" + System.currentTimeMillis());
        }
    });

webView.callHandler("callJs", "{\"callJsData\":\"data\"}", new WJCallbacks() {
        @Override
        public void onCallback(String data) {
            Toast.makeText(getApplicationContext(),"callJs callback" + data,Toast.LENGTH_SHORT).show();
        }
    });
```
## Gradle
    compile 'org.amphiaraus.webkit:jsbridge:[version]'
        
## QQBrowser X5 core
    compile 'org.amphiaraus.webkit:jsbridge-qqx5tbs:[version]'

## Developed by
 Amphiaraus - <a href='javascript:'>amphiarause@gmail.com</a>


## License
	Copyright 2015 The Android Open Source Project for WebViewJavascriptBridge
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.


