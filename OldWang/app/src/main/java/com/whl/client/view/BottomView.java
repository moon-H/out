package com.whl.client.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whl.framework.utils.Utils;
import com.whl.client.R;


public class BottomView extends LinearLayout implements OnClickListener {

    private OnBottomBarClickListener listener;

    private ImageView mImgLeft;
    private ImageView mImgCenterLeft;
    private ImageView mImgCenterRight;
    private ImageView mImgRight;

    private TextView mTvLeft;
    private TextView mTvCenterLeft;
    private TextView mTvCenterRight;
    private TextView mTvRight;

    //    private BadgeView mBadgeView;

    public BottomView(Context context) {
        super(context);
        initView(context);
    }

    public BottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public BottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View parentView = inflate(context, R.layout.bottom_bar, this);
        View leftView = parentView.findViewById(R.id.lly_left);
        View centerLeftView = parentView.findViewById(R.id.lly_center_left);
        View centerRightView = parentView.findViewById(R.id.lly_center_right);
        View rightView = parentView.findViewById(R.id.lly_right);

        mImgLeft = (ImageView) findViewById(R.id.img_left);
        mImgCenterLeft = (ImageView) findViewById(R.id.img_center_left);
        mImgRight = (ImageView) findViewById(R.id.img_right);
        mImgCenterRight = (ImageView) findViewById(R.id.img_center_right);

        ImageView mImgBadgeView = (ImageView) findViewById(R.id.img_badge);
        //        mBadgeView = new BadgeView(context, mImgBadgeView);
        //        mBadgeView.setBackgroundResource(R.drawable.ic_badge);
        //        mBadgeView.setLayoutParams(new LayoutParams(getResources().getDimensionPixelOffset(R.dimen.badge_width), getResources().getDimensionPixelOffset(R.dimen.badge_height)));
        //        // mBadgeView.setWidth(getResources().getDimensionPixelOffset(R.dimen.badge_width));
        //        // mBadgeView.setHeight(getResources().getDimensionPixelOffset(R.dimen.badge_height));
        //        mBadgeView.setGravity(Gravity.CENTER);

        mTvLeft = (TextView) findViewById(R.id.tv_left);
        mTvCenterLeft = (TextView) findViewById(R.id.tv_center_left);
        mTvRight = (TextView) findViewById(R.id.tv_right);
        mTvCenterRight = (TextView) findViewById(R.id.tv_center_right);

