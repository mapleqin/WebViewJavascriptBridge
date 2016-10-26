# WebViewJavascriptBridge
This is a communication between Android applications and Web Javascript to establish a bridge between the call support each other

<a href="https://github.com/marcuswestin/WebViewJavascriptBridge">WebViewJavascriptBridge</a> for Android Implement!

## Sample
init
```java

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
## Maven
    android-WebView
        <dependency>
            <groupId>net.soulwolf.common</groupId>
            <artifactId>WebViewJavascriptBridge</artifactId>
            <version>0.0.3</version>
            <type>pom</type>
        </dependency>
    	
    QQBrowser X5 core
        <dependency>
            <groupId>net.soulwolf.common</groupId>
            <artifactId>WebViewJavascriptBridge-qqx5tbs</artifactId>
            <version>0.0.1</version>
            <type>pom</type>
        </dependency> 
## Gradle
	allprojects {
       repositories {
          jcenter()
          maven{
            url "https://dl.bintray.com/soulwolf/maven"
          }
       }
	}
    android-WebView	
        compile 'net.soulwolf.common:WebViewJavascriptBridge:0.0.3'
        
    QQBrowser X5 core
        compile 'net.soulwolf.common:WebViewJavascriptBridge-qqx5tbs:0.0.1'

## Developed by
 Ching Soulwolf - <a href='javascript:'>Ching.Soulwolf@gmail.com</a>


## License
	Copyright 2015 Soulwolf Ching
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


