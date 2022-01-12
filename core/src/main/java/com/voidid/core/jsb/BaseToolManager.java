package com.voidid.core.jsb;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.voidid.core.AppGlobal;
import com.voidid.core.tools.DebugTool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class BaseToolManager {
    private static final String TAG = BaseToolManager.class.getName();
    private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte

    /**
     * 获取原生渠道名称
     * @return 渠道名称
     */
    public static String getChannel() {
//        DebugTool.i(TAG, "call native getChannel.", BuildConfig.CHANNEL);
//        return BuildConfig.CHANNEL;
        return "";
    }

    /**
     * 获取当前应用包名
     * @return 包名
     */
    public static String getPackageName() {
        return AppGlobal.getContext().getPackageName();
    }

    /**
     * 获取当前应用版本号
     * @return 版本号
     */
    public static String getVersion() {
        PackageManager pm = AppGlobal.getContext().getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(BaseToolManager.getPackageName(), 0);
            return packinfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前应用名称
     * @return 应用名称
     */
    public static String getAppName() {
        PackageManager pm = AppGlobal.getContext().getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(BaseToolManager.getPackageName(), 0);
            return info.loadLabel(pm).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * 下载文件
     * @param url 文件的URL
     * @param storeFile 本地缓存位置
     * @param progressCallbackName 进度更新回调静态函数名
     * @param completeCallbackName 下载完成回调静态函数名
     */
    public static void downloadFile(String url, String storeFile, String progressCallbackName, String completeCallbackName) {
        DebugTool.d(TAG, "downloadFile:", url, storeFile);
        Thread subThread;

        subThread = new Thread(() -> {
            String jsCode;

            try {
                // 判断本地是否已经存在同名文件
                File localFile = new File(storeFile);
                if (localFile.exists()) {
                    throw new Exception("File exists:" + storeFile + ".");
                }

                URL remoteUrl = new URL(url);
                // 获取网络流的总大小
                int total = remoteUrl.openConnection().getContentLength();
                // 当前已下载的流的总大小
                int current = 0;
                InputStream iStream = remoteUrl.openStream();
                // 打开手机对应的输出流，输出到文件中
//            OutputStream oStream = getContext().openFileOutput(storeFile, Context.MODE_PRIVATE);
                FileOutputStream oStream = BaseToolManager.openFileOutput(storeFile);

                byte[] buffer = new byte[1024];
                int len;
                // 从输入流中读取数据，读到缓冲区中
                while((len = iStream.read(buffer)) > 0) {
                    oStream.write(buffer,0, len);
                    current += len;
                    // JS progress callback.
                    if (progressCallbackName != null && !progressCallbackName.equals("")) {
                        jsCode = progressCallbackName + "(" + current + ", " + total + ")";
                        JSBridgeManager.postMessageToJS(jsCode, false);
                    }
                }

                // 关闭输入输出流
                iStream.close();
                oStream.close();

                // 下载完成
                String res = "{url: '" + url + "', storeFile: '" + storeFile + "', file: '" + storeFile + "'}";
                jsCode = completeCallbackName + "(undefined, " + res + ")";
                JSBridgeManager.postMessageToJS(jsCode);
            } catch (Exception ex) {
                // 下载异常
                String err = "{errMsg: '" + ex.getMessage() + "', url: '" + url + "'}";
                jsCode = completeCallbackName + "(" + err + ")";
                JSBridgeManager.postMessageToJS(jsCode);
            }
        });
        subThread.start();
    }

    /**
     * 创建文件输出流
     * @param filePath 文件路径
     * @return 文件输出流
     */
    private static FileOutputStream openFileOutput(String filePath) throws Exception {
        File file = new File(filePath);

        if (!file.exists()) {
            File dir = new File(Objects.requireNonNull(file.getParent()));
            boolean isSuccess = dir.mkdir();
            if (!isSuccess) DebugTool.e(TAG, "创建文件目录失败");
            isSuccess = file.createNewFile();
            if (!isSuccess) DebugTool.e(TAG, "创建文件失败");
        }

        return new FileOutputStream(file);
    }

    /**
     * 下载文件
     * @param zipFilePath 压缩文件地址
     * @param saveDir 解压缓存目录
     * @param progressCallbackName 进度更新回调静态函数名
     * @param completeCallbackName 下载完成回调静态函数名
     */
    public static void unzipFile(String zipFilePath, String saveDir, String progressCallbackName, String completeCallbackName) {
        String jsCode;

        try {
            ZipFile zf = new ZipFile(zipFilePath);
            for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
                ZipEntry entry = ((ZipEntry)entries.nextElement());
                String str = saveDir + File.separator + entry.getName();
                // 跳过某些文件
                if (str.contains("MACOSX")) {
                    continue;
                }

                InputStream iStream = zf.getInputStream(entry);
                str = new String(str.getBytes("8859_1"), "GB2312");
                File desFile = new File(str);

                // 创建文件
                if (!desFile.exists()) {
                    boolean isDirectory = desFile.isDirectory() || str.endsWith("/");
                    // 如果是个文件夹
                    if (isDirectory) {
                        boolean isSuccess = desFile.mkdirs();
                        if (!isSuccess) DebugTool.e(TAG, "创建文件目录失败");
                        iStream.close();
                        continue;
                    }

                    // 如果是文件
                    File fileParentDir = desFile.getParentFile();
                    assert fileParentDir != null;
                    if (!fileParentDir.exists()) {
                        boolean isSuccess = fileParentDir.mkdirs();
                        if (!isSuccess) DebugTool.e(TAG, "创建文件目录失败");
                    }
                    boolean isSuccess = desFile.createNewFile();
                    if (!isSuccess) DebugTool.e(TAG, "创建文件失败");
                } else if (desFile.isDirectory()) {
                    // 是文件夹，且已存在，进入下一次循环
                    continue;
                }
                // 输出到文件
                OutputStream oStream = new FileOutputStream(desFile);
                byte[] buffer = new byte[BUFF_SIZE];
                int realLength;
                while ((realLength = iStream.read(buffer)) > 0) {
                    oStream.write(buffer, 0, realLength);
                }

                // 关闭输入输出流
                iStream.close();
                oStream.close();
//                DebugTool.d(TAG, "unzip file success to: ", str);
            }

            // 解压完成
            String res = "{filePath: '" + zipFilePath + "', saveDir: '" + saveDir + "', path: '" + saveDir + "'}";
            jsCode = completeCallbackName + "(undefined, " + res + ")";
            JSBridgeManager.postMessageToJS(jsCode);
        } catch (Exception ex) {
            // 解压异常
            String err = "{errMsg: '" + ex.getMessage() + "', zipFilePath: '" + zipFilePath + "'}";
            jsCode = completeCallbackName + "(" + err + ")";
            JSBridgeManager.postMessageToJS(jsCode);
        }
    }

    /**
     * 实名校验
     */
    public static void checkRealName() {
        DebugTool.i(TAG, "call native checkRealName.");

//        if (BuildConfig.REAL_NAME_CLASS_NAME.equals("")) {
//            return;
//        }

//        try {
//            Class cla = Class.forName(BuildConfig.REAL_NAME_CLASS_NAME);
//            cla.getMethod("initYsdk").invoke(cla);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }
}
