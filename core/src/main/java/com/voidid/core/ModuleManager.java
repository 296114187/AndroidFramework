package com.voidid.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.startup.Initializer;

import com.voidid.core.enums.EEventNames;
import com.voidid.core.event.EventManager;
import com.voidid.core.jsb.DeviceManager;
import com.voidid.core.jsb.JSBridgeManager;
import com.voidid.core.tools.SystemTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 核心模块管理类
 */
public class ModuleManager extends BaseModule {
    private static final String TAG = ModuleManager.class.getName();
    /**
     * 覆写 依赖函数
     * @return 依赖列表
     */
    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return dependencies;
    }

    /**
     * 初始化
     */
    public void init() {
        if (AppGlobal.application == null) AppGlobal.application = SystemTools.getApplication();
        if (AppGlobal.getContext() == null) AppGlobal.context = AppGlobal.getApplication().getApplicationContext();
        // 监听Activity生命周期
        setApplicationCallbacks(AppGlobal.getApplication());
        DeviceManager.init();
        JSBridgeManager.init();
    }

    /**
     * 监听Activity生命周期
     * @param application 应用
     */
    public static void setApplicationCallbacks(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                if (AppGlobal.mainActivity == null) AppGlobal.mainActivity = activity;
                // Activity创建回调
                try {
                    JSONObject object = new JSONObject();
                    object.put("Activity", activity);
                    object.put("SavedInstanceState", savedInstanceState);
                    EventManager.emit(EEventNames.ACTIVITY_ON_CREATE, object);
                    Log.d(TAG, "onActivityCreated: " + AppGlobal.mainActivity.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("Activity", activity);
                    EventManager.emit(EEventNames.ACTIVITY_ON_START, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("Activity", activity);
                    EventManager.emit(EEventNames.ACTIVITY_ON_RESUME, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("Activity", activity);
                    EventManager.emit(EEventNames.ACTIVITY_ON_PAUSE, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("Activity", activity);
                    EventManager.emit(EEventNames.ACTIVITY_ON_STOP, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("Activity", activity);
                    EventManager.emit(EEventNames.ACTIVITY_ON_DESTROY, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
