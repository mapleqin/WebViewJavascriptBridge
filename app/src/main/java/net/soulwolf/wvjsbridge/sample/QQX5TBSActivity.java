package net.soulwolf.wvjsbridge.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import net.soulwolf.wvjsbridge.WJBridgeHandler;
import net.soulwolf.wvjsbridge.WJBridgeWebView;
import net.soulwolf.wvjsbridge.WJCallbacks;
import net.soulwolf.wvjsbridge.qqx5tbs.WJBridgeX5WebView;

public class QQX5TBSActivity extends AppCompatActivity {

    private WJBridgeX5WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqx5_tbs);
        this.webView = (WJBridgeX5WebView) findViewById(R.id.web_view);
        this.webView.loadUrl("file:///android_asset/WebViewJavascriptBridgeDemo.html");
        this.webView.registerHandler("callNative", new WJBridgeHandler() {
            @Override
            public void handler(String data, WJCallbacks callbacks) {
                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                callbacks.onCallback("callNative response data" + System.currentTimeMillis());
            }
        });
    }

    public void callHandlerJSToast(View view){
        this.webView.callHandler("callJs", "{\"callJsData\":\"data\"}", new WJCallbacks() {
            @Override
            public void onCallback(String data) {
                Toast.makeText(getApplicationContext(), "callJs callback" + data, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
