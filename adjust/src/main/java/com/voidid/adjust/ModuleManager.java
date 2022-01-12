package com.voidid.adjust;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.LogLevel;
import com.voidid.core.AppConfig;
import com.voidid.core.AppGlobal;
import com.voidid.core.BaseModule;
import com.voidid.core.tools.DebugTool;

/**
 * 归因类
 * https://help.adjust.com/zh/article/get-started-android-sdk
 */
public class ModuleManager extends BaseModule {
    private static final String TAG = ModuleManager.class.getName();

    /**
     * 初始化Adjust SDK
     */
    @Override
    protected void init() {
        super.init();
        DebugTool.d(TAG, "adjust init");
        String env = AppConfig.isAdjustProduction ? AdjustConfig.ENVIRONMENT_PRODUCTION : AdjustConfig.ENVIRONMENT_SANDBOX;
        AdjustConfig config = new AdjustConfig(AppGlobal.getContext(), AppConfig.adjustAppToken, env);

        if (!AppConfig.isAdjustProduction) {
            config.setLogLevel(LogLevel.WARN);
        }
        Adjust.onCreate(config);
    }

    /**
     * 添加跟踪事件
     * @param eventName 事件名称
     */
    public static void trackEvent(String eventName) {
        DebugTool.d(TAG, "Attribution.trackEvent("  +  eventName + ")");
        AdjustEvent event = new AdjustEvent(eventName);
        Adjust.trackEvent(event);
    }
}
