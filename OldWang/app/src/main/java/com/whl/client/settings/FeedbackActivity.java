package com.whl.client.settings;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.whl.framework.http.model.Result;
import com.whl.framework.utils.MLog;
import com.whl.framework.utils.Utils;
import com.whl.client.R;
import com.whl.client.app.BaseActivity;
import com.whl.client.app.BizApplication;
import com.whl.client.gateway.MobileGateway;
import com.whl.client.gateway.MobileGateway.MobileGatewayListener;
import com.whl.client.gateway.model.wallet.RequestCommitFeedbackRs;
import com.whl.client.login.LoginManager;
import com.whl.client.umengShare.UMengShare;
import com.whl.client.view.TitleBarView;
import com.whl.client.view.TitleBarView.OnTitleBarClickListener;

import org.apache.http.Header;


public class FeedbackActivity extends BaseActivity {
    private static final String TAG = "FeedbackActivity";

    private TitleBarView mTitleBar;
    private EditText mEdtContent;
    //    private OrderView mProgressView;
    private Button mBtnSend;
    private MobileGateway mGateway;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_feedback);
        BizApplication.getInstance().addActivity(this);

        mTitleBar = (TitleBarView) findViewById(R.id.title_bar);
        mTitleBar.setTitle(getString(R.string.feedback));
        mTitleBar.setOnTitleBarClickListener(titleBarClickListener);
        mTitleBar.showBackButton(true);

        mEdtContent = (EditText) findViewById(R.id.edt_content);
        //        mProgressView = findViewById(R.id.progressView);
        mGateway = new MobileGateway(FeedbackActivity.this);

        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(btnSendFeedbackListener);
        mEdtContent.addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLog.d(TAG, "onResume");
        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_FeedbackActivity));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLog.d(TAG, "onPause");
        UMengShare.countPageAndTimeEnd(this, getString(R.string.statistic_FeedbackActivity));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BizApplication.getInstance().removeActivity(this);
    }

    private OnClickListener btnSendFeedbackListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Utils.hideSoftInput(getApplicationContext(), mEdtContent);
            if (mEdtContent.getText().toString().length() > 0) {
                sendFeedback();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.feedback_empty_msg), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void showProgressView() {
        //        mProgressView.setVisibility(OrderView.VISIBLE);
        showProgress();
    }

    private void dissmissProgressView() {
        //        mProgressView.setVisibility(OrderView.GONE);
        dismissProgress();
    }

    private void sendFeedback() {
        String userId = LoginManager.getPhoneNumber(getApplicationContext());
        showProgressView();
        mGateway.requestCommitFeedback(userId, mEdtContent.getText().toString().trim(), new MobileGatewayListener<RequestCommitFeedbackRs>() {

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
            public void onSuccess(RequestCommitFeedbackRs response) {
                dissmissProgressView();
                Toast.makeText(getApplicationContext(), getString(R.string.feedback_success), Toast.LENGTH_SHORT).show();
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
                sendFeedback();

            }

            @Override
            public void onAutoLoginFailed(Result result) {
                dissmissProgressView();
                handleAutoLoginFailed(result);
            }


        });

    }

    OnTitleBarClickListener titleBarClickListener = new OnTitleBarClickListener() {

        @Override
        public void onBackClicked(View view) {
            Utils.hideSoftInput(getApplicationContext(), mEdtContent);
            FeedbackActivity.this.finish();
        }

        @Override
        public void onMenuClicked(View view) {

        }
    };

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mEdtContent.getText().toString().trim().length() > 0) {
                setEdtEnable(true);
            } else {
                setEdtEnable(false);
            }
        }
    };

    private void setEdtEnable(boolean flag) {
        if (flag) {
            mBtnSend.setBackgroundResource(R.drawable.selector_login_btn);
            mBtnSend.setEnabled(true);
        } else {
            mBtnSend.setBackgroundColor(getResources().getColor(R.color.AAAAAA));
            mBtnSend.setEnabled(false);

        }
    }

}
