package com.whl.client.login;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.whl.framework.http.model.Result;
import com.whl.framework.utils.MLog;
import com.whl.framework.utils.Utils;
import com.whl.client.R;
import com.whl.client.app.BaseActivity;
import com.whl.client.app.BizApplication;
import com.whl.client.dialog.NoticeDialog;
import com.whl.client.dialog.NoticeDialog.OnNoticeDialogClickListener;
import com.whl.client.gateway.MobileGateway.MobileGatewayListener;
import com.whl.client.gateway.WalletGateway;
import com.whl.client.gateway.model.wallet.ResetUserLoginPwdRs;
import com.whl.client.umengShare.UMengShare;
import com.whl.client.view.ClearEditText;
import com.whl.client.view.TitleBarView;
import com.whl.client.view.TitleBarView.OnTitleBarClickListener;

import org.apache.http.Header;


public class ResetLoginPwdActivity extends BaseActivity implements OnClickListener, OnFocusChangeListener {

    private static final String TAG = "ResetLoginPwdActivity";
        private TitleBarView mTitleBar;
    //    private OrderView mProgressView;
    private NoticeDialog mCommonDialog;
    private WalletGateway mGateway;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        MLog.d(TAG, "onCreate");
        setContentView(R.layout.activity_find_pwd);
        BizApplication.getInstance().addActivity(this);

        mTitleBar = (TitleBarView) findViewById(R.id.title_bar);
        mTitleBar.setTitle(getString(R.string.reset_find_pwd));
        mTitleBar.setOnTitleBarClickListener(titleBarClickListener);
        mTitleBar.showBackButton(true);

        mGateway = new WalletGateway(ResetLoginPwdActivity.this);

        mCommonDialog = new NoticeDialog(ResetLoginPwdActivity.this, NoticeDialog.ONE_BUTTON);
        mCommonDialog.setOnNoticeDialogClickListener(commoNoticeDialogClickListener);
        mCommonDialog.setButtonName(getString(R.string.ok), "");
        mCommonDialog.setTitle(getString(R.string.dialog_head));

        Button btnFindPwd = (Button) findViewById(R.id.btn_find_pwd);
        btnFindPwd.setOnClickListener(this);

        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);

        ClearEditText edtMobile = (ClearEditText) findViewById(R.id.edittext_mobile);
        //        mProgressView = findViewById(R.id.progressView);
        edtMobile.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        Utils.hideSoftInput(getApplicationContext(), findViewById(R.id.edittext_mobile));
        switch (v.getId()) {
            case R.id.btn_find_pwd:
                findPwd();
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLog.d(TAG, "onResume");
        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_ResetLoginPwdActivity));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLog.d(TAG, "onPause");
        UMengShare.countPageAndTimeEnd(this, getString(R.string.statistic_ResetLoginPwdActivity));
    }

    @Override
    protected void onDestroy() {
        if (mCommonDialog != null) {
            mCommonDialog.dismiss();
        }
        super.onDestroy();
        BizApplication.getInstance().removeActivity(this);
        dismissProgressView();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {

            case R.id.edittext_mobile:
                ClearEditText edtMobile = (ClearEditText) findViewById(R.id.edittext_mobile);
                edtMobile.onFocusChange(v, hasFocus);
                String mobileStr = edtMobile.getText().toString().trim();

                if (hasFocus == false) {
                    if (mobileStr.length() == 0) {
                        Toast.makeText(ResetLoginPwdActivity.this, getString(R.string.verify_input_phone), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!isMobileNO(mobileStr)) {
                        Toast.makeText(ResetLoginPwdActivity.this, getString(R.string.input_valid_phone_number), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;
        }
    }

    private void findPwd() {
        ClearEditText edtUserId = (ClearEditText) findViewById(R.id.edittext_mobile);
        if (edtUserId.getText().toString().length() == 0) {
            Toast.makeText(ResetLoginPwdActivity.this, getString(R.string.verify_input_phone), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isMobileNO(edtUserId.getText().toString().trim())) {
            Toast.makeText(ResetLoginPwdActivity.this, getString(R.string.input_valid_phone_number), Toast.LENGTH_SHORT).show();
            return;
        }
        showProgressView();
        mGateway.resetUserLoginPwd(edtUserId.getText().toString().trim(), new MobileGatewayListener<ResetUserLoginPwdRs>() {

            @Override
            public void onFailed(Result result) {
                dismissProgressView();
                Toast.makeText(ResetLoginPwdActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onHttpFailed(int statusCode, Header[] headers) {
                dismissProgressView();
                Toast.makeText(ResetLoginPwdActivity.this, getString(R.string.connect_server_failed), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResetUserLoginPwdRs response) {
                dismissProgressView();
//                BizApplication.getInstance().getLockPatternUtils().clearLock();
                mCommonDialog.show(getString(R.string.send_password_to_email));
            }

            @Override
            public void onNoNetwork() {
                dismissProgressView();
                Toast.makeText(ResetLoginPwdActivity.this, getString(R.string.network_exception), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAutoLoginSuccess() {

            }

            @Override
            public void onAutoLoginFailed(Result result) {

            }

        });
    }

    private void showProgressView() {
        BizApplication.getInstance().getProgressDialog(ResetLoginPwdActivity.this).show();
    }

    private void dismissProgressView() {
        BizApplication.getInstance().dismissProgressDialog();
    }

    private OnTitleBarClickListener titleBarClickListener = new OnTitleBarClickListener() {

        @Override
        public void onBackClicked(View view) {
            finish();
        }

        @Override
        public void onMenuClicked(View view) {

        }
    };
    private OnNoticeDialogClickListener commoNoticeDialogClickListener = new OnNoticeDialogClickListener() {

        @Override
        public void onRightButtonClicked(View view) {

        }

        @Override
        public void onLeftButtonClicked(View view) {
            finish();
        }
    };

    private boolean isMobileNO(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        } else if (number.length() == 11) {
            return true;
        } else {
            return false;
        }
    }
}
