package com.whl.client.home;

import android.os.Bundle;

import com.whl.framework.utils.MLog;
import com.whl.client.app.BaseActivity;

/**
 * Created by lenovo on 2016/10/18.
 */
public class BlankActivity extends BaseActivity {
    private static final String TAG = "BlankActivity";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        MLog.d(TAG, "onCreate");
    }
}
