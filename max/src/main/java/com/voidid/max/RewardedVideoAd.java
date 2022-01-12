package com.voidid.max;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.voidid.core.baseclass.BaseAD;
import com.voidid.core.enums.ADEventType;
import com.voidid.core.enums.ADType;
import com.voidid.core.AppGlobal;
import com.voidid.core.tools.DebugTool;

import java.util.HashMap;
import java.util.Map;

public class RewardedVideoAd extends BaseAD implements MaxRewardedAdListener {
    private static final String TAG = RewardedVideoAd.class.getName();
    private boolean isPlayEnded = false;

    private boolean isLoading = false;

    /**
     * 获取RewardVideoAd广告对象
     * @param placementID 广告ID
     * @return 广告对象
     */
    public static RewardedVideoAd getInstance(String placementID) {
        RewardedVideoAd ad = (RewardedVideoAd) RewardedVideoAd.adMap.get(placementID);
        if (ad == null) {
            ad = new RewardedVideoAd(placementID);
            ad.setID(placementID);
            RewardedVideoAd.adMap.put(placementID, ad);
        }
        return ad;
    }

    public RewardedVideoAd(String placementID) {
        this.ad = MaxRewardedAd.getInstance(placementID, AppGlobal.getMainActivity());
        ((MaxRewardedAd)this.ad).setListener(this);
        this.load();
    }


    /**
     * 广告是否就绪
     * @return 就绪与否
     */
    public boolean isReady() {
        return ((MaxRewardedAd)this.ad).isReady();
    }

    @Override
    public void load() {
        if (this.isLoading) {
            DebugTool.d(TAG, "激励视频正在加载，请勿重复加载...");
            return;
        }

        this.isLoading = true;
        ((MaxRewardedAd)this.ad).loadAd();
    }

    @Override
    public void show() {
        DebugTool.d(TAG, "show()");
        this.isPlayEnded = false;
        if (this.isReady()) {
            ((MaxRewardedAd)this.ad).showAd();
            this.isLoadToShow = false;
        } else {
            // 广告未就绪，重新触发加载
            this.load();
            this.isLoadToShow = true;
        }
    }

    @Override
    public void destroy() {
        DebugTool.d(TAG, "destroy()");
    }

    @Override
    public void onAdLoaded(MaxAd ad) {
        // 加载完成
        DebugTool.d(TAG, "激励视频加载成功 isLoadToShow:" + this.isLoadToShow);

        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad loaded.");
        sendEvent(ADType.REWARDED, ADEventType.LOADED, map);

        this.isLoading = false;
        if (this.isLoadToShow) {
            this.show();
        }
    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError adError) {
        // 出现错误
        DebugTool.e(TAG, "激励视频加载失败:" + adError.getMessage());
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", adError.getMessage());
        map.put("errCode", adError.getCode());
        sendEvent(ADType.REWARDED, ADEventType.LOAD_FAILED, map);

        this.isLoading = false;
    }

    @Override
    public void onAdDisplayed(MaxAd ad) {
        DebugTool.d(TAG, "RewardedVideoAd.onAdDisplayed");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad showed.");
        sendEvent(ADType.REWARDED, ADEventType.PLAY_VIDEO_STARTED, map);
    }

    @Override
    public void onAdHidden(MaxAd ad) {
        DebugTool.d(TAG, "RewardedVideoAd.onAdHidden");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "play ended.");
        map.put("isEnded", isPlayEnded);
        sendEvent(ADType.REWARDED, ADEventType.CLOSED, map);
        this.load();
    }

    @Override
    public void onAdClicked(MaxAd ad) {
        // 点击广告
        DebugTool.d(TAG, "激励视频被点击");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad clicked");
        sendEvent(ADType.REWARDED, ADEventType.CLICKED, map);
    }

    @Override
    public void onRewardedVideoStarted(MaxAd ad) {
        // 开始播放
        DebugTool.d(TAG, "激励视频开始播放");
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad showed.");
        sendEvent(ADType.REWARDED, ADEventType.PLAY_VIDEO_STARTED, map);
    }

    @Override
    public void onRewardedVideoCompleted(MaxAd ad) {
        DebugTool.d(TAG, "RewardedVideoAd.onRewardedVideoCompleted");

        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "ad play end.");
        sendEvent(ADType.REWARDED, ADEventType.PLAY_VIDEO_ENDED, map);
    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError adError) {
        this.isLoading = false;
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", adError.getMessage());
        map.put("errCode", adError.getCode());
        sendEvent(ADType.REWARDED, ADEventType.PLAY_VIDEO_FAILED, map);
        this.load();
    }

    @Override
    public void onUserRewarded(MaxAd ad, MaxReward reward) {
        DebugTool.d(TAG, "下发奖励");
        isPlayEnded = true;
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", "get Reward.");
        sendEvent(ADType.REWARDED, ADEventType.REWARDED, map);
    }
}
