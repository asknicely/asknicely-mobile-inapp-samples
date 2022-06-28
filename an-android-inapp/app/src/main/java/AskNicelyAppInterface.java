import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

public class AskNicelyAppInterface extends Activity {
    Context mContext;
    WebView askNicelyWebView;

    /** Instantiate the interface and set the context */
    public AskNicelyAppInterface(Context c, WebView wv) {
        mContext = c;
        askNicelyWebView = wv;
    }
    @JavascriptInterface
    public void postMessage(String jsonResult) throws JSONException {
        Log.d("appinterface", jsonResult);
        JSONObject jObj = new JSONObject(jsonResult);
        String result = jObj.getString("event");
        if (result.equals("askNicelyDone")) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    askNicelyWebView.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}
