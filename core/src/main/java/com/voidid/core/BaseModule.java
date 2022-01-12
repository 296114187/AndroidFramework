package com.voidid.core;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import com.voidid.core.enums.EEventNames;
import com.voidid.core.event.EventFunction;
import com.voidid.core.event.EventManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 模块基类
 */
public abstract class BaseModule implements Initializer<BaseModule> {
    public static String TAG = BaseModule.class.getName();
    protected List<Class<? extends Initializer<?>>> dependencies = new ArrayList<>();

    @NonNull
    @Override
    public BaseModule create(@NonNull Context mContext) {
        EventManager.on(EEventNames.ACTIVITY_ON_CREATE, onCreate);
        init();
        return this;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        dependencies.add(ModuleManager.class);
        return dependencies;
    }

    /**
     * Activity创建成功
     */
    private final EventFunction onCreate = (eventName, data) -> _onActivityCreate();
    private void _onActivityCreate() {
        EventManager.off(EEventNames.ACTIVITY_ON_CREATE, onCreate);
        onActivityCreate();
    }

    /* *********************** 生命周期函数 *********************** */

    /**
     * 初始化
     */
    protected void init() { }
    /**
     * Activity创建成功
     */
    protected void onActivityCreate() { }
}
