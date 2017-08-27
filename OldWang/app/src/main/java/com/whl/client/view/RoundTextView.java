package com.whl.client.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.whl.client.R;


public class RoundTextView extends TextView {

	private Context mContext;
	private int mBgColor = 0;
	private int mCornerSize = 0;
	
	public RoundTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getAttrs(context, attrs);
		mContext = context;
	}

	@Override
    protected void onDraw(Canvas canvas)
    {
	    super.onDraw(canvas);
        setBackgroundRounded(this.getMeasuredWidth(), this.getMeasuredHeight(), this);
    }
	

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.round_textview);
        mBgColor = ta.getColor(R.styleable.round_textview_round_tv_color, getResources().getColor(R.color.transparent));
        mCornerSize = (int)ta.getDimension(R.styleable.round_textview_round_tv_corner_size, 8);
        
        ta.recycle();
    }
	
    public void setBackgroundRounded(int w, int h, View v)
    {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        double dH = (metrics.heightPixels / 100) * 1.5;
        int iHeight = (int)dH;

        
        iHeight = mCornerSize;
        
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(mBgColor);
//        paint.setColor(mContext.getResources().getColor(R.color.blue));
        RectF rec = new RectF(0, 0, w, h);
        c.drawRoundRect(rec, iHeight, iHeight, paint);

        v.setBackgroundDrawable(new BitmapDrawable(getResources(), bmp));
    }

	public int getmBgColor() {
		return mBgColor;
	}

	public void setmBgColor(int mBgColor) {
		this.mBgColor = mBgColor;
	}
    
    
}
