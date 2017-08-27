//package com.cssweb.shankephone.settings;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.MotionEvent;
//import android.view.OrderView;
//import android.view.OrderView.OnTouchListener;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.cssweb.framework.http.model.Result;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.framework.utils.Utils;
//import com.cssweb.shankephone.R;
//import com.cssweb.shankephone.app.BaseActivity;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.dialog.NoticeDialog;
//import com.cssweb.shankephone.dialog.NoticeDialog.OnNoticeDialogClickListener;
//import com.cssweb.shankephone.gateway.MobileGateway.MobileGatewayListener;
//import com.cssweb.shankephone.gateway.WalletGateway;
//import com.cssweb.shankephone.gateway.model.wallet.RequestWalletLoginRs;
//import com.cssweb.shankephone.login.LoginManager;
//import com.cssweb.shankephone.preference.MPreference;
//import com.cssweb.shankephone.utils.KeyboardUtil;
//import com.cssweb.shankephone.view.ClearEditText;
//
//import org.apache.http.Header;
//
//
//public class CheckPasswordActivity extends BaseActivity implements OrderView.OnClickListener, OnNoticeDialogClickListener {
//
//    private static final String TAG = "ChangeLoginPasswordActivity";
//    public static final String PASSWORD = "password";
//    public static final int CODE_USE_GUSTURE_PWD = 101;
//    public static final int CODE_CHANGE_GUESTURE_PWD = 102;
//    public static final int CODE_RESET_GUESTURE_PWD = 103;
//    private Button mLeftButton;
//    private Button mRightButton;
//    private ClearEditText mPwdEditText;
//    private KeyboardUtil mKeyboardUtil;
//    private OrderView mPwdView;
//    private OrderView mBackgroundView;
//    private OrderView mProgressbarView;
//    private NoticeDialog mNoticeDialog;
//    private boolean mCheckResult = false;
//    private WalletGateway mGateway;
//
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        setContentView(R.layout.pwd_confirm_dialog);
//        BizApplication.getInstance().addActivity(this);
//
//        MLog.d(TAG, "onCreate");
//        mGateway = new WalletGateway(CheckPasswordActivity.this);
//
//        mLeftButton = (Button) findViewById(R.id.left);
//        mRightButton = (Button) findViewById(R.id.right);
//        mPwdEditText = (ClearEditText) findViewById(R.id.password);
//        mPwdView = findViewById(R.id.pwd_view);
//        mBackgroundView = findViewById(R.id.background);
//        mProgressbarView = findViewById(R.id.progressbar);
//
//        mBackgroundView.getBackground().setAlpha(90);
//        mKeyboardUtil = new KeyboardUtil(CheckPasswordActivity.this, getApplicationContext(), mPwdEditText);
//        mNoticeDialog = new NoticeDialog(CheckPasswordActivity.this, NoticeDialog.ONE_BUTTON);
//        // mNoticeDialog.setTitleVisible(false);
//        mNoticeDialog.setOnNoticeDialogClickListener(this);
//
//        mLeftButton.setOnClickListener(this);
//        mRightButton.setOnClickListener(this);
//
//        mLeftButton.setText(getString(R.string.ok));
//        mRightButton.setText(getString(R.string.cancel));
//
//        mPwdEditText.setOnTouchListener(onPasswordEdtTouch);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        BizApplication.getInstance().removeActivity(this);
//    }
//
//    private OnTouchListener onPasswordEdtTouch = new OnTouchListener() {
//
//        @Override
//        public boolean onTouch(OrderView v, MotionEvent event) {
//            Utils.hideSoftInput(CheckPasswordActivity.this, mPwdEditText);
//            if (!mKeyboardUtil.isShowing()) {
//                Utils.showSysSoftInput(mPwdEditText, false);
//                mKeyboardUtil.showKeyboard();
//            }
//            return false;
//        }
//    };
//
//    @Override
//    public void onClick(OrderView v) {
//        switch (v.getId()) {
//            case R.id.left:
//                preCheckPwd(mPwdEditText.getText().toString().trim());
//                // mNoticeDialog.show("密码错误");
//                break;
//            case R.id.right:
//                mKeyboardUtil.hideKeyboard();
//                finishView();
//                break;
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        mKeyboardUtil.hideKeyboard();
//    }
//
//    private void preCheckPwd(String pwd) {
//        if (pwd.length() == 0) {
//            Toast.makeText(getApplicationContext(), getString(R.string.check_pwd_input_pwd), Toast.LENGTH_SHORT).show();
//            return;
//        }
//        hideInputPwdView();
//        mKeyboardUtil.hideKeyboard();
//        requsetCheckLoginPwd();
//    }
//
//    private void requsetCheckLoginPwd() {
//        showProgressbar();
//        String userId = LoginManager.getPhoneNumber(CheckPasswordActivity.this);
//        mGateway.requestLoginClient(userId, Utils.generatePassword(mKeyboardUtil.getInputedStr()), MPreference.getMobileId(getApplicationContext()), new MobileGatewayListener<RequestWalletLoginRs>() {
//
//            @Override
//            public void onSuccess(RequestWalletLoginRs response) {
//                dismissProgressbar();
//                mCheckResult = true;
//                // showInputPwdView();
//                finishView();
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressbar();
//                Toast.makeText(getApplicationContext(), getString(R.string.network_exception), Toast.LENGTH_SHORT).show();
//                showInputPwdView();
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressbar();
//                Toast.makeText(getApplicationContext(), getString(R.string.connect_server_failed), Toast.LENGTH_SHORT).show();
//                showInputPwdView();
//            }
//
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressbar();
//                Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
//                showInputPwdView();
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onAutoLoginChangeCard() {
//                // TODO Auto-generated method stub
//
//            }
//        });
//    }
//
//    private void showProgressbar() {
//        mProgressbarView.setVisibility(OrderView.VISIBLE);
//    }
//
//    private void dismissProgressbar() {
//        mProgressbarView.setVisibility(OrderView.GONE);
//    }
//
//    private void showInputPwdView() {
//        mPwdView.setVisibility(OrderView.VISIBLE);
//    }
//
//    private void hideInputPwdView() {
//        mPwdView.setVisibility(OrderView.GONE);
//    }
//
//    private void finishView() {
//        Intent intent = new Intent();
//        intent.putExtra(PASSWORD, mCheckResult);
//        setResult(RESULT_OK, intent);
//        finish();
//        overridePendingTransition(0, R.anim.anim_fade_exit);
//    }
//
//    @Override
//    public void onLeftButtonClicked(OrderView view) {
//        showInputPwdView();
//        dismissProgressbar();
//    }
//
//    @Override
//    public void onRightButtonClicked(OrderView view) {
//
//    }
//}
