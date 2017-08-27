//package com.cssweb.shankephone.login.register;
//
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.support.v4.content.LocalBroadcastManager;
//import android.text.TextUtils;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.view.animation.Animation;
//import android.view.animation.Animation.AnimationListener;
//import android.view.animation.AnimationUtils;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cssweb.framework.http.model.Result;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.framework.utils.Utils;
//import com.cssweb.shankephone.R;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.app.CssConstant;
//import com.cssweb.shankephone.gateway.MobileGateway;
//import com.cssweb.shankephone.gateway.MobileGateway.MobileGatewayListener;
//import com.cssweb.shankephone.gateway.SpServiceGateway;
//import com.cssweb.shankephone.gateway.WalletGateway;
//import com.cssweb.shankephone.gateway.model.wallet.RegistertPanchanUserRs;
//import com.cssweb.shankephone.gateway.model.wallet.SendAuthCodeBySmsRs;
//import com.cssweb.shankephone.preference.MPreference;
//import com.cssweb.shankephone.umengShare.UMengShare;
//import com.cssweb.shankephone.view.ClearEditText;
//import com.cssweb.shankephone.view.TitleBarView;
//import com.cssweb.shankephone.view.TitleBarView.OnTitleBarClickListener;
//
//import org.apache.http.Header;
//
//
//public class RegisterThirdUserActivity extends SyncPreSetAppletActivity implements OnTitleBarClickListener, OnClickListener, OnTouchListener {
//
//    private static final String TAG = "RegisterThirdUserActivity";
//
//    private static final int REQUEST_READ_CLAUSE = 101;
//
//    private TitleBarView mTitleBarView;
//
//    private Button mBtnNext;
//    private ClearEditText mEdtPhoneNumber;
//    private ClearEditText mEdtVerifyCode;
//    private TextView mTvGetVerifyCode;
//    private View mProgressView;
//    private LinearLayout mLlyReadClause;
//    private RelativeLayout mrl_all;
//
//    private CountDownTimer mCountDownTimer;
//
//    private WalletGateway mGateway;
//    private SpServiceGateway mSpServiceGateway;
//
//    private boolean isGetVerifyCode = false;
//    private boolean isDestroy = false;
//    //    private boolean isProceeding = false;
//
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        setContentView(R.layout.activity_register_third_user);
//        BizApplication.getInstance().addActivity(this);
//        //        BizApplication.getInstance().getLockPatternUtils().clearLock();
//        MPreference.removeLoginID(getApplicationContext());
//        MPreference.removeLoginPwd(getApplicationContext());
//
//        mGateway = new WalletGateway(RegisterThirdUserActivity.this);
//        mSpServiceGateway = new SpServiceGateway(getApplicationContext());
//
//        mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
//        mTitleBarView.setOnTitleBarClickListener(this);
//        mTitleBarView.setTitle(getString(R.string.register_number));
//
//        mEdtPhoneNumber = (ClearEditText) findViewById(R.id.edt_phone_number);
//        mEdtVerifyCode = (ClearEditText) findViewById(R.id.edt_verify_code);
//
//        mrl_all = (RelativeLayout) findViewById(R.id.rl_all);
//        mBtnNext = (Button) findViewById(R.id.btn_next);
//        mTvGetVerifyCode = (TextView) findViewById(R.id.tv_get_verify_code);
//        mProgressView = findViewById(R.id.progressView);
//        mLlyReadClause = (LinearLayout) findViewById(R.id.lly_clause);
//
//        mrl_all.setOnClickListener(this);
//        mLlyReadClause.setOnClickListener(this);
//        mBtnNext.setOnClickListener(this);
//        mTvGetVerifyCode.setOnClickListener(this);
//
//        mCountDownTimer = new CountDownTimer(60000, 1000) {
//
//            @Override
//            public void onTick(long l) {
//                mTvGetVerifyCode.setText("( " + l / 1000 + "s )");
//            }
//
//            @Override
//            public void onFinish() {
//                setGetVerifyBtnEnable(true);
//            }
//        };
//
//        mEdtVerifyCode.setOnTouchListener(this);
//        mEdtPhoneNumber.setOnTouchListener(this);
//    }
//
//    @Override
//    protected void onResume() {
//        isDestroy = false;
//        super.onResume();
//        MLog.d(TAG, "onResume");
//        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_RegisterUserActivity));
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MLog.d(TAG, "onPause");
//        UMengShare.countPageAndTimeEnd(this, getString(R.string.statistic_RegisterUserActivity));
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (mCountDownTimer != null) {
//            mCountDownTimer.cancel();
//        }
//        isDestroy = true;
//        super.onDestroy();
//        BizApplication.getInstance().removeActivity(this);
//        dissmissProgressView();
//    }
//
//    @Override
//    public void onBackPressed() {
//        saveThirdStatue();
//        finish();
//    }
//
//    private void saveThirdStatue() {
//        //没有用到
//        Intent intent = new Intent();
//        intent.putExtra("test", "testValue");
//        RegisterThirdUserActivity.this.setResult(RESULT_OK, intent);
//    }
//
//    @Override
//    public void onBackClicked(View view) {
//        saveThirdStatue();
//        finish();
//    }
//
//    @Override
//    public void onMenuClicked(View view) {
//        // TODO: 2016/10/12 ？？？？？？
//        //        startActivity(new Intent(RegisterThirdUserActivity.this, ChangeCardActivity.class));
//        //        finish();
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (Utils.isFastDoubleClick())
//            return;
//        Utils.hideSoftInput(getApplicationContext(), v);
//        switch (v.getId()) {
//            case R.id.btn_next:
//                registerUser();
//                break;
//            case R.id.tv_get_verify_code:
//                isGetVerifyCode = true;
//                getVerifyCode();
//                break;
//            case R.id.lly_clause:
//                startActivityForResult(new Intent(RegisterThirdUserActivity.this, ClauseActivity.class), REQUEST_READ_CLAUSE);
//                break;
//            case R.id.rl_all:
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                break;
//            default:
//                break;
//        }
//
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_READ_CLAUSE && resultCode == RESULT_OK) {
//            //            mChkAgree.setChecked(true);
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        switch (v.getId()) {
//            case R.id.edt_password:
//                break;
//            case R.id.edt_confirm_password:
//                break;
//            default:
//                break;
//        }
//        return false;
//    }
//
//    private void setGetVerifyBtnEnable(boolean isEnable) {
//        if (isEnable) {
//            if (mCountDownTimer != null) {
//                mCountDownTimer.cancel();
//            }
//            mTvGetVerifyCode.setClickable(true);
//            mTvGetVerifyCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_login_btn));
//            mTvGetVerifyCode.setText(getString(R.string.get_verify_code));
//        } else {
//            mTvGetVerifyCode.setClickable(false);
//            mTvGetVerifyCode.setBackgroundColor(getResources().getColor(R.color.get_verify_code_gray));
//        }
//    }
//
//    private void getVerifyCode() {
//        String phoneNumber = mEdtPhoneNumber.getText().toString().trim();
//        if (isMobileNO(phoneNumber)) {
//            setGetVerifyBtnEnable(false);
//            mCountDownTimer.start();
//            requestVerifyCode(phoneNumber);
//        } else {
//            Toast.makeText(getApplicationContext(), getString(R.string.input_valid_phone_number), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * 注册用户
//     */
//    private void registerUser() {
//        final String phoneNumber = mEdtPhoneNumber.getText().toString().trim();
//        String authCode = mEdtVerifyCode.getText().toString().trim();
//        if (!isMobileNO(phoneNumber)) {
//            Toast.makeText(getApplicationContext(), getString(R.string.input_valid_phone_number), Toast.LENGTH_SHORT).show();
//            return;
//        } else if (!isGetVerifyCode) {
//            Toast.makeText(getApplicationContext(), getString(R.string.must_get_verify_code), Toast.LENGTH_SHORT).show();
//            return;
//        } else if (!isValidCode(authCode)) {
//            Toast.makeText(getApplicationContext(), getString(R.string.input_valid_code), Toast.LENGTH_SHORT).show();
//            return;
//        }
//        showProgressView();
//
//        mGateway.registertPanchanUser(phoneNumber, authCode, new MobileGatewayListener<RegistertPanchanUserRs>() {
//            @Override
//            public void onSuccess(RegistertPanchanUserRs response) {
//                dissmissProgressView();
//                if (response != null && response.getResult().getCode() == Result.OK) {
//                    MLog.d(TAG, "注册成功后返回response" + "  " + response.toString());
//                    BizApplication.getInstance().setCurrentMno(response.getMno());
//                    MPreference.putString(RegisterThirdUserActivity.this, MPreference.THIRD_LOGIN_STATUE, MobileGateway.LOGIN_STATUS_ACTIVATED);
//                    MPreference.saveLoginID(RegisterThirdUserActivity.this, phoneNumber);
//                    BizApplication.getInstance().setPanchanToken(response.getToken());
//                    Toast.makeText(getApplicationContext(), R.string.third_active_success, Toast.LENGTH_LONG).show();
//                    MPreference.putString(RegisterThirdUserActivity.this, MPreference.THIRD_RIGESTER_PHONENUMBER, phoneNumber);
//                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(CssConstant.Action.ACTION_LOGIN_SUCCESS));
//                    finish();
//                }
//            }
//
//            @Override
//            public void onFailed(Result result) {
//                dissmissProgressView();
//                Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dissmissProgressView();
//                Toast.makeText(getApplicationContext(), getString(R.string.connect_server_failed), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dissmissProgressView();
//                Toast.makeText(getApplicationContext(), getString(R.string.network_exception), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                dissmissProgressView();
//                registerUser();
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                dissmissProgressView();
//            }
//
//        });
//    }
//
//
//    /**
//     * 获取验证码
//     */
//    private void requestVerifyCode(String msisdn) {
//        showProgressView();
//        mGateway.sendAuthCodeBySms(msisdn, new MobileGatewayListener<SendAuthCodeBySmsRs>() {
//
//            @Override
//            public void onSuccess(SendAuthCodeBySmsRs response) {
//                dissmissProgressView();
//                Toast.makeText(getApplicationContext(), getString(R.string.send_success), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dissmissProgressView();
//                Toast.makeText(getApplicationContext(), getString(R.string.network_exception), Toast.LENGTH_SHORT).show();
//                setGetVerifyBtnEnable(true);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dissmissProgressView();
//                setGetVerifyBtnEnable(true);
//                Toast.makeText(getApplicationContext(), getString(R.string.connect_server_failed), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailed(Result result) {
//                dissmissProgressView();
//                setGetVerifyBtnEnable(true);
//                Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//
//            }
//
//
//        });
//    }
//
//    private boolean isValidCode(String code) {
//        if (!TextUtils.isEmpty(code)) {
//            if (code.trim().length() == 4) {
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
//    }
//
//    private void showProgressView() {
//        //        isProceeding = true;
//        //        mProgressView.setVisibility(OrderView.VISIBLE);
//        BizApplication.getInstance().getProgressDialog(RegisterThirdUserActivity.this).show();
//    }
//
//    private void dissmissProgressView() {
//        //        isProceeding = false;
//        //        mProgressView.setVisibility(OrderView.GONE);
//        BizApplication.getInstance().dismissProgressDialog();
//    }
//
//    private void startAnimation(TextView view) {
//        view.setVisibility(View.VISIBLE);
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.ani_in);
//        view.startAnimation(animation);
//    }
//
//    private void stopAnimation(final TextView view) {
//        if (view.getVisibility() != View.VISIBLE) {
//            return;
//        }
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.ani_out);
//        view.startAnimation(animation);
//        animation.setAnimationListener(new AnimationListener() {
//
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                view.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }
//
//    private boolean isMobileNO(String number) {
//        if (TextUtils.isEmpty(number)) {
//            return false;
//        } else if (number.length() == 11) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//}
