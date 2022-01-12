package com.voidid.core.enums;


import androidx.annotation.NonNull;

/**
 * 广告类型
 * @author Guo
 */
public enum ADType {
    /** 横幅广告 **/
    BANNER("BANNER"),
    /** 激励视频广告 **/
    REWARDED("REWARDED"),
    /** 原生广告 **/
    NATIVE("NATIVE"),
    /** 开屏广告 **/
    SPLASH("SPLASH"),
    /** 插屏广告 **/
    INTERSTITIAL("INTERSTITIAL");

    private final String text;
    ADType(final String text){
        this.text = text;
    }
    @NonNull
    @Override
    public String toString(){
        return text;
    }
}
