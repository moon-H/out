//package com.cssweb.shankephone.settings;
//
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cssweb.framework.download.DownloadInfo;
//import com.cssweb.framework.http.model.Result;
//import com.cssweb.framework.utils.DeviceInfo;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.framework.utils.Utils;
//import com.cssweb.shankephone.BuildConfig;
//import com.cssweb.shankephone.R;
//import com.cssweb.shankephone.app.BaseActivity;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.app.CommonToast;
//import com.cssweb.shankephone.dialog.NoticeDialog;
//import com.cssweb.shankephone.dialog.NoticeDialog.OnNoticeDialogClickListener;
//import com.cssweb.shankephone.dialog.UpgradeDialog;
//import com.cssweb.shankephone.gateway.MobileGateway.MobileGatewayListener;
//import com.cssweb.shankephone.gateway.SpServiceGateway;
//import com.cssweb.shankephone.gateway.WalletGateway;
//import com.cssweb.shankephone.gateway.model.wallet.CheckWalletUpdateRs;
//import com.cssweb.shankephone.home.card.seservice.instance.CtServiceAccessor;
//import com.cssweb.shankephone.home.card.seservice.instance.TransitChangSha;
//import com.cssweb.shankephone.login.LoginActivity;
//import com.cssweb.shankephone.login.LoginManager;
//import com.cssweb.shankephone.login.register.RegisterThirdUserActivity;
//import com.cssweb.shankephone.preference.MPreference;
//import com.cssweb.shankephone.umengShare.UMengShare;
//import com.cssweb.shankephone.upgrade.DownLoadActivity;
//import com.cssweb.shankephone.upgrade.DownLoadService;
//import com.cssweb.shankephone.view.TitleBarView;
//
//import org.apache.http.Header;
//
//
//public class SettingsActivity extends BaseActivity implements OnClickListener, TitleBarView.OnTitleBarClickListener {
//
//    private static final String TAG = "SettingsActivity";
//    private WalletGateway mGateway;
//    private NoticeDialog mCheckVersionDialog;
//    //    private NoticeDialog mLogoutDialog;
//    private UpgradeDialog upgradeDialog;
//    private boolean isForceUpgrade = false;
//    private String mDownLoadUrl;
//    private String mDownLoadAppName;
//    private View mProgressView;
//    private Button mExitButton;
//    private TextView mRegister;
//    private TextView mNumber;
//    private ImageView mBindArrow;
//
//    private CtServiceAccessor mCtServiceAccessor;
//
//    @Override
//    public void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        MLog.d(TAG, "onCreate");
//        setContentView(R.layout.fragment_settings);
//        BizApplication.getInstance().addActivity(this);
//        TitleBarView mTitleBar = (TitleBarView) findViewById(R.id.title_bar);
//        mTitleBar.setTitle(getString(R.string.settings_title));
//        mTitleBar.setOnTitleBarClickListener(this);
//        mCheckVersionDialog = new NoticeDialog(SettingsActivity.this, NoticeDialog.TWO_BUTTON);
//        mCheckVersionDialog.setTitle(getString(R.string.dialog_head));
//        mCheckVersionDialog.setButtonName(getString(R.string.upgrade), getString(R.string.cancel));
//        mCheckVersionDialog.setOnNoticeDialogClickListener(checkVersionDialogClickListener);
//
//        NoticeDialog mCommonDialog = new NoticeDialog(SettingsActivity.this, NoticeDialog.ONE_BUTTON);
//        mCommonDialog.setOnNoticeDialogClickListener(commonDialogClickListener);
//        mCommonDialog.setButtonName(getString(R.string.ok), "");
//        mCommonDialog.setTitle(getString(R.string.dialog_head));
//
//        upgradeDialog = new UpgradeDialog(SettingsActivity.this, UpgradeDialog.TWO_BUTTON);
//
//        mGateway = new WalletGateway(SettingsActivity.this);
//
//        View mCheckVersionView = findViewById(R.id.check_version);
//        View mFeedbackView = findViewById(R.id.feedback);
//        View mAboutView = findViewById(R.id.about);
//        View mHelpView = findViewById(R.id.help);
//
//        View mRegisterView = findViewById(R.id.ll_register_number);
//        mRegisterView.setOnClickListener(this);
//        mRegister = (TextView) findViewById(R.id.tv_register_phone);
//        mNumber = (TextView) findViewById(R.id.tv_phone_number);
//        mBindArrow = (ImageView) findViewById(R.id.ic_bind_arrow);
//        mExitButton = (Button) findViewById(R.id.exit);
//        mProgressView = findViewById(R.id.progressView);
//        View mNfcReaderView = findViewById(R.id.nfc_reader);
//        mCheckVersionView.setOnClickListener(this);
//        mFeedbackView.setOnClickListener(this);
//        mAboutView.setOnClickListener(this);
//        mHelpView.setOnClickListener(this);
//        mExitButton.setOnClickListener(this);
//        mNfcReaderView.setOnClickListener(this);
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(DownLoadActivity.ACTION_DOWNLOAD_CLICK_RUN_IN_BACKGROUND);
//        filter.addAction(DownLoadActivity.ACTION_DOWNLOAD_CLICK_CANCEL);
//        registerReceiver(mDownloadActivityReceiver, filter);
//
//        checkRegisterNumber();
//
//        if (BuildConfig.LOG_DEBUG && !TextUtils.isEmpty(BizApplication.getInstance().getCtAID())) {
//            Button deleteCtService = (Button) findViewById(R.id.btn_delete_ct);
//            deleteCtService.setVisibility(View.VISIBLE);
//            deleteCtService.setOnClickListener(this);
//            mCtServiceAccessor = BizApplication.getInstance().getSeManager().getCtServiceAccessor(SettingsActivity.this, ctServiceCallback, CtServiceAccessor.TYPE_WALLET);
//        }
//    }
//
//    private void checkRegisterNumber() {
//        MLog.d(TAG, LoginManager.getPhoneNumber(this));
//        if (BizApplication.getInstance().isLoginClient()) {
//            if (TextUtils.isEmpty(LoginManager.getPhoneNumber(this))) {
//                mBindArrow.setVisibility(View.VISIBLE);
//            } else {
//                mBindArrow.setVisibility(View.GONE);
//            }
//            if (LoginManager.hasShankePhoneId(this)) {
//                mNumber.setText(MPreference.getLoginId(this));
//            } else {
//                if (!TextUtils.isEmpty(MPreference.getString(getApplicationContext(), MPreference.THIRD_RIGESTER_PHONENUMBER)))
//                    //                    showBindPhoneNumberDialog(this);
//                    //                else{
//                    mNumber.setText(LoginManager.getPhoneNumber(this));
//                //                }
//            }
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        MLog.d(TAG, "onResume");
//        if (BizApplication.getInstance().isLoginClient()) {
//            checkRegisterNumber();
//            mExitButton.setText(getString(R.string.settings_exit));
//        } else {
//            mExitButton.setText(getString(R.string.login));
//        }
//        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_splashActivity));
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        MLog.d(TAG, "onPause");
//        UMengShare.countPageAndTimeEnd(this, getString(R.string.statistic_splashActivity));
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        MLog.d(TAG, "onDestroy");
//        unregisterReceiver(mDownloadActivityReceiver);
//        BizApplication.getInstance().removeActivity(this);
//        if (BuildConfig.LOG_DEBUG && mCtServiceAccessor != null) {
//            if (!TextUtils.isEmpty(BizApplication.getInstance().getCtAID()))
//                mCtServiceAccessor.unbindWalletService();
//        }
//    }
//
//    private BroadcastReceiver mDownloadActivityReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            MLog.d(TAG, "onReceive :: action = " + action);
//            if (action.equals(DownLoadActivity.ACTION_DOWNLOAD_CLICK_RUN_IN_BACKGROUND)) {
//            } else if (action.equals(DownLoadActivity.ACTION_DOWNLOAD_CLICK_CANCEL)) {
//                if (isForceUpgrade) {
//                    // TODO Need check logic
//                    exitApp(true);
//                }
//            }
//        }
//    };
//
//    @Override
//    public void onClick(View v) {
//        if (Utils.isFastDoubleClick())
//            return;
//        MLog.d(TAG, "onClick");
//        switch (v.getId()) {
//            case R.id.check_version:
//                checkVersion();
//                //                requestAddMyService("18021");
//                break;
//            case R.id.feedback:
//                if (BizApplication.getInstance().isLoginClient())
//                    if (LoginManager.hasPhoneNumber(getApplicationContext()))
//                        startActivity(new Intent(SettingsActivity.this, FeedbackActivity.class));
//                    else {
//                        showBindPhoneNumberDialog(SettingsActivity.this);
//                    }
//                else
//                    launchLoginPage();
//                break;
//            case R.id.about:
//                startActivity(new Intent(SettingsActivity.this, AboutActivity.class));
//                break;
//            case R.id.help:
//                startActivity(new Intent(SettingsActivity.this, HelpActivity.class));
//                break;
//            case R.id.exit:
//                if (BizApplication.getInstance().isLoginClient()) {
//                    handleLogout();
//                } else {
//                    startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
//                }
//                break;
//
//            case R.id.nfc_reader:
//                checkNfcAvailable();
//                break;
//            case R.id.ll_register_number:
//                if (BizApplication.getInstance().isLoginClient()) {
//                    if (TextUtils.isEmpty(LoginManager.getPhoneNumber(this))) {
//                        Intent intent = new Intent(this, RegisterThirdUserActivity.class);
//                        startActivity(intent);
//                    }
//                } else {
//                    launchLoginPage();
//                }
//                break;
//            case R.id.btn_delete_ct:
//                if (mCtServiceAccessor != null) {
//                    showProgressView();
//                    mCtServiceAccessor.sendAuthenticateRequest(CtServiceAccessor.OPERTYPE_DELETE, TransitChangSha.AID_CHANGSHA);
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void handleLogout() {
//        NoticeDialog mLogoutDialog = new NoticeDialog(SettingsActivity.this, NoticeDialog.TWO_BUTTON);
//        mLogoutDialog.setOnNoticeDialogClickListener(new OnNoticeDialogClickListener() {
//            @Override
//            public void onLeftButtonClicked(View view) {
//                clearLoginInfo();
//                mNumber.setText("");
//                mGateway.requestLogoutClient(null);
//                mExitButton.setText(getString(R.string.login));
//            }
//
//            @Override
//            public void onRightButtonClicked(View view) {
//
//            }
//        });
//        mLogoutDialog.setButtonName(getString(R.string.ok), getString(R.string.cancel));
//        mLogoutDialog.setTitle(getString(R.string.dialog_head));
//        mLogoutDialog.show(getString(R.string.settings_logout_confirm));
//    }
//
//    private void launchLoginPage() {
//        startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
//    }
//
//    private void checkNfcAvailable() {
//        if (DeviceInfo.isNfcAvailable(SettingsActivity.this)) {
//            //            startActivity(new Intent(SettingsActivity.this, NfcReadRecordActivity.class));
//        } else {
//            NoticeDialog dialog = new NoticeDialog(SettingsActivity.this, NoticeDialog.ONE_BUTTON);
//            dialog.show(getString(R.string.device_not_support_nfc));
//            dialog.setOnNoticeDialogClickListener(new OnNoticeDialogClickListener() {
//
//                @Override
//                public void onLeftButtonClicked(View view) {
//                    // TODO Auto-generated method stub
//
//                }
//
//                @Override
//                public void onRightButtonClicked(View view) {
//                    // TODO Auto-generated method stub
//
//                }
//            });
//        }
//    }
//
//    private OnNoticeDialogClickListener checkVersionDialogClickListener = new OnNoticeDialogClickListener() {
//
//        @Override
//        public void onLeftButtonClicked(View view) {
//
//            DownloadInfo downloadInfo = new DownloadInfo();
//            downloadInfo.setUrl(mDownLoadUrl);
//            downloadInfo.setAppName(mDownLoadAppName);
//            downloadInfo.setMendetory(isForceUpgrade);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(DownLoadService.DOWNLOAD_DOWNLOADINFO, downloadInfo);
//            Intent intent = new Intent(SettingsActivity.this, DownLoadActivity.class);
//            intent.putExtra(DownLoadService.DOWNLOAD_DOWNLOADINFO, bundle);
//            startActivity(intent);
//        }
//
//        @Override
//        public void onRightButtonClicked(View view) {
//            if (isForceUpgrade) {
//                // TODO Need check logic
//                exitApp(true);
//            }
//
//        }
//    };
//    private OnNoticeDialogClickListener commonDialogClickListener = new OnNoticeDialogClickListener() {
//
//        @Override
//        public void onLeftButtonClicked(View view) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onRightButtonClicked(View view) {
//            // TODO Auto-generated method stub
//
//        }
//    };
//    private OnNoticeDialogClickListener logouDialogClickListener = new OnNoticeDialogClickListener() {
//
//        @Override
//        public void onLeftButtonClicked(View view) {
//
//        }
//
//        @Override
//        public void onRightButtonClicked(View view) {
//            // TODO Auto-generated method stub
//
//        }
//    };
//
//    private void showProgressView() {
//        BizApplication.getInstance().getProgressDialog(SettingsActivity.this).show();
//    }
//
//    private void dissmissProgressView() {
//        BizApplication.getInstance().dismissProgressDialog();
//    }
//
//    private void checkVersion() {
//        MLog.d(TAG, "checkVersion");
//        showProgressView();
//        // TODO Need check logic
//        mGateway.checkUpdateClient(new MobileGatewayListener<CheckWalletUpdateRs>() {
//
//            @Override
//            public void onFailed(Result result) {
//                dissmissProgressView();
//                Toast.makeText(SettingsActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dissmissProgressView();
//                handleHttpFaild(statusCode);
//            }
//
//            @Override
//            public void onSuccess(CheckWalletUpdateRs response) {
//                dissmissProgressView();
//                handleCheckUpdateResult(response);
//                //                if (response.getExistsUpdateVersion().equalsIgnoreCase("Y")) {
//                //                    isForceUpgrade = response.getMandatoryYn().equalsIgnoreCase("Y");
//                //                    mDownLoadUrl = response.getDownloadUrl();
//                //                    mDownLoadAppName = getString(R.string.shankephone_app_name);
//                //                    String version = response.getUpdateVersion();
//                //                    MPreference.saveAppDownLoadUrl(SettingsActivity.this, mDownLoadUrl);
//                //                    if (isForceUpgrade) {
//                //                        mCheckVersionDialog.setTitle(getString(R.string.find_new_version) + " " + version);
//                //                        mCheckVersionDialog.show(getString(R.string.mendatory_update) + "\n" + "\n" + response.getAppversionDesc());
//                //
//                //                    } else {
//                //                        mCheckVersionDialog.setTitle(getString(R.string.find_new_version) + " " + version);
//                //                        mCheckVersionDialog.show(response.getAppversionDesc());
//                //                    }
//                //                } else {
//                //                    Toast.makeText(SettingsActivity.this, getString(R.string.settings_latest_version), Toast.LENGTH_SHORT).show();
//                //                }
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dissmissProgressView();
//                Toast.makeText(SettingsActivity.this, getString(R.string.network_exception), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                dissmissProgressView();
//                checkVersion();
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                dissmissProgressView();
//                handleAutoLoginFailed(result);
//            }
//
//        });
//    }
//
//    @Override
//    public void onBackClicked(View view) {
//        finish();
//    }
//
//    @Override
//    public void onMenuClicked(View view) {
//
//    }
//
//    SpServiceGateway spServiceGateway;
//
//    private CtServiceAccessor.CtServiceCallback ctServiceCallback = new CtServiceAccessor.CtServiceCallback() {
//        @Override
//        public void handleAuthenticateRequest(boolean authenticateResult) {
//            dissmissProgressView();
//            MLog.d(TAG, "handleAuthenticateRequest " + authenticateResult);
//            if (mCtServiceAccessor != null) {
//                if (!TextUtils.isEmpty(BizApplication.getInstance().getCtAID())) {
//                    if (authenticateResult) {
//                        mCtServiceAccessor.sendAuthenticateResult(true, CtServiceAccessor.OPERTYPE_INSTALL, BizApplication.getInstance().getCtAID());
//                        BizApplication.getInstance().exitShankePhone();
//                    }
//                } else {
//                    //open NFC
//                }
//            } else {
//                MLog.d(TAG, "handleAuthenticateRequest mCtServiceAccessor is null");
//            }
//        }
//
//        @Override
//        public void handleAuthenticateResult(boolean result) {
//            MLog.d(TAG, "handleAuthenticateResult " + result);
//
//        }
//
//        @Override
//        public void onOpenNfcInstall(boolean result, String mes) {
//
//        }
//
//        @Override
//        public void onNeedDismiss() {
//
//        }
//    };
//
//    private void handleCheckUpdateResult(final CheckWalletUpdateRs response) {
//        //        mDownLoadUrl = response.getDownloadUrl();
//        MPreference.saveAppDownLoadUrl(BizApplication.getInstance(), response.getDownloadUrl());
//        if (response.getExistsUpdateVersion().equalsIgnoreCase("Y")) {
//            //            mUpdateVersion = response.getUpdateVersion().trim();
//            isForceUpgrade = response.getMandatoryYn().equalsIgnoreCase("Y");
//            upgradeDialog.setOnNoticeDialogClickListener(new NoticeDialog.OnNoticeDialogClickListener() {
//                @Override
//                public void onLeftButtonClicked(View view) {
//                    if (isForceUpgrade) {
//                        BizApplication.getInstance().exit();
//                        //                    finish();
//                    } else {
//                        checkNotRemind(response);
//                    }
//                }
//
//                @Override
//                public void onRightButtonClicked(View view) {
//                    //                    checkNotRemind(response);
//                    DownloadInfo downloadInfo = new DownloadInfo();
//                    downloadInfo.setUrl(response.getDownloadUrl());
//                    downloadInfo.setAppName(getString(R.string.shankephone_app_name));
//                    downloadInfo.setMendetory(isForceUpgrade);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(DownLoadService.DOWNLOAD_DOWNLOADINFO, downloadInfo);
//                    Intent intent = new Intent(SettingsActivity.this, DownLoadActivity.class);
//                    intent.putExtra(DownLoadService.DOWNLOAD_DOWNLOADINFO, bundle);
//                    intent.putExtra(DownLoadActivity.IS_MENDETORY, isForceUpgrade);
//                    startActivity(intent);
//                }
//            });
//            upgradeDialog.setVersion("v" + response.getUpdateVersion());
//            upgradeDialog.setContent(response.getAppversionDesc());
//            if (isForceUpgrade) {
//                upgradeDialog.setButtonName(getString(R.string.cancel), getString(R.string.update_now));
//                //                showUpgradeDialog(getString(R.string.mendatory_update) + "\n" + "\n" + response.getAppversionDesc());
//                upgradeDialog.show();
//            } else {
//                //                if (MPreference.getRemindDownloadFlag(getApplicationContext())) {
//                //                    String version = MPreference.getNotRemindVersion(getApplicationContext());
//                //                    if (version != null && version.equals(response.getUpdateVersion().trim())) {
//                //                        //                        upgradeDialog.showCheckBox(false);
//                //                        //                                checkWalletStatusBySeId();
//                //                        //                        checkLoginState();
//                //                    } else {
//                //                        //                        upgradeDialog.showCheckBox(true);
//                //                        //                        showUpgradeDialog(response.getAppversionDesc());
//                //                        upgradeDialog.show();
//                //                    }
//                //                } else {
//                //                    upgradeDialog.showCheckBox(true);
//                //                    showUpgradeDialog(response.getAppversionDesc());
//                upgradeDialog.show();
//                //                }
//            }
//
//        } else {
//            //                    checkWalletStatusBySeId();
//            //            checkLoginState();
//            CommonToast.toast(getApplicationContext(), getString(R.string.settings_latest_version));
//        }
//    }
//
//    private void checkNotRemind(CheckWalletUpdateRs response) {
//        //        if (upgradeDialog.isChecked()) {
//        MPreference.saveRemindDownloadFlag(getApplicationContext(), true);
//        MPreference.saveNotRemindVersion(getApplicationContext(), response.getUpdateVersion());
//        //        } else {
//        //            MPreference.saveRemindDownloadFlag(getApplicationContext(), false);
//        //        }
//    }
//}
