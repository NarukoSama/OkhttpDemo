package com.bwei.okhttpdemo;

import android.app.Application;

/**
 * Created by muhanxi on 17/5/26.
 */

public class IApplication extends Application {

    public static IApplication application ;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this ;
    }
}
