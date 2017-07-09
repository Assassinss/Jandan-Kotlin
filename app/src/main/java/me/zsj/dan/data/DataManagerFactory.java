package me.zsj.dan.data;

import android.app.Activity;

/**
 * @author zsj
 */

public class DataManagerFactory {

    private static DataManager sInstance;

    private DataManagerFactory() {
        throw new AssertionError("No instance");
    }

    public static DataManager getInstance(Activity context) {
        if (sInstance == null) {
            sInstance = new DataManager(context);
        }
        return sInstance;
    }
}
