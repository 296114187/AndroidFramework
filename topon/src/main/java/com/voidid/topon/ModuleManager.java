package com.voidid.topon;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.anythink.china.api.ATChinaSDKHandler;
import com.anythink.core.api.ATSDK;
import com.anythink.network.toutiao.TTATInitManager;
import com.voidid.core.AppConfig;
import com.voidid.core.AppGlobal;
import com.voidid.core.BaseModule;
import com.voidid.core.enums.ADEventType;
import com.voidid.core.enums.ADType;
import com.voidid.core.enums.EEventNames;
import com.voidid.core.event.EventFunction;
import com.voidid.core.event.EventManager;
import com.voidid.core.tools.DebugTool;

public class ModuleManager extends BaseModule {
    private static final String TAG = ModuleManager.class.getName();

    @Override
    public void init() {
        super.init();
        DebugTool.d(TAG, "topon init");
        // 日志模式
        ATSDK.setNetworkLogDebug(AppConfig.ENABLE_DEBUG_LOG);
        // 检查广告平台的集成状态
        ATSDK.integrationChecking(AppGlobal.getContext());
        // [穿山甲] 初始化TopOn SDK之前调用此方法
        TTATInitManager.getInstance().setIsOpenDirectDownload(false);
        // 初始化SDK
        ATSDK.init(AppGlobal.getContext(), AppConfig.TOP_ON_APP_ID, AppConfig.TOP_ON_APP_KEY);
        // 申请权限
        requestPermissionIfNecessary();
    }

    /**
     * Activity创建成功
     */
    @Override
    protected void onActivityCreate() {
        super.onActivityCreate();
        // 显示开屏
        if (AppConfig.isShowSplash) {
            EventManager.on(EEventNames.EVENT_AD, onSplashLoaded);
            showSplashAd();
        } else {
            preloadAd();
        }
    }

    /**
     * 动态申请权限
     */
    public static void requestPermissionIfNecessary() {
        // 动态获取READ_PHONE_STATE及WRITE_EXTERNAL_STORAGE权限
//        ATChinaSDKHandler.requestPermissionIfNecessary(AppGlobal.getContext());
    }

    /**
     * 开屏预加载监听
     */
    private static final EventFunction onSplashLoaded = (eventName, data) -> {
        ADType adType = (ADType) data.get("adType");
        ADEventType adEventType = (ADEventType) data.get("adEventType");
        if (adType == ADType.SPLASH && adEventType == ADEventType.LOADED) {
            DebugTool.d(TAG, "开屏广告加载结束，预加载其它广告" + adType + ", " + adEventType);
            EventManager.off(EEventNames.EVENT_AD, ModuleManager.onSplashLoaded);
            preloadAd();
        }
    };

    /**
     * 预加载广告
     */
    public static void preloadAd() {
        DebugTool.d(TAG, "preload AD.");
//        RewardedVideoAd.getInstance()
//        new Handler().postDelayed(()->{
//            RewardedVideoAd.getInstance(AppConfig.REWARD_VIDEO_AD_UNIT_ID).show();
//            InterstitialAd.getInstance(AppConfig.INTERSTITIAL_AD_UNIT_ID);
//            BannerAd.getInstance(AppConfig.BANNER_AD_UNIT_ID, null);
//            NativeAd.getInstance(AppConfig.NATIVE_AD_UNIT_ID);
//        }, 5000);
    }

    private static void showSplashAd() {
        DebugTool.d(TAG, "[fl-api]show splash ad.");
        if (!AppConfig.SPLASH_AD_UNIT_ID.equals("")) {
            AppConfig.isShowSplash = false;
            Intent intent = new Intent(AppGlobal.getMainActivity(), SplashAdShowActivity.class);
            AppGlobal.getMainActivity().startActivity(intent);
        }
    }
}
