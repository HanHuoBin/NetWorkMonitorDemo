package com.hb.network.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast统一管理类
 */
public class T {
    // Toast
    private static Toast toast;

    private T() {

    }

    public static void init(Context context) {
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示Toast
     * 
     * @param message
     */
    public static void showShort(String message, Object... args) {
        show(Toast.LENGTH_SHORT, message, args);
    }

    /**
     * 短时间显示Toast
     * 
     * @param message
     */
    public static void showShort(int message) {
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(message);
        toast.show();
    }

    /**
     * 长时间显示Toast
     * 
     * @param message
     */
    public static void showLong(String message, Object... args) {
        show(Toast.LENGTH_LONG, message, args);
    }

    /**
     * 长时间显示Toast
     * 
     * @param message
     */
    public static void showLong(int message) {
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setText(message);
        toast.show();
    }

    /**
     * @param duration
     * @param message
     * @param args
     */
    public static void show(int duration, String message, Object... args) {
        toast.setDuration(duration);
        if (args.length > 0) {
            message = String.format(message, args);
        }
        toast.setText(message);
        toast.show();
    }

    /**
     * 自定义显示Toast时间
     * 
     * @param message
     * @param duration
     */
    public static void show(int message, int duration) {
        toast.setDuration(duration);
        toast.setText(message);
        toast.show();
    }

    /** Hide the toast, if any. */
    public static void hideToast() {
        if (null != toast) {
            toast.cancel();
        }
    }
}
