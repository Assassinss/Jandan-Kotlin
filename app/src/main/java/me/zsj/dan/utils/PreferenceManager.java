package me.zsj.dan.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author zsj
 */

public class PreferenceManager {

    private static final String PREFS_NAME = "jandan";
    public static final String DAY = "day";

    private static SharedPreferences preferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor edit(Context context) {
        return preferences(context).edit();
    }

    public static void putBoolean(Context context, String key, boolean value) {
        edit(context).putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return preferences(context).getBoolean(key, defaultValue);
    }

}
