package com.whl.client.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.whl.framework.utils.Utils;
import com.whl.client.R;


public class UpgradeDialog extends Dialog implements View.OnClickListener {

    //    public static final int ONE_BUTTON = 1;
    public static final int TWO_BUTTON = 2;
    private int mBtnCount;

    private Button mLeftButton;
    private Button mRightButton;
    //    private Button mOneButton;
    private EditText mContentEditText;
    private TextView mTitleView;
    private View mTwoBtnView;
    private Resources mResources;
    private CheckBox mNotRemindCheckBox;
    private View mNotRemindView;
    //    private ImageView mTitleLine;
    private TextView mTvVersion;

    private NoticeDialog.OnNoticeDialogClickListener mListener;

    private Activity mActivity;

    public UpgradeDialog(Activity activity, int btnCount) {
        super(activity, R.style.DialogTheme);
        init(activity, btnCount);
    }

    public UpgradeDialog(Activity activity, int btnCount, String content, NoticeDialog.OnNoticeDialogClickListener listener) {
        super(activity, R.style.DialogTheme);
        init(activity, btnCount);
        setOnNoticeDialogClickListener(listener);
        setContent(content);
    }

    private void init(Activity activity, int btnCount) {
        mActivity = activity;

        setContentView(R.layout.upgrade_dialog);
        LayoutParams params = getWindow().getAttributes();
        params.height = LayoutParams.WRAP_CONTENT;
        params.width = LayoutParams.MATCH_PARENT;
        getWindow().setAttributes((LayoutParams) params);

        mResources = activity.getResources();
        mBtnCount = btnCount;
        mLeftButton = (Button) findViewById(R.id.left);
        mRightButton = (Button) findViewById(R.id.right);
        //        mOneButton = (Button) findViewById(R.id.one_button);
        mContentEditText = (EditText) findViewById(R.id.notice_content);
        mTwoBtnView = findViewById(R.id.two_button);
        mTitleView = (TextView) findViewById(R.id.notice_title);
        mNotRemindCheckBox = (CheckBox) findViewById(R.id.check_not_remind);
        mNotRemindView = findViewById(R.id.not_remind_view);
        //        mTitleLine = (ImageView) findViewById(R.id.title_line);
        mTvVersion = (TextView) findViewById(R.id.tv_version);

        mLeftButton.setOnClickListener(this);
        mRightButton.setOnClickListener(this);
        //        mOneButton.setOnClickListener(this);
        mNotRemindView.setOnClickListener(onCheckViewClick);

        Resources res = activity.getResources();
        // Default title
        //        mTitleView.setText(res.getString(R.string.dialog_head));
        // Default button name
        //        mLeftButton.setText(res.getString(R.string.ok));
        //        mRightButton.setText(res.getString(R.string.cancel));
        //        mOneButton.setText(res.getString(R.string.ok));

        mNotRemindView.setVisibility(View.GONE);

        //        switch (btnCount) {
        //            case ONE_BUTTON:
        //                mTwoBtnView.setVisibility(OrderView.GONE);
        ////                mOneButton.setVisibility(OrderView.VISIBLE);
        //                break;
        //            case TWO_BUTTON:
        //                mTwoBtnView.setVisibility(OrderView.VISIBLE);
        ////                mOneButton.setVisibility(OrderView.GONE);
        //                break;
        //        }
        setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        if (Utils.isFastDoubleClick())
            return;
        dismiss();
        if (mListener == null)
            return;
        switch (v.getId()) {
            case R.id.left:
                mListener.onLeftButtonClicked(v);
                break;
            case R.id.right:
                mListener.onRightButtonClicked(v);
                break;
            case R.id.one_button:
                mListener.onLeftButtonClicked(v);
                break;
        }
    }

    private View.OnClickListener onCheckViewClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            setChecked();
        }

    };

    private void setChecked() {
        if (mNotRemindCheckBox.isChecked()) {
            mNotRemindCheckBox.setChecked(false);
        } else {
            mNotRemindCheckBox.setChecked(true);
        }
    }

    public void setContentTextSize(float size) {
        mContentEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置‘下次不再显示’是否显示
     */
    public void showCheckBox(boolean isShow) {
        if (isShow) {
            mNotRemindView.setVisibility(View.VISIBLE);
        } else {
            mNotRemindView.setVisibility(View.GONE);
        }
    }

    /**
     * is checkbox checked
     **/
    public boolean isChecked() {
        return mNotRemindCheckBox.isChecked();
    }

    /**
     * set click listener
     *
     * @param listener
     */
    public void setOnNoticeDialogClickListener(NoticeDialog.OnNoticeDialogClickListener listener) {
        mListener = listener;
    }

    public void show(String str) {
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        mContentEditText.setText(str);
        show();
    }

    @Override
    public void show() {
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        super.show();
    }

    public void setTitle(String title) {
        mTitleView.setText(title);
    }

    public void setTitle(int strId) {
        mTitleView.setText(mResources.getString(strId));
    }

    public void setContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            mContentEditText.setText(content);
        }
    }

    public void setContent(SpannableString content) {
        if (!TextUtils.isEmpty(content)) {
            mContentEditText.setText(content);
        }
    }

    public void setVersion(String version) {
        if (!TextUtils.isEmpty(version)) {
            mTvVersion.setText(version);
        }
    }

    //    public void setTitleVisible(boolean isVisible) {
    //        if (isVisible) {
    //            mTitleView.setVisibility(OrderView.VISIBLE);
    //            mTitleLine.setVisibility(OrderView.VISIBLE);
    //        } else {
    //            mTitleView.setVisibility(OrderView.GONE);
    //            mTitleLine.setVisibility(OrderView.GONE);
    //        }
    //    }

    /**
     * If has only one button,leftName is button name
     */
    public void setButtonName(String leftName, String rightName) {
        if (!TextUtils.isEmpty(leftName)) {
            if (mBtnCount == TWO_BUTTON) {
                mLeftButton.setText(leftName);
                mRightButton.setText(rightName);
                return;
            }
            //            mOneButton.setText(leftName);
        }
        if (!TextUtils.isEmpty(rightName)) {
            mRightButton.setText(rightName);
        }
    }

    //    public interface OnNoticeDialogClickListener {
    //
    //        void onLeftButtonClicked(OrderView view);
    //
    //        void onRightButtonClicked(OrderView view);
    //    }
}
