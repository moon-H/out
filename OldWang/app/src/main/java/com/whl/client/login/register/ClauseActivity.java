package com.whl.client.login.register;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.app.BaseActivity;
import com.whl.client.app.BizApplication;
import com.whl.client.umengShare.UMengShare;
import com.whl.client.view.ShankeWebView;
import com.whl.client.view.TitleBarView;
import com.whl.client.view.TitleBarView.OnTitleBarClickListener;


public class ClauseActivity extends BaseActivity implements OnTitleBarClickListener {
    private static final String TAG = "ClauseActivity";
    private TitleBarView mTitleBarView;

    private Button mBtnAgree;
    private static final String USER_CLAUSE_URL = "http://120.52.9.5/agreement/user_clause.html";
    private ShankeWebView mShankeWebView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_clause);
        BizApplication.getInstance().addActivity(this);

        mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
        mTitleBarView.setOnTitleBarClickListener(this);
        mTitleBarView.setTitle(getString(R.string.clause));
        mShankeWebView = (ShankeWebView) findViewById(R.id.sc_agreement);

        mBtnAgree = (Button) findViewById(R.id.btn_agree);
        mBtnAgree.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, new Intent());
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLog.d(TAG, "onResume");
        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_ClauseActivity));
        mShankeWebView.loadUrl(USER_CLAUSE_URL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLog.d(TAG, "onPause");
        UMengShare.countPageAndTimeEnd(this, getString(R.string.statistic_ClauseActivity));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BizApplication.getInstance().removeActivity(this);
    }

    @Override
    public void onBackClicked(View view) {
        finish();
    }

    @Override
    public void onMenuClicked(View view) {

    }
}
