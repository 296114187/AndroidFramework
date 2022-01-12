package com.voidid.core.jsb;

import static android.content.Context.VIBRATOR_SERVICE;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Process;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.voidid.core.AppGlobal;
import com.voidid.core.tools.DataTool;
import com.voidid.core.tools.DebugTool;

import org.json.JSONObject;

import java.net.Socket;

public class DeviceManager {
    private static final String TAG = DeviceManager.class.getName();

    private static String oaid = null;
    private static final JSONObject systemInfo = new JSONObject();

    /**
     * 初始化设备信息
     */
    public static void init() {
        DebugTool.d(TAG, "DeviceTool.init()");
//        MdidSdkHelper.InitSdk(BaseGlobal.context, true, new IIdentifierListener() {
//            @Override
//            public void OnSupport(boolean b, IdSupplier idSupplier) {
//                try {
//                    DeviceTool.setOaid(idSupplier.getOAID());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });


        try {
            // 主板
            systemInfo.put("board", Build.BOARD);
            // 系统定制商
            systemInfo.put("brand", Build.BRAND);
            //CPU指令集
            systemInfo.put("supported_abis", Build.SUPPORTED_ABIS);
            // 设备参数
            systemInfo.put("device", Build.DEVICE);
            // 显示屏参数
            systemInfo.put("display", Build.DISPLAY);
            // 唯一编号
            systemInfo.put("fingderprint", Build.FINGERPRINT);
            // 硬件序列号
//            systemInfo.put("SERIAL", Build.getSerial());
            // 修订版本列表
            systemInfo.put("id", Build.ID);
            // 硬件制造商
            systemInfo.put("manufacturer", Build.MANUFACTURER);
            //版本
            systemInfo.put("model", Build.MODEL);
            //硬件名
            systemInfo.put("hardware", Build.HARDWARE);
            //手机产品名
            systemInfo.put("product", Build.PRODUCT);
            // 描述build的标签
            systemInfo.put("tags", Build.TAGS);
            // Builder类型
            systemInfo.put("type", Build.TYPE);
            //当前开发代号
            systemInfo.put("version_codename", Build.VERSION.CODENAME);
            //源码控制版本号
            systemInfo.put("version_incremental", Build.VERSION.INCREMENTAL);
            //版本字符串
            systemInfo.put("version_release", Build.VERSION.RELEASE);
            //版本号
            systemInfo.put("version_sdk_int", Build.VERSION.SDK_INT);
            // HOST值
            systemInfo.put("host", Build.HOST);
            // User名
            systemInfo.put("user", Build.USER);
            // 编译时间
            systemInfo.put("build_time", Build.TIME);
            // OS版本号
            systemInfo.put("os_version", System.getProperty("os.version"));
            // OS名称
            systemInfo.put("os_name", System.getProperty("os.name"));
            // OS架构
            systemInfo.put("os_arch", System.getProperty("os.arch"));
            //home属性
            systemInfo.put("user_home", System.getProperty("user.home"));
            // name属性
            systemInfo.put("user_name", System.getProperty("user.name"));
            //dir属性
            systemInfo.put("user_dir", System.getProperty("user.dir"));
            //时区
            systemInfo.put("user_timezone", System.getProperty("user.timezone"));
            //路径分隔符
            systemInfo.put("path_separator", System.getProperty("path.separator"));
            // 行文分隔符
            systemInfo.put("line_separator", System.getProperty("line.separator"));
            //文件分隔符
            systemInfo.put("file_separator", System.getProperty("file.separator"));
            // Java vender URL属性
            systemInfo.put("java_verdor_url", System.getProperty("java.vendor.url"));
            // Java Class路径
            systemInfo.put("jave_class_path", System.getProperty("java.class.path"));
            // Java class版本
            systemInfo.put("jave_class_version", System.getProperty("java.class.version"));
            // java vender 属性
            systemInfo.put("java_vendor", System.getProperty("java.vendor"));
            // Java 版本
            systemInfo.put("java_version", System.getProperty("java.version"));
            // Java Home 属性
            systemInfo.put("java_home", System.getProperty("java.home"));

            // 系统名称及版本
            systemInfo.put("system", systemInfo.get("os_name") + " " + systemInfo.get("os_version"));
            // 平台
            systemInfo.put("platform", "Android");
            // 设备方向
            int orient = AppGlobal.getApplication().getResources().getConfiguration().orientation;
            systemInfo.put("deviceOrientation", orient == Configuration.ORIENTATION_PORTRAIT ? "portrait" : "landscape");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取设备信息
     *
     * @return 设备信息
     */
    public static String getSystemInfoSync() {
        try {
            String platform = (String) DeviceManager.systemInfo.get("platform");

            if (platform.equals("")) {
                DebugTool.d(TAG, "DeviceTool.init()");
                DeviceManager.init();
            }
        } catch (Exception ex) {
            DebugTool.d(TAG, "getSystemInfoSync error:" + ex.getMessage());
            ex.printStackTrace();
            DeviceManager.init();
        }

        DebugTool.d(TAG, "getSystemInfoSync info: ", DeviceManager.systemInfo.toString());
        return DeviceManager.systemInfo.toString();
    }

    /**
     * 获取本地设备ID
     */
    public static String getDeviceId() {
        return "test device id";
    }

    /**
     * 移动安全联盟获取OAID方式
     */
    public static String getOaid() {
        if (oaid != null && !oaid.equals("")) {
            return DeviceManager.oaid;
        }

        return null;
    }

    /**
     * 设置OAID
     *
     * @param oaid 设备oaid
     */
    public static void setOaid(String oaid) {
        if (oaid != null && !oaid.equals("")) {
            return;
        }

        DebugTool.d(TAG, "set OAID:", oaid);
        DeviceManager.oaid = oaid;
    }

    /**
     * 获取电量和充电状态
     *
     * @return
     */
    public static String getBatteryInfoSync() {
        JSONObject jsonObj = new JSONObject();

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = AppGlobal.getContext().registerReceiver(null, ifilter);

        try {
            // 获取充电状态
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            jsonObj.put("isCharging", status == BatteryManager.BATTERY_PLUGGED_USB || status == BatteryManager.BATTERY_PLUGGED_AC);

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

//        float batteryPct = level * 100 / (float)scale;
            jsonObj.put("level", level);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "{\"level\": 0, \"isCharging\": false}";
        }

        return jsonObj.toString();
    }

    /**
     * 显示吐司提示
     *
     * @param content 文本内容
     * @param isLong  是否长时间显示
     */
    public static void showToast(String content, boolean isLong) {
        Toast.makeText(AppGlobal.getContext(), content, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示模态弹窗
     *
     * @param modelStr            弹窗内容
     * @param cancelCallbackName  取消回调
     * @param confirmCallbackName 确定回调
     */
    public static void showModal(String modelStr, String cancelCallbackName, String confirmCallbackName) throws Exception {
        try {
            JSONObject jsonObj = DataTool.parseJson(modelStr);
            AlertDialog.Builder dialog = new AlertDialog.Builder(AppGlobal.getContext());

            dialog.setMessage(jsonObj.getString("content"));
            dialog.setTitle(jsonObj.getString("title"));
            dialog.setPositiveButton(jsonObj.getString("confirmText"), (DialogInterface dia, int which) -> {
                String jsCode = confirmCallbackName + "()";
                JSBridgeManager.postMessageToJS(jsCode);
            });
            dialog.setNeutralButton(jsonObj.getString("cancelText"), (DialogInterface dia, int which) -> {
                String jsCode = cancelCallbackName + "()";
                JSBridgeManager.postMessageToJS(jsCode);
            });
            dialog.show();
        } catch (Exception ex) {
            DebugTool.e(TAG, ex.getMessage());
        }
    }

    /**
     * 退出当前进程
     */
    public static void exitApp() {
        Process.killProcess(Process.myPid());
    }


    /**
     * 设置系统剪贴板的内容.
     *
     * @param content 剪贴板的内容.
     */
    public static void setClipboardData(String content) {
        DebugTool.d(TAG, "复制'" + content + "'到剪切板");
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) AppGlobal.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", content);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }

    /**
     * 获取系统剪贴板的内容.
     */
    public static String getClipboardData() {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) AppGlobal.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = cm.getPrimaryClip();
        ClipData.Item item = clip.getItemAt(0);

        if (item != null) {
            return item.getText().toString();
        }

        return null;
    }

    /**
     * 打开浏览器
     *
     * @param url 链接
     */
    public static void openBrowserUrl(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        AppGlobal.getContext().startActivity(intent);
    }


    /**
     * 设备震动
     *
     * @param time      震动时长
     * @param amplitude 振幅
     */
    public static void vibrator(int time, int amplitude) {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) AppGlobal.getMainActivity().getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(time, amplitude));
        } else {
            ((Vibrator) AppGlobal.getMainActivity().getSystemService(VIBRATOR_SERVICE)).vibrate(time);
        }
    }

    /**
     * 设置屏幕常亮
     *
     * @param keepScreenOn 是否常亮
     */
    public static void setKeepScreenOn(boolean keepScreenOn) {
        Window window = AppGlobal.getMainActivity().getWindow();

        if (keepScreenOn) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    /**
     * 设置屏幕亮度(不影响其它APP)
     *
     * @param value 亮度参数[0, 1]
     */
    public static void setScreenBrightness(float value) {
        Window window = AppGlobal.getMainActivity().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = value;
        window.setAttributes(lp);
    }

    /**
     * 获取屏幕亮度
     *
     * @return
     */
    public static float getScreenBrightness() {
        Window window = AppGlobal.getMainActivity().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        return lp.screenBrightness;
    }

    /**
     * 获取本地IP地址
     *
     * @return
     */
    public static String getLocalIPAddress() {
        Socket socket = null;
        try {
            // 这里也可以使用ip，比如使用自己公司服务器的ip和端口
            socket = new Socket("baidu.com", 80);
            return socket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            DebugTool.e(TAG, e.getMessage());
        }
        return null;
    }
}
