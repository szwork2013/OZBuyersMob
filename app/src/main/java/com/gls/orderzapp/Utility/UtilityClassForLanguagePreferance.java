package com.gls.orderzapp.Utility;

/**
 * Created by prajyot on 1/4/14.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;


public class UtilityClassForLanguagePreferance {

    public static void setLocale(Context context) {
        String default_language;
        SharedPreferences aspr = PreferenceManager.getDefaultSharedPreferences(context);
        default_language = aspr.getString("DEFAULT_LANGUAGE", "en");
        Locale myLocale = new Locale(default_language);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public static Typeface getTypeFace(Context context) {
        String default_language;
        Typeface tf = null;
        SharedPreferences aspr = PreferenceManager.getDefaultSharedPreferences(context);
        default_language = aspr.getString("DEFAULT_LANGUAGE", "en");
        if (default_language.equals("hi")) {
            tf = Typeface.createFromAsset(context.getAssets(), "DroidHindi.ttf");
        } else if (default_language.equals("ma")) {
            tf = Typeface.createFromAsset(context.getAssets(), "DroidHindi.ttf");
        } else if (default_language.equals("gu")) {
            tf = Typeface.createFromAsset(context.getAssets(), "shruti.ttf");
        }
        return tf;
    }

    public static ViewGroup getParentView(View v) {
        ViewGroup vg = null;
        if (v != null) {
            vg = (ViewGroup) v.getRootView();
        }
        return vg;
    }

    public static void applyTypeface(ViewGroup v, Typeface f) {
        if (v != null) {
            int vgCount = v.getChildCount();
            for (int i = 0; i < vgCount; i++) {
                if (v.getChildAt(i) == null) continue;
                if (v.getChildAt(i) instanceof ViewGroup) {
                    applyTypeface((ViewGroup) v.getChildAt(i), f);
                } else {
                    View view = v.getChildAt(i);
                    if (view instanceof TextView) {
                        ((TextView) (view)).setTypeface(f);
                    } else if (view instanceof EditText) {
                        ((EditText) (view)).setTypeface(f);
                    } else if (view instanceof Button) {
                        ((Button) (view)).setTypeface(f);
                    }
                }
            }
        }
    }
}