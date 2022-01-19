package com.voidid.tenjin;

import com.tenjin.android.TenjinSDK;
import com.voidid.core.AppConfig;
import com.voidid.core.AppGlobal;
import com.voidid.core.BaseModule;
import com.voidid.core.jsb.BaseToolManager;
import com.voidid.core.tools.DebugTool;

/**
 * 归因类tenjin
 * https://docs.tenjin.com/en/send-events/android.html
 */
public class ModuleManager extends BaseModule {
    private static final String TAG = ModuleManager.class.getName();

    /**
     * 初始化 SDK
     */
    @Override
    protected void init() {
        super.init();
        DebugTool.d(TAG, "tenjin init");
        TenjinSDK instance = TenjinSDK.getInstance(AppGlobal.getContext(), AppConfig.tenjinApiKey);
        // 设置发布的应用商店，GooglePlay（谷歌商店）、amazon（亚马逊商店）、other（其它渠道，例：国内）
        TenjinSDK.AppStoreType channel = BaseToolManager.getChannel().equals("GAME_ANDROID_GOOGLE") ? TenjinSDK.AppStoreType.googleplay : TenjinSDK.AppStoreType.other;
        instance.setAppStore(channel);
        // 添加深度归因
        instance.getDeeplink((clickedTenjinLink, isFirstSession, data) -> {
            if (clickedTenjinLink) {
                if (isFirstSession) {
                    if (data.containsKey(TenjinSDK.DEEPLINK_URL)) {
                        // use the deferred_deeplink_url to direct the user to a specific part of your app
                    }
                }
            }
            DebugTool.d(TAG, "Attribution.Deeplink.data: " + data.toString());
            DebugTool.d(TAG, "Attribution.Deeplink.clickedTenjinLink: " + clickedTenjinLink);
            DebugTool.d(TAG, "Attribution.Deeplink.isFirstSession: " + isFirstSession);
        });
        // A/B测子版本，例：如果您的应用程序版本1.0.1和 设置appendAppSubversion(8888)，它将报告应用程序版本为1.0.1.8888
//        instance.appendAppSubversion(8888);
        // 连接
        instance.connect();
        // 发送自定义事件(首次测试时开启，之后可关闭)
        instance.eventWithName("swipe right");
    }
}