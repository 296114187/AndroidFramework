package com.voidid.game_analytics;

import com.voidid.core.AppConfig;
import com.voidid.core.AppGlobal;
import com.voidid.core.BaseModule;
import com.voidid.core.tools.DebugTool;
import com.gameanalytics.sdk.GameAnalytics;

/**
 * Game Analytics
 * https://gameanalytics.com/docs/s/article/Android-SDK-Setup
 */
public class ModuleManager extends BaseModule {
    private static final String TAG = ModuleManager.class.getName();

    @Override
    protected void onActivityCreate() {
        super.onActivityCreate();
        DebugTool.d(TAG, "GameAnalytics init");
        // 信息日志
        GameAnalytics.setEnabledInfoLog(false);
        // 冗长日志
        GameAnalytics.setEnabledVerboseLog(false);
        // 错误报告开关
        GameAnalytics.setEnabledErrorReporting(false);
        // 远程配置监听
        GameAnalytics.addRemoteConfigsListener(() -> {
            DebugTool.i(TAG, "远程配置准备好了 ");
        });
        // 自动获取应用版本号
        GameAnalytics.configureAutoDetectAppVersion(true);
        // 初始化
        GameAnalytics.initialize(AppGlobal.getMainActivity(), AppConfig.gameAnalyticsGameKey, AppConfig.gameAnalyticsSecretKey);
    }
}
