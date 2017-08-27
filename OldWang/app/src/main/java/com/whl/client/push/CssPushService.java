package com.whl.client.push;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.whl.framework.utils.MLog;
import com.whl.framework.utils.Utils;
import com.whl.client.app.CssNotification;
import com.whl.client.gateway.model.CssPushInboxMessage;
import com.whl.client.preference.MPreference;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.FeedbackCmdMessage;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.igexin.sdk.message.SetTagCmdMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lenovo on 2017/1/12.
 */

public class CssPushService extends GTIntentService {
    private static final String TAG = "CssPushService";
    private CssPushManager mPushManager;

    public CssPushService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        MLog.d(TAG, "onReceiveServicePid:::" + pid);
    }

    @Override
    public void onReceiveMessageData(final Context context, GTTransmitMessage msg) {
        mPushManager = new CssPushManager(context);
        MLog.d(TAG, "msg result:::" + msg);
        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        MLog.d(TAG, "appid result:::" + appid);
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();

        // 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
        MLog.d(TAG, "call sendFeedbackMessage = " + (result ? "success" : "failed"));
        MLog.d(TAG, "onReceiveMessageData -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nmessageid = " + messageid + "\npkg = " + pkg + "\ncid = " + cid);

        if (payload == null) {
            MLog.e(TAG, "receiver payload = null");
        } else {
            final String data = new String(payload);
            MLog.d(TAG, "receiver payload = " + data);
            MLog.d(TAG, "in main thread = " + Utils.isInMainThread());
            Handler handler = new Handler(getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    parsePushMsg(context, data);
                }
            });
            // 测试消息为了观察数据变化
        }
        MLog.d(TAG, "----------------------------------------------------------------------------------------------");
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        MLog.e(TAG, "##onReceiveClientId -> " + "clientid = " + clientid);
        MPreference.putString(context, MPreference.PUSH_CLIENT_ID, clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        MLog.d(TAG, "onReceiveCommandResult -> " + cmdMessage);

        int action = cmdMessage.getAction();

        if (action == PushConsts.SET_TAG_RESULT) {
            setTagResult((SetTagCmdMessage) cmdMessage);
        } else if ((action == PushConsts.THIRDPART_FEEDBACK)) {
            feedbackResult((FeedbackCmdMessage) cmdMessage);
        }
    }

    /**
     * 解析推送消息
     */
    private void parsePushMsg(final Context context, String msg) {
        try {
            JSONObject jsonObject = new JSONObject(msg);
            //            MLog.d(TAG, "json = " + jsonObject.toString());
            String type = jsonObject.optString("type");
            String sender = jsonObject.optString("sender");
            String messageJson = jsonObject.optString("message");

            if (!TextUtils.isEmpty(type)) {
                if (type.equals(CssPushManager.PUSH_TYPE_SINGLE_TICKET)) {
                    MLog.d(TAG, "receiver single ticket push msg");
                    mPushManager.handleCommonSingleTicket(messageJson);

                } else if (type.equals(CssPushManager.PUSH_TYPE_CLOUD_TICKET)) {
                    MLog.d(TAG, "receiver cloud ticket push msg");
//                    CssNotification.showCommonNotification(context, mPushManager.parseCloudGateTicketNoti(messageJson));
//                    Intent newIntent = new Intent(SingleTicketManager.ACTION_CLOUD_TICKET_ENTRY_STATUS);
//                    String ticketStatus = mPushManager.parseCloudTicketAction(messageJson);
//                    MLog.d(TAG, "ticketStatus = " + ticketStatus);
//                    newIntent.putExtra(SingleTicketManager.CLOUD_TICKET_STATUS, ticketStatus);
//                    LocalBroadcastManager.getInstance(context).sendBroadcast(newIntent);
                } else if (type.equals(CssPushManager.PUSH_TYPE_NFC_SERVICE_MANAGE)) {
                    //                    Intent newIntent = new Intent(CssPushManager.ACTION_HANDLE_NFC_SERVICE);
                    //                    newIntent.putExtra(CssPushManager.HANDLE_NFC_MSG, messageJson);
                    //                    LocalBroadcastManager.getInstance(context).sendBroadcast(newIntent);
                    //                    MLog.d(TAG, "Send broadcast:" + CssPushManager.ACTION_HANDLE_NFC_SERVICE);
                    mPushManager.handleNfcService(messageJson);

                } else if (type.equals(CssPushManager.PUSH_TYPE_INBOX)) {
                    CssPushInboxMessage message = mPushManager.parseInboxPushMessage(messageJson);
                    CssNotification.showInboxNotification(context, message.getMessageContent(), message);
                } else if (type.equals(CssPushManager.PUSH_TYPE_DEVICE_CHANGE)) {
                    //账号在另一个设备登录
                    mPushManager.handleUserChangeDevice(messageJson);
                } else if (type.equals(CssPushManager.QR_CODE_SJT)) {
                    mPushManager.handleQrCodePush(messageJson);
                } else {
                    MLog.d(TAG, "Unknown push message");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void feedbackResult(FeedbackCmdMessage feedbackCmdMsg) {
        String appid = feedbackCmdMsg.getAppid();
        String taskid = feedbackCmdMsg.getTaskId();
        String actionid = feedbackCmdMsg.getActionId();
        String result = feedbackCmdMsg.getResult();
        long timestamp = feedbackCmdMsg.getTimeStamp();
        String cid = feedbackCmdMsg.getClientId();
        MLog.d(TAG, "onReceiveCommandResult -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nactionid = " + actionid + "\nresult = " + result + "\ncid = " + cid + "\ntimestamp = " + timestamp);
    }

    private void setTagResult(SetTagCmdMessage setTagCmdMsg) {
        String sn = setTagCmdMsg.getSn();
        String code = setTagCmdMsg.getCode();

        String text = "设置标签失败, 未知异常";
        switch (Integer.valueOf(code)) {
            case PushConsts.SETTAG_SUCCESS:
                text = "设置标签成功";
                break;

            case PushConsts.SETTAG_ERROR_COUNT:
                text = "设置标签失败, tag数量过大, 最大不能超过200个";
                break;

            case PushConsts.SETTAG_ERROR_FREQUENCY:
                text = "设置标签失败, 频率过快, 两次间隔应大于1s且一天只能成功调用一次";
                break;

            case PushConsts.SETTAG_ERROR_REPEAT:
                text = "设置标签失败, 标签重复";
                break;

            case PushConsts.SETTAG_ERROR_UNBIND:
                text = "设置标签失败, 服务未初始化成功";
                break;

            case PushConsts.SETTAG_ERROR_EXCEPTION:
                text = "设置标签失败, 未知异常";
                break;

            case PushConsts.SETTAG_ERROR_NULL:
                text = "设置标签失败, tag 为空";
                break;

            case PushConsts.SETTAG_NOTONLINE:
                text = "还未登陆成功";
                break;

            case PushConsts.SETTAG_IN_BLACKLIST:
                text = "该应用已经在黑名单中,请联系售后支持!";
                break;

            case PushConsts.SETTAG_NUM_EXCEED:
                text = "已存 tag 超过限制";
                break;

            default:
                break;
        }

        Log.d(TAG, "settag result sn = " + sn + ", code = " + code + ", text = " + text);
    }
}
