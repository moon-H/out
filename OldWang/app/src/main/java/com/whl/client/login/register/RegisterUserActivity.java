//package com.cssweb.shankephone.login.register;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.text.InputType;
//import android.text.TextUtils;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.view.animation.Animation;
//import android.view.animation.Animation.AnimationListener;
//import android.view.animation.AnimationUtils;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cssweb.framework.http.model.Result;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.framework.utils.Utils;
//import com.cssweb.shankephone.R;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.gateway.MobileGateway.MobileGatewayListener;
//import com.cssweb.shankephone.gateway.SpServiceGateway;
//import com.cssweb.shankephone.gateway.WalletGateway;
//import com.cssweb.shankephone.gateway.model.wallet.RegisterUserRs;
//import com.cssweb.shankephone.gateway.model.wallet.SendAuthCodeBySmsRs;
//import com.cssweb.shankephone.login.LoginActivity;
//import com.cssweb.shankephone.login.LoginManager;
//import com.cssweb.shankephone.preference.MPreference;
//import com.cssweb.shankephone.umengShare.UMengShare;
//import com.cssweb.shankephone.utils.KeyboardUtil;
//import com.cssweb.shankephone.utils.KeyboardUtil.OnKeyboradStateChangeListener;
//import com.cssweb.shankephone.view.ClearEditText;
//
//import org.apache.http.Header;
//
//
//public class RegisterUserActivity extends SyncPreSetAppletActivity implements OnClickListener, OnTouchListener, OnKeyboradStateChangeListener {
//
//    private static final String TAG = "RegisterUserActivity";
//
//    private static final int REQUEST_READ_CLAUSE = 101;
//
////    private TitleBarView mTitleBarView;
//
//    private Button mBtnNext;
//    private ClearEditText mEdtPhoneNumber;
//    private ClearEditText mEdtVerifyCode;
//    private ClearEditText mEdtLoginPwd;
//    private ClearEditText mEdtConfirmPwd;
//    private TextView mTvGetVerifyCode;
//    private TextView mTvturnlogin;
//    private View mProgressView;
//    private ImageView mIv_cancle;
//    private LinearLayout mLlyReadClause;
//    private CheckBox mChkAgree;
//
//    private CountDownTimer mCountDownTimer;
//
//    private WalletGateway mGateway;
//    private SpServiceGateway mSpServiceGateway;
//
//    private boolean isGetVerifyCode = false;
//    private boolean isDestroy = false;
//    private boolean isProceeding = false;
//
//    private KeyboardUtil mKeyboardUtil1;
//    private KeyboardUtil mKeyboardUtil2;
//
//    private LoginManager mLoginManager;
//
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        setContentView(R.layout.activity_register_user);
//        BizApplication.getInstance().addActivity(this);
////        BizApplication.getInstance().getLockPatternUtils().clearLock();
//        MPreference.removeLoginID(getApplicationContext());
//        MPreference.removeLoginPwd(getApplicationContext());
//
//        mGateway = new WalletGateway(RegisterUserActivity.this);
//        mSpServiceGateway = new SpServiceGateway(getApplicationContext());
//        mLoginManager = new LoginManager(RegisterUserActivity.this);
//
////        mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
////        mTitleBarView.setOnTitleBarClickListener(this);
////        mTitleBarView.setTitle(getString(R.string.register_user));
////        mTitleBarView.showBackButton(true);
//        //        mTitleBarView.setMenuName(getString(R.string.change_card));
//
//        mEdtPhoneNumber = (ClearEditText) findViewById(R.id.edt_phone_number);
//        mEdtVerifyCode = (ClearEditText) findViewById(R.id.edt_verify_code);
//        mEdtLoginPwd = (ClearEditText) findViewById(R.id.edt_password);
//        mEdtConfirmPwd = (ClearEditText) findViewById(R.id.edt_confirm_password);
//        mBtnNext = (Button) findViewById(R.id.btn_next);
//        mTvGetVerifyCode = (TextView) findViewById(R.id.tv_get_verify_code);
//        mProgressView = findViewById(R.id.progressView);
//        mLlyReadClause = (LinearLayout) findViewById(R.id.lly_clause);
//        mChkAgree = (CheckBox) findViewById(R.id.chk_user_tnc);
//
//        mIv_cancle = (ImageView) findViewById(R.id.iv_cancle);
//        mTvturnlogin = (TextView) findViewById(R.id.tv_turn_login);
//
//        mEdtPhoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
//        mEdtVerifyCode.setInputType(InputType.TYPE_CLASS_NUMBER);
//        mTvturnlogin.setOnClickListener(this);
//        mLlyReadClause.setOnClickListener(this);
//        mBtnNext.setOnClickListener(this);
//        mTvGetVerifyCode.setOnClickListener(this);
//        mIv_cancle.setOnClickListener(this);
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
//        mKeyboardUtil1 = new KeyboardUtil(RegisterUserActivity.this, getApplicationContext(), mEdtLoginPwd);
//        mKeyboardUtil2 = new KeyboardUtil(RegisterUserActivity.this, getApplicationContext(), mEdtConfirmPwd);
//        mKeyboardUtil1.setStateChangeListener(this);
//        mKeyboardUtil2.setStateChangeListener(this);
//
//        mEdtLoginPwd.setOnTouchListener(this);
//        mEdtConfirmPwd.setOnTouchListener(this);
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
//        if (mKeyboardUtil1 != null && mKeyboardUtil1.isShowing()) {
//            mKeyboardUtil1.forceHideKeyboard();
//            return;
//        } else if (!isProceeding) {
//            //            BizApplication.getInstance().exit();
//            finish();
//        }
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (Utils.isFastDoubleClick())
//            return;
//        Utils.hideSoftInput(getApplicationContext(), v);
//        if (mKeyboardUtil1 != null && mKeyboardUtil1.isShowing()) {
//            mKeyboardUtil1.hideKeyboard();
//        }
//        switch (v.getId()) {
//            case R.id.btn_next:
//                registerUser();
//                break;
//            case R.id.tv_get_verify_code:
//                isGetVerifyCode = true;
//                getVerifyCode();
//                break;
//            case R.id.lly_clause:
//                startActivityForResult(new Intent(RegisterUserActivity.this, ClauseActivity.class), REQUEST_READ_CLAUSE);
//                break;
//            case R.id.tv_turn_login:
//                finish();
//                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                break;
//            case R.id.iv_cancle:
//                finish();
//                break;
//            default:
//                break;
//        }
//
//    }
//
//    @Override
//    public void onChange(boolean isHiden) {
//        if (isHiden) {
//            mBtnNext.setVisibility(View.VISIBLE);
//        } else {
//            mBtnNext.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_READ_CLAUSE && resultCode == RESULT_OK) {
//            mChkAgree.setChecked(true);
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        Utils.hideSoftInput(RegisterUserActivity.this, mEdtLoginPwd);
//
//        switch (v.getId()) {
//            case R.id.edt_password:
//                Utils.showSysSoftInput(mEdtLoginPwd, false);
//                mKeyboardUtil1.changeInputEdt(mEdtLoginPwd);
//                mKeyboardUtil1.showKeyboard();
//                break;
//            case R.id.edt_confirm_password:
//                Utils.showSysSoftInput(mEdtConfirmPwd, false);
//                mKeyboardUtil2.changeInputEdt(mEdtConfirmPwd);
//                mKeyboardUtil2.showKeyboard();
//                break;
//
//            default:
//                if (mKeyboardUtil1 != null && mKeyboardUtil1.isShowing()) {
//                    mKeyboardUtil1.hideKeyboard();
//                }
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
//        final String fakePwd = mEdtLoginPwd.getText().toString().trim();
//        // final String pwd = mKeyboardUtil1.getInputedStr().trim();
//        String fakePwdConfirm = mEdtConfirmPwd.getText().toString().trim();
//        // String pwdConfirm = mKeyboardUtil2.getInputedStr().trim();
//        if (!isMobileNO(phoneNumber)) {
//            Toast.makeText(getApplicationContext(), getString(R.string.input_valid_phone_number), Toast.LENGTH_SHORT).show();
//            return;
//        } else if (!isGetVerifyCode) {
//            Toast.makeText(getApplicationContext(), getString(R.string.must_get_verify_code), Toast.LENGTH_SHORT).show();
//            return;
//        } else if (!isValidCode(authCode)) {
//            Toast.makeText(getApplicationContext(), getString(R.string.input_valid_code), Toast.LENGTH_SHORT).show();
//            return;
//        } else if (TextUtils.isEmpty(fakePwd)) {
//            Toast.makeText(getApplicationContext(), getString(R.string.input_login_pwd), Toast.LENGTH_SHORT).show();
//            return;
//        } else if (!isPwdValid(fakePwd)) {
//            Toast.makeText(getApplicationContext(), getString(R.string.pwd_rule), Toast.LENGTH_SHORT).show();
//            return;
//        } else if (TextUtils.isEmpty(fakePwdConfirm)) {
//            Toast.makeText(getApplicationContext(), getString(R.string.input_login_pwd_confirm), Toast.LENGTH_SHORT).show();
//            return;
//        } else if (!mKeyboardUtil1.getInputedStr().equals(mKeyboardUtil2.getInputedStr())) {
//            Toast.makeText(getApplicationContext(), getString(R.string.confirm_pwd_invalid), Toast.LENGTH_SHORT).show();
//            return;
//        } else if (!mChkAgree.isChecked()) {
//            Toast.makeText(getApplicationContext(), getString(R.string.agree_clause), Toast.LENGTH_SHORT).show();
//            return;
//        }
//        showProgressView();
//        mGateway.registerUser(phoneNumber, Utils.generatePassword(mKeyboardUtil1.getInputedStr()), authCode, new MobileGatewayListener<RegisterUserRs>() {
//
//            @Override
//            public void onSuccess(RegisterUserRs response) {
//                //                dissmissProgressView();
//                //                MPreference.saveMobileId(getApplicationContext(), response.getMobileId());
//                requestLogin(phoneNumber, Utils.generatePassword(mKeyboardUtil1.getInputedStr()));
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dissmissProgressView();
//                Toast.makeText(getApplicationContext(), getString(R.string.network_exception), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dissmissProgressView();
//                Toast.makeText(getApplicationContext(), getString(R.string.connect_server_failed), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailed(Result result) {
//                dissmissProgressView();
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
//        });
//
//    }
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
//    /**
//     * 登录
//     */
//    private void requestLogin(final String userId, final String pwd) {
//
////        mLoginManager.setHandleLoginListener(new HandleLoginListener() {
////            @Override
////            public void onLoginLogicSuccess() {
////                dissmissProgressView();
//////                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(CssConstant.Action.ACTION_LOGIN_SUCCESS));
////                Intent intent = new Intent(RegisterUserActivity.this, STHomeActivity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
////                startActivity(intent);
////                finish();
////            }
////
////            @Override
////            public void onLoginLogicFailed() {
////                dissmissProgressView();
////                moveToLoginPage();
////
////            }
////
////            @Override
////            public void onLoginStateNormal(RequestWalletLoginRs response) {
////                MPreference.saveLoginID(getApplicationContext(), userId);
////                MPreference.saveLoginPwd(getApplicationContext(), pwd);
////            }
////
////            @Override
////            public void onThirdLoginStateNormal(RequestThirdpartyLoginRs response) {
////
////            }
////
////            @Override
////            public void onLoginStateError(RequestWalletLoginRs response) {
////                dissmissProgressView();
////                moveToLoginPage();
////
////            }
////        });
////        mLoginManager.requestLogin(userId, pwd);
//    }
//
//    private void moveToLoginPage() {
//        if (!isDestroy) {
//            startActivity(new Intent(RegisterUserActivity.this, LoginActivity.class));
//            finish();
//        }
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
//        isProceeding = true;
//        //        mProgressView.setVisibility(OrderView.VISIBLE);
//        showProgress();
//    }
//
//    private void dissmissProgressView() {
//        isProceeding = false;
//        //        mProgressView.setVisibility(OrderView.GONE);
//        dismissProgress();
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
//
//    private boolean isPwdValid(String pwd) {
//        if (TextUtils.isEmpty(pwd)) {
//            return false;
//        } else if (pwd.length() < 6) {
//            return false;
//        } else if (pwd.length() > 24) {
//            return false;
//        }
//        return true;
//    }
//
//}
