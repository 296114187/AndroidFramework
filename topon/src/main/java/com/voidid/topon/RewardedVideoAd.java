package com.voidid.topon;

import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.anythink.rewardvideo.api.ATRewardVideoAd;
import com.anythink.rewardvideo.api.ATRewardVideoListener;
import com.voidid.core.baseclass.BaseAD;
import com.voidid.core.enums.ADEventType;
import com.voidid.core.enums.ADType;
import com.voidid.core.AppGlobal;
import com.voidid.core.tools.DebugTool;

import java.util.HashMap;
import java.util.Map;

public class RewardedVideoAd extends BaseAD implements ATRewardVideoListener {
    public static String TAG = RewardedVideoAd.class.getName();

    private boolean isPlayEnded = false;

    /**
     * 获取RewardVideoAd广告对象
     *
     * @param placementID 广告ID
     * @return 广告对象
     */
    public static RewardedVideoAd getInstance(String placementID) {
        DebugTool.d(TAG, "[fl-api]获取激励视频广告，ID=" + placementID);
        RewardedVideoAd ad = (RewardedVideoAd) RewardedVideoAd.adMap.get(placementID);

        if (ad == null) {
            DebugTool.d(TAG, "[fl-api]未缓存激励视频对象，重新创建：" + placementID);
            ad = new RewardedVideoAd(placementID);
            ad.setID(placementID);
            RewardedVideoAd.adMap.put(placementID, ad);
        }

        return ad;
    }

    /**
     * 构造函数
     *
     * @param placementID 广告ID
     */
    public RewardedVideoAd(String placementID) {
        DebugTool.d(TAG, "create()");
        this.ad = new ATRewardVideoAd(AppGlobal.getContext(), placementID);
        ((ATRewardVideoAd) this.ad).setAdListener(this);
        this.load();
    }

    /**
     * 广告是否就绪
     *
     * @return isReady
     */
    public boolean isReady() {
        return ((ATRewardVideoAd) this.ad).isAdReady();
    }

    /**
     * 加载广告
     */
    public void load() {
        DebugTool.d(TAG, "[fl-api]load()");
        if (((ATRewardVideoAd) this.ad).checkAdStatus().isLoading()) {
            DebugTool.d(TAG, "[fl-api]激励视频正在加载，请勿重复加载...");
            return;
        }
        ((ATRewardVideoAd) this.ad).load();
    }

    /**
     * 显示广告
     */
    public void show() {
        DebugTool.d(TAG, "show()");
        this.isPlayEnded = false;
        if (this.isReady()) {
            ((ATRewardVideoAd) this.ad).show(AppGlobal.getMainActivity());
            this.isLoadToShow = false;
        } else {
            // 广告未就绪，重新触发加载
            this.load();
            this.isLoadToShow = true;
        }
    }

    public void destroy() {
        DebugTool.d(TAG, "destroy()");
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        // 加载完成
        DebugTool.d(TAG, "[fl-api]激励视频加载成功 isLoadToShow:" + this.isLoadToShow);

        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad loaded.");
        sendEvent(ADType.REWARDED, ADEventType.LOADED, map);
        if (this.isLoadToShow) {
            this.show();
        }
    }

    @Override
    public void onRewardedVideoAdFailed(AdError adError) {
        // 出现错误

        //AdError，请参考 https://docs.toponad.com/#/zh-cn/android/android_doc/android_test?id=aderror
        DebugTool.e(TAG, "[fl-api]激励视频加载失败:" + adError.getFullErrorInfo());
        DebugTool.e(TAG, "[fl-api]激励视频广告ID:" + this.adUnitId);

        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", adError.getDesc());
        map.put("errCode", adError.getCode());
        sendEvent(ADType.REWARDED, ADEventType.LOAD_FAILED, map);
    }

    @Override
    public void onRewardedVideoAdPlayStart(ATAdInfo atAdInfo) {
        // 开始播放
        DebugTool.d(TAG, "[fl-api]激励视频开始播放");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad showed.");
        sendEvent(ADType.REWARDED, ADEventType.PLAY_VIDEO_STARTED, map);
    }

    @Override
    public void onRewardedVideoAdPlayEnd(ATAdInfo atAdInfo) {
        // 播放结束
        DebugTool.d(TAG, "[fl-api]激励视频播放结束");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad play end.");
        sendEvent(ADType.REWARDED, ADEventType.PLAY_VIDEO_ENDED, map);
        // 加载下一次的广告
        this.load();
    }

    @Override
    public void onRewardedVideoAdPlayFailed(AdError adError, ATAdInfo atAdInfo) {
        // 播放失败
        //注意：禁止在此回调中执行广告的加载方法进行重试，否则会引起很多无用请求且可能会导致应用卡顿
        //AdError，请参考 https://docs.toponad.com/#/zh-cn/android/android_doc/android_test?id=aderror
        DebugTool.e(TAG, "[fl-api]激励视频播放结束失败:" + adError.getFullErrorInfo());

        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", adError.getDesc());
        map.put("errCode", adError.getCode());
        sendEvent(ADType.REWARDED, ADEventType.PLAY_VIDEO_FAILED, map);
    }

    @Override
    public void onRewardedVideoAdClosed(ATAdInfo atAdInfo) {
        // 广告关闭
        DebugTool.d(TAG, "[fl-api]激励视频关闭");

        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "play ended.");
        map.put("isEnded", isPlayEnded);
        sendEvent(ADType.REWARDED, ADEventType.CLOSED, map);
    }

    @Override
    public void onReward(ATAdInfo atAdInfo) {
        //建议在此回调中下发奖励，一般在onRewardedVideoAdClosed之前回调
        DebugTool.d(TAG, "[fl-api]激励视频播放结束-下发奖励");
        isPlayEnded = true;
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "get Reward.");
        sendEvent(ADType.REWARDED, ADEventType.REWARDED, map);
    }

    @Override
    public void onRewardedVideoAdPlayClicked(ATAdInfo atAdInfo) {
        // 点击广告
        DebugTool.d(TAG, "[fl-api]激励视频被点击");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad clicked");
        sendEvent(ADType.REWARDED, ADEventType.CLICKED, map);
    }
}
