package net.soulwolf.wvjsbridge.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import net.soulwolf.wvjsbridge.WJBridgeHandler;
import net.soulwolf.wvjsbridge.WJCallbacks;
import net.soulwolf.wvjsbridge.qqx5tbs.WJBridgeX5WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class QQX5TBSActivity extends AppCompatActivity {

    private WJBridgeX5WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqx5_tbs);
        this.webView = (WJBridgeX5WebView) findViewById(R.id.web_view);
        this.webView.loadUrl("file:///android_asset/WebViewJavascriptBridgeDemo.html");
        this.webView.registerHandler("getTimeFromNative", new WJBridgeHandler() {
            @Override
            public void handler(String data, WJCallbacks callbacks) {
                String format = getFormatFromJson(data);
                Toast.makeText(getApplicationContext(), "JSArgs " + data, Toast.LENGTH_SHORT).show();
                SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
                callbacks.onCallback("NativeReturn " + dateFormat.format(System.currentTimeMillis()));
            }
        });
    }

    public void getTimeFromJs(View view) {
        this.webView.callHandler("getTimeFromJs", "{\"format\":\"yyyy-MM-dd hh:mm\"}", new WJCallbacks() {
            @Override
            public void onCallback(String data) {
                Toast.makeText(getApplicationContext(), "JSReturn " + data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFormatFromJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.optString("format", "yyyy-MM-dd");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "yyyy-MM-dd";
    }
}
