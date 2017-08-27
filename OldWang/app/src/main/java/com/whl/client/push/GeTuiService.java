package com.whl.client.push;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.whl.framework.utils.MLog;
import com.igexin.sdk.GTServiceManager;


public class GeTuiService extends Service {

    private final static String TAG = "GeTuiService";

    @Override
    public void onCreate() {
        super.onCreate();
        MLog.d(TAG, "onCreate");
        GTServiceManager.getInstance().onCreate(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return GTServiceManager.getInstance().onStartCommand(this, intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return GTServiceManager.getInstance().onBind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GTServiceManager.getInstance().onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        GTServiceManager.getInstance().onLowMemory();
    }
}
