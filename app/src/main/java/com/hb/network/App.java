package com.hb.network;

import android.app.Application;

/**
 * Created by hanbin on 2017/9/14.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        T.init(this);
    }
}
