package com.whl.client.home.ticket.detail;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.app.BaseActivity;
import com.whl.client.app.BizApplication;
import com.whl.client.app.CssConstant;
import com.whl.client.dialog.ImageDialog;
import com.whl.client.home.ticket.STHomeActivity;
import com.whl.client.home.ticket.SingleTicketManager;
import com.whl.client.view.TitleBarView;

/**
 * Created by lijy on 2015/11/4.
 * 取票成功页面
 */
public class STGetTicketCompleteActivity extends BaseActivity implements TitleBarView.OnTitleBarClickListener {
    private static final String TAG = "STGetTicketCompleteActivity";
    private String mOrderNO;
    private String mCityCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_complete);
        BizApplication.getInstance().addActivity(this);

        MLog.d(TAG, "onCreate");
        TitleBarView mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
        mTitleBarView.setOnTitleBarClickListener(this);
        TextView mDescribe = (TextView) findViewById(R.id.pay_complete_describe);
        TextView mTip = (TextView) findViewById(R.id.pay_tip);
        mOrderNO = getIntent().getStringExtra(SingleTicketManager.ST_ORDER_ID);
        String mCategoryCode = getIntent().getStringExtra(CssConstant.KEY_CATEGORY_CODE);
        //        String mTicketStatus = getIntent().getStringExtra(SingleTicketManager.CLOUD_TICKET_STATUS);
        mCityCode = getIntent().getStringExtra(CssConstant.KEY_CITY_CODE);
        MLog.d(TAG, "cityCode = " + mCityCode);
        //        if (mCategoryCode.equals(OrderManager.CLOUDGATE_CATEGORYCODE)) {
        //            if (mTicketStatus.equals(SingleTicketManager.CLOUD_TICKET_ARRIVAL)) {
        //                mTitleBarView.setTitle(getString(R.string.st_cg_ticket_title));
        //                mTip.setText(getString(R.string.st_cg_ticket_title));
        //                mDescribe.setText(getString(R.string.st_cg_ticket_complete_tip));
        //            } else {
        //                mTitleBarView.setTitle(getString(R.string.st_ticket_complete_title));
        //                mTip.setText(getString(R.string.st_ticket_complete));
        //                mDescribe.setText(getString(R.string.st_cg_ticket_complete_tip));
        //            }
        //        } else {
        mTitleBarView.setTitle(getString(R.string.st_ticket_complete_title));
        mTip.setText(getString(R.string.st_ticket_complete));
        mDescribe.setText(getString(R.string.st_ticket_complete_tip));
        //        }
        MLog.d(TAG, "orderNO====" + mOrderNO);
        String snapshotUrl = getIntent().getStringExtra(CssConstant.KEY_EVENT_SNAPSHOT_URL);
        String eventUrl = getIntent().getStringExtra(CssConstant.KEY_EVENT_URL);
        MLog.d(TAG, "snapshotUrl = " + snapshotUrl + " eventUrl = " + eventUrl);
        if (!TextUtils.isEmpty(snapshotUrl) && !TextUtils.isEmpty(eventUrl)) {
            ImageDialog dialog = new ImageDialog(STGetTicketCompleteActivity.this);
            dialog.displayImage(snapshotUrl, eventUrl);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLog.d(TAG, "onDestroy");
        BizApplication.getInstance().removeActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLog.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        MLog.d(TAG, "onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLog.d(TAG, "onResume");
    }

    @Override
    public void onBackClicked(View view) {
        backHomePage();
    }

    @Override
    public void onMenuClicked(View view) {


    }

    @Override
    public void onBackPressed() {
        backHomePage();
    }

    /**
     * 返回home页面
     */
    private void backHomePage() {
        // TODO: 2016/10/12 流程需要重新整理
        Intent intent = new Intent(getApplicationContext(), STHomeActivity.class);
        intent.setAction(SingleTicketManager.ACTION_LAUNCH_BUYAGAIN_BY_GETTICKETCOMPLETE);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(SingleTicketManager.ST_ORDER_ID, mOrderNO);
        intent.putExtra(CssConstant.KEY_CITY_CODE, mCityCode);
        MLog.d(TAG, "OrderNO=" + mOrderNO + " cityCode = " + mCityCode);
        startActivity(intent);
        finish();
    }


}
