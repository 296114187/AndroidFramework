package com.voidid.core.jsb;

import android.util.Log;

import com.voidid.core.AppGlobal;
import com.voidid.core.tools.DataTool;
import com.voidid.core.tools.DebugTool;
import com.voidid.core.enums.ADEventType;
import com.voidid.core.enums.ADType;
import com.voidid.core.enums.EventNames;
import com.voidid.core.event.EventManager;

import org.json.JSONObject;

public class JSBridgeManager {
    private static final String TAG = JSBridgeManager.class.getName();

    public static void init() {
        DebugTool.d(TAG, "JSBridge.init()");
        JSBridgeManager.initAdCallback();
    }

    /**
     * 回调JS全局函数
     * @param jsCode JS代码
     */
    public static void postMessageToJS(String jsCode) {
        JSBridgeManager.postMessageToJS(jsCode, true);
    }

    /**
     * 回调JS全局函数
     * @param jsCode JS代码
     * @param isShowLog 是否显示日志
     */
    public static void postMessageToJS(String jsCode, boolean isShowLog) {
        if (AppGlobal.getMainActivity() == null) {
            DebugTool.e(TAG, "BaseGlobal.mainActivity is null");
            return;
        }

        // 一定要在 GL 线程中执行
        try {
            if (isShowLog) {
                DebugTool.d(TAG, "postMessageToJS: " + jsCode);
            }
//            ((Cocos2dxActivity)BaseGlobal.mainActivity).runOnGLThread(() -> Cocos2dxJavascriptJavaBridge.evalString(jsCode));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 回调JS全局函数
     * @param staticMethodName 全局函数名
     * @param params 参数数组
     */
    public static void postMessageToJS(String staticMethodName, JSONObject params) {
        // 构造JS代码
        String jsCode = staticMethodName + "('" + params.toString() + "')";
        JSBridgeManager.postMessageToJS(jsCode);
    }

    private static void initAdCallback() {
        EventManager.on(EventNames.EVENT_AD, (String eventName, JSONObject data)->{
            String className = "", methodName = "";
            boolean isError = false;
            Log.i(TAG, "initAdCallback: " + data.toString());
            switch ((ADType)data.get("adType")) {
                case SPLASH: return;
                case BANNER: className = "BannerAd"; break;
                case NATIVE: className = "NativeAd"; break;
                case REWARDED: className = "RewardedVideoAd"; break;
                case INTERSTITIAL: className = "InterstitialAd"; break;
            }

            switch ((ADEventType)data.get("adEventType")) {
                case LOADED: methodName = "onLoadedCallback"; break;
                case SHOWED: methodName = "onShowedCallback"; break;
                case CLOSED: methodName = "onClosedCallback"; break;
                case CREATED: methodName = "onCreatedCallback"; break;
                case CLICKED: methodName = "onClickedCallback"; break;
                case REWARDED: methodName = "onRewardedCallback"; break;
                case LOAD_FAILED: methodName = "onLoadFailedCallback"; isError = true; break;
                case SHOW_FAILED: methodName = "onShowFailedCallback"; isError = true; break;
                case PLAY_VIDEO_ENDED: methodName = "onPlayEndedCallback"; break;
                case PLAY_VIDEO_FAILED: methodName = "onPlayVideoFailedCallback"; isError = true; break;
                case PLAY_VIDEO_STARTED: methodName = "onPlayVideoStartedCallback"; break;
            }
            // JS本身就有携带回调函数名
            try {
                String jsCode = (String) data.get("callback");
                if (!jsCode.equals("")) {
                    if (isError) {
                        jsCode += "(" + data.toString() + ")";
                    } else {
                        jsCode += "(undefined, " + data.toString() + ")";
                    }
                    postMessageToJS(jsCode);
                } else {
                    DebugTool.d(TAG, EventNames.EVENT_AD, "callback", jsCode);
                }
            } catch (Exception ex) {
                DebugTool.e(TAG, ex.getMessage());
            }
            postMessageToJS(className + "." + methodName, data);
        });
    }

    /**
     * 触发广告事件
     * @param jsonStr 广告参数
     * @throws Exception 异常
     */
    public static void emitEventAD(String jsonStr) throws Exception {
        DebugTool.i(TAG, "call native emitEventAD:" + jsonStr);
        JSONObject jsonObj = DataTool.parseJson(jsonStr);

//        jsonObj.put("adType", ADType.BANNER);
//        jsonObj.put("adEventType", ADEventType.CREATE);

        EventManager.emit(EventNames.EVENT_AD, jsonObj);
    }

    /**
     * 触发埋点统计事件
     * @param jsonStr 事件参数 {analyticsEventType: string, key: any, value?: any}
     * @throws Exception 异常
     */
    public static void emitEventAnalytics(String jsonStr) throws Exception {
        DebugTool.i(TAG, "call native emitEventAD:" + jsonStr);

        JSONObject jsonObj = DataTool.parseJson(jsonStr);
        EventManager.emit(EventNames.EVENT_ANALYTICS, jsonObj);
    }
}
