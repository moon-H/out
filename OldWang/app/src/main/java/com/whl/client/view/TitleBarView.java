package com.whl.client.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whl.framework.utils.Utils;
import com.whl.client.R;


public class TitleBarView extends LinearLayout implements OnClickListener {

    public TitleBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private View mParentView;
    private View mBackView;
    private ImageView mBackImgView;
    private View mMenuView;
    private TextView mBackName;
    private TextView mMenuTextView;
    private TextView mTitleView;
    private ImageView mMenuImg;
    private LinearLayout mRlyBackground;
    private OnTitleBarClickListener mOnTitleBarClickListener;
    private TypedArray mTypedArray;
    private boolean isShowBack;
    private boolean isShowMenu;

    private ImageView mImgIcon;

    private void init(Context context, AttributeSet attrs) {
        mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        mParentView = inflate(context, R.layout.title_bar, this);
        mRlyBackground = (LinearLayout) mParentView.findViewById(R.id.rlv_parent);
        mBackView = mParentView.findViewById(R.id.back);
        mBackImgView = (ImageView) mParentView.findViewById(R.id.back_text);
        mMenuView = mParentView.findViewById(R.id.menu);
        mMenuTextView = (TextView) mParentView.findViewById(R.id.menu_text);
        mTitleView = (TextView) findViewById(R.id.title_name);
        mBackName = (TextView) findViewById(R.id.back_name);
        mImgIcon = (ImageView) findViewById(R.id.img_main_icon);
        mMenuImg = (ImageView) findViewById(R.id.image);
        isShowMenu = mTypedArray.getBoolean(R.styleable.TitleBar_showMenu, false);
        isShowBack = mTypedArray.getBoolean(R.styleable.TitleBar_showBack, false);

        mBackView.setOnClickListener(this);
        mMenuView.setOnClickListener(this);
        if (isShowBack) {
            mBackView.setVisibility(View.VISIBLE);
        } else {
            mBackView.setVisibility(View.INVISIBLE);
        }
        if (isShowMenu) {
            mMenuView.setVisibility(View.VISIBLE);
        } else {
            mMenuView.setVisibility(View.INVISIBLE);
        }

    }

    public void setMenu(boolean flag) {
        if (flag) {
            mMenuView.setVisibility(View.VISIBLE);
        } else {
            mMenuView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (Utils.isFastDoubleClick())
            return;
        switch (v.getId()) {
            case R.id.back:
                mOnTitleBarClickListener.onBackClicked(v);
                break;
            case R.id.menu:
                mOnTitleBarClickListener.onMenuClicked(v);
                break;
            default:
                break;
        }

    }

    public void setMenuTextSize(float size) {
        mMenuTextView.setTextSize(size);
    }

    public void setBackTextSize(float size) {
        mBackName.setTextSize(size);
    }

    public void setTitle(String name) {
        mTitleView.setText(name);
        mTitleView.setGravity(Gravity.CENTER);
    }

    public void setTitle(int resid) {
        mTitleView.setText(resid);
        mTitleView.setGravity(Gravity.CENTER);

    }

    public String getTitleName() {
        return mTitleView.getText().toString();
    }

    public void setMenuImg(int resid) {
        mMenuImg.setImageResource(resid);
        mMenuImg.setVisibility(View.VISIBLE);
    }

    public void setBackName(String backName) {
        mBackName.setText(backName);
        mBackName.setVisibility(View.VISIBLE);
    }

    public void setTitleIconVisible(boolean flag) {
        if (flag) {
            mImgIcon.setVisibility(View.VISIBLE);
        } else {
            mImgIcon.setVisibility(View.GONE);
        }
    }

    public void setTitleBackgroundResource(int id) {
        mTitleView.setBackgroundResource(id);
    }

    public void setBackgroundColor(int colorId) {
        mRlyBackground.setBackgroundColor(colorId);
    }

    public void setMenuName(String name) {
        if (!TextUtils.isEmpty(name)) {
            mMenuTextView.setText(name);
        }
    }

    public String getMenuName() {
        return mMenuTextView.getText().toString();
    }

    public void showBackButton(boolean flag) {
        if (flag) {
            mBackView.setVisibility(View.VISIBLE);
        } else {
            mBackView.setVisibility(View.INVISIBLE);
        }
    }

    public void setBackButtonBg(int resid) {
        mBackImgView.setImageResource(resid);
        mBackImgView.setVisibility(View.VISIBLE);
    }

    public void setMenuBackground(Drawable drawable) {
        if (drawable != null) {
            mMenuView.setBackgroundDrawable(drawable);
        }
    }

	/*
     * public void setMenuGravity(Drawable drawable) { if (drawable != null) { LinearLayout.LayoutParams params = new
	 * LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	 * mMenuView.setLayoutParams(params); } }
	 */

    public void showMenuBack(boolean flag) {
        if (flag) {
            mMenuView.setVisibility(View.VISIBLE);
        } else {
            mMenuView.setVisibility(View.INVISIBLE);
        }
    }

    public void setMenuTextViewDrawableRight(Drawable left, int resId) {
        if (left != null) {
            left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
            mMenuTextView.setCompoundDrawables(left, null, null, null);
        }
        mMenuTextView.setText(resId);
    }

    public void setMenuBackground(int resId) {
        if (resId != 0) {
            mMenuView.setBackgroundResource(resId);
        }
    }

    // public void setBackName(String name) {
    // if (!TextUtils.isEmpty(name)) {
    // mBackImgView.setText(name);
    // }
    // }

    public View getMenuView() {
        return mMenuView;
    }

    public void setOnTitleBarClickListener(OnTitleBarClickListener listener) {
        this.mOnTitleBarClickListener = listener;
    }

    public void setHideBack(boolean b) {
        if (b) {
            mBackImgView.setVisibility(View.GONE);
        } else {
            mBackImgView.setVisibility(View.VISIBLE);
        }

    }

    public interface OnTitleBarClickListener {

        void onBackClicked(View view);

        void onMenuClicked(View view);
    }
}
