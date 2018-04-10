package net.soulwolf.wvjsbridge.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import net.soulwolf.wvjsbridge.WJBridgeHandler;
import net.soulwolf.wvjsbridge.WJBridgeWebView;
import net.soulwolf.wvjsbridge.WJCallbacks;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private WJBridgeWebView webView;

    private MenuItem mMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.webView = (WJBridgeWebView) findViewById(R.id.web_view);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenuItem = menu.add("QQX5TBS");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == mMenuItem) {
            startActivity(new Intent(this, QQX5TBSActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
