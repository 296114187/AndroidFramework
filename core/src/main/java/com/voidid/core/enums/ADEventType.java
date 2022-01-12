package com.voidid.core.enums;


import androidx.annotation.NonNull;

/**
 * 广告回调枚举
 * @author Guo
 */
public enum ADEventType {
    /** 创建广告 */
    CREATE("CREATE"),
    /** 销毁广告 */
    DESTROY("DESTROY"),
    /** 广告加载 **/
    LOAD("LOAD"),
    /** 广告展示 **/
    SHOW("SHOW"),
    /** 广告隐藏 **/
    HIDE("HIDE"),

    /** 广告创建成功 **/
    CREATED("CREATED"),
    /** 广告加载成功 **/
    LOADED("LOADED"),
    /** 广告显示成功 **/
    SHOWED("SHOWED"),
    /** 广告被点击 **/
    CLICKED("CLICKED"),
    /** 广告已关闭 **/
    CLOSED("CLOSED"),

    /** 广告加载失败 **/
    LOAD_FAILED("LOAD_FAILED"),
    /** 广告展示点击 **/
    SHOW_FAILED("SHOW_FAILED"),
    /** 视频播放结束 **/
    PLAY_VIDEO_ENDED("PLAY_VIDEO_ENDED"),
    /** 视频播放开始 **/
    PLAY_VIDEO_STARTED("PLAY_VIDEO_STARTED"),
    /** 视频播放失败 **/
    PLAY_VIDEO_FAILED("PLAY_VIDEO_FAILED"),
    /** 获得奖励 **/
    REWARDED("REWARDED");

    private final String text;
    ADEventType(final String text){
        this.text = text;
    }
    @NonNull
    @Override
    public String toString(){
        return text;
    }
}


