package com.whl.client.settings;


import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import android.R.color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
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
import com.whl.client.gateway.model.wallet.RequestChangeLoginPwdRs;
import com.whl.client.login.LoginManager;
import com.whl.client.preference.MPreference;
import com.whl.client.umengShare.UMengShare;
import com.whl.client.utils.KeyboardUtil;
import com.whl.client.view.ClearEditText;
import com.whl.client.view.TitleBarView;
import com.whl.client.view.TitleBarView.OnTitleBarClickListener;


public class ChangeLoginPasswordActivity extends BaseActivity implements OnTouchListener {

    private static final String TAG = "ChangeLoginPasswordActivity";

    private static final int MSGCODE_CHG_BG = 0x100;
    private static final int MSGCODE_RESTORE_BG = 0x101;

    private TitleBarView mTitleBar;
    private WalletGateway mGateway;
    private NoticeDialog mNoticeDialog;
    private Timer timer;
    private int count;
    private View mProgressView;

    private ClearEditText mEdtOrgPwd;
    private ClearEditText mEdtNewPwd;
    private ClearEditText mEdtNewPwdConfirm;
    private Button mBtnChangePwd;

    private KeyboardUtil mKeyboardUtil1;
    private KeyboardUtil mKeyboardUtil2;
    private KeyboardUtil mKeyboardUtil3;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_changepwd);
        BizApplication.getInstance().addActivity(this);

        mTitleBar = (TitleBarView) findViewById(R.id.title_bar);
        mTitleBar.setTitle(getString(R.string.pwd_update));
        mTitleBar.setOnTitleBarClickListener(titleBarClickListener);
        mTitleBar.showBackButton(true);

        mProgressView = findViewById(R.id.progressView);
        mEdtOrgPwd = (ClearEditText) findViewById(R.id.edt_org_pwd);
        mEdtNewPwd = (ClearEditText) findViewById(R.id.edt_new_pwd);
        mEdtNewPwdConfirm = (ClearEditText) findViewById(R.id.edt_pwd_confirm);
        mBtnChangePwd = (Button) findViewById(R.id.btn_change_pwd);

        mGateway = new WalletGateway(ChangeLoginPasswordActivity.this);
        // 初始化1个按钮的Dialog
        mNoticeDialog = new NoticeDialog(ChangeLoginPasswordActivity.this, NoticeDialog.ONE_BUTTON);
        mNoticeDialog.setTitle(getString(R.string.dialog_head));
        mNoticeDialog.setOnNoticeDialogClickListener(onNoticeDialogClickListener);

        mBtnChangePwd.setOnClickListener(btnUpdatePwdListener);
        mKeyboardUtil1 = new KeyboardUtil(ChangeLoginPasswordActivity.this, getApplicationContext(), mEdtOrgPwd);
        mKeyboardUtil2 = new KeyboardUtil(ChangeLoginPasswordActivity.this, getApplicationContext(), mEdtNewPwd);
        mKeyboardUtil3 = new KeyboardUtil(ChangeLoginPasswordActivity.this, getApplicationContext(), mEdtNewPwdConfirm);

        mEdtOrgPwd.setOnTouchListener(this);
        mEdtNewPwd.setOnTouchListener(this);
        mEdtNewPwdConfirm.setOnTouchListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MLog.d(TAG, "onResume");
        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_ChangeLoginPasswordActivity));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLog.d(TAG, "onPause");
        UMengShare.countPageAndTimeEnd(this, getString(R.string.statistic_ChangeLoginPasswordActivity));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        BizApplication.getInstance().removeActivity(this);
    }

    @Override
    public void onBackPressed() {
        if (mKeyboardUtil1 != null && mKeyboardUtil1.isShowing()) {
            mKeyboardUtil1.hideKeyboard();
            return;
        }
        super.onBackPressed();
    }

    private void showProgressView() {
        mProgressView.setVisibility(View.VISIBLE);
    }

    private void dissmissProgressView() {
        mProgressView.setVisibility(View.GONE);
    }

    private OnClickListener btnUpdatePwdListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Utils.hideSoftInput(getApplicationContext(), mEdtOrgPwd);
            if (mKeyboardUtil1 != null && mKeyboardUtil1.isShowing()) {
                mKeyboardUtil1.hideKeyboard();
            }
            String fakeOrgPwd = mEdtOrgPwd.getText().toString().trim();
            String fakeNewPwd = mEdtNewPwd.getText().toString().trim();
            String fakeNewPwdConfirm = mEdtNewPwdConfirm.getText().toString().trim();
            // String orgPwd = mKeyboardUtil1.getInputedStr().trim();
            // String newPwd = mKeyboardUtil2.getInputedStr().trim();
            // String newPwdConfirm = mKeyboardUtil3.getInputedStr().trim();
            // Toast.makeText(getApplicationContext(),
            // "orgPwd = " + orgPwd + "\n" + "newPwd = " + newPwd + "\n" + "newPwdConfirm = " + newPwdConfirm,
            // Toast.LENGTH_SHORT).show();
            //
            // return;
            if (TextUtils.isEmpty(fakeOrgPwd) && TextUtils.isEmpty(fakeNewPwd) && TextUtils.isEmpty(fakeNewPwdConfirm)) {
                timer = new Timer();
                timer.schedule(new RemindTask(), 0 * 1000, 1 * 150);
                return;
            } else if (TextUtils.isEmpty(fakeOrgPwd)) {
                Toast.makeText(getApplicationContext(), getString(R.string.input_org_pwd), Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(fakeNewPwd)) {
                Toast.makeText(getApplicationContext(), getString(R.string.input_new_pwd), Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(fakeNewPwdConfirm)) {
                Toast.makeText(getApplicationContext(), getString(R.string.input_new_pwd_again), Toast.LENGTH_SHORT).show();
                return;
            } else if (!isPwdValid(mKeyboardUtil2.getInputedStr())) {
                Toast.makeText(getApplicationContext(), getString(R.string.pwd_rule), Toast.LENGTH_SHORT).show();
                return;
            } else if (!mKeyboardUtil2.getInputedStr().equals(mKeyboardUtil3.getInputedStr())) {
                Toast.makeText(getApplicationContext(), getString(R.string.pwd_not_equals), Toast.LENGTH_SHORT).show();
                return;

            } else {
                updatePwd();
            }

        }
    };

    private OnNoticeDialogClickListener onNoticeDialogClickListener = new OnNoticeDialogClickListener() {

        @Override
        public void onRightButtonClicked(View view) {

        }

        @Override
        public void onLeftButtonClicked(View view) {
            MLog.d(TAG, "close noticedialog");
        }
    };

    private void updatePwd() {
        // 显示ProgressDialog
        showProgressView();
        String userId = LoginManager.getPhoneNumber(getApplicationContext());
        String newPwd = Utils.generatePassword(mKeyboardUtil2.getInputedStr().trim());
        String orgPwd = Utils.generatePassword(mKeyboardUtil1.getInputedStr().trim());

        mGateway.requestChangeLoginPwd(userId, newPwd, orgPwd, new MobileGatewayListener<RequestChangeLoginPwdRs>() {

            @Override
            public void onFailed(Result result) {
                dissmissProgressView();
                Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onHttpFailed(int statusCode, Header[] headers) {
                dissmissProgressView();
                handleHttpFaild(statusCode);
            }

            @Override
            public void onSuccess(RequestChangeLoginPwdRs response) {
                dissmissProgressView();
                MPreference.saveLoginPwd(getApplicationContext(), Utils.generatePassword(mKeyboardUtil2.getInputedStr().trim()));
                Toast.makeText(getApplicationContext(), getString(R.string.change_pwd_success), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onNoNetwork() {
                dissmissProgressView();
                Toast.makeText(getApplicationContext(), getString(R.string.network_exception), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAutoLoginSuccess() {
                dissmissProgressView();
                updatePwd();
            }

            @Override
            public void onAutoLoginFailed(Result result) {
                dissmissProgressView();
                handleAutoLoginFailed(result);

            }


        });
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSGCODE_CHG_BG) {
                mBtnChangePwd.setEnabled(false);
                if (count % 2 != 0) {
                    mEdtOrgPwd.setBackgroundDrawable(ChangeLoginPasswordActivity.this.getResources().getDrawable(R.drawable.bg_changepwd_pink));
                    mEdtNewPwd.setBackgroundDrawable(ChangeLoginPasswordActivity.this.getResources().getDrawable(R.drawable.bg_changepwd_pink));
                    mEdtNewPwdConfirm.setBackgroundDrawable(ChangeLoginPasswordActivity.this.getResources().getDrawable(R.drawable.bg_changepwd_pink));
                } else {
                    mEdtOrgPwd.setBackgroundColor(color.transparent);
                    mEdtNewPwd.setBackgroundColor(color.transparent);
                    mEdtNewPwdConfirm.setBackgroundColor(color.transparent);
                }

            } else if (msg.what == MSGCODE_RESTORE_BG) {
                mEdtOrgPwd.setBackgroundColor(color.transparent);
                mEdtNewPwd.setBackgroundColor(color.transparent);
                mEdtNewPwdConfirm.setBackgroundColor(color.transparent);
                mBtnChangePwd.setEnabled(true);
            }
        }
    };

    class RemindTask extends TimerTask {

        public void run() {
            count++;
            handler.sendEmptyMessage(MSGCODE_CHG_BG);
            if (count >= 4) {
                handler.sendEmptyMessage(MSGCODE_RESTORE_BG);
                timer.cancel(); // Terminate the timer thread
                count = 0;
            }
        }
    }

    OnTitleBarClickListener titleBarClickListener = new OnTitleBarClickListener() {

        @Override
        public void onBackClicked(View view) {
            ChangeLoginPasswordActivity.this.finish();
        }

        @Override
        public void onMenuClicked(View view) {

        }
    };

    private boolean isPwdValid(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            return false;
        } else if (pwd.length() < 6) {
            return false;
        } else if (pwd.length() > 24) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Utils.hideSoftInput(ChangeLoginPasswordActivity.this, mEdtOrgPwd);

        switch (v.getId()) {
            case R.id.edt_org_pwd:
                Utils.showSysSoftInput(mEdtOrgPwd, false);
                mKeyboardUtil1.changeInputEdt(mEdtOrgPwd);
                mKeyboardUtil1.showKeyboard();
                break;
            case R.id.edt_new_pwd:
                Utils.showSysSoftInput(mEdtNewPwd, false);
                mKeyboardUtil2.changeInputEdt(mEdtNewPwd);
                mKeyboardUtil2.showKeyboard();
                break;
            case R.id.edt_pwd_confirm:
                Utils.showSysSoftInput(mEdtNewPwdConfirm, false);
                mKeyboardUtil3.changeInputEdt(mEdtNewPwdConfirm);
                mKeyboardUtil3.showKeyboard();
                break;

            default:
                break;
        }

        return false;
    }

}
