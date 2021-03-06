package com.whl.client.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.whl.framework.utils.Utils;
import com.whl.client.R;


/**
 * Created by liwx on 11/6/14.
 */
public class ArcProgress extends View {
    private Paint paint;
    private Paint paint2;
    //    private Paint paint3;
    private Paint prefixRoundPaint;
    private Paint suffixRoundPaint;


    protected Paint textPaint;

    private RectF rectF = new RectF();

    private float strokeWidth;
    private float suffixTextSize;
    private float bottomTextSize;
    private String bottomText;
    private float textSize;
    private int textColor;
    private float progress = 0;
    private int max;
    private int finishedStrokeColor;
    private int unfinishedStrokeColor;
    private float arcAngle;
    private String suffixText = "%";
    private float suffixTextPadding;

    private float arcBottomHeight;

    private final int default_finished_color = Color.WHITE;
    private final int default_unfinished_color = Color.rgb(72, 106, 176);
    private final int default_text_color = Color.rgb(66, 145, 241);
    private final float default_suffix_text_size;
    private final float default_suffix_padding;
    private final float default_bottom_text_size;
    private final float default_stroke_width;
    private final String default_suffix_text;
    private final int default_max = 50000;
    private float default_arc_angle = 360 * 0.7f;
    private float default_text_size;
    private final int min_size;
    private boolean isDrawText = false;

    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_STROKE_WIDTH = "stroke_width";
    private static final String INSTANCE_SUFFIX_TEXT_SIZE = "suffix_text_size";
    private static final String INSTANCE_SUFFIX_TEXT_PADDING = "suffix_text_padding";
    private static final String INSTANCE_BOTTOM_TEXT_SIZE = "bottom_text_size";
    private static final String INSTANCE_BOTTOM_TEXT = "bottom_text";
    private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_TEXT_COLOR = "text_color";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color";
    private static final String INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color";
    private static final String INSTANCE_ARC_ANGLE = "arc_angle";
    private static final String INSTANCE_SUFFIX = "suffix";

    //是否逆时针旋转
    private boolean isAntiClockWise = false;

    public ArcProgress(Context context) {
        this(context, null);
    }

    public ArcProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_text_size = Utils.sp2px(context, 18);
        min_size = Utils.dip2px(context, 100);
        default_text_size = Utils.sp2px(context, 40);
        default_suffix_text_size = Utils.sp2px(context, 15);
        default_suffix_padding = Utils.dip2px(context, 4);
        default_suffix_text = "%";
        default_bottom_text_size = Utils.sp2px(context, 10);
        default_stroke_width = Utils.dip2px(context, 4);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();

