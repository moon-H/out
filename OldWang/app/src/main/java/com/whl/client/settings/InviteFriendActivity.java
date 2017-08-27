package com.whl.client.settings;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.whl.framework.http.model.Result;
import com.whl.framework.utils.DeviceInfo;
import com.whl.framework.utils.MLog;
import com.whl.framework.utils.Utils;
import com.whl.client.R;
import com.whl.client.app.BaseActivity;
import com.whl.client.app.BizApplication;
import com.whl.client.app.CommonToast;
import com.whl.client.app.CssConstant;
import com.whl.client.gateway.EventGateway;
import com.whl.client.gateway.MobileGateway;
import com.whl.client.gateway.model.Event;
import com.whl.client.gateway.model.event.GetPicListRs;
import com.whl.client.preference.MPreference;
import com.whl.client.umengShare.CustomShareBoard;
import com.whl.client.umengShare.UMengShare;
import com.whl.client.view.TitleBarView;
import com.whl.client.view.TitleBarView.OnTitleBarClickListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.apache.commons.net.util.Base64;
import org.apache.http.Header;

import java.util.List;

/**
 * 邀请好友页面
 **/

public class InviteFriendActivity extends BaseActivity implements OnTitleBarClickListener {

    private static final String TAG = "InviteFriendActivity";
    private ImageView mImgEvent;
    private EventGateway mEventGateway;
    private Event mEvent;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_share);
        BizApplication.getInstance().addActivity(this);
        MLog.d(TAG, "onCreate");
        mEventGateway = new EventGateway(this);
        initView();
        UMengShare.configPlatform();//配置友盟信息
        getPicList();
    }

    private void initView() {
        mImgEvent = (ImageView) findViewById(R.id.img_event);

        TitleBarView titleBarView = (TitleBarView) findViewById(R.id.title_bar);
        titleBarView.setTitle(getString(R.string.invite_friend_title));
        titleBarView.setMenu(false);
        titleBarView.setOnTitleBarClickListener(this);

        findViewById(R.id.btn_invite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                postShare();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLog.d(TAG, "onResume");
        UMengShare.countPageAndTimeStart(this, getString(R.string.invite_friend_title));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLog.d(TAG, "onPause");
        UMengShare.countPageAndTimeEnd(this, getString(R.string.invite_friend_title));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLog.d(TAG, "onDestroy");
        BizApplication.getInstance().removeActivity(this);
    }

    @Override
    public void onBackClicked(View view) {
        finish();
    }

    @Override
    public void onMenuClicked(View view) {

    }

    private void getPicList() {
        mEventGateway.getPicList(CssConstant.FLAG_GET_PIC_INVITE_FRIEND, new MobileGateway.MobileGatewayListener<GetPicListRs>() {
            @Override
            public void onFailed(Result result) {
                CommonToast.onFailed(getApplicationContext(), result);
            }

            @Override
            public void onHttpFailed(int statusCode, Header[] headers) {
                CommonToast.onHttpFailed(getApplicationContext());
            }

            @Override
            public void onSuccess(GetPicListRs response) {
                List<Event> list = response.getEventList();
                if (list != null && list.size() > 0) {
                    mEvent = list.get(0);
                    MLog.d(TAG, "start load pic");
                    Glide.with(getApplicationContext()).load(list.get(0).getEventImageUrl()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            MLog.d(TAG, "load pic onException");
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            MLog.d(TAG, "load pic onResourceReady");
                            return false;
                        }
                    }).into(mImgEvent);
                } else {
                    MLog.d(TAG, "pic list is null launch home");
                }
            }

            @Override
            public void onNoNetwork() {
                CommonToast.onNoNetwork(InviteFriendActivity.this);
            }

            @Override
            public void onAutoLoginSuccess() {

            }

            @Override
            public void onAutoLoginFailed(Result result) {

            }

        });
    }

    /**
     * 调用postShare分享。跳转至分享编辑页，然后再分享。</br> [注意]<li>
     * 对于新浪，豆瓣，人人，腾讯微博跳转到分享编辑页，其他平台直接跳转到对应的客户端
     */
    private void postShare() {
        CustomShareBoard shareBoard = new CustomShareBoard(InviteFriendActivity.this);
        shareBoard.showAtLocation(InviteFriendActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        shareBoard.setOnShareItemsListener(mOnShareItemsListener);
    }

    CustomShareBoard.OnShareItemsListener mOnShareItemsListener = new CustomShareBoard.OnShareItemsListener() {
        @Override
        public void clickShare(SHARE_MEDIA platform) {
            if (mEvent != null) {
                String url = buildInviteInfo(mEvent.getEventUrl());
                MLog.d(TAG, "URL = " + url);
                UMengShare.shareEvent(InviteFriendActivity.this, mEvent.getEventName(), mEvent.getShareContent(), mEvent.getEventImageUrl(), url, platform);
            }
        }
    };

    /**
     * 拼接邀请好友链接
     */
    private String buildInviteInfo(String url) {
        //        String param = "?seId=123&versionName=2.0&walletId=00001&recommendUserId=15502142822";
        String userId = MPreference.getLoginId(this);
        Base64 base64 = new Base64();
        String base64Id = base64.encodeToString(userId.getBytes());
        MLog.d(TAG, " base64 Id = " + base64Id);
        return url + "?seId=" + DeviceInfo.getICCID(this) + "&versionName=" + DeviceInfo.getAppVersionName(this) + "&walletId=" + BizApplication.WALLET_ID + "&recommendUserId=" + base64Id;
    }

}
