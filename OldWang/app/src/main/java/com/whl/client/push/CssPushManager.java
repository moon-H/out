package com.whl.client.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.whl.framework.utils.MLog;
import com.whl.framework.utils.VibratorUtils;
import com.whl.client.R;
import com.whl.client.app.CssConstant;
import com.whl.client.app.CssNotification;
import com.whl.client.gateway.model.CssPushInboxMessage;
import com.whl.client.gateway.model.wallet.UserDeviceChangeInfo;
import com.whl.client.home.ticket.STHomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liwx on 2015/9/23.
 */
public class CssPushManager {
    private static final String TAG = "CssPushManager";

    public static final String ACTION_RESTART_PUSH_SERVICE_ALARM = "com.cssweb.shankephone.ACTION_RESTART_PUSH_SERVICE_ALARM";
    public static final String ACTION_DEPLOY_SUCCESS = "com.cssweb.shankephone.ACTION_DEPLOY_SUCCESS";
    public static final String ACTION_DEPLOY_FAIL = "com.cssweb.shankephone.ACTION_DEPLOY_FAIL";
    public static final String ACTION_TOPUP_SUCCESS = "com.cssweb.shankephone.ACTION_TOPUP_SUCCESS";
    public static final String ACTION_TOPUP_FAIL = "com.cssweb.shankephone.ACTION_TOPUP_FAIL";
    public static final String ACTION_PERSONALIZED_SUCCESS = "com.cssweb.shankephone.ACTION_PERSONALIZED_SUCCESS";
    public static final String ACTION_PERSONALIZED_FAIL = "com.cssweb.shankephone.ACTION_PERSONALIZED_FAIL";
    public static final String ACTION_DEL_CARD_SUCCESS = "com.cssweb.shankephone.ACTION_DEL_CARD_SUCCESS";
    public static final String ACTION_DEL_CARD_FAIL = "com.cssweb.shankephone.ACTION_DEL_CARD_FAIL";

    public static final String ACTION_HANDLE_NFC_SERVICE = "com.cssweb.shankephone.ACTION_HANDLE_NFC_SERVICE";


    public static final String PUSH_TYPE_SPTSM = "SPTSM";
    public static final String PUSH_TYPE_INBOX = "INBOX";
    public static final String PUSH_TYPE_TOPUP = "TOPUP";
    public static final String PUSH_TYPE_DEPLOY = "DEPLOY";
    public static final String PUSH_TYPE_PERSONALIZED = "PERSONALIZED";
    public static final String PUSH_TYPE_SINGLE_TICKET = "SINGLE_TICKET";
    public static final String PUSH_TYPE_CLOUD_TICKET = "CLOUD_TICKET";
    public static final String PUSH_TYPE_DELETE = "DELETE";
    public static final String PUSH_TYPE_NFC_SERVICE_MANAGE = "NFC_SERVICE_MANAGE";//锁定解锁等
    public static final String PUSH_TYPE_DEVICE_CHANGE = "DEVICECHANGE";//用户换设备登录
    public static final String QR_CODE_SJT = "QR_CODE_SJT";//二维码单程票相关


    public final static String PUSH_MESSAGE_SUCCESS = "SUCCESS";

    public static final String TOPUP_AMOUNT = "topup_amount";

    public static final String HANDLE_NFC_MSG = "handle_nfc_msg";

    private Context mContext;
    private Resources mResource;

    //    private PushMsgReceiver mPushMsgReceiver;

    public CssPushManager(Context context) {
        mContext = context;
        mResource = mContext.getResources();
        //        registerRestartAlram();
    }

    public void startPushService() {
        //        mContext.startService(new Intent(mContext, GeTuiService.class));
    }

    //    public void registerRestartAlram() {
    //        MLog.d(TAG, "registerRestartAlarm");
    //        Intent intent = new Intent(mContext, DMReceiver.class);
    //        intent.setAction(ACTION_RESTART_PUSH_SERVICE_ALARM);
    //        PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent, 0);
    //        long firstTime = SystemClock.elapsedRealtime();
    //        firstTime += 10 * 1000; // 10 second
    //        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
    //        am.setRepeating(AlarmManager.RTC_WAKEUP, firstTime, 60 * 1000, sender);
    //    }

    // private void unregisterRestartAlram() {
    // MLog.d(TAG, "unregisterRestartAlarm");
    // Intent intent = new Intent(GeTuiService.this, RestartServiceReceiver.class);
    // intent.setAction(RestartServiceReceiver.ACTION_RESTART_CSS_PUSH_SERVICE);
    // PendingIntent sender = PendingIntent.getBroadcast(GeTuiService.this, 0, intent, 0);
    // AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
    // am.cancel(sender);
    // }
    //    public void registerScreenReceiver() {
    //        IntentFilter filter = new IntentFilter();
    //        filter.addAction(Intent.ACTION_SCREEN_OFF);
    //        filter.addAction(Intent.ACTION_SCREEN_ON);
    //        mContext.registerReceiver(mScreenStateReceiver, filter);
    //    }