        initPainters();
    }

    protected void initByAttributes(TypedArray attributes) {
        finishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_finished_color, default_finished_color);
        unfinishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_unfinished_color, default_unfinished_color);
        textColor = attributes.getColor(R.styleable.ArcProgress_arc_text_color, default_text_color);
        textSize = attributes.getDimension(R.styleable.ArcProgress_arc_text_size, default_text_size);
        arcAngle = attributes.getFloat(R.styleable.ArcProgress_arc_angle, default_arc_angle);
        setMax(attributes.getInt(R.styleable.ArcProgress_arc_max, default_max));
        setProgress(attributes.getInt(R.styleable.ArcProgress_arc_progress, 0));
        strokeWidth = attributes.getDimension(R.styleable.ArcProgress_arc_stroke_width, default_stroke_width);
        suffixTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_suffix_text_size, default_suffix_text_size);
        suffixText = TextUtils.isEmpty(attributes.getString(R.styleable.ArcProgress_arc_suffix_text)) ? default_suffix_text : attributes.getString(R.styleable.ArcProgress_arc_suffix_text);
        suffixTextPadding = attributes.getDimension(R.styleable.ArcProgress_arc_suffix_text_padding, default_suffix_padding);
        bottomTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_bottom_text_size, default_bottom_text_size);
        bottomText = attributes.getString(R.styleable.ArcProgress_arc_bottom_text);
        isDrawText = attributes.getBoolean(R.styleable.ArcProgress_arc_draw_text, false);

        float arcAngleOffset = attributes.getFloat(R.styleable.ArcProgress_arc_angle_offset, 0.7f);
        arcAngle = 360 * arcAngleOffset;
    }

    /**
     * 0:Round,1:BUTT
     **/
    int paintCap = 0;

    protected void initPainters() {
        textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);

        paint = new Paint();
        paint.setColor(default_unfinished_color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        //        prefixRoundPaint = new Paint();
        //        prefixRoundPaint.setAntiAlias(true);
        //        prefixRoundPaint.setStyle(Paint.Style.STROKE);
        //        prefixRoundPaint.setStrokeCap(Paint.Cap.ROUND);
        //
        //        suffixRoundPaint = new Paint();
        //        suffixRoundPaint.setAntiAlias(true);
        //        suffixRoundPaint.setStyle(Paint.Style.STROKE);
        //        suffixRoundPaint.setStrokeCap(Paint.Cap.ROUND);


        paint2 = new Paint();
        paint2.setColor(default_unfinished_color);
        paint2.setAntiAlias(true);
        paint2.setStrokeWidth(strokeWidth);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    public void invalidate() {
        initPainters();
        super.invalidate();
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        this.invalidate();
    }

    public float getSuffixTextSize() {
        return suffixTextSize;
    }

    public void setSuffixTextSize(float suffixTextSize) {
        this.suffixTextSize = suffixTextSize;
        this.invalidate();
    }

    public String getBottomText() {
        return bottomText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
        this.invalidate();
    }

    public float getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
    }

    /**
     * 可移动的最大进度
     */
    private float maxProgress;

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        isProgressStop(false);
        this.progress = progress;
        if (this.progress > getMaxProgress()) {
            this.progress = getMaxProgress();
        }
        if (this.progress > getMax()) {
            this.progress = getMax();
        }
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
            invalidate();
        }
    }

    public float getBottomTextSize() {
        return bottomTextSize;
    }

    public void setBottomTextSize(float bottomTextSize) {
        this.bottomTextSize = bottomTextSize;
        this.invalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        this.invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.invalidate();
    }

    public int getFinishedStrokeColor() {
        return finishedStrokeColor;
    }

    public void setFinishedStrokeColor(int finishedStrokeColor) {
        this.finishedStrokeColor = finishedStrokeColor;
        this.invalidate();
    }

    public int getUnfinishedStrokeColor() {
        return unfinishedStrokeColor;
    }

    public void setUnfinishedStrokeColor(int unfinishedStrokeColor) {
        this.unfinishedStrokeColor = unfinishedStrokeColor;
        this.invalidate();
    }

    public float getArcAngle() {
        return arcAngle;
    }

    public void setArcAngle(float arcAngle) {
        this.arcAngle = arcAngle;
        this.invalidate();
    }

    public String getSuffixText() {
        return suffixText;
    }

    public void setSuffixText(String suffixText) {
        this.suffixText = suffixText;
        this.invalidate();
    }

    public float getSuffixTextPadding() {
        return suffixTextPadding;
    }

    public void setSuffixTextPadding(float suffixTextPadding) {
        this.suffixTextPadding = suffixTextPadding;
        this.invalidate();
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return min_size;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return min_size;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        rectF.set(strokeWidth / 2f, strokeWidth / 2f, width - strokeWidth / 2f, MeasureSpec.getSize(heightMeasureSpec) - strokeWidth / 2f);
        float radius = width / 2f;
        float angle = (360 - arcAngle) / 2f;
        arcBottomHeight = radius * (float) (1 - Math.cos(angle / 180 * Math.PI));
    }

    float beginFinishedSweepAngle = 100f;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startAngle = 270 - arcAngle / 2f;
        float finishedSweepAngle = progress / (float) getMax() * arcAngle;

        float finishedStartAngle = startAngle;
        if (isAntiClockWise)
            finishedStartAngle = arcAngle / 2f - 90;

        //        float suffixFinishedStartAngle = arcAngle / 2f - 90;
        paint.setColor(unfinishedStrokeColor);
        //总的进度
        canvas.drawArc(rectF, startAngle, arcAngle, false, paint);

        //        if (Math.abs(finishedSweepAngle) > 0) {
        //            if (isAntiClockWise) {
        //                //逆时针
        //                prefixRoundPaint.setColor(unfinishedStrokeColor);
        //                prefixRoundPaint.setStrokeWidth(strokeWidth);
        //                suffixRoundPaint.setColor(finishedStrokeColor);
        //                suffixRoundPaint.setStrokeWidth(strokeWidth + 2);
        //            } else {
        //                prefixRoundPaint.setColor(finishedStrokeColor);
        //                prefixRoundPaint.setStrokeWidth(strokeWidth + 2);
        //                suffixRoundPaint.setColor(unfinishedStrokeColor);
        //                suffixRoundPaint.setStrokeWidth(strokeWidth);
        //            }
        //        } else {
        //            prefixRoundPaint.setColor(unfinishedStrokeColor);
        //            suffixRoundPaint.setColor(unfinishedStrokeColor);
        //            prefixRoundPaint.setStrokeWidth(strokeWidth);
        //            suffixRoundPaint.setStrokeWidth(strokeWidth);
        //        }
        //        if (progress >= max) {
        //            suffixRoundPaint.setColor(finishedStrokeColor);
        //            suffixRoundPaint.setStrokeWidth(strokeWidth + 2);
        //            prefixRoundPaint.setColor(finishedStrokeColor);
        //            prefixRoundPaint.setStrokeWidth(strokeWidth + 2);
        //        }

        //        if (isAntiClockWise) {
        //            //左侧弧形
        //            canvas.drawArc(rectF, startAngle, -beginFinishedSweepAngle, false, prefixRoundPaint);
        //            //右侧弧形
        //            canvas.drawArc(rectF, suffixFinishedStartAngle, beginFinishedSweepAngle, false, suffixRoundPaint);
        //        } else {
        //            //左侧弧形
        //            canvas.drawArc(rectF, startAngle, -beginFinishedSweepAngle, false, prefixRoundPaint);
        //            //右侧弧形
        //            canvas.drawArc(rectF, suffixFinishedStartAngle, beginFinishedSweepAngle, false, suffixRoundPaint);
        //        }
        if (isAntiClockWise) {
            finishedSweepAngle = -finishedSweepAngle;
        }
        paint2.setColor(finishedStrokeColor);
        //变化的进度条
        canvas.drawArc(rectF, finishedStartAngle, finishedSweepAngle, false, paint2);

        //        if (isProgressStop) {
        //            if (isAntiClockWise) {
        //                paint.setColor(finishedStrokeColor);
        //                paint2.setColor(unfinishedStrokeColor);
        //            } else {
        //                paint.setColor(unfinishedStrokeColor);
        //                paint2.setColor(finishedStrokeColor);
        //            }
        //            canvas.drawArc(rectF, startAngle, arcAngle, false, paint);
        //            canvas.drawArc(rectF, startAngle,arcAngle- Math.abs(finishedSweepAngle), false, paint2);
        //        }


        String text;
        if (isAntiClockWise) {
            text = parseMoney(max - getProgress());
        } else
            text = parseMoney(getProgress());
        if (!TextUtils.isEmpty(text)) {
            textPaint.setColor(textColor);
            textPaint.setTextSize(textSize);
            float textHeight = textPaint.descent() + textPaint.ascent();
            float textBaseline = (getHeight() - textHeight) / 2.0f;
            if (isDrawText)
                canvas.drawText(text, (getWidth() - textPaint.measureText(text)) / 2.0f, textBaseline, textPaint);
            //            textPaint.setTextSize(suffixTextSize);
            //            float suffixHeight = textPaint.descent() + textPaint.ascent();
            //百分比
            //            canvas.drawText(suffixText, getWidth() / 2.0f + textPaint.measureText(text) + suffixTextPadding, textBaseline + textHeight - suffixHeight, textPaint);
        }

        if (!TextUtils.isEmpty(getBottomText())) {
            textPaint.setTextSize(bottomTextSize);
            float bottomTextBaseline = getHeight() - arcBottomHeight - (textPaint.descent() + textPaint.ascent()) / 2;
            canvas.drawText(getBottomText(), (getWidth() - textPaint.measureText(getBottomText())) / 2.0f, bottomTextBaseline, textPaint);
        }
    }

    boolean isProgressStop;

    public void isProgressStop(boolean flag) {
        isProgressStop = flag;
        invalidate();
    }

    /**
     * 设置方向，默认顺时针方向
     */
    public void setAntiClockWise(boolean flag) {
        this.isAntiClockWise = flag;
        invalidate();
    }

    /**
     * 设置方向初始角度
     */
    public void setDefaultAngle(float offset) {
        this.arcAngle = 360 * offset;
        invalidate();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putFloat(INSTANCE_STROKE_WIDTH, getStrokeWidth());
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_SIZE, getSuffixTextSize());
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_PADDING, getSuffixTextPadding());
        bundle.putFloat(INSTANCE_BOTTOM_TEXT_SIZE, getBottomTextSize());
        bundle.putString(INSTANCE_BOTTOM_TEXT, getBottomText());
        bundle.putFloat(INSTANCE_TEXT_SIZE, getTextSize());
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor());
        bundle.putFloat(INSTANCE_PROGRESS, getProgress());
        bundle.putInt(INSTANCE_MAX, getMax());
        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, getFinishedStrokeColor());
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, getUnfinishedStrokeColor());
        bundle.putFloat(INSTANCE_ARC_ANGLE, getArcAngle());
        bundle.putString(INSTANCE_SUFFIX, getSuffixText());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            strokeWidth = bundle.getFloat(INSTANCE_STROKE_WIDTH);
            suffixTextSize = bundle.getFloat(INSTANCE_SUFFIX_TEXT_SIZE);
            suffixTextPadding = bundle.getFloat(INSTANCE_SUFFIX_TEXT_PADDING);
            bottomTextSize = bundle.getFloat(INSTANCE_BOTTOM_TEXT_SIZE);
            bottomText = bundle.getString(INSTANCE_BOTTOM_TEXT);
            textSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
            textColor = bundle.getInt(INSTANCE_TEXT_COLOR);
            setMax(bundle.getInt(INSTANCE_MAX));
            setProgress(bundle.getFloat(INSTANCE_PROGRESS));
            finishedStrokeColor = bundle.getInt(INSTANCE_FINISHED_STROKE_COLOR);
            unfinishedStrokeColor = bundle.getInt(INSTANCE_UNFINISHED_STROKE_COLOR);
            suffixText = bundle.getString(INSTANCE_SUFFIX);
            initPainters();
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    //    public void setUnfinishedPaintType(Paint.Cap cap) {
    //        paint.setStrokeCap(cap);
    //    }
    //
    //    public void setfinishedPaintType(Paint.Cap cap) {
    //        paint2.setStrokeCap(cap);
    //    }
    public static String parseMoney(float amount) {
        //        double value = Double.valueOf(amount);
        return Utils.formatDouble2f(amount / 100.00);
    }
}
