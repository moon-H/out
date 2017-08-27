
package com.whl.client.view;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.whl.framework.utils.Utils;
import com.whl.client.R;


public class ClearEditText extends EditText implements OnFocusChangeListener, TextWatcher {

	/**
	 * 删除按钮的引用
	 */
	private Drawable mClearDrawable;
	/**
	 * 控件是否有焦点
	 */
	private boolean hasFoucs;
	private boolean hadFoucs;
	private boolean mFunctionBtnIsShowing;
	private Drawable mFunctionDrawable;
	private OnFunctionButtonClickListener mFunctionButtonClickListener;
	private Context mContext;
	private int mDrawablePaddingRight = 24;
	
	private boolean isShowAlertIcon = true;

	public ClearEditText(Context context) {
		this(context, null);
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		// 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	public void setShowAlertIcon(boolean isShowAlertIcon){
		this.isShowAlertIcon = isShowAlertIcon;
	}

	private void init(Context context) {
		// 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片

		mContext = context;
		mClearDrawable = getCompoundDrawables()[2];
		if (mClearDrawable == null) {
			// throw new NullPointerException("You can add drawableRight attribute in XML");
			mClearDrawable = getResources().getDrawable(R.drawable.selector_delete);
		}
		int paddingRight = Utils.px2dip(context, mDrawablePaddingRight);
		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
		setPadding(0, 0, paddingRight, 0);
		// 默认设置隐藏图标
		setClearIconVisible(false);
		// 设置焦点改变的监听
		setOnFocusChangeListener(this);
		// 设置输入框里面内容发生改变的监听
		addTextChangedListener(this);
		// 设置光标可见
		setCursorVisible(true);
		int padding = Utils.dip2px(mContext, 8);
		setPadding(padding, padding, padding, padding);
	}

	/**
	 * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 - 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 -
	 * 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (getCompoundDrawables()[2] != null) {

				boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
						&& (event.getX() < ((getWidth() - getPaddingRight())));

				if (touchable) {
					// setFocusableInTouchMode(false);
					if (mFunctionBtnIsShowing) {
						// setFocusable(true);
						// requestFocus();
						Utils.hideSoftInput(mContext, this);
						if (mFunctionButtonClickListener != null) {
							mFunctionButtonClickListener.onFunctionButtonClicked();
						}
						return true;
					} else {
						// setFocusable(true);
						this.setText("");
					}
				} else {
					setFocusableInTouchMode(true);
					// setFocusable(true);
				}
			}
		}

		return super.onTouchEvent(event);
	}

	public void setFuctionBtnVisible(Drawable drawable, OnFunctionButtonClickListener listener) {
		this.mFunctionDrawable = drawable;
		this.mFunctionDrawable.setBounds(0, 0, mFunctionDrawable.getIntrinsicWidth(),
				mFunctionDrawable.getIntrinsicHeight());
		this.mFunctionButtonClickListener = listener;
		setClearIconVisible(false);
	}

	/**
	 * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		this.hasFoucs = hasFocus;
		if (hasFocus) {
			hadFoucs = true;
			setClearIconVisible(getText().length() > 0);
		} else {
			setClearIconVisible(false);
		}

	}

	public void initFoucus() {
		hadFoucs = false;
	}

	/**
	 * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	 * 
	 * @param visible
	 */
	protected void setClearIconVisible(boolean visible) {
		if (visible) {
			this.mFunctionBtnIsShowing = false;
		} else {
			this.mFunctionBtnIsShowing = true;
		}
		Drawable right = visible ? mClearDrawable : mFunctionDrawable;
		if (right == null && hadFoucs && !hasFoucs && getText().length() <= 0 && isShowAlertIcon) {
			Drawable alertDrawable = getResources().getDrawable(R.drawable.ic_allpage_prompt);
			alertDrawable.setBounds(0, 0, alertDrawable.getIntrinsicWidth(), alertDrawable.getIntrinsicHeight());
			right = alertDrawable;
		}
		setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	/**
	 * 当输入框里面内容发生变化的时候回调的方法
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		if (hasFoucs) {
			setClearIconVisible(s.length() > 0);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	/**
	 * 设置晃动动画
	 */
	public void setShakeAnimation() {
		this.setAnimation(shakeAnimation(5));
	}

	/**
	 * 晃动动画
	 * 
	 * @param counts
	 *            1秒钟晃动多少下
	 * @return
	 */
	public static Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

	public boolean hasFoucs() {
		return hasFoucs;

	}
	public interface OnFunctionButtonClickListener {

		void onFunctionButtonClicked();
	}

	// add20150116
	public void setPromptDrawable() {
		Drawable alertDrawable = getResources().getDrawable(R.drawable.ic_allpage_prompt);
		setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], alertDrawable,
				getCompoundDrawables()[3]);
	}
}
