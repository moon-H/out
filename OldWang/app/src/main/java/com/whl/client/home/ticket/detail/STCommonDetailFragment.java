package com.whl.client.home.ticket.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whl.framework.utils.MLog;
import com.whl.framework.utils.Utils;
import com.whl.client.R;
import com.whl.client.app.BaseFragment;
import com.whl.client.gateway.model.singleticket.PurchaseOrder;
import com.whl.client.home.ticket.SingleTicketManager;
import com.whl.client.view.RoundTextView;

/**
 * Created by liwx on 2015/10/27.
 */
public class STCommonDetailFragment extends BaseFragment {
    private static final String TAG = "STCommonDetailFragment";
    private static STCommonDetailFragment mFragment;

    private View mRootView;
    private View mPaySuccessView;
    private TextView mTvOrderNumber;
    private TextView mTvTicketCount;
    private TextView mTvTicketPrice;
    private TextView mTvStartStation;
    private TextView mTvEndStation;
    private RoundTextView mTvStartLine;
    private RoundTextView mTvEndLine;

    private SingleTicketManager mSingleTicketManager;

    public static STCommonDetailFragment getInstance() {
        mFragment = new STCommonDetailFragment();
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSingleTicketManager = new SingleTicketManager(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MLog.d(TAG, "onCreateView");
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_st_common_detail, container, false);
            mTvOrderNumber = (TextView) mRootView.findViewById(R.id.tv_order_num);
            mTvTicketCount = (TextView) mRootView.findViewById(R.id.tv_ticket_count);
            mTvStartLine = (RoundTextView) mRootView.findViewById(R.id.tv_start_line_name);
            mTvStartStation = (TextView) mRootView.findViewById(R.id.tv_start_station_name);
            mTvEndLine = (RoundTextView) mRootView.findViewById(R.id.tv_end_line_name);
            mTvEndStation = (TextView) mRootView.findViewById(R.id.tv_end_station_name);
            mTvTicketPrice = (TextView) mRootView.findViewById(R.id.tv_one_ticket_price);
            mPaySuccessView = mRootView.findViewById(R.id.lly_pay_success);
        }
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        super.onDestroyView();
    }

    /***
     * 1.订单号
     * 2.张数
     * 3.起始线路
     * 4.起始站
     * 5.终点线路
     * 6.终点站
     * 7.单价
     */
    public void updateOrderInfo(PurchaseOrder order) {
        MLog.d(TAG, "updateOrderInfo");
        mTvOrderNumber.setText(order.getOrderNo());
        mTvTicketCount.setText(new StringBuffer(10).append(order.getSingleTicketNum()).append(getString(R.string.st_order_detail_zhang)));
        mTvTicketPrice.setText(new StringBuffer(10).append(getString(R.string.st_renminbi)).append(Utils.parseMoney(order.getTicketPrice())).append("/").append(getString(R.string.st_order_detail_zhang)));
        mTvStartStation.setText(order.getPickupStationNameZH());
        String endStationName = order.getGetoffStationNameZH();
        if (!TextUtils.isEmpty(endStationName)) {
            mTvEndStation.setText(endStationName);
        }else{
            mTvEndStation.setText(getString(R.string.st_station_no_end));
        }
        if (!TextUtils.isEmpty(order.getPickupLineCode()) && !TextUtils.isEmpty(order.getSlineBgColor())) {
            mTvStartLine.setText(order.getSlineShortName());
//            mTvStartLine.setBackgroundColor(Utils.getColorFromString(order.getSlineBgColor()));
            mTvStartLine.setmBgColor(Utils.getColorFromString(order.getSlineBgColor()));
        }

        if (!TextUtils.isEmpty(order.getGetoffLineCode())&& !TextUtils.isEmpty(order.getElineBgColor())) {
            mTvEndLine.setText(order.getElineShortName());
//            mTvEndLine.setBackgroundColor(Utils.getColorFromString(order.getElineBgColor()));
            mTvEndLine.setmBgColor(Utils.getColorFromString(order.getElineBgColor()));
        }
//        mSingleTicketManager.displayLineName(mTvStartLine, mTvEndLine, order);
    }

    public void showPaySuccessView(boolean flag) {
        if (flag)
            mPaySuccessView.setVisibility(View.VISIBLE);
        else
            mPaySuccessView.setVisibility(View.GONE);
    }

}
