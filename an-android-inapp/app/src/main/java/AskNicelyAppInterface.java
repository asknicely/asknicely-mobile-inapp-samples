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

    //Fired when we are finished with the survey.
    final String ASKNICELY_DONE = "askNicelyDone";

    public AskNicelyAppInterface(Context c, WebView wv) {
        mContext = c;
        askNicelyWebView = wv;
    }
    @JavascriptInterface
    public void postMessage(String jsonResult) throws JSONException {
        //The results that we send are json encoded objects, so first we need to decode them.
        JSONObject jObj = new JSONObject(jsonResult);
        String result = jObj.getString("event");
        if (result.equals(ASKNICELY_DONE)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    askNicelyWebView.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}
