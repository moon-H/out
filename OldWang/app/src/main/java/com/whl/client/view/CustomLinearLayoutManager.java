package com.whl.client.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.whl.framework.utils.DeviceInfo;
import com.whl.client.R;

/**
 * Created by liwx on 2017/8/4.
 */

public class CustomLinearLayoutManager extends LinearLayoutManager {
    private Context mContext;
    private int mViewHeight;
    private int mViewWidth;
    private int mItemHeight;

    public CustomLinearLayoutManager(Context context, int dataCount) {
        super(context);
        mContext = context;
        mItemHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.order_coffee_goods_height3);
        mViewHeight = mItemHeight * dataCount;
        mViewWidth = DeviceInfo.getScreenWidth(mContext) - (getDimens(R.dimen.common_margin_right) + getDimens(R.dimen.common_shadow_width)) * 2;

    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        //        View childView = recycler.getViewForPosition(0);
        //        if (childView != null)
        //            measureChild(childView, widthSpec, mItemHeight);
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    private int getDimens(int id) {
        return mContext.getResources().getDimensionPixelOffset(id);
    }
}
