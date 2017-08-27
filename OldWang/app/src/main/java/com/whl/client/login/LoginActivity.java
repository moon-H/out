package com.whl.client.login;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whl.framework.http.model.Result;
import com.whl.framework.utils.MLog;
import com.whl.framework.utils.Utils;
import com.whl.client.R;
import com.whl.client.app.BaseActivity;
import com.whl.client.app.BizApplication;
import com.whl.client.app.CommonToast;
import com.whl.client.app.CssConstant;
import com.whl.client.dialog.NoticeDialog;
import com.whl.client.gateway.model.wallet.UserDeviceChangeInfo;
import com.whl.client.login.register.ClauseActivity;
import com.whl.client.preference.MPreference;
import com.whl.client.umengShare.UMengShare;
import com.whl.client.view.ClearEditText;
import com.umeng.socialize.bean.SHARE_MEDIA;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * 登录、注册界面
 */
public class LoginActivity extends BaseActivity implements OnClickListener, LoginContract.LoginView {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_READ_CLAUSE = 101;
    private ClearEditText mEdtPhoneNumber;
    private ClearEditText mEdtAuthCode;
    private LoginContract.Presenter mPresenter;

    private CheckBox mChkAgree;
    private CountDownTimer mCountDownTimer;
    private TextView mTvGetVerifyCode;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_login);
        MLog.d(TAG, "onCreate");
        BizApplication.getInstance().addActivity(this);
        new LoginPresenter(this, this);

        mEdtPhoneNumber = (ClearEditText) findViewById(R.id.edt_phone_number);
        mEdtAuthCode = (ClearEditText) findViewById(R.id.edt_verify_code);

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        ImageView iv_cancle = (ImageView) findViewById(R.id.iv_cancle);
        mTvGetVerifyCode = (TextView) findViewById(R.id.tv_get_verify_code);
        mTvGetVerifyCode.setOnClickListener(this);

        findViewById(R.id.login_qq).setOnClickListener(this);
        findViewById(R.id.login_wx).setOnClickListener(this);
        findViewById(R.id.login_sina).setOnClickListener(this);

        LinearLayout llyReadClause = (LinearLayout) findViewById(R.id.lly_clause);
        mChkAgree = (CheckBox) findViewById(R.id.chk_user_tnc);

        llyReadClause.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        iv_cancle.setOnClickListener(this);

        String phoneNumber = MPreference.getLoginId(getApplicationContext());
        mEdtPhoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (!TextUtils.isEmpty(phoneNumber)) {
            mEdtPhoneNumber.setText(phoneNumber);
            mEdtPhoneNumber.setSelection(phoneNumber.length());
        }
        mCountDownTimer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long l) {
                mTvGetVerifyCode.setText("( " + l / 1000 + "s )");
            }

            @Override
            public void onFinish() {
                setGetVerifyBtnEnable(true);
            }
        };
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        UserDeviceChangeInfo info = (UserDeviceChangeInfo) intent.getSerializableExtra(CssConstant.KEY_USER_CHANGE_DEVICE_INFO);
        if (info != null) {
            NoticeDialog dialog = new NoticeDialog(LoginActivity.this, NoticeDialog.ONE_BUTTON);

            dialog.show(getString(R.string.login_prompt1) + "\n" + getString(R.string.login_date) + ":" + info.getLoginDate() + "\n" + getString(R.string.login_device) + ":" + info.getLoginDevice() + "\n" + getString(R.string.login_prompt));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLog.d(TAG, "onResume");
        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_LoginActivity));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLog.d(TAG, "onPause");
        UMengShare.countPageAndTimeEnd(this, getString(R.string.statistic_LoginActivity));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        BizApplication.getInstance().removeActivity(this);
        dismissProgress();
    }

    @Override
    public void onClick(View v) {
        if (Utils.isFastDoubleClick())
            return;
        Utils.hideSoftInput(getApplicationContext(), mEdtPhoneNumber);
        switch (v.getId()) {
            case R.id.btn_login:
                String phoneNumber = mEdtPhoneNumber.getText().toString().trim();
                String authCode = mEdtAuthCode.getText().toString().trim();
                if (!mChkAgree.isChecked()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.agree_clause), Toast.LENGTH_SHORT).show();
                    return;
                }
                mPresenter.onRequestLogin(phoneNumber, authCode);
                break;
            case R.id.login_wx:
                mPresenter.onThirdPartyLogin(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.login_qq:
                mPresenter.onThirdPartyLogin(SHARE_MEDIA.QQ);
                break;
            case R.id.login_sina:
                mPresenter.onThirdPartyLogin(SHARE_MEDIA.SINA);
                break;
            case R.id.iv_cancle:
                finish();
                break;
            case R.id.tv_get_verify_code:
                if (getVerifyCode())
                    mPresenter.onSendAuthCodeForLogin(mEdtPhoneNumber.getText().toString().trim());
                else
                    CommonToast.toast(this, this.getResources().getString(R.string.input_valid_phone_number));
                break;
            case R.id.lly_clause:
                startActivityForResult(new Intent(LoginActivity.this, ClauseActivity.class), REQUEST_READ_CLAUSE);
                break;
            default:
                break;
        }

    }

    private boolean getVerifyCode() {
        String phoneNumber = mEdtPhoneNumber.getText().toString().trim();
        if (isMobileNO(phoneNumber)) {
            return true;
        } else
            return false;
    }

    private void setGetVerifyBtnEnable(boolean isEnable) {
        if (isEnable) {
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
            }
            mTvGetVerifyCode.setClickable(true);
            mTvGetVerifyCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_get_code));
            mTvGetVerifyCode.setText(getString(R.string.get_verify_code));
        } else {
            mTvGetVerifyCode.setClickable(false);
            mTvGetVerifyCode.setBackgroundColor(getResources().getColor(R.color.get_verify_code_gray));
        }
    }

    private boolean isMobileNO(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        } else if (number.length() == 11) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onHandleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        MLog.d(TAG, "onBackPressed");
        finish();
        dismissProgress();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onNoNetwork() {
        dismissProgress();
        CommonToast.onNoNetwork(this);
    }

    @Override
    public void onLogicFailed(Result result) {
        dismissProgress();
        CommonToast.onLogicFaild(this, result);

    }

    @Override
    public void onAutoLoginFailed(Result result) {
        dismissProgress();
    }

    @Override
    public void onShowProgress(String msg) {
        showProgress(msg);
    }

    @Override
    public void onDismissProgress() {
        dismissProgress();
    }

    @Override
    public void onLoginSuccess() {
        dismissProgress();
        CommonToast.toast(LoginActivity.this, getString(R.string.login_success));
        finish();
    }

    @Override
    public void onLoginFailed(Result result) {
        dismissProgress();
        CommonToast.toast(result.getMessage());
    }

    @Override
    public void onLocalCheckFailed(String msg) {
        dismissProgress();
        CommonToast.toast(msg);
    }

    @Override
    public void onThirdPartyAuthFailed() {
        dismissProgress();
        CommonToast.toast(getString(R.string.auth_login_fail));
    }

    @Override
    public void onRequestAuthCodeSuccess() {
        mCountDownTimer.start();
        setGetVerifyBtnEnable(false);
        dismissProgress();
        CommonToast.toast(getApplicationContext(), getString(R.string.send_success));
    }
}
