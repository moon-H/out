package com.whl.client.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.whl.framework.app.MApplication;
import com.whl.framework.utils.DeviceInfo;
import com.whl.framework.utils.MLog;
import com.whl.framework.utils.Utils;
import com.whl.client.preference.MPreference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by liwx on 2016/5/1.
 */
public class CrashManager {
    private static final String TAG = "CrashManager";


    public static final String CRASH_FILE_PATH = "/crash";
    public static final String CRASH_TEMP_FILE_NAME = "/temp.log";
    public static final String CRASH_FILE_NAME = "/crash.log";

    private static Map<String, String> mInfosMap = new HashMap<>();
    private static SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public static void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = DeviceInfo.getAppVersionName(ctx);
                String versionCode = pi.versionCode + "";
                String time = mDateFormat.format(System.currentTimeMillis());
                mInfosMap.put("UserId", MPreference.getLoginId(ctx));
                mInfosMap.put("android_version", Build.VERSION.SDK_INT + "");
                mInfosMap.put("Time", time);
                mInfosMap.put("versionName", versionName);
                mInfosMap.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            MLog.d(TAG, "an error occur when collect package info: " + e.getMessage(), e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mInfosMap.put(field.getName(), field.get(null).toString());
                // MLog.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                MLog.d(TAG, "an error occur when collect crash info.." + e.getMessage(), e);
            }
        }
    }

    public static String saveCrashInfo2File(Context context, Throwable ex, String tag) {
        StringBuffer sb = new StringBuffer();
        sb.append("TAG = ").append(tag).append("\n");
        for (Map.Entry<String, String> entry : mInfosMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            File cachDir = Utils.getCacheDirectory(context, MApplication.PARENT_CACHE_DIR);
            File dir = new File(cachDir.getAbsolutePath() + CRASH_FILE_PATH);
            String tempfilePath = dir.getAbsolutePath() + CRASH_TEMP_FILE_NAME;
            String filePath = dir.getAbsolutePath() + CRASH_FILE_NAME;
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(tempfilePath);
            fos.write(sb.toString().getBytes());
            fos.close();

            FileOutputStream fos2 = new FileOutputStream(filePath);
            fos2.write(sb.toString().getBytes());
            fos2.close();

            return tempfilePath;
        } catch (Exception e) {
            MLog.e(TAG, "an error occur while writing file...", e);
        }
        return null;
    }

    public static void startSendLogService(Context context) {
        Intent intent = new Intent(context, SendLogService.class);
        context.startService(intent);
    }

}