        leftView.setOnClickListener(this);
        centerLeftView.setOnClickListener(this);
        centerRightView.setOnClickListener(this);
        rightView.setOnClickListener(this);
        setCurrentTab(0);
    }

    public void setCurrentTab(int position) {
        switch (position) {
            case 0:
                mImgLeft.setSelected(true);
                mImgCenterLeft.setSelected(false);
                mImgCenterRight.setSelected(false);
                mImgRight.setSelected(false);

                mTvLeft.setTextColor(getResources().getColor(R.color.bottom_bg_selected));
                mTvCenterLeft.setTextColor(getResources().getColor(R.color.bottom_text_nor));
                mTvCenterRight.setTextColor(getResources().getColor(R.color.bottom_text_nor));
                mTvRight.setTextColor(getResources().getColor(R.color.bottom_text_nor));

                // mLeftView.setBackgroundColor(getResources().getColor(R.color.bottom_bg_selected));
                // mCenterLeftView.setBackgroundColor(getResources().getColor(R.color.bottom_bg_nor));
                // mRightView.setBackgroundColor(getResources().getColor(R.color.bottom_bg_nor));

                break;
            case 1:
                mImgCenterLeft.setSelected(true);
                mImgCenterRight.setSelected(false);
                mImgLeft.setSelected(false);
                mImgRight.setSelected(false);

                mTvCenterLeft.setTextColor(getResources().getColor(R.color.bottom_bg_selected));
                mTvCenterRight.setTextColor(getResources().getColor(R.color.bottom_text_nor));
                mTvLeft.setTextColor(getResources().getColor(R.color.bottom_text_nor));
                mTvRight.setTextColor(getResources().getColor(R.color.bottom_text_nor));

                // mLeftView.setBackgroundColor(getResources().getColor(R.color.bottom_bg_nor));
                // mCenterLeftView.setBackgroundColor(getResources().getColor(R.color.bottom_bg_selected));
                // mRightView.setBackgroundColor(getResources().getColor(R.color.bottom_bg_nor));

                break;
            case 2:
                mImgCenterRight.setSelected(true);
                mImgRight.setSelected(false);
                mImgCenterLeft.setSelected(false);
                mImgLeft.setSelected(false);

                mTvCenterRight.setTextColor(getResources().getColor(R.color.bottom_bg_selected));
                mTvRight.setTextColor(getResources().getColor(R.color.bottom_text_nor));
                mTvCenterLeft.setTextColor(getResources().getColor(R.color.bottom_text_nor));
                mTvLeft.setTextColor(getResources().getColor(R.color.bottom_text_nor));

                // mLeftView.setBackgroundColor(getResources().getColor(R.color.bottom_bg_nor));
                // mCenterLeftView.setBackgroundColor(getResources().getColor(R.color.bottom_bg_nor));
                // mRightView.setBackgroundColor(getResources().getColor(R.color.bottom_bg_selected));

                break;
            case 3:
                mImgRight.setSelected(true);
                mImgCenterLeft.setSelected(false);
                mImgCenterRight.setSelected(false);
                mImgLeft.setSelected(false);

                mTvRight.setTextColor(getResources().getColor(R.color.bottom_bg_selected));
                mTvCenterLeft.setTextColor(getResources().getColor(R.color.bottom_text_nor));
                mTvCenterRight.setTextColor(getResources().getColor(R.color.bottom_text_nor));
                mTvLeft.setTextColor(getResources().getColor(R.color.bottom_text_nor));

                // mLeftView.setBackgroundColor(getResources().getColor(R.color.bottom_bg_nor));
                // mCenterLeftView.setBackgroundColor(getResources().getColor(R.color.bottom_bg_nor));
                // mRightView.setBackgroundColor(getResources().getColor(R.color.bottom_bg_selected));

                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (Utils.isFastDoubleClick())
            return;
        if (listener == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.lly_left:
                listener.onBottomBarClicked(view, 0);
                //                setCurrentTab(0);
                break;
            case R.id.lly_center_left:
                listener.onBottomBarClicked(view, 1);
                //                setCurrentTab(1);
                break;
            case R.id.lly_center_right:
                listener.onBottomBarClicked(view, 2);
                //                setCurrentTab(2);
                break;
            case R.id.lly_right:
                listener.onBottomBarClicked(view, 3);
                //                setCurrentTab(3);
                break;
            default:
                break;
        }
    }

    //    public void setBadge(int count) {
    //        //        if (count > 99) {
    //        //            mBadgeView.setText("99+");
    //        //            int paddingLeft = getResources().getDimensionPixelSize(R.dimen.badge_99_pading_horizontal);
    //        //            int paddingTop = getResources().getDimensionPixelSize(R.dimen.badge_99_pading_vertical);
    //        //            mBadgeView.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
    //        //            mBadgeView.setTextSize(getResources().getDimensionPixelSize(R.dimen.home_menu_badge_size));
    //        //        } else if (count > 9) {
    //        //            mBadgeView.setText(String.valueOf(count));
    //        //            mBadgeView.setTextSize(getResources().getDimensionPixelSize(R.dimen.home_menu_badge_size));
    //        //            int padingLeft = getResources().getDimensionPixelSize(R.dimen.home_menu_badge_paddingleft);
    //        //            int padingTop = getResources().getDimensionPixelSize(R.dimen.home_menu_badge_paddingtop);
    //        //            mBadgeView.setPadding(padingLeft, padingTop, padingLeft, padingTop);
    //        //        } else {
    //        //            mBadgeView.setText(String.valueOf(count));
    //        //            mBadgeView.setTextSize(getResources().getDimensionPixelSize(R.dimen.home_menu_badge_size2));
    //        //        }
    //        if (count == 0) {
    //            mBadgeView.hide();
    //        } else {
    //            mBadgeView.show();
    //        }
    //    }

    public void setOnBottomBarClickListener(OnBottomBarClickListener listener) {
        this.listener = listener;
    }

    public interface OnBottomBarClickListener {

        void onBottomBarClicked(View view, int index);

        //        void onLeftClicked(OrderView view);
        //
        //        void onCenterLeftClicked(OrderView view);
        //
        //        void onCenterRightClicked(OrderView view);
        //
        //        void onRightClicked(OrderView view);
    }
}
