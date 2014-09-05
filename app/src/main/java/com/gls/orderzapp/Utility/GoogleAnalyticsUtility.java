package com.gls.orderzapp.Utility;

import android.app.Application;

import com.gls.orderzapp.R;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

/**
 * Created by avinash on 24/6/14.
 */
public class GoogleAnalyticsUtility extends Application {

    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UA-54358226-1";

    //Logging TAG
    private static final String TAG = "OrderZapp";


    public static int GENERAL_TRACKER = 0;
    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public GoogleAnalyticsUtility() {
        super();
    }

    synchronized public Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            com.google.android.gms.analytics.GoogleAnalytics analytics = com.google.android.gms.analytics.GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(R.xml.app_tracker)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : analytics.newTracker(R.xml.ecommerce_tracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }
}

