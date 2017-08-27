package com.whl.client.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.app.BaseActivity;
import com.whl.client.app.CssConstant;
import com.whl.client.view.ShankeWebView;
import com.whl.client.view.TitleBarView;

/**
 * Created by lenovo on 2017/5/3.
 * 浏览器页面,用于打开各种网页
 */

public class BrowserActivity extends BaseActivity implements TitleBarView.OnTitleBarClickListener {
    private static final String TAG = "BrowserActivity";
    private ShankeWebView mWebview;
    private TitleBarView mTitleBarView;
    private String mTitleName;
    private String mUrl;

    //通过Intent 接收两个值，1.页面的title，2.url
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        Intent intent = getIntent();
        if (intent != null) {
            MLog.d(TAG, "mTitleName:" + mTitleName);
            mTitleName = intent.getStringExtra(CssConstant.KEY_QR_HELP_TITLE);
            mUrl = intent.getStringExtra(CssConstant.KEY_QR_HELP_URL_NAME);
        }

        initView();
    }

    private void initView() {
        mWebview = (ShankeWebView) findViewById(R.id.webview);
        mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
        mTitleBarView.setTitle(mTitleName);
        mTitleBarView.setOnTitleBarClickListener(this);
        mWebview.loadUrl(mUrl);
    }

    @Override
    public void onBackClicked(View view) {
        finish();
    }

    @Override
    public void onMenuClicked(View view) {

    }
}
