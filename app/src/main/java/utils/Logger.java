package utils;

import android.util.Log;

public class Logger {
    public static final boolean B_LOG_OPEN = false;
    public static final String TAG = "http";

    public static void i(String msg) {
        if (!B_LOG_OPEN)
            return;
        Log.i(TAG, msg);
    }

    public static void e(String msg) {
        if (!B_LOG_OPEN)
            return;
        Log.e(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (!B_LOG_OPEN)
            return;
        Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (!B_LOG_OPEN)
            return;
        Log.e(tag, msg);
    }
}
