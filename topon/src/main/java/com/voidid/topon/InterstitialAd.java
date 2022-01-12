package com.voidid.topon;

import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.anythink.interstitial.api.ATInterstitial;
import com.anythink.interstitial.api.ATInterstitialListener;
import com.voidid.core.baseclass.BaseAD;
import com.voidid.core.enums.ADEventType;
import com.voidid.core.enums.ADType;
import com.voidid.core.AppGlobal;
import com.voidid.core.tools.DebugTool;

import java.util.HashMap;
import java.util.Map;

public class InterstitialAd extends BaseAD implements ATInterstitialListener {
    public static String TAG = InterstitialAd.class.getName();

    private boolean isPlayEnded = false;

    /**
     * 获取广告对象
     *
     * @param placementID 广告ID
     * @return 广告对象
     */
    public static InterstitialAd getInstance(String placementID) {
        InterstitialAd ad = (InterstitialAd) InterstitialAd.adMap.get(placementID);

        if (ad == null) {
            ad = new InterstitialAd(placementID);
            ad.setID(placementID);
            InterstitialAd.adMap.put(placementID, ad);
        }

        return ad;
    }

    /**
     * 构造函数
     *
     * @param placementID 广告ID
     */
    public InterstitialAd(String placementID) {
        DebugTool.d(TAG, "create()");
        this.ad = new ATInterstitial(AppGlobal.getContext(), placementID);
        ((ATInterstitial) this.ad).setAdListener(this);
        this.load();
    }

    /**
     * 广告是否就绪
     *
     * @return isReady
     */
    public boolean isReady() {
        return ((ATInterstitial) ad).isAdReady();
    }


    @Override
    public void load() {
        DebugTool.d(TAG, "[fl-api]load()");
        if (((ATInterstitial) this.ad).checkAdStatus().isLoading()) {
            DebugTool.d(TAG, "[fl-api]插屏视频正在加载，请勿重复加载...");
            return;
        }
        ((ATInterstitial) ad).load();
    }

    @Override
    public void show() {
        DebugTool.d(TAG, "[fl-api]show()");
        this.isPlayEnded = false;
        if (this.isReady()) {
            ((ATInterstitial) ad).show(AppGlobal.getMainActivity());
            this.isLoadToShow = false;
        } else {
            // 广告未就绪，重新触发加载
            this.load();
            this.isLoadToShow = true;
        }
    }

    @Override
    public void destroy() {
        DebugTool.d(TAG, "[fl-api]destroy()");
    }

    @Override
    public void onInterstitialAdLoaded() {
        DebugTool.d(TAG, "[fl-api]插屏视频加载成功");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad loaded.");
        sendEvent(ADType.INTERSTITIAL, ADEventType.LOADED, map);

        if (this.isLoadToShow) {
            this.show();
        }
    }

    @Override
    public void onInterstitialAdLoadFail(AdError adError) {
        DebugTool.e(TAG, "[fl-api]插屏视频加载失败:" + adError.getFullErrorInfo());

        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", adError.getDesc());
        map.put("errCode", adError.getCode());
        sendEvent(ADType.INTERSTITIAL, ADEventType.LOAD_FAILED, map);
    }

    @Override
    public void onInterstitialAdClicked(ATAdInfo atAdInfo) {
        DebugTool.d(TAG, "[fl-api]插屏视频被点击");

        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad clicked");
        sendEvent(ADType.INTERSTITIAL, ADEventType.CLICKED, map);
    }

    @Override
    public void onInterstitialAdShow(ATAdInfo atAdInfo) {
        DebugTool.d(TAG, "[fl-api]插屏视频展示");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad showed.");
        sendEvent(ADType.INTERSTITIAL, ADEventType.SHOWED, map);
    }

    @Override
    public void onInterstitialAdClose(ATAdInfo atAdInfo) {
        DebugTool.d(TAG, "[fl-api]插屏视频关闭");
        // 加载下一次的广告
        this.load();

        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "play ended.");
        map.put("isEnded", isPlayEnded);
        sendEvent(ADType.INTERSTITIAL, ADEventType.CLOSED, map);
    }

    @Override
    public void onInterstitialAdVideoStart(ATAdInfo atAdInfo) {
        DebugTool.d(TAG, "[fl-api]插屏视频开始播放");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad showed.");
        sendEvent(ADType.INTERSTITIAL, ADEventType.PLAY_VIDEO_STARTED, map);
    }

    @Override
    public void onInterstitialAdVideoEnd(ATAdInfo atAdInfo) {
        DebugTool.d(TAG, "[fl-api]插屏视频播放结束");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad play end.");
        sendEvent(ADType.INTERSTITIAL, ADEventType.PLAY_VIDEO_ENDED, map);
        this.isPlayEnded = true;
    }

    @Override
    public void onInterstitialAdVideoError(AdError adError) {
        DebugTool.d(TAG, "[fl-api]插屏视频发生错误");

        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", adError.getDesc());
        map.put("errCode", adError.getCode());
        sendEvent(ADType.INTERSTITIAL, ADEventType.PLAY_VIDEO_FAILED, map);
    }
}
