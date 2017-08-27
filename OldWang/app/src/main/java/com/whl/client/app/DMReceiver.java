package com.whl.client.app;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.whl.framework.utils.MLog;
import com.whl.client.push.CssPushManager;


public class DMReceiver extends BroadcastReceiver {

    private static final String TAG = "DMReceiver";

    public static final String ACTION_DM_CM_CLIENT_INSTALLED = "com.cssweb.shankephone.action.DM_CM_CLIENT_INSTALLED";
    public static final String ACTION_DM_SP_PLUGIN_INSTALLED = "com.cssweb.shankephone.action.DM_SP_PLUGIN_INSTALLED";

    public static final String PACKAGE_NAME = "package_name";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        MLog.d(TAG, "onReceive action = " + intent.getAction());
        if (TextUtils.isEmpty(action))
            return;
        if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            MLog.d(TAG, "onReceive packageName = " + packageName);
            if (!TextUtils.isEmpty(packageName)) {
                Intent spAddedIntent = new Intent(ACTION_DM_SP_PLUGIN_INSTALLED);
                spAddedIntent.putExtra(PACKAGE_NAME, packageName);
                LocalBroadcastManager.getInstance(context).sendBroadcast(spAddedIntent);
                MLog.d(TAG, "send broadcast");
            }

        } else if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            //            BizApplication.getInstance().getCssPushManager().startPushService();
        } else if (action.equals(CssPushManager.ACTION_RESTART_PUSH_SERVICE_ALARM)) {
            //            BizApplication.getInstance().getCssPushManager().startPushService();
        } else if (action.equals(CssNotification.ACTION_NT_CLEAR_NOTI)) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(0);
        }
    }
}
