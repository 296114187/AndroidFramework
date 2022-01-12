package com.voidid.core.tools;

import android.util.Log;

import org.json.JSONObject;

public class DebugTool {
    private static String TAG = DebugTool.class.getName();
    private static String TAG_LOG = "[android-native-api]";

    public static void d(String tag, String content) {
        Log.d(tag == null ? TAG : tag, TAG_LOG + content);
    }

    public static void d(String tag, String content1, String content2) {
        Log.d(tag == null ? TAG : tag, TAG_LOG + content1 + " " + content2);
    }

    public static void d(String tag, String content1, String content2, String content3) {
        Log.d(tag == null ? TAG : tag, TAG_LOG + content1 + " " + content2 + " " + content3);
    }

    public static void d(String tag, String content1, String content2, String content3, String content4) {
        Log.d(tag == null ? TAG : tag, TAG_LOG + content1 + " " + content2 + " " + content3 + " " + content4);
    }

    public static void d(String tag, JSONObject content) {
        Log.d(tag == null ? TAG : tag, TAG_LOG + content.toString());
    }

    public static void d(String tag, String content, JSONObject content2) {
        Log.d(tag == null ? TAG : tag, TAG_LOG + content + " " + content2.toString());
    }




    public static void i(String tag, String content) {
        Log.i(tag == null ? TAG : tag, TAG_LOG + content);
    }

    public static void i(String tag, String content1, String content2) {
        Log.i(tag == null ? TAG : tag, TAG_LOG + content1 + " " + content2);
    }

    public static void i(String tag, String content1, String content2, String content3) {
        Log.i(tag == null ? TAG : tag, TAG_LOG + content1 + " " + content2 + " " + content3);
    }

    public static void i(String tag, String content1, String content2, String content3, String content4) {
        Log.i(tag == null ? TAG : tag, TAG_LOG + content1 + " " + content2 + " " + content3 + " " + content4);
    }

    public static void i(String tag, JSONObject content) {
        Log.i(tag == null ? TAG : tag, TAG_LOG + content.toString());
    }

    public static void i(String tag, String content, JSONObject content2) {
        Log.i(tag == null ? TAG : tag, TAG_LOG + content + " " + content2.toString());
    }




    public static void w(String tag, String content) {
        Log.w(tag == null ? TAG : tag, TAG_LOG + content);
    }

    public static void w(String tag, String content1, String content2) {
        Log.w(tag == null ? TAG : tag, TAG_LOG + content1 + " " + content2);
    }

    public static void w(String tag, String content1, String content2, String content3) {
        Log.w(tag == null ? TAG : tag, TAG_LOG + content1 + " " + content2 + " " + content3);
    }

    public static void w(String tag, String content1, String content2, String content3, String content4) {
        Log.w(tag == null ? TAG : tag, TAG_LOG + content1 + " " + content2 + " " + content3 + " " + content4);
    }

    public static void w(String tag, JSONObject content) {
        Log.w(tag == null ? TAG : tag, TAG_LOG + content.toString());
    }

    public static void w(String tag, String content, JSONObject content2) {
        Log.w(tag == null ? TAG : tag, TAG_LOG + content + " " + content2.toString());
    }





    public static void e(String tag, String content) {
        Log.e(tag == null ? TAG : tag, TAG_LOG + content);
    }

    public static void e(String tag, String content1, String content2) {
        Log.e(tag == null ? TAG : tag, TAG_LOG + content1 + " " + content2);
    }

    public static void e(String tag, String content1, String content2, String content3) {
        Log.e(tag == null ? TAG : tag, TAG_LOG + content1 + " " + content2 + " " + content3);
    }

    public static void e(String tag, String content1, String content2, String content3, String content4) {
        Log.e(tag == null ? TAG : tag, TAG_LOG + content1 + " " + content2 + " " + content3 + " " + content4);
    }

    public static void e(String tag, JSONObject content) {
        Log.e(tag == null ? TAG : tag, TAG_LOG + content.toString());
    }

    public static void e(String tag, String content, JSONObject content2) {
        Log.e(tag == null ? TAG : tag, TAG_LOG + content + " " + content2.toString());
    }
}
