package com.whl.client.view;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whl.client.R;
import com.whl.client.view.ScrollOverListView.OnScrollOverListener;


public class PullDownView extends LinearLayout implements OnScrollOverListener {

    private static final int START_PULL_DEVIATION = 50;
    private static final int WHAT_DID_MORE = 5;
    private static final int WHAT_DID_REFRESH = 3;
    private RelativeLayout mFooterView;
    private TextView mFooterTextView;
    private ProgressBar mFooterLoadingView;
    private View line;
    private ScrollOverListView mListView;
    private OnPullDownListener mOnPullDownListener;
    private float mMotionDownLastY;
    private boolean mIsFetchMoreing;
    private boolean mIsPullUpDone;
    private boolean mEnableAutoFetchMore;

    public View getLine() {
        return line;
    }

    public void setLine(View line) {
        this.line = line;
    }

    public PullDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderViewAndFooterViewAndListView(context);
    }

    public PullDownView(Context context) {
        super(context);
        initHeaderViewAndFooterViewAndListView(context);
    }


    public interface OnPullDownListener {

        void onRefresh();

        void onMore();
    }

    public void notifyDidMore() {
        mUIHandler.sendEmptyMessage(WHAT_DID_MORE);
    }

    public void RefreshComplete() {
        mUIHandler.sendEmptyMessage(WHAT_DID_REFRESH);
    }

    public void setOnPullDownListener(OnPullDownListener listener) {
        mOnPullDownListener = listener;
    }

    public ListView getListView() {
        return mListView;
    }

    public void enableAutoFetchMore(boolean enable, int index) {
        if (enable) {
            mListView.setBottomPosition(index);
            mFooterLoadingView.setVisibility(View.GONE);
            mFooterTextView.setText(R.string.pulldown_footer_text);
        } else {
            //mFooterTextView.setVisibility(OrderView.GONE);
            mFooterLoadingView.setVisibility(View.GONE);
            mListView.setFooterDividersEnabled(false);
            mFooterTextView.setText(R.string.no_more);
        }
        mEnableAutoFetchMore = enable;
    }

    private void initHeaderViewAndFooterViewAndListView(Context context) {

        setOrientation(LinearLayout.VERTICAL);


        mListView = new ScrollOverListView(context);


        mFooterView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.pulldown_footer, null);
        mFooterTextView = (TextView) mFooterView.findViewById(R.id.pulldown_footer_text);
        mFooterLoadingView = (ProgressBar) mFooterView.findViewById(R.id.pulldown_footer_loading);
        line = mFooterView.findViewById(R.id.line);
        mFooterView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!mIsFetchMoreing && mEnableAutoFetchMore) {
                    mIsFetchMoreing = true;
                    mFooterTextView.setText(R.string.pulldown_loading);
                    mFooterLoadingView.setVisibility(View.VISIBLE);
                    mOnPullDownListener.onMore();
                }
            }
        });

        mListView.setOnScrollOverListener(this);
        mListView.setCacheColorHint(0);
        mListView.setDividerHeight(0);
        addView(mListView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

        // 空的listener
        mOnPullDownListener = new OnPullDownListener() {

            @Override
            public void onRefresh() {
            }

            @Override
            public void onMore() {
            }
        };

        mListView.addFooterView(mFooterView, null, true);

        // mListView.setAdapter(mListView.getAdapter());

    }

    private Handler mUIHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_DID_REFRESH: {
                    mListView.onRefreshComplete();
                    return;
                }

                case WHAT_DID_MORE: {
                    mIsFetchMoreing = false;
                    if (mEnableAutoFetchMore) {
                        mFooterTextView.setText(R.string.pulldown_footer_text);
                    } else {
                        mFooterView.setVisibility(View.VISIBLE);
                        mFooterTextView.setVisibility(View.VISIBLE);
                        mFooterTextView.setText(R.string.no_more);
                    }
                    mFooterLoadingView.setVisibility(View.GONE);
                }
            }
        }

    };

    public boolean isFillScreenItem() {
        final int firstVisiblePosition = mListView.getFirstVisiblePosition();
        final int lastVisiblePostion = mListView.getLastVisiblePosition() - mListView.getFooterViewsCount();
        final int visibleItemCount = lastVisiblePostion - firstVisiblePosition + 1;
        final int totalItemCount = mListView.getCount() - mListView.getFooterViewsCount();

        if (visibleItemCount < totalItemCount)
            return true;
        return false;
    }

	/*
     * 实现 OnScrollOverListener接口
	 */

    @Override
    public boolean onListViewTopAndPullDown(int delta) {

        return true;
    }

    @Override
    public boolean onListViewBottomAndPullUp(int delta) {
        if (!mEnableAutoFetchMore || mIsFetchMoreing) {
            return false;
        }
        if (isFillScreenItem()) {
            mIsFetchMoreing = true;
            mFooterTextView.setText(R.string.pulldown_loading);
            mFooterLoadingView.setVisibility(View.VISIBLE);
            mOnPullDownListener.onMore();
            return true;
        }
        return false;
    }


    @Override
    public boolean onMotionDown(MotionEvent ev) {
        mIsPullUpDone = false;
        mMotionDownLastY = ev.getRawY();

        return false;
    }

    @Override
    public boolean onMotionMove(MotionEvent ev, int delta) {
        if (mIsPullUpDone)
            return true;

        final int absMotionY = (int) Math.abs(ev.getRawY() - mMotionDownLastY);
        if (absMotionY < START_PULL_DEVIATION)
            return true;

        return false;
    }

    @Override
    public boolean onMotionUp(MotionEvent ev) {
        if (ScrollOverListView.canRefleash) {
            ScrollOverListView.canRefleash = false;
            mOnPullDownListener.onRefresh();
        }
        return false;
    }

    public void setHideHeader() {
        mListView.showRefresh = false;
    }

    public void setShowHeader() {
        mListView.showRefresh = true;
    }

    public void setHideFooter() {
        //mFooterView.setVisibility(OrderView.GONE);
        mFooterTextView.setVisibility(View.GONE);

        mFooterLoadingView.setVisibility(View.GONE);
        enableAutoFetchMore(false, 1);
    }

    public void setFooterTextView(int visibility) {
        mFooterTextView.setVisibility(visibility);
    }

    public void setShowFooter() {
        mFooterView.setVisibility(View.VISIBLE);
        mFooterTextView.setVisibility(View.VISIBLE);
        mFooterTextView.setText(R.string.pulldown_loading);
        mFooterLoadingView.setVisibility(View.GONE);
        enableAutoFetchMore(true, 1);
    }

}
