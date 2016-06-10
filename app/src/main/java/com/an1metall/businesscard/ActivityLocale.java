package com.an1metall.businesscard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

public  class ActivityLocale {

    private static final String EXTRA_STRING_NAME = "lang";
    private static final String LANGUAGE_CODE_RU = "ru";
    private static final String LANGUAGE_CODE_EN = "en";

    private static String language_code = LANGUAGE_CODE_RU;

    public static void setLocale(Context context, Intent intent) {
        Resources resources = context.getResources();
        if (intent.getStringExtra(EXTRA_STRING_NAME) != null) {
            language_code = intent.getStringExtra(EXTRA_STRING_NAME);
        }
        DisplayMetrics dm = resources.getDisplayMetrics();
        android.content.res.Configuration conf = resources.getConfiguration();
        conf.locale = new Locale(language_code.toLowerCase());
        resources.updateConfiguration(conf, dm);
    }

    public static void changeLocale(Activity activity, Intent intent) {
        if (language_code.equals(LANGUAGE_CODE_RU)) {
            language_code = LANGUAGE_CODE_EN;
        } else {
            language_code = LANGUAGE_CODE_RU;
        }
        intent.putExtra(EXTRA_STRING_NAME, language_code);
        activity.finish();
        activity.startActivity(intent);
    }

}
