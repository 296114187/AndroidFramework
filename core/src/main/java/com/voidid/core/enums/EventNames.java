package com.voidid.core.enums;

/**
 * 事件名称定义
 */
public class EventNames {

    /** Activity执行创建回调 */
    public static final String ACTIVITY_ON_CREATE = "ACTIVITY_ON_CREATE";
    /** 应用切到前台 */
    public static final String ACTIVITY_ON_RESUME = "ACTIVITY_ON_RESUME";
    /** 应用切到后台 */
    public static final String ACTIVITY_ON_PAUSE = "ACTIVITY_ON_PAUSE";
    /** 释放Activity */
    public static final String ACTIVITY_ON_DESTROY = "ACTIVITY_ON_DESTROY";
    /** 重启 */
    public static final String ACTIVITY_ON_RESTART = "ACTIVITY_ON_RESTART";
    /** 停止 */
    public static final String ACTIVITY_ON_STOP = "ACTIVITY_ON_STOP";
    /** 按下返回按钮 */
    public static final String ACTIVITY_CLICK_BACK_BTN = "ACTIVITY_CLICK_BACK_BTN";
    /** 开始 */
    public static final String ACTIVITY_ON_START = "ACTIVITY_ON_START";




    /* ******************* 事件埋点相关 ***************************** */

    /** 统计事件 */
    public static final String EVENT_ANALYTICS = "EVENT_ANALYTICS";
    /** 广告事件 */
    public static final String EVENT_AD = "EVENT_AD";
    /** 自定义事件 */
    public static final String EVENT_CUSTOM = "EVENT_CUSTOM";
    /** 事件 -- 剧情 */
    public static final String EVENT_PLOT = "EVENT_PLOT";
    /** 事件 -- 道具获得、消耗 */
    public static final String EVENT_PROP = "EVENT_PROP";
    /** 事件 -- 货币获得、消耗 */
    public static final String EVENT_CURRENCY = "EVENT_CURRENCY";
    /** 事件 -- 支付 */
    public static final String EVENT_PAY = "EVENT_PAY";
}
