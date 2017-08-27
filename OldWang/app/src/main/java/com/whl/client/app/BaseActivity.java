package com.whl.client.app;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Toast;

import com.whl.framework.http.model.Result;
import com.whl.framework.preference.MSharedPreference;
import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.dialog.NoticeDialog;
import com.whl.client.dialog.NoticeDialog.OnNoticeDialogClickListener;
import com.whl.client.gateway.MobileGateway;
import com.whl.client.gateway.WalletGateway;
import com.whl.client.home.ticket.STHomeActivity;
import com.whl.client.login.SplashActivity;
import com.whl.client.preference.MPreference;
import com.whl.client.view.CssProgressDialog;

import java.util.concurrent.ExecutorService;


public class BaseActivity extends FragmentActivity {

    private static final String TAG = "BaseActivity";
    private WalletGateway mGateway;
    private NoticeDialog noticeDialog;
    NoticeDialog loginstateDialog;
    private CssProgressDialog mProgressDialog;
    public Handler mHandler = new Handler();
    public ExecutorService mExecutor = BizApplication.getInstance().getThreadPool();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        MLog.d(TAG, "onCreate");

        Thread.currentThread().setUncaughtExceptionHandler(new CrashHandler(this));

        mGateway = new WalletGateway(BaseActivity.this);
        noticeDialog = new NoticeDialog(this, NoticeDialog.ONE_BUTTON);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BizApplication.getInstance().setIsAppResume(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BizApplication.getInstance().setIsAppResume(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noticeDialog != null) {
            noticeDialog.dismiss();
        }
    }

    public void exitApp(boolean sendRequest) {
        if (sendRequest) {
            sendLogoutRequset();
        }
        MPreference.clearCacheData(BaseActivity.this);
        BizApplication.getInstance().exitApp(BaseActivity.this);
        finish();
    }

    /**
     * 清除登录信息
     */
    public void clearLoginInfo() {
        LocalBroadcastManager.getInstance(BaseActivity.this).sendBroadcast(new Intent(CssConstant.Action.ACTION_LOGOUT_CLIENT));
        MPreference.removeLoginInfo(getApplicationContext());
        BizApplication.getInstance().setIsLoginClient(false);
        BizApplication.getInstance().setPanchanToken("");
        MobileGateway gateway = new MobileGateway(this);
        gateway.cancelAllRequest();
    }

    public void logoutApp() {
    }

    public void sendLogoutRequset() {
        mGateway.setMaxRetriesAndTimeout(0, 0);
        mGateway.requestLogoutClient(null);
    }


    public void handleAutoLoginFailed(Result result) {
        if (result != null) {
            Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
        }
        logoutApp();
    }

    public void handleChangeCard() {
        // TODO: 2016/10/12 3.0注释
        //        if (loginstateDialog != null && loginstateDialog.isShowing()) {
        //            return;
        //        }
        //
        //        if (loginstateDialog == null) {
        //            loginstateDialog = new NoticeDialog(BaseActivity.this, NoticeDialog.ONE_BUTTON);
        //        }
        //        loginstateDialog.setButtonName(getString(R.string.change_card), getString(R.string.cancel));
        //        loginstateDialog.setCancelable(false);
        //        loginstateDialog.setOnNoticeDialogClickListener(new OnNoticeDialogClickListener() {
        //
        //            @Override
        //            public void onRightButtonClicked(OrderView view) {
        //            }
        //
        //            @Override
        //            public void onLeftButtonClicked(OrderView view) {
        //                startActivity(new Intent(BaseActivity.this, ChangeCardActivity.class));
        //
        //            }
        //        });
        //        loginstateDialog.show(getString(R.string.login_change_card));
    }

    public void handleHttpFaild(final int statusCode) {
        switch (statusCode) {
            case 401:
                noticeDialog.setButtonName(getString(R.string.ok), "");
                noticeDialog.setOnNoticeDialogClickListener(new OnNoticeDialogClickListener() {

                    @Override
                    public void onRightButtonClicked(View view) {

                    }

                    @Override
                    public void onLeftButtonClicked(View view) {
                        switch (statusCode) {
                            case 401:
                                // logoutApp();
                                MSharedPreference.removeCookie(BaseActivity.this);
                                Intent intent = new Intent(BaseActivity.this, SplashActivity.class);
                                intent.setAction(BizApplication.ACTION_RELOGIN_APP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(intent);
                                finish();
                                break;

                            default:
                                break;
                        }
                    }
                });
                noticeDialog.show(getString(R.string.login_state_timeout));
                break;
            default:
                Toast.makeText(getApplicationContext(), getString(R.string.connect_server_failed), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void showBindPhoneNumberDialog(final Activity activity) {
        NoticeDialog dialog = new NoticeDialog(activity, NoticeDialog.TWO_BUTTON);
        dialog.setButtonName(getString(R.string.bind), getString(R.string.cancel));
        dialog.setOnNoticeDialogClickListener(new NoticeDialog.OnNoticeDialogClickListener() {
            @Override
            public void onLeftButtonClicked(View view) {
            }

            @Override
            public void onRightButtonClicked(View view) {

            }
        });
        dialog.show(getString(R.string.need_register_phone));
    }

    /**
     * 显示加载框
     */
    public void showProgress(String msg) {
        showProgressDialog(msg, false);
    }


    /**
     * 显示加载信息
     */

    public void showProgress() {
        showProgressDialog("", false);
    }

    /**
     * 显示加载信息
     */
    public void showProgress(String msg, boolean cancelAble) {
        showProgressDialog(msg, cancelAble);
    }

    private void showProgressDialog(String msg, boolean cancelAble) {
        if (mProgressDialog == null) {
            mProgressDialog = new CssProgressDialog(this);
        }
        mProgressDialog.setContent(msg);
        if (cancelAble)
            mProgressDialog.setCancelable(true);
        else
            mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    /**
     * 关闭dialog
     */
    public void dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    //
    //    @Override
    //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //        FragmentManager fm = getSupportFragmentManager();
    //        int index = requestCode >> 16;
    //        if (index != 0) {
    //            index--;
    //            if (fm.getFragments() == null || index < 0 || index >= fm.getFragments().size()) {
    //                MLog.d(TAG, "Activity result fragment index out of range: 0x" + Integer.toHexString(requestCode));
    //                return;
    //            }
    //            Fragment frag = fm.getFragments().get(index);
    //            if (frag == null) {
    //                MLog.d(TAG, "Activity result no fragment exists for index: 0x" + Integer.toHexString(requestCode));
    //            } else {
    //                handleResult(frag, requestCode, resultCode, data);
    //            }
    //            return;
    //        }
    //    }
    //
    //    private void handleResult(Fragment frag, int requestCode, int resultCode, Intent data) {
    //        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
    //        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
    //        if (frags != null) {
    //            for (Fragment f : frags) {
    //                if (f != null)
    //                    handleResult(f, requestCode, resultCode, data);
    //            }
    //        }
    //    }
    public void moveToHomePage() {
        Intent homeIntent = new Intent(this, STHomeActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
    }

    /**
     * 根据action确定跳转至哪个订单页面
     */
    public void moveToOrderPage(String action) {
        Intent intent = new Intent(this, STHomeActivity.class);
        intent.setAction(action);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
