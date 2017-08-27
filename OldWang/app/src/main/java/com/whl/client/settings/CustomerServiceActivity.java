package com.whl.client.settings;

import android.os.Bundle;
import android.view.View;

import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.app.BaseActivity;
import com.whl.client.view.ShankeWebView;
import com.whl.client.view.TitleBarView;

/**
 * Created by liwx on 2017/8/14.
 * 客服页面
 */

public class CustomerServiceActivity extends BaseActivity {
    private static final String TAG = "CustomerServiceActivity";
    private ShankeWebView mWebview;
    private static final String URL = "http://a1.7x24cc.com/phone_webChat.html?accountId=N000000008783&chatId=zrww-28630910-eda3-11e6-868c-eb62d7294a1a";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        MLog.d(TAG, "onCreate");
        setContentView(R.layout.activity_customer_service);
        mWebview = (ShankeWebView) findViewById(R.id.webview);
        mWebview.loadUrl(URL);
        initTitleBar();
    }

    private void initTitleBar() {
        TitleBarView titleBarView = (TitleBarView) findViewById(R.id.title_bar);
        titleBarView.setOnTitleBarClickListener(new TitleBarView.OnTitleBarClickListener() {
            @Override
            public void onBackClicked(View view) {
                finish();
            }

            @Override
            public void onMenuClicked(View view) {

            }
        });
        titleBarView.setTitle(getResources().getString(R.string.customer_service));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLog.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLog.d(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLog.d(TAG, "onDestroy");
    }
}
