package com.whl.client.home.ticket.detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.whl.framework.utils.DeviceInfo;
import com.whl.framework.utils.MLog;
import com.whl.framework.utils.Utils;
import com.whl.client.R;
import com.whl.client.app.BaseActivity;
import com.whl.client.app.BizApplication;
import com.whl.client.app.CssConstant;
import com.whl.client.home.ticket.SingleTicketManager;
import com.whl.client.settings.zxing.encoding.EncodingHandler;
import com.whl.client.umengShare.CustomShareBoard;
import com.whl.client.umengShare.UMengShare;
import com.whl.client.view.TitleBarView;
import com.google.zxing.WriterException;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * Created by lijy on 2015/11/4.
 * 取票二维码页面
 */
public class STGetTicketActivity extends BaseActivity implements TitleBarView.OnTitleBarClickListener, View.OnClickListener, CustomShareBoard.OnShareItemsListener {
    private static final String TAG = "STGetTicketActivity";
    private ImageView mQrCodeView;
    private TitleBarView mTitleBarView;
    private TextView mDescribe;
    private int mScreenWidth;
    private Bitmap mQrCodeBmp;
    private String mGetTicketToken;
    private Button btnShare;
    private int mQrCodeSize;
    private String mOrderNO;
    private String mCategoryCode;
    private String mCityCode;

