package com.hb.network.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.hb.network.App;

/**
 * Created by hanbin on 2017/10/27.
 */

public class PackageUtils {
    /**
     * 获取系统版本号
     *
     * @return
     */
    public static int getCurrentVersionCode() {
        int curVersionCode = 0;
        try {
            PackageInfo info = App.getInstance().getPackageManager()
                    .getPackageInfo(App.getInstance().getPackageName(), 0);
            curVersionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return curVersionCode;
    }

    /**
     * 获取app当前版本
     *
     * @return
     */
    public static String getCurrentVersion() {
        String curVersion = "";
        try {
            PackageInfo info = App.getInstance().getPackageManager()
                    .getPackageInfo(App.getInstance().getPackageName(), 0);
            curVersion = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return curVersion;
    }
}
