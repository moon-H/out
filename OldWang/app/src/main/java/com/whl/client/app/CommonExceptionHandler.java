package com.whl.client.app;


import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.whl.framework.utils.MLog;
import com.whl.client.BuildConfig;

import java.lang.Thread.UncaughtExceptionHandler;


public class CommonExceptionHandler implements UncaughtExceptionHandler {

    private static final String TAG = "CommonExceptionHandler";

    //    public static final String CRASH_FILE_PATH = "/crash";
    //    public static final String CRASH_TEMP_FILE_NAME = "/crash.log";

    private static CommonExceptionHandler INSTANCE = new CommonExceptionHandler();

    private UncaughtExceptionHandler mDefaultHandler;

    private Context mContext;

    //    private Map<String, String> mInfosMap = new HashMap<String, String>();
    //
    //    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // MApplication.getInstance().exitApp(mContext);
        ex.printStackTrace();
        MLog.e(TAG, "an error occur ...", ex);
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            MLog.d(TAG, "## trace1");
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            MLog.d(TAG, "## trace2");
            BizApplication.getInstance().dismissProgressDialog();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CrashManager.startSendLogService(mContext);
            //            BizApplication.getInstance().exit();
        }
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CommonExceptionHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        new Thread() {

            @Override
            public void run() {
                Looper.prepare();
                if (BuildConfig.LOG_DEBUG)
                    Toast.makeText(mContext, "客户端操作失败", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        // 收集设备参数信息
        CrashManager.collectDeviceInfo(mContext);
        // 保存日志文件
        CrashManager.saveCrashInfo2File(mContext, ex, TAG);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    //    public void collectDeviceInfo(Context ctx) {
    //        try {
    //            PackageManager pm = ctx.getPackageManager();
    //            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
    //            if (pi != null) {
    //                String versionName = DeviceInfo.getAppVersionName(ctx);
    //                String versionCode = pi.versionCode + "";
    //                String time = mDateFormat.format(System.currentTimeMillis());
    //                mInfosMap.put("UserId", MPreference.getLoginId(ctx));
    //                mInfosMap.put("android_version", Build.VERSION.SDK_INT + "");
    //                mInfosMap.put("Time", time);
    //                mInfosMap.put("versionName", versionName);
    //                mInfosMap.put("versionCode", versionCode);
    //            }
    //        } catch (NameNotFoundException e) {
    //            MLog.e(TAG, "an error occured when collect package info: " + e.getMessage(), e);
    //        }
    //        Field[] fields = Build.class.getDeclaredFields();
    //        for (Field field : fields) {
    //            try {
    //                field.setAccessible(true);
    //                mInfosMap.put(field.getName(), field.get(null).toString());
    //                // MLog.d(TAG, field.getName() + " : " + field.get(null));
    //            } catch (Exception e) {
    //                MLog.e(TAG, "an error occured when collect crash info.." + e.getMessage(), e);
    //            }
    //        }
    //    }

    //    private String saveCrashInfo2File(Throwable ex) {
    //        StringBuffer sb = new StringBuffer();
    //        for (Map.Entry<String, String> entry : mInfosMap.entrySet()) {
    //            String key = entry.getKey();
    //            String value = entry.getValue();
    //            sb.append(key + "=" + value + "\n");
    //        }
    //
    //        Writer writer = new StringWriter();
    //        PrintWriter printWriter = new PrintWriter(writer);
    //        ex.printStackTrace(printWriter);
    //        printWriter.close();
    //        String result = writer.toString();
    //        sb.append(result);
    //        try {
    //            File cachDir = Utils.getCacheDirectory(mContext, MApplication.PARENT_CACHE_DIR);
    //            File dir = new File(cachDir.getAbsolutePath() + CRASH_FILE_PATH);
    //            String filePaht = dir.getAbsolutePath() + CRASH_TEMP_FILE_NAME;
    //            if (!dir.exists()) {
    //                dir.mkdirs();
    //            }
    //            FileOutputStream fos = new FileOutputStream(filePaht);
    //            fos.write(sb.toString().getBytes());
    //            fos.close();
    //
    //            return filePaht;
    //        } catch (Exception e) {
    //            MLog.e(TAG, "an error occured while writing file...", e);
    //        }
    //        return null;
    //    }
}
