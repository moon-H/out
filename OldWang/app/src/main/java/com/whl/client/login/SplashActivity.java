package com.whl.client.login;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.whl.framework.http.model.Result;
import com.whl.framework.utils.DeviceInfo;
import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.app.BaseActivity;
import com.whl.client.app.BizApplication;
import com.whl.client.app.CssConstant;
import com.whl.client.app.SendLogService;
import com.whl.client.dialog.NoticeDialog;
import com.whl.client.dialog.NoticeDialog.OnNoticeDialogClickListener;
import com.whl.client.gateway.EventGateway;
import com.whl.client.gateway.MobileGateway;
import com.whl.client.gateway.WalletGateway;
import com.whl.client.gateway.model.Event;
import com.whl.client.gateway.model.event.GetPicListRs;
import com.whl.client.home.ticket.STHomeActivity;
import com.whl.client.home.ticket.SingleTicketManager;
import com.whl.client.preference.MPreference;
import com.whl.client.push.CssPushService;
import com.whl.client.push.GeTuiService;
import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";

    private WalletGateway mGateway;
    private LoginManager mLoginManager;
    private EventGateway mEventGateway;
    private Timer timer = new Timer();

    private ImageView mAdvertiseImg;
    private Button mSkipBtn;

    private int count = 5;

    private boolean isCanceled = false;
    private ExecutorService mExecutor = Executors.newCachedThreadPool();
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MLog.d(TAG, "## onCreate");
        PushManager.getInstance().initialize(this.getApplicationContext(), GeTuiService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), CssPushService.class);
        MLog.d(TAG, "clientId = " + PushManager.getInstance().getClientid(getApplicationContext()));
        if (savedInstanceState != null) {
            MLog.d(TAG, "savedInstanceState is not null");
            finish();
            return;
        }
        BizApplication.getInstance().addActivity(this);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            MLog.d(TAG, "flag = " + getIntent().getFlags());
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);

        String action = getIntent().getAction();
        MLog.d(TAG, "action = " + action);
        if (!TextUtils.isEmpty(action) && action.equals(BizApplication.ACTION_RELOGIN_APP)) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
            return;
        }

        initView();

        initData();

        launchHomeOrCheckLogin();

        MobclickAgent.openActivityDurationTrack(false);

        handleCtRequest();
        saveLocalVersionFile();
        stopService(new Intent(SplashActivity.this, SendLogService.class));
    }

    private void handleCtRequest() {
        Intent intent = getIntent();
        //读取电信AID和startMode参数
        int ctstartMode = intent.getIntExtra("startMode", -1);
        String ctAID = intent.getStringExtra("AID");
        MLog.d(TAG, "ctAID:" + ctAID);
        MLog.d(TAG, "startMode:" + ctstartMode);
        BizApplication.getInstance().setCtAID(ctAID);
        BizApplication.getInstance().setCtstartMode(ctstartMode);
    }

    private void initData() {
        mGateway = new WalletGateway(SplashActivity.this);
        mLoginManager = new LoginManager(SplashActivity.this);
        mEventGateway = new EventGateway(SplashActivity.this);

        BizApplication.getInstance().setOMApiAvailable(DeviceInfo.isOMApiAvailable());
        String cacheCityCode = MPreference.getString(getApplicationContext(), MPreference.CHOICE_CITY_CODE);
        if (TextUtils.isEmpty(cacheCityCode)) {
            MPreference.putString(getApplicationContext(), MPreference.CHOICE_CITY_NAME, getString(R.string.city_name_guangzhou));
            MPreference.putString(getApplicationContext(), MPreference.CHOICE_CITY_CODE, CssConstant.SINGLE_TICKET.CITY_CODE_GUANGZHOU);
        }
    }

    private void initView() {
        NoticeDialog commonDialog = new NoticeDialog(SplashActivity.this, NoticeDialog.ONE_BUTTON);
        commonDialog.setOnNoticeDialogClickListener(commoNoticeDialogClickListener);
        commonDialog.setButtonName(getString(R.string.ok), "");
        commonDialog.setTitle(getString(R.string.dialog_head));

        mAdvertiseImg = (ImageView) findViewById(R.id.img_advertisement);
        mSkipBtn = (Button) findViewById(R.id.skip_btn);
        mSkipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeAndStopTimer();
            }
        });
        mAdvertiseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Event event = (Event) v.getTag();
                    //                MLog.d(TAG,"######## event = "+event.toString());
                    if (event != null && event.getOpenType().equals(Event.OPEN_TYPE_HTML)) {
                        stopTimer();
                    }
                } catch (Exception e) {
                    //                    CommonToast.toast(getApplicationContext(), "异常了");
                    MLog.e(TAG, "click advertisement occur error = ", e);
                }

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        MLog.d(TAG, "onResume");
        //        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_splashActivity));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLog.d(TAG, "onPause");
        //        UMengShare.countPageAndTimeEnd(this, getString(R.string.statistic_splashActivity));
    }

    @Override
    protected void onDestroy() {
        //        unregisterDownloadReceiver();
        //        stopService(new Intent(this, LockScreenService.class));
        isCanceled = true;
        super.onDestroy();
        MLog.d(TAG, "onDestroy");
        BizApplication.getInstance().removeActivity(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        MLog.d(TAG, "onNewIntent :: action = " + action);
        if (TextUtils.isEmpty(action)) {
            return;
        }
        if (action.equals(BizApplication.ACTION_EXIT_APP)) {
            finish();
        } else if (action.equals(BizApplication.ACTION_RELOGIN_APP)) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }

        super.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        if (isCanceled) {
            isCanceled = true;
        }
        super.onBackPressed();
    }

    private OnNoticeDialogClickListener commoNoticeDialogClickListener = new OnNoticeDialogClickListener() {

        @Override
        public void onRightButtonClicked(View view) {

        }

        @Override
        public void onLeftButtonClicked(View view) {
            finish();
        }
    };

    private void checkLoginState() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                MLog.d(TAG, "trace start----");
                final String statue = MPreference.getString(getApplicationContext(), MPreference.THIRD_LOGIN_STATUE);
                final String uid = MPreference.getString(getApplicationContext(), MPreference.THIRD_LOGIN_UID);
                final String name = MPreference.getString(getApplicationContext(), MPreference.THIRD_LOGIN_NICKNAME);
                final String gender = MPreference.getString(getApplicationContext(), MPreference.THIRD_LOGIN_GENDER);
                final String iconUrl = MPreference.getString(getApplicationContext(), MPreference.THIRD_LOGIN_ICONURL);
                final String uidType = MPreference.getString(getApplicationContext(), MPreference.THIRD_LOGIN_UIDTYPE);
                MLog.d(TAG, "splash保存的status：" + statue + "  uid" + uid + " " + gender + " " + iconUrl);

                String currentIccid = DeviceInfo.getICCID(getApplicationContext());
                String savedIccid = MPreference.getString(getApplicationContext(), MPreference.ICCID);
                MLog.d(TAG, "trace end----");
                MLog.d(TAG, "currentIccid = " + currentIccid + " savedIccid = " + savedIccid);
                if (currentIccid != null && savedIccid != null) {
                    if (!currentIccid.equalsIgnoreCase(savedIccid)) {
                        MPreference.removeLoginPwd(getApplicationContext());
                        MPreference.removeLoginID(getApplicationContext());
                    }
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLoginManager.setHandleLoginListener(new HandleLoginListener() {
                            @Override
                            public void onLoginLogicSuccess() {
                                MLog.d(TAG, "onLoginLogicSuccess");
                                getPicList();
                            }

                            @Override
                            public void onLoginLogicFailed(Result result) {
                                if (result.getCode() == LoginManager.ERROR_CODE_USER_STATUS_INVALID) {
                                    NoticeDialog dialog = new NoticeDialog(SplashActivity.this, NoticeDialog.ONE_BUTTON);
                                    dialog.setOnNoticeDialogClickListener(new OnNoticeDialogClickListener() {
                                        @Override
                                        public void onLeftButtonClicked(View view) {
                                            finish();
                                        }

                                        @Override
                                        public void onRightButtonClicked(View view) {

                                        }
                                    });
                                    dialog.show(result.getMessage());
                                } else {
                                    getPicList();
                                }

                            }

                        });
                        if (LoginManager.hasShankePhoneId(getApplicationContext())) {
                            mLoginManager.requestWalletLoginByToken();
                        } else if (!statue.isEmpty()) {
                            mLoginManager.requestThirdLogin(uid, name, gender, iconUrl, uidType);
                        } else {
                            getPicList();
                        }

                    }
                });
            }
        });


    }

    private void launchHomePage() {
        //        new Handler().postDelayed(new Runnable() {
        //            @Override
        //            public void run() {
        Intent intent = new Intent(SplashActivity.this, STHomeActivity.class);
        startActivity(intent);
        finish();
        //            }
        //        }, LAUNCH_DELAY);


        //        unregisterDownloadReceiver();
    }

    private void launchHomePageDelay() {
        launchHomePage();
    }


    private void launchHomeOrCheckLogin() {
        if (BizApplication.getInstance().getNetworkManager().isNetworkAvailable()) {
            checkLoginState();
        } else {
            launchHomePage();
        }
    }

    private void saveLocalVersionFile() {
        final SingleTicketManager singleTicketManager = new SingleTicketManager(getApplicationContext());
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                singleTicketManager.saveLocalVersionInfo();
            }
        });
    }

    private void getPicList() {
        mEventGateway.getPicList(CssConstant.FLAG_GET_PIC_ADVERTISE, new MobileGateway.MobileGatewayListener<GetPicListRs>() {
            @Override
            public void onFailed(Result result) {
                launchHomePage();
            }

            @Override
            public void onHttpFailed(int statusCode, Header[] headers) {
                launchHomePage();
            }

            @Override
            public void onSuccess(GetPicListRs response) {
                List<Event> list = response.getEventList();
                if (list != null && list.size() > 0) {
                    final Event event = list.get(0);
                    if (isFinishing()) {
                        finish();
                        return;
                    }
                    MLog.d(TAG, "start load pic");
                    Glide.with(SplashActivity.this).load(list.get(0).getEventImageUrl()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            MLog.d(TAG, "load pic onException");
                            launchHomePage();
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            MLog.d(TAG, "load pic onResourceReady");
                            mAdvertiseImg.setTag(event);
                            mSkipBtn.setText(getString(R.string.skip) + "( " + count + "s )");
                            mAdvertiseImg.setVisibility(View.VISIBLE);
                            mSkipBtn.setVisibility(View.VISIBLE);
                            //                            mCountDownTimer.start();
                            timer.schedule(task, 1000, 1000);       // timeTask

                            return false;
                        }
                    }).into(mAdvertiseImg);
                } else {
                    MLog.d(TAG, "pic list is null launch home");
                    launchHomePageDelay();
                }
            }

            @Override
            public void onNoNetwork() {
                launchHomePage();
            }

            @Override
            public void onAutoLoginSuccess() {

            }

            @Override
            public void onAutoLoginFailed(Result result) {

            }

        });
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MLog.d(TAG, "#### ----" + count);
                    if (count == 1) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                launchHomeAndStopTimer();
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mSkipBtn.setText(getString(R.string.skip) + "( " + count + "s )");
                            }
                        });
                    }
                    count--;
                }
            });
        }
    };

    private void launchHomeAndStopTimer() {
        stopTimer();
        launchHomePage();
    }

    private void stopTimer() {
        if (timer != null)
            timer.cancel();
    }
}