    //    public void unregisterScreenReceiver() {
    //        mContext.unregisterReceiver(mScreenStateReceiver);
    //    }

    //    public void unregisterPushMsgReceiver() {
    //        if (mPushMsgReceiver != null) {
    //            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mPushMsgReceiver);
    //        }
    //    }

    //注册广播，处理推送消息
    //    public void registerPushMsgReceiver() {
    //        if (mPushMsgReceiver == null) {
    //            mPushMsgReceiver = new PushMsgReceiver();
    //            IntentFilter filter = new IntentFilter();
    //            filter.addAction(CssPushManager.ACTION_HANDLE_NFC_SERVICE);
    //            LocalBroadcastManager.getInstance(mContext).registerReceiver(mPushMsgReceiver, filter);
    //        }
    //    }

    private BroadcastReceiver mScreenStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            MLog.d(TAG, "action = " + action);
            if (action.equals(Intent.ACTION_SCREEN_ON) || action.equals(Intent.ACTION_SCREEN_OFF)) {
                //                registerRestartAlram();
            }
        }
    };

    /**
     * 解析取票推送
     */
    public String parseGetSingleTicketNoti(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String maxNum = jsonObject.optString("ACTUAL_TAKE_TICKET_NUM");
            if (TextUtils.isEmpty(maxNum))
                return mResource.getString(R.string.st_ticket_complete_title);
            else
                return String.format(mResource.getString(R.string.st_get_ticket_success_and_num), maxNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mResource.getString(R.string.st_ticket_complete_title);
    }

    /**
     * 解析云闸机扫描推送
     */
    public String parseCloudGateTicketNoti(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String cgmaxNum = jsonObject.optString("ENTRY_STATION_NOTI");
            if (TextUtils.isEmpty(cgmaxNum))
                return mResource.getString(R.string.st_cg_ticket_title);
            else
                return String.format(mResource.getString(R.string.st_get_ticket_success_and_num), cgmaxNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mResource.getString(R.string.st_ticket_complete_title);
    }

    public String parseCloudTicketAction(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.optString("ACTION");
            //            if (TextUtils.isEmpty(maxNum))
            //                return mResource.getString(R.string.st_ticket_complete_title);
            //            else
            //                return String.format(mResource.getString(R.string.st_get_ticket_success_and_num), maxNum);
        } catch (JSONException e) {
            e.printStackTrace();
            MLog.d(TAG, "parseCloudTicketAction occur an error : " + e.getMessage());
        }
        return "";
    }

    public CssPushInboxMessage parseInboxPushMessage(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String type = jsonObject.optString("TYPE");
            String messageId = jsonObject.optString("MessageId");
            String url = jsonObject.optString("TARGET_URL");
            String title = jsonObject.optString("TITLE");
            String content = jsonObject.optString("CONTENT");
            String time = jsonObject.optString("PUSH_TIME");
            CssPushInboxMessage message = new CssPushInboxMessage();
            message.setMessageType(type);
            message.setMessageId(messageId);
            message.setMessageTitle(title);
            message.setUrl(url);
            message.setMessageTime(time);
            message.setMessageContent(content);

            return message;
        } catch (JSONException e) {
            e.printStackTrace();
            MLog.d(TAG, "parseInboxPushMessage occur an error : " + e.getMessage());
        }
        return null;
    }

    /**
     * 处理锁定解锁等
     */
    public void handleNfcService(String msg) {
    }

    //    private class PushMsgReceiver extends BroadcastReceiver {
    //        @Override
    //        public void onReceive(Context context, Intent intent) {
    //            String action = intent.getAction();
    //            MLog.d(TAG, "PushMsgReceiver action = " + action);
    //            if (action.equals(ACTION_HANDLE_NFC_SERVICE)) {
    //                handleNfcService(intent.getStringExtra(HANDLE_NFC_MSG));
    //            }
    //        }
    //    }

    /**
     * 处理用户换设备登录
     **/
    public void handleUserChangeDevice(String msg) {
        try {
            JSONObject jsonObject = new JSONObject(msg);
            String time = jsonObject.optString("loginDate");
            String loginType = jsonObject.optString("loginType");
            String loginDevice = jsonObject.optString("loginDevice");
            UserDeviceChangeInfo info = new UserDeviceChangeInfo();
            info.setLoginDate(time);
            info.setLoginType(loginType);
            info.setLoginDevice(loginDevice);

            Intent logoutIntent = new Intent(mContext, STHomeActivity.class);
            logoutIntent.setAction(CssConstant.Action.ACTION_USER_CHANGE_DEVICE);
            logoutIntent.putExtra(CssConstant.KEY_USER_CHANGE_DEVICE_INFO, info);
            logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(logoutIntent);
        } catch (JSONException e) {
            MLog.e(TAG, "handleNfcService occur an error : " + e.getMessage());
        }
    }

    /**
     * 处理二维码单程票推送
     **/
    void handleQrCodePush(String msg) {
        try {
            JSONObject jsonObject = new JSONObject(msg);
            String status = jsonObject.optString("SJT_STATUS");
            String arriveType = jsonObject.optString("PAY_UPON_ARRIVAL_TYPE");
            String amount = jsonObject.optString("PAY_UPON_ARRIVAL_AMUONT");
            String orderId = jsonObject.optString("ORDER_NO");
            String subOrderId = jsonObject.optString("EXTERNAL_ORDER_NO");
            String noticeMsg = jsonObject.optString("NOTICE_MSG");
            String param1 = jsonObject.optString("PARAM1");
            String param2 = jsonObject.optString("PARAM2");
            MLog.d(TAG, "status = " + status + " arriveType = " + arriveType + " amount = " + amount + " orderId = " + orderId + " subOrderId = " + subOrderId);
            if (!TextUtils.isEmpty(status)) {
                if (status.equals("04")) {
                    //已进站
                    Intent newIntent = new Intent(CssConstant.Action.ACTION_QRCODE_ENTRY_STATION_SUCCESS);
                    newIntent.putExtra(CssConstant.KEY_EVENT_SNAPSHOT_URL, param1);
                    newIntent.putExtra(CssConstant.KEY_EVENT_URL, param2);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(newIntent);
                } else if (status.equals("05")) {
                    //已出站
                    Intent newIntent = new Intent(CssConstant.Action.ACTION_QRCODE_EXIT_STATION_SUCCESS);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(newIntent);
                } else if (status.equals("32")) {
                    //                    VibratorUtils.Vibrate(mContext, new long[]{100, 100, 0, 0}, false);
                    if (!TextUtils.isEmpty(arriveType)) {
                        if (arriveType.equals("30")) {
                            //超时
                            Intent newIntent = new Intent(CssConstant.Action.ACTION_QRCODE_OVER_TIME);
                            newIntent.putExtra(CssConstant.KEY_ORDERID, orderId);
                            newIntent.putExtra(CssConstant.KEY_AMOUNT, amount);
                            newIntent.putExtra(CssConstant.KEY_SUB_ORDER_ID, subOrderId);
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(newIntent);
                        } else if (arriveType.equals("31")) {
                            //超乘
                            Intent newIntent = new Intent(CssConstant.Action.ACTION_QRCODE_OVER_RIDE);
                            newIntent.putExtra(CssConstant.KEY_ORDERID, orderId);
                            newIntent.putExtra(CssConstant.KEY_AMOUNT, amount);
                            newIntent.putExtra(CssConstant.KEY_SUB_ORDER_ID, subOrderId);
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(newIntent);
                        } else if (arriveType.equals("32")) {
                            //超时和超乘
                            Intent newIntent = new Intent(CssConstant.Action.ACTION_QRCODE_OVER_TIME_RIDE);
                            newIntent.putExtra(CssConstant.KEY_ORDERID, orderId);
                            newIntent.putExtra(CssConstant.KEY_SUB_ORDER_ID, subOrderId);
                            newIntent.putExtra(CssConstant.KEY_AMOUNT, amount);
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(newIntent);
                        }
                    }

                } else if (status.equals("51")) {
                    //重复进站
                    Intent newIntent = new Intent(CssConstant.Action.ACTION_QRCODE_MULTI_ENTRY);
                    newIntent.putExtra(CssConstant.KEY_PUSH_MSG, noticeMsg);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(newIntent);
                }
            }
        } catch (JSONException e) {
            //            e.printStackTrace();
            MLog.d(TAG, "handleQrCodePush occur an error : ", e);
        }

    }

    /**
     * 处理普通单程票相关推送
     */
    public void handleCommonSingleTicket(String msg) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(msg);
            String action = jsonObject.optString("ACTION");
            String param1 = jsonObject.optString("PARAM1");
            String param2 = jsonObject.optString("PARAM2");
            if (!TextUtils.isEmpty(action)) {
                if (action.equalsIgnoreCase("TVIP_TAKE_TIKET_NOTI")) {
                    VibratorUtils.Vibrate(mContext, new long[]{100, 100, 0, 0}, false);
                    CssNotification.showCommonNotification(mContext, parseGetSingleTicketNoti(msg));
                    Intent newIntent = new Intent(CssConstant.Action.ACTION_GET_TICKET_SUCCESS);
                    newIntent.putExtra(CssConstant.KEY_EVENT_SNAPSHOT_URL, param1);
                    newIntent.putExtra(CssConstant.KEY_EVENT_URL, param2);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(newIntent);
                }
            }

        } catch (JSONException e) {
            MLog.d(TAG, "handleCommonSingleTicket push occur an error ", e);
        }
    }

}
