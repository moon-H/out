
package com.whl.client.app;


import android.os.Bundle;

import com.whl.framework.utils.MLog;


public class ExitActivity extends BaseActivity {

	private static final String TAG = "ExitActivity";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		MLog.d(TAG, "ExitActivity");
		finish();
	}
}
