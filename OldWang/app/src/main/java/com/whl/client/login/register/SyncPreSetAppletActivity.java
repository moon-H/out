//package com.cssweb.shankephone.login.register;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.text.TextUtils;
//
//import com.cssweb.framework.preference.MSharedPreference;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.app.CrashHandler;
//import com.cssweb.shankephone.gateway.MobileGateway;
//import com.cssweb.shankephone.gateway.WalletGateway;
//import com.cssweb.shankephone.gateway.model.spservice.Service;
//import com.cssweb.shankephone.login.SplashActivity;
//import com.cssweb.shankephone.preference.MPreference;
//import com.cssweb.shankephone.home.card.seservice.SeManager;
//import com.cssweb.shankephone.home.card.SpServiceManager;
//import com.cssweb.shankephone.view.CssProgressDialog;
//
//import java.util.List;
//
//
//public class SyncPreSetAppletActivity extends FragmentActivity {
//
//    private static final String TAG = "SyncPreSetAppletActivity";
//
//    private SeManager mSeManager;
//    public SpServiceManager mSpServiceManager;
//    private CssProgressDialog mProgressDialog;
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        Thread.currentThread().setUncaughtExceptionHandler(new CrashHandler(this));
//        mSeManager = BizApplication.getInstance().getSeManager();
//        mSpServiceManager = new SpServiceManager(SyncPreSetAppletActivity.this);
//    }
//
//    public void logoutApp() {
//        MSharedPreference.removeCookie(SyncPreSetAppletActivity.this);
//        Intent intent = new Intent(SyncPreSetAppletActivity.this, SplashActivity.class);
//        intent.setAction(BizApplication.ACTION_RELOGIN_APP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//        startActivity(intent);
//        finish();
//    }
//
//    public void launchDefaltServicePage(boolean isCancel, Service service, boolean isFinish) {
//        // TODO: 2016/10/12 3.0修改
//        //        if (isCancel || isFinishing()) {
//        //            return;
//        //        }
//        //        Intent intent = new Intent(SyncPreSetAppletActivity.this, CsSubwayDetailActivity.class);
//        //        intent.putExtra("service", service);
//        //        intent.putExtra(CsSubwayDetailActivity.FIRST_LAUNCH, true);
//        //        startActivity(intent);
//        //        if (isFinish) {
//        //            finish();
//        //        }
//    }
//
//    public void sendActiveCode(MobileGateway mGateway, List<Service> myServiceList) {
//        List<String> preSetAidList = mSpServiceManager.getPreSetServiceList(myServiceList);
//        if (preSetAidList != null && preSetAidList.size() > 0) {
//            // TODO 添加发送激活码接口
//            new WalletGateway(SyncPreSetAppletActivity.this).sendActiveSubAuthCode(MPreference.getLoginId(getApplicationContext()), null);
//        } else {
//            MLog.d(TAG, "Conditions are not satisfied!Not send active");
//        }
//    }
//
//
//    /**
//     * 显示加载框
//     */
//    public void showProgress(String msg) {
//        showProgressDialog(msg, false);
//    }
//
//
//    /**
//     * 显示加载信息
//     */
//
//    public void showProgress() {
//        showProgressDialog("", false);
//    }
//
//    /**
//     * 显示加载信息
//     */
//    public void showProgress(String msg, boolean cancelAble) {
//        showProgressDialog(msg, cancelAble);
//    }
//
//    private void showProgressDialog(String msg, boolean cancelAble) {
//        if (mProgressDialog == null) {
//            mProgressDialog = new CssProgressDialog(this);
//        }
//        if (!TextUtils.isEmpty(msg))
//            mProgressDialog.setContent(msg);
//        if (cancelAble)
//            mProgressDialog.setCancelable(true);
//        else
//            mProgressDialog.setCancelable(false);
//        mProgressDialog.show();
//    }
//
//    /**
//     * 关闭dialog
//     */
//    public void dismissProgress() {
//        if (mProgressDialog != null) {
//            mProgressDialog.dismiss();
//        }
//    }
//}
