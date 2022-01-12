package com.voidid.max;

import android.util.Log;

import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import com.voidid.core.AppGlobal;
import com.voidid.core.BaseModule;
import com.voidid.core.tools.DebugTool;

/**
 * Max广告
 * https://dash.applovin.com/documentation/mediation/android/getting-started/integration
 */
public class ModuleManager extends BaseModule {
    private static final String TAG = ModuleManager.class.getName();

    @Override
    protected void init() {
        super.init();
        DebugTool.d(TAG,"AppLovin init");
        AppLovinSdk appLovinSdk = AppLovinSdk.getInstance(AppGlobal.getContext());
        appLovinSdk.setMediationProvider(AppLovinMediationProvider.MAX);
        // 设置广告静音
        appLovinSdk.getSettings().setMuted(false);
        // 启用完整日志
        appLovinSdk.getSettings().setVerboseLogging(true);
        AppLovinSdk.initializeSdk(AppGlobal.getContext(), config -> {
            DebugTool.d(TAG,"AppLovin SDK is initialized, start loading ads");
            preloadAd();
            // 开启测试模式
//            enableDebugView();
        });
    }

    /**
     * 开启测试界面（初始化结束后调用）
     */
    public static void enableDebugView() {
        AppLovinSdk.getInstance( AppGlobal.getContext() ).showMediationDebugger();
    }

    /**
     * 预加载广告
     */
    public static void preloadAd() {
        Log.d(TAG, "preload AD.");
//        RewardedVideoAd.getInstance(AppConfig.REWARD_VIDEO_AD_UNIT_ID);
//        InterstitialAd.getInstance(AppConfig.INTERSTITIAL_AD_UNIT_ID);
//        BannerAdView.getInstance(AppConfig.BANNER_AD_UNIT_ID, null);
//        NativeAd.getInstance(AppConfig.NATIVE_AD_UNIT_ID);
    }
}
