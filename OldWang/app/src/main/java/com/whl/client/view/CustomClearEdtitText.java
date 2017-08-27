package com.whl.client.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.whl.framework.utils.MLog;
import com.whl.client.R;

import static com.whl.client.R.id.chk_show_truth;

/**
 * Created by liwx on 2015/8/28.
 */
public class CustomClearEdtitText extends LinearLayout implements View.OnClickListener, View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener, TextWatcher {

    private static final String TAG = "CustomClearEdtitText";

    private Context mContext;

    private EditText mInputEdt;
    private ImageView mDeleteImg;
    private ImageView mPromptImg;
    private CheckBox mShowTruthChk;

    private String mHintText;
    private boolean isShowTruth;

    private TypedArray mTypedArray;


    public CustomClearEdtitText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomClearEdtitText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        View mParentView = inflate(context, R.layout.layout_custom_clear_edittext, this);
        mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomClearEditText);

        mInputEdt = (EditText) mParentView.findViewById(R.id.edt_input);
        mDeleteImg = (ImageView) mParentView.findViewById(R.id.img_delete);
        mPromptImg = (ImageView) mParentView.findViewById(R.id.img_prompt);
        mShowTruthChk = (CheckBox) mParentView.findViewById(chk_show_truth);

        mInputEdt.setOnFocusChangeListener(this);
        mShowTruthChk.setOnCheckedChangeListener(this);
        mInputEdt.addTextChangedListener(this);
        mDeleteImg.setOnClickListener(this);

        mHintText = mTypedArray.getString(R.styleable.CustomClearEditText_hint);
        isShowTruth = mTypedArray.getBoolean(R.styleable.CustomClearEditText_showTruth, false);
        if (isShowTruth) {
            mShowTruthChk.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mHintText)) {
            mInputEdt.setHint(mHintText);
        }
    }

    public EditText getEditText() {
        return mInputEdt;
    }

    public void setText(String s) {
        if (!TextUtils.isEmpty(s))
            mInputEdt.setText(s);
    }

    public void setSelection(int index) {
        mInputEdt.setSelection(index);
    }

    public String getText() {
        return mInputEdt.getText().toString().trim();
    }

    public void setInputType(int inputType) {
        mInputEdt.setInputType(inputType);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_delete:
                mInputEdt.setText("");
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        String input = mInputEdt.getText().toString().trim();
        if (hasFocus) {
            mPromptImg.setVisibility(View.GONE);
            if (isShowTruth) {
                mShowTruthChk.setVisibility(View.VISIBLE);
            }
            if (input.length() > 0) {
                mDeleteImg.setVisibility(View.VISIBLE);
            } else {
                mDeleteImg.setVisibility(View.GONE);
            }
        } else {
            mDeleteImg.setVisibility(View.GONE);
            if (isShowTruth) {
                mShowTruthChk.setVisibility(View.GONE);
            }
            if (input.length() == 0) {
                mPromptImg.setVisibility(View.VISIBLE);
            } else {
                mPromptImg.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mInputEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            mInputEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        mInputEdt.setSelection(mInputEdt.getText().toString().length());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        MLog.d(TAG, "afterTextChanged");
        String input = mInputEdt.getText().toString().trim();
        if (input.length() > 0) {
            mDeleteImg.setVisibility(View.VISIBLE);
        } else {
            mDeleteImg.setVisibility(View.GONE);
        }
    }
}
