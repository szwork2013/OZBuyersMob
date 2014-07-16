package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;

/**
 * Created by avinash on 16/6/14.
 */
public class WebViewActivity extends Activity {
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        setTitle(getIntent().getStringExtra("ACTIVITY_NAME"));

        WebView web_view = (WebView) findViewById(R.id.web_view);
        url = getIntent().getStringExtra("URL");
        web_view.getSettings().setJavaScriptEnabled(true);
//        web_view.getSettings().setLoadWithOverviewMode(true);
        web_view.getSettings().setUseWideViewPort(true);
        web_view.getSettings().setDisplayZoomControls(true);
        web_view.getSettings().setDefaultFontSize(20);
        web_view.setWebViewClient(new MyWebViewClient());
        web_view.loadUrl(url);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        com.google.android.gms.analytics.GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Stop the analytics tracking
        com.google.android.gms.analytics.GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return false;

        }
    }
}
