package com.whl.client.app;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.whl.framework.app.MApplication;
import com.whl.framework.utils.MLog;
import com.whl.framework.utils.Utils;
import com.whl.client.gateway.MobileGateway;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class SendLogService extends Service {

    private static final String TAG = "SendLogService";
    private File mLogFile;

    @Override
    public void onCreate() {
        super.onCreate();
        MLog.d(TAG, "onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MLog.d(TAG, "### onStartCommand");

        String crachLogPath = Utils.getCacheDirectory(getApplicationContext(), MApplication.PARENT_CACHE_DIR).getAbsolutePath() + CrashManager.CRASH_FILE_PATH;
        MLog.d(TAG, "crachLogPath = " + crachLogPath);
        FileInputStream inputStream = null;
        mLogFile = new File(crachLogPath, CrashManager.CRASH_TEMP_FILE_NAME);
        try {
            inputStream = new FileInputStream(mLogFile);
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            inputStream.read(buffer);
            String log = new String(buffer).trim();
            MLog.d(TAG, "CRASH---------" + "\n" + log);
            MobileGateway gateway = new MobileGateway(getApplicationContext());
            gateway.sendCrashLog(log);
            mLogFile.delete();
        } catch (Exception e) {
            MLog.e(TAG, "an error occured when read logfile..." + "\n", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    MLog.e(TAG, "an error occured when close FileInputStream..." + "\n", e);
                }
            }
        }
        // TODO Send log to server
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MLog.d(TAG, "onDestroy");
    }
}
