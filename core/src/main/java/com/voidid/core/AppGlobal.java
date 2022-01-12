package com.voidid.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.voidid.core.tools.DebugTool;


public class AppGlobal {
    private static final String TAG = AppGlobal.class.getName();

    protected static Application application;
    @SuppressLint("StaticFieldLeak")
    protected static Activity mainActivity;
    @SuppressLint("StaticFieldLeak")
    protected static Context context;

    public static Context getContext() {
        return context;
    }

    public static Application getApplication() {
        if (application == null) {
            DebugTool.e(TAG, "Application获取失败，请联系框架组检查Core.ModuleManager");
        }
        return application;
    }

    public static Activity getMainActivity() {
        if (mainActivity == null) {
            DebugTool.e(TAG, "Activity未创建， 请在创建后进行调用");
        }
        return mainActivity;
    }
}
