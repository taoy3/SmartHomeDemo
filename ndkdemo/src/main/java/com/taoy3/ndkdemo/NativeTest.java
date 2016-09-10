package com.taoy3.ndkdemo;

import android.util.Log;

/**
 * Created by taoy3 on 16/9/5.
 */
public class NativeTest {
    private static final String TAG="native_test";
    static {
        Log.i(TAG,"static");
        System.loadLibrary("nativetask");
    }
    public static native String getProp(String name);
}
