package com.whl.client.home.ticket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.whl.framework.download.DownloadInfo;
import com.whl.framework.http.model.Result;
import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.app.BaseActivity;
import com.whl.client.app.BizApplication;
import com.whl.client.app.CssConstant;
import com.whl.client.dialog.NoticeDialog;
import com.whl.client.dialog.UpgradeDialog;
import com.whl.client.gateway.MobileGateway;
import com.whl.client.gateway.WalletGateway;
import com.whl.client.gateway.model.wallet.CheckWalletUpdateRs;
import com.whl.client.preference.MPreference;
import com.whl.client.umengShare.UMengShare;
import com.whl.client.upgrade.DownLoadActivity;
import com.whl.client.upgrade.DownLoadService;

import org.apache.http.Header;

/**
 * Created by liwx on 2015/10/26.
 */
public class STHomeActivity extends BaseActivity {
    private static final String TAG = "STHomeActivity";

    private WalletGateway mWalletGateway;

    private long mExitTime;
    private boolean isForceUpgrade = false;
    private UpgradeDialog upgradeDialog;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        MLog.d(TAG, "onCreate ");
        setContentView(R.layout.activity_single_ticket_home);
        BizApplication.getInstance().addActivity(this);
        initData();
        onNewIntent(getIntent());
        LocalBroadcastManager.getInstance(STHomeActivity.this).unregisterReceiver(mDownloadActivityReceiver);
        registerUpgradeReceiver();
        checkWalletUpdate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        MLog.d(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        MLog.d(TAG, "onNewIntent");
        String action = intent.getAction();
        MLog.d(TAG, "action = " + action);
        if (TextUtils.isEmpty(action)) {
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLog.d(TAG, "onResume");
        UMengShare.countTimeResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLog.d(TAG, "onPause");
        UMengShare.countTimePause(this);
    }


    private void initData() {
        mWalletGateway = new WalletGateway(STHomeActivity.this);
        String cacheCityCode = MPreference.getString(getApplicationContext(), MPreference.CHOICE_CITY_CODE);
        if (TextUtils.isEmpty(cacheCityCode)) {
            MPreference.putString(getApplicationContext(), MPreference.CHOICE_CITY_NAME, getString(R.string.city_name_guangzhou));
            MPreference.putString(getApplicationContext(), MPreference.CHOICE_CITY_CODE, CssConstant.SINGLE_TICKET.CITY_CODE_GUANGZHOU);
        }
        upgradeDialog = new UpgradeDialog(STHomeActivity.this, UpgradeDialog.TWO_BUTTON);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 1500) {
            Toast.makeText(getApplicationContext(), getString(R.string.double_click_exit), Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            BizApplication.getInstance().exit();
        }
    }

    @Override
    protected void onDestroy() {
        MLog.d(TAG, "onDestroy");
        super.onDestroy();
        BizApplication.getInstance().removeActivity(this);
    }

    /**
     * 检查客户端版本
     */
    private void checkWalletUpdate() {
        mWalletGateway.checkUpdateClient(new MobileGateway.MobileGatewayListener<CheckWalletUpdateRs>() {

            @Override
            public void onSuccess(CheckWalletUpdateRs response) {
                handleCheckUpdateResult(response);
            }

            @Override
            public void onNoNetwork() {
            }

            @Override
            public void onHttpFailed(int statusCode, Header[] headers) {
            }

            @Override
            public void onFailed(Result result) {
                NoticeDialog dialog = new NoticeDialog(STHomeActivity.this, NoticeDialog.ONE_BUTTON);
                dialog.show(result.getMessage());
                dialog.setOnNoticeDialogClickListener(new NoticeDialog.OnNoticeDialogClickListener() {
                    @Override
                    public void onLeftButtonClicked(View view) {
                        finish();
                    }

                    @Override
                    public void onRightButtonClicked(View view) {

                    }
                });
            }

            @Override
            public void onAutoLoginSuccess() {

            }

            @Override
            public void onAutoLoginFailed(Result result) {

            }
        });
    }

    private void handleCheckUpdateResult(final CheckWalletUpdateRs response) {
        MPreference.saveAppDownLoadUrl(BizApplication.getInstance(), response.getDownloadUrl());
        if (response.getExistsUpdateVersion().equalsIgnoreCase("Y")) {
            isForceUpgrade = response.getMandatoryYn().equalsIgnoreCase("Y");
            upgradeDialog.setOnNoticeDialogClickListener(new NoticeDialog.OnNoticeDialogClickListener() {
                @Override
                public void onLeftButtonClicked(View view) {
                    if (isForceUpgrade) {
                        BizApplication.getInstance().exit();
                    } else {
                        checkNotRemind(response);
                    }
                }

                @Override
                public void onRightButtonClicked(View view) {
                    DownloadInfo downloadInfo = new DownloadInfo();
                    downloadInfo.setUrl(response.getDownloadUrl());
                    downloadInfo.setAppName(getString(R.string.shankephone_app_name));
                    downloadInfo.setMendetory(isForceUpgrade);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DownLoadService.DOWNLOAD_DOWNLOADINFO, downloadInfo);
                    Intent intent = new Intent(STHomeActivity.this, DownLoadActivity.class);
                    intent.putExtra(DownLoadService.DOWNLOAD_DOWNLOADINFO, bundle);
                    intent.putExtra(DownLoadActivity.IS_MENDETORY, isForceUpgrade);
                    startActivity(intent);
                }
            });
            upgradeDialog.setVersion("v" + response.getUpdateVersion());
            upgradeDialog.setContent(response.getAppversionDesc());
            if (isForceUpgrade) {
                upgradeDialog.setButtonName(getString(R.string.cancel), getString(R.string.update_now));
                upgradeDialog.show();
            } else {
                if (MPreference.getRemindDownloadFlag(getApplicationContext())) {
                    String version = MPreference.getNotRemindVersion(getApplicationContext());
                    if (TextUtils.isEmpty(version) || !version.equals(response.getUpdateVersion().trim())) {
                        upgradeDialog.show();
                    }
                } else {
                    upgradeDialog.show();
                }
            }

        }
    }

    private void checkNotRemind(CheckWalletUpdateRs response) {
        MPreference.saveRemindDownloadFlag(getApplicationContext(), true);
        MPreference.saveNotRemindVersion(getApplicationContext(), response.getUpdateVersion());
    }

    private void registerUpgradeReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownLoadActivity.ACTION_DOWNLOAD_CLICK_RUN_IN_BACKGROUND);
        filter.addAction(DownLoadActivity.ACTION_DOWNLOAD_CLICK_CANCEL);
        LocalBroadcastManager.getInstance(STHomeActivity.this).registerReceiver(mDownloadActivityReceiver, filter);
    }

    private BroadcastReceiver mDownloadActivityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            MLog.d(TAG, "onReceive :: action = " + action);
            if (action.equals(DownLoadActivity.ACTION_DOWNLOAD_CLICK_CANCEL)) {
                if (isForceUpgrade) {
                    BizApplication.getInstance().exit();
                }
            }
        }
    };

}
