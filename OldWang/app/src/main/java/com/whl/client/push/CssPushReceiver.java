//package com.cssweb.shankephone.push;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.content.LocalBroadcastManager;
//import android.text.TextUtils;
//
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.framework.utils.VibratorUtils;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.app.CssNotification;
//import com.cssweb.shankephone.gateway.model.CssPushInboxMessage;
//import com.cssweb.shankephone.preference.MPreference;
//import com.cssweb.shankephone.home.ticket.SingleTicketManager;
//import com.igexin.sdk.PushConsts;
//import com.igexin.sdk.PushManager;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * Created by liwx on 2016/7/6.
// */
//public class CssPushReceiver extends BroadcastReceiver {
//    private static final String TAG = "CssPushReceiver";
//    private CssPushManager mPushManager;
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        String action = intent.getAction();
//        MLog.d(TAG, "onReceive action= " + action);
//        Bundle bundle = intent.getExtras();
//        int act = bundle.getInt(PushConsts.CMD_ACTION);
//        MLog.d(TAG, "onReceive act= " + act);
//        MLog.d(TAG, "onReceive is foreground= " + BizApplication.getInstance().isAppResume());
//
//        mPushManager = new CssPushManager(context);
//        switch (act) {
//            case PushConsts.GET_MSG_DATA:
//                // 获取透传数据
//                // String appid = bundle.getString("appid");
//                byte[] payload = bundle.getByteArray("payload");
//
//                String taskid = bundle.getString("taskid");
//                String messageid = bundle.getString("messageid");
//                MLog.d(TAG, "taskid= " + taskid + " messageId = " + messageid);
//
//                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
//                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
//                MLog.d(TAG, "sendFeedbackMessage result = " + result);
//                if (payload != null) {
//                    String data = new String(payload);
//                    MLog.d(TAG, "####msg = " + data);
//                    parsePushMsg(context, data);
//                }
//                break;
//            case PushConsts.GET_CLIENTID:
//                // 获取ClientID(CID)
//                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
//                String cid = bundle.getString("clientid");
//                MPreference.putString(context, MPreference.PUSH_CLIENT_ID, cid);
//                MLog.d(TAG, "clientId = " + cid);
//                break;
//            case PushConsts.GET_SDKONLINESTATE:
//                boolean online = bundle.getBoolean("onlineState");
//                MLog.d(TAG, "online = " + online);
//                break;
//        }
//    }
//
//    /**
//     * 解析推送消息
//     */
//    private void aVoidparsePushMsg(Context context, String msg) {
//        try {
//            JSONObject jsonObject = new JSONObject(msg);
//            //            MLog.d(TAG, "json = " + jsonObject.toString());
//            String type = jsonObject.optString("type");
//            String sender = jsonObject.optString("sender");
//            String messageJson = jsonObject.optString("message");
//
//            if (!TextUtils.isEmpty(type)) {
//                if (type.equals(CssPushManager.PUSH_TYPE_SINGLE_TICKET)) {
//                    MLog.d(TAG, "receiver single ticket push msg");
//                    VibratorUtils.Vibrate(context, new long[]{100, 100, 0, 0}, false);
//                    CssNotification.showCommonNotification(context, mPushManager.parseGetSingleTicketNoti(messageJson));
//                    Intent newIntent = new Intent(SingleTicketManager.ACTION_GET_TICKET_SUCCESS);
//                    newIntent.putExtra(SingleTicketManager.CLOUD_TICKET_STATUS, "");
//                    LocalBroadcastManager.getInstance(context).sendBroadcast(newIntent);
//                } else if (type.equals(CssPushManager.PUSH_TYPE_CLOUD_TICKET)) {
//                    MLog.d(TAG, "receiver cloud ticket push msg");
//                    CssNotification.showCommonNotification(context, mPushManager.parseCloudGateTicketNoti(messageJson));
//                    Intent newIntent = new Intent(SingleTicketManager.ACTION_CLOUD_TICKET_ENTRY_STATUS);
//                    String ticketStatus = mPushManager.parseCloudTicketAction(messageJson);
//                    MLog.d(TAG, "ticketStatus = " + ticketStatus);
//                    newIntent.putExtra(SingleTicketManager.CLOUD_TICKET_STATUS, ticketStatus);
//                    LocalBroadcastManager.getInstance(context).sendBroadcast(newIntent);
//                } else if (type.equals(CssPushManager.PUSH_TYPE_NFC_SERVICE_MANAGE)) {
//                    //                    Intent newIntent = new Intent(CssPushManager.ACTION_HANDLE_NFC_SERVICE);
//                    //                    newIntent.putExtra(CssPushManager.HANDLE_NFC_MSG, messageJson);
//                    //                    LocalBroadcastManager.getInstance(context).sendBroadcast(newIntent);
//                    //                    MLog.d(TAG, "Send broadcast:" + CssPushManager.ACTION_HANDLE_NFC_SERVICE);
//                    mPushManager.handleNfcService(messageJson);
//
//                } else if (type.equals(CssPushManager.PUSH_TYPE_INBOX)) {
//                    CssPushInboxMessage message = mPushManager.parseInboxPushMessage(messageJson);
//                    CssNotification.showInboxNotification(context, message.getMessageTitle(), message);
//                } else {
//                    MLog.d(TAG, "Unknown push message");
//                }
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//}
