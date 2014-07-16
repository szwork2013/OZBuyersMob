package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.UtilityClassForLanguagePreferance;

/**
 * Created by avinash on 2/4/14.
 */
public class HelpActivity extends Activity {
    Context context;
    ImageView help_edt_activity1, help_edt_activity2, help_edt_activity3, help_edt_activity4, help_edt_activity5, help_edt_activity6, help_edt_activity7;
    TextView help_txt_activity1, help_txt_activity2, help_txt_activity3, help_txt_activity4, help_txt_activity5, help_txt_activity6, help_txt_activity7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        context = HelpActivity.this;

        UtilityClassForLanguagePreferance.setLocale(getApplicationContext());
        setContentView(R.layout.help);
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
        help_edt_activity1 = (ImageView) findViewById(R.id.help_edt_activity1);
        help_edt_activity2 = (ImageView) findViewById(R.id.help_edt_activity2);
        help_edt_activity3 = (ImageView) findViewById(R.id.help_edt_activity3);
        help_edt_activity4 = (ImageView) findViewById(R.id.help_edt_activity4);
        help_edt_activity5 = (ImageView) findViewById(R.id.help_edt_activity5);
        help_edt_activity6 = (ImageView) findViewById(R.id.help_edt_activity6);
        help_edt_activity7 = (ImageView) findViewById(R.id.help_edt_activity7);
        help_txt_activity1 = (TextView) findViewById(R.id.help_txt_activity1);
        help_txt_activity2 = (TextView) findViewById(R.id.help_txt_activity2);
        help_txt_activity3 = (TextView) findViewById(R.id.help_txt_activity3);
        help_txt_activity4 = (TextView) findViewById(R.id.help_txt_activity4);
        help_txt_activity5 = (TextView) findViewById(R.id.help_txt_activity5);
        help_txt_activity6 = (TextView) findViewById(R.id.help_txt_activity6);
        help_txt_activity7 = (TextView) findViewById(R.id.help_txt_activity7);
        UtilityClassForLanguagePreferance.applyTypeface(UtilityClassForLanguagePreferance.getParentView(help_txt_activity1), UtilityClassForLanguagePreferance.getTypeFace(context));
    }
}
