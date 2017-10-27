package com.hb.network;

import android.app.Application;

/**
 * Created by hanbin on 2017/9/14.
 */

public class App extends Application {
    private static App        instance;
    @Override
    public void onCreate() {
        super.onCreate();
        T.init(this);
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }
}
