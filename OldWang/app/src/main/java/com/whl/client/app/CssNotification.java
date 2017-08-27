package com.whl.client.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.whl.client.R;
import com.whl.client.gateway.model.CssPushInboxMessage;
import com.whl.client.home.BlankActivity;

/**
 * Created by liwx on 2015/11/19.
 */
public class CssNotification {
    private static final String TAG = "CssNotification";

    private static final int NOTI_ID_INBOX = 1;
    private static final int NOTI_ID_SERVICE = 2;

    public static final String ACTION_NT_CLEAR_NOTI = "com.cssweb.shankephone.action.ACTION_NT_CLEAR_NOTI";

    public static void showCommonNotification(Context context, String content) {
        NotificationCompat.Builder builder = getBuilder(context, context.getResources().getString(R.string.shankephone_app_name), content);
        builder.setContentIntent(getClearNotiPendingBroadcastIntent(context));
        getNotificationManager(context).notify(NOTI_ID_SERVICE, builder.build());
    }

    public static void showInboxNotification(Context context, String content, CssPushInboxMessage msg) {
        NotificationCompat.Builder builder = getBuilder(context, msg.getMessageTitle(), content);
        Intent resultIntent = new Intent(context, BlankActivity.class);
        resultIntent.putExtra(CssConstant.PUSH.PUSH_MESSAGE, msg);
        //        builder.setContentIntent(getInboxNotiPendingBroadcastIntent(context, CssConstant.Action.ACTION_PUSH_MSG_INBOX, msg));
        builder.setContentIntent(getPendingActivityIntent(context, resultIntent));
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS);
        getNotificationManager(context).notify(NOTI_ID_INBOX, builder.build());
    }

    private static NotificationCompat.Builder getBuilder(Context context, String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setAutoCancel(true);
        return builder;
    }

    private static PendingIntent getPendingActivityIntent(Context context, Intent resultIntent) {
        return PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent getClearNotiPendingBroadcastIntent(Context context) {
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_NT_CLEAR_NOTI), 0);
        return cancelPendingIntent;
    }

    private static PendingIntent getInboxNotiPendingBroadcastIntent(Context context, String action, CssPushInboxMessage msg) {
        Intent intent = new Intent(action);
        intent.putExtra(CssConstant.PUSH.PUSH_MESSAGE, msg);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