    private UMShareAPI mShareAPI = null;
    UMImage image = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_ticket);
        BizApplication.getInstance().addActivity(this);
        // 配置需要分享的相关平台
        mShareAPI = UMShareAPI.get(this);
        configPlatforms();
        MLog.d(TAG, "onCreate");
        mQrCodeView = (ImageView) findViewById(R.id.qr_code);
        mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
        mTitleBarView.setTitle(getString(R.string.getTicket_title));
        mTitleBarView.setOnTitleBarClickListener(this);
        mScreenWidth = DeviceInfo.getScreenWidth(STGetTicketActivity.this);
        btnShare = (Button) findViewById(R.id.btnShare);
        mDescribe = (TextView) findViewById(R.id.textview_share_desc01);
        mDescribe.setVisibility(View.VISIBLE);
        btnShare.setVisibility(View.VISIBLE);
        btnShare.setOnClickListener(this);
        mGetTicketToken = getIntent().getStringExtra(CssConstant.KEY_QR_CODE_TOKEN);
        mOrderNO = getIntent().getStringExtra(SingleTicketManager.ST_ORDER_ID);
        mCategoryCode = getIntent().getStringExtra(CssConstant.KEY_CATEGORY_CODE);
        mCityCode = getIntent().getStringExtra(CssConstant.KEY_CITY_CODE);
        MLog.d(TAG, "orderNO===" + mOrderNO);
        mQrCodeSize = getResources().getDimensionPixelSize(R.dimen.st_qr_code_size);
        MLog.d(TAG, "mGetTicketToken=" + mGetTicketToken);
        if (TextUtils.isEmpty(mGetTicketToken)) {
            btnShare.setVisibility(View.GONE);
            return;
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    if (!TextUtils.isEmpty(mGetTicketToken)) {
                        mQrCodeBmp = EncodingHandler.createQRCode(mGetTicketToken, mQrCodeSize);
                        if (mQrCodeBmp != null) {
                            mhHandler.sendEmptyMessage(0);
                        }
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        registerSingleReceiver();
        //        if (!TextUtils.isEmpty(mCategoryCode) && mCategoryCode.equals(OrderManager.CLOUDGATE_CATEGORYCODE)) {
        //            mDescribe.setGravity(Gravity.CENTER_VERTICAL);
        //            mDescribe.setText(getString(R.string.share_cg_get_ticket));
        //        } else {
        mDescribe.setText(getString(R.string.share_scan_get_ticket));
        //        }

        //        mhHandler.postDelayed(new Runnable() {
        //            @Override
        //            public void run() {
        //                Intent newIntent = new Intent(SingleTicketManager.ACTION_GET_TICKET_SUCCESS);
        //                LocalBroadcastManager.getInstance(STGetTicketActivity.this).sendBroadcast(newIntent);
        //
        //            }
        //        },5000);

    }

    Handler mhHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                if (mQrCodeBmp != null) {
                    mQrCodeView.setImageBitmap(mQrCodeBmp);
                }
            }
        }


    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLog.d(TAG, "onDestroy");
        LocalBroadcastManager.getInstance(STGetTicketActivity.this).unregisterReceiver(mgtBroadcastReceiver);
        BizApplication.getInstance().removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLog.d(TAG, "onResume");
        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_STGetTicketActivity));

    }

    @Override
    protected void onPause() {
        super.onPause();
        MLog.d(TAG, "onPause");
        UMengShare.countPageAndTimeEnd(this, getString(R.string.statistic_STGetTicketActivity));
    }

    @Override
    protected void onStop() {
        super.onStop();
        MLog.d(TAG, "onStop");
    }


    @Override
    public void onBackClicked(View view) {
        finish();
    }

    @Override
    public void onMenuClicked(View view) {


    }

    public void configPlatforms() {
        UMengShare.configPlatform();
    }


    public void setShareContent() {
        try {
            image = new UMImage(this, EncodingHandler.createQRCode(mGetTicketToken, 200));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void registerSingleReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CssConstant.Action.ACTION_GET_TICKET_SUCCESS);
        //        filter.addAction(SingleTicketManager.ACTION_CLOUD_TICKET_ENTRY_STATUS);
        LocalBroadcastManager.getInstance(STGetTicketActivity.this).registerReceiver(mgtBroadcastReceiver, filter);
    }

    private BroadcastReceiver mgtBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //            String mTicketStatus = intent.getStringExtra(SingleTicketManager.CLOUD_TICKET_STATUS);
            MLog.d(TAG, "action = " + action);
            if (!TextUtils.isEmpty(action)) {
                if (action.equals(CssConstant.Action.ACTION_GET_TICKET_SUCCESS)) {
                    String snapshotUrl = intent.getStringExtra(CssConstant.KEY_EVENT_SNAPSHOT_URL);
                    String eventUrl = intent.getStringExtra(CssConstant.KEY_EVENT_URL);

                    Intent tintent = new Intent(STGetTicketActivity.this, STGetTicketCompleteActivity.class);
                    tintent.putExtra(SingleTicketManager.ST_ORDER_ID, mOrderNO);
                    tintent.putExtra(CssConstant.KEY_CATEGORY_CODE, mCategoryCode);
                    tintent.putExtra(CssConstant.KEY_CITY_CODE, mCityCode);
                    tintent.putExtra(CssConstant.KEY_EVENT_SNAPSHOT_URL, snapshotUrl);
                    tintent.putExtra(CssConstant.KEY_EVENT_URL, eventUrl);
                    MLog.d(TAG, "cityCode = " + mCityCode);
                    startActivity(tintent);
                    finish();

                }
            }
        }
    };


    /**
     * 调用postShare分享。跳转至分享编辑页，然后再分享。</br> [注意]<li>
     * 对于新浪，豆瓣，人人，腾讯微博跳转到分享编辑页，其他平台直接跳转到对应的客户端
     */
    private void postShare() {
        CustomShareBoard shareBoard = new CustomShareBoard(STGetTicketActivity.this);
        shareBoard.showAtLocation(STGetTicketActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        shareBoard.setOnShareItemsListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShare:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                setShareContent();
                postShare();
                break;
        }
    }

    @Override
    public void clickShare(SHARE_MEDIA platform) {
        new ShareAction(STGetTicketActivity.this).setPlatform(platform).setCallback(umShareListener).withTitle(getString(R.string.shankephone_app_name)).withMedia(image).withTargetUrl("http://www.shankephone.com/").share();
        //        new ShareAction(STGetTicketActivity.this).setPlatform(platform).setCallback(umShareListener).withTitle("哈哈哈哈哈").withMedia(image).withText("123")
        //            .withTargetUrl("http://www.shankephone.com/").share();
        //        Glide.with(STGetTicketActivity.this).load("").downloadOnly(4,4).get();

        //        UMImage image = new UMImage(STGetTicketActivity.this, "http://120.52.9.6:8980/corpay/ci/eventDownload/47/47_event20170627185650.jpg");
        //        new ShareAction(STGetTicketActivity.this).setPlatform(platform).setCallback(umShareListener).withMedia(image).withTitle(getString(R.string.shankephone_app_name)).withText("闪客蜂可以购票啦").withTargetUrl("http://www.shankephone.com/").share();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            //            Toast.makeText(STGetTicketActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable throwable) {
            //            Toast.makeText(STGetTicketActivity.this, platform + " 分享失败,请重试", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            //            Toast.makeText(STGetTicketActivity.this, platform + " 分享取消", Toast.LENGTH_SHORT).show();
        }
    };
}
