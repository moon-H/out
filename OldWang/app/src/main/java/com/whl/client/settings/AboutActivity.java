package com.whl.client.settings;


import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.whl.framework.utils.DeviceInfo;
import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.app.BaseActivity;
import com.whl.client.app.BizApplication;
import com.whl.client.umengShare.UMengShare;
import com.whl.client.view.TitleBarView;
import com.whl.client.view.TitleBarView.OnTitleBarClickListener;


public class AboutActivity extends BaseActivity implements OnTitleBarClickListener {
    private static final String TAG = "AboutActivity";

    private TitleBarView mTitleBarView;
    private TextView mVersionView;
    private TextView mTvContactTel;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_about);
        BizApplication.getInstance().addActivity(this);

        mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
        mTitleBarView.setTitle(getString(R.string.about));
        mTitleBarView.setMenuName(getString(R.string.more));
        mTitleBarView.setOnTitleBarClickListener(this);

        mVersionView = (TextView) findViewById(R.id.version);
        mVersionView.setText(getString(R.string.about_current_version) + " " + DeviceInfo.getAppVersionName(AboutActivity.this));

        mTvContactTel = (TextView) findViewById(R.id.tv_contact);
        mTvContactTel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mTvContactTel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mTvContactTel.getText().toString().trim()));
                startActivity(intent);
            }
        });
        //        findViewById(R.id.test).setOnClickListener(new OnClickListener() {
        //            @Override
        //            public void onClick(OrderView v) {
        //                Intent intent = new Intent();
        //                ComponentName componentName;
        //                componentName = new ComponentName("com.cssweb.shankephone", "com.cssweb.shankephone.home.HomeActivity");
        //                intent.setComponent(componentName);
        //                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //                intent.setAction(SpServiceManager.ACTION_LAUNCH_HOME_BY_PANCHAN);
        //                startActivity(intent);
        //                finish();
        //            }
        //        });
    }

    @Override
    public void onBackClicked(View view) {
        finish();
    }

    @Override
    public void onMenuClicked(View view) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        MLog.d(TAG, "onResume");
        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_AboutActivity));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLog.d(TAG, "onPause");
        UMengShare.countPageAndTimeEnd(this, getString(R.string.statistic_AboutActivity));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        BizApplication.getInstance().removeActivity(this);
    }
}
