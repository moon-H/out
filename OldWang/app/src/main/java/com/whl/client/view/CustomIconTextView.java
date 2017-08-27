package com.whl.client.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whl.framework.utils.MLog;
import com.whl.client.R;

/**
 * Created by liwx on 2016/5/24.
 */
public class CustomIconTextView extends LinearLayout {
    private static final String TAG = "CustomIconTextView";

    private View mParentView;
    private int mClickTimeout;

    private int mTouchSlop;
    private TextView mTvTransit;
    //    private TextView mTvOder;
    //    private ImageView mImgTransit;
    private ImageView mImgOrder;
    private int type;
    private int selectedColor;
    private int norColor;
    private float mFirstDownY; // 首次按下的Y

    private float mFirstDownX; // 首次按下的X
    private PerformClick mPerformClick;

    public CustomIconTextView(Context context) {
        this(context, null);
    }

    public CustomIconTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomIconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomIconTextView, defStyleAttr, 0);
        type = attributes.getInt(R.styleable.CustomIconTextView_type, 0);
        attributes.recycle();
        init(context);
    }

    private void init(Context context) {
        mParentView = inflate(context, R.layout.layout_custom_transaction, this);
        mTvTransit = (TextView) mParentView.findViewById(R.id.tv_tansact_hist);
        //        mTvOder = (TextView) mParentView.findViewById(R.id.tv_order_list);
        //        mImgTransit = (ImageView) mParentView.findViewById(R.id.img_transaction);
        mImgOrder = (ImageView) mParentView.findViewById(R.id.img_transaction);

        selectedColor = context.getResources().getColor(R.color.color_title_bg);
        norColor = context.getResources().getColor(R.color._797979);
        mImgOrder.setBackgroundResource(R.drawable.icon_transaction_nor);
        mTvTransit.setTextColor(norColor);
        MLog.d(TAG, "type =" + type);
        if (type == 0) {
            mTvTransit.setTextColor(norColor);
            mTvTransit.setText(context.getString(R.string.transaction_record));
            mImgOrder.setBackgroundResource(R.drawable.icon_transaction_nor);
        } else if (type == 1) {
            mTvTransit.setTextColor(norColor);
            mTvTransit.setText(context.getString(R.string.order_list));
            mImgOrder.setBackgroundResource(R.drawable.icon_detail_order_nor);
        }
        mClickTimeout = ViewConfiguration.getPressedStateDuration() + ViewConfiguration.getTapTimeout();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        MLog.d(TAG, "### action =" + action);
        float x = event.getX();
        float y = event.getY();
        float deltaX = Math.abs(x - mFirstDownX);
        float deltaY = Math.abs(y - mFirstDownY);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mFirstDownX = x;
                mFirstDownY = y;
                mTvTransit.setTextColor(selectedColor);
                switch (type) {
                    case 0:
                        mImgOrder.setBackgroundResource(R.drawable.icon_transaction_pre);
                        break;
                    case 1:
                        mImgOrder.setBackgroundResource(R.drawable.icon_detail_order_pre);
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                //                MLog.d(TAG, "### UP");
                float time = event.getEventTime() - event.getDownTime();
                if (deltaY < mTouchSlop && deltaX < mTouchSlop && time < mClickTimeout) {
                    if (mPerformClick == null) {
                        mPerformClick = new PerformClick();
                    }
                    if (!post(mPerformClick)) {
                        performClick();
                    }
                }
                mTvTransit.setTextColor(norColor);
                switch (type) {
                    case 0:
                        mImgOrder.setBackgroundResource(R.drawable.icon_transaction_nor);
                        break;
                    case 1:
                        mImgOrder.setBackgroundResource(R.drawable.icon_detail_order_nor);
                        break;
                }
                break;
        }

        return true;
    }

    private final class PerformClick implements Runnable {

        public void run() {
            MLog.d(TAG, "perform click");
            performClick();
        }
    }
}
