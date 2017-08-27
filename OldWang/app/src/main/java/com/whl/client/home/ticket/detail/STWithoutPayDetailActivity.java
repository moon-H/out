package com.whl.client.home.ticket.detail;

import android.os.Bundle;

import com.whl.client.R;
import com.whl.client.app.BaseActivity;
import com.whl.client.app.BizApplication;
import com.whl.client.home.ticket.SingleTicketManager;

/**
 * Created by liwx on 2015/11/2.
 */
public class STWithoutPayDetailActivity extends BaseActivity {
    private static final String TAG = "STWithoutPayDetailActivity";
    private String mOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_st_without_pay_detail);
        BizApplication.getInstance().addActivity(this);
        mOrderId = getIntent().getStringExtra(SingleTicketManager.ST_ORDER_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BizApplication.getInstance().removeActivity(this);
    }

    private void showProgressView() {
        BizApplication.getInstance().getProgressDialog(this, true).show();
    }

    private void dismissProgressView() {
        BizApplication.getInstance().dismissProgressDialog();
    }

}
