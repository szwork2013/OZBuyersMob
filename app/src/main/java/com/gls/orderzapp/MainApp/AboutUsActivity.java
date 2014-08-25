package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;

/**
 * Created by avinash on 2/4/14.
 */
public class AboutUsActivity extends Activity {
    Context context;
    TextView about_us_txt_about_us;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = AboutUsActivity.this;
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);

//        UtilityClassForLanguagePreferance.setLocale(getApplicationContext());
        setContentView(R.layout.about_us);
        findViewsById();

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

    private void findViewsById() {
        about_us_txt_about_us = (TextView) findViewById(R.id.about_us_txt_about_us);
//        UtilityClassForLanguagePreferance.applyTypeface(UtilityClassForLanguagePreferance.getParentView(about_us_txt_about_us), UtilityClassForLanguagePreferance.getTypeFace(context));
    }
}
