package com.alert;

import android.app.Application;
import android.content.Context;

/**
 * Created by kena on 5/31/2017.
 */

public class MyApp extends Application {

    private static final String TAG = "MyApp";
    private static Context mContext;
    private static String mPackageName;

    public static Context getAppContext() {
        return MyApp.mContext;
    }

    public static String getPackagePath() {
        return MyApp.mPackageName;
    }

    public static String getResourcePath() {
        return "android.resource://" + MyApp.mPackageName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApp.mContext = getApplicationContext();
        MyApp.mPackageName = MyApp.mContext.getPackageName();

    }
}
