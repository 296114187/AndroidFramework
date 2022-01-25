package com.voidid.core.tools;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 系统工具
 */
public class SystemTools {
    /**
     * 获取Application对象
     **/
    @SuppressLint("PrivateApi")
    public static Application getApplication() {
        Application application = null;
        Class<?> activityThreadClass;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            final Method method2 = activityThreadClass.getMethod("currentActivityThread", new Class[0]);
            // 得到当前的ActivityThread对象
            Object localObject = method2.invoke(null, (Object[]) null);
            final Method method = activityThreadClass.getMethod("getApplication");
            application = (Application) method.invoke(localObject, (Object[]) null);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return application;
    }

    /**
     * 获取Context对象
     **/
    public static Context getContext() {
        return getApplication().getApplicationContext();
    }
}
