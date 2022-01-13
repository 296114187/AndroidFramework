package com.voidid.core;

import com.voidid.core.enums.EDBType;

public class AppConfig {
    // 是否启用调试日志
    public static boolean ENABLE_DEBUG_LOG = false;

    // 是否显示开屏广告
    public static boolean isShowSplash = true;
    // 标识设备安装时的来源渠道。设备仅记录首次安装激活的渠道，在其他渠道再次安装不会重复计量。
    // 英文字母、阿拉伯数字、下划线、中划线、空格、括号组成，不要使用纯数字作为渠道ID，“unknown” 及其各种大小写形式，作为【友盟+】保留的字段，不可以作为渠道名
    public static String CHANNEL = "";
//            BuildConfig.CHANNEL;






    //****************************************************
    // 【数据库】
    //****************************************************
    /** 启用的数据库类型 */
    public static EDBType enableDBType = EDBType.SQLite;



    //****************************************************
    // 【腾讯实名认证YSDK】
    //****************************************************
    public static boolean enableYSDK = true;






    //****************************************************
    // 【友盟+】
    //****************************************************
    // 友盟+为每一个应用生成的唯一标识码，用来在数据计算时，区分数据是哪个应用的
    public static String UMENG_APP_KEY = "61d58d44e0f9bb492bbcc89f";
    // 标识设备安装时的来源渠道。设备仅记录首次安装激活的渠道，在其他渠道再次安装不会重复计量。
    // 英文字母、阿拉伯数字、下划线、中划线、空格、括号组成，不要使用纯数字作为渠道ID，“unknown” 及其各种大小写形式，作为【友盟+】保留的字段，不可以作为渠道名
    public static String UMENG_APP_CHANNEL = AppConfig.CHANNEL;




    //****************************************************
    // 【TopOn】
    //****************************************************
    // 为每一个应用生成的唯一标识码
    public static String TOP_ON_APP_ID = "a61133ce3ac507";
    // 为每一个应用生成的唯一标识码
    public static String TOP_ON_APP_KEY = "561e9243d5827981992eb26e573c26fd";
    // 标识设备安装时的来源渠道。设备仅记录首次安装激活的渠道，在其他渠道再次安装不会重复计量。
    // 英文字母、阿拉伯数字、下划线、中划线、空格、括号组成，不要使用纯数字作为渠道ID，“unknown” 及其各种大小写形式，作为保留的字段，不可以作为渠道名
    public static String TOP_ON_APP_CHANNEL = AppConfig.CHANNEL;

//    /** Banner广告ID */
    public static String BANNER_AD_UNIT_ID = "b61133d65b1bc0";
    /** 激励视频广告ID */
    public static String REWARD_VIDEO_AD_UNIT_ID = "b61133d64f3ca4";
    /** 插屏广告ID */
    public static String INTERSTITIAL_AD_UNIT_ID = "b61133d6559c88";
    /** 原生广告ID */
    public static String NATIVE_AD_UNIT_ID = "b61133d6618a72";
    /** 开屏广告 */
    public static String SPLASH_AD_UNIT_ID = "b61133d65e33d8";
    /** Banner广告ID */
//    public static String BANNER_AD_UNIT_ID = "b7a2d915885c4fc0";
//    /** 激励视频广告ID */
//    public static String REWARD_VIDEO_AD_UNIT_ID = "66d411c08872ff0c";
//    /** 插屏广告ID */
//    public static String INTERSTITIAL_AD_UNIT_ID = "17abe47178832a53";
//    /** 原生广告ID */
//    public static String NATIVE_AD_UNIT_ID = "54a19b9da4cc878f";
//    /** 开屏广告 */
//    public static String SPLASH_AD_UNIT_ID = "b61133d65e33d8";




    //****************************************************
    // 【Max】
    //****************************************************




    //****************************************************
    // 【Tenjin】
    //****************************************************
    public static String tenjinApiKey = "YBVOZTLH6TTSFVXH9VVKWHEUCQPTMPN1";




    //****************************************************
    // 【Adjust】
    //****************************************************
    public static String adjustAppToken = "";
    /** 是否是正式的环境 */
    public static boolean isAdjustProduction = true;

    //****************************************************
    // 【GameAnalytics】
    //****************************************************
    public static String gameAnalyticsGameKey = "f8fc63247daacea26829fd3bfea83659";
    public static String gameAnalyticsSecretKey = "c3f135a4c1de4b40922dc66cd644218027a822d7";
}
