package com.whl.client.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.app.BaseFragment;
import com.whl.client.umengShare.UMengShare;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lenovo on 2016/9/27.
 */
public class ErrorFragment extends BaseFragment {
    private static final String TAG = "ErrorFragment";

    private View mRootView;
    private TextView mTvErrorMsg;
    private String mStrErrorMsg = "";
    private int mErrorCode = -1;

    private ExecutorService mExcutor = Executors.newCachedThreadPool();

    public static ErrorFragment getInstance() {
        return new ErrorFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MLog.d(TAG, "onCreate");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        MLog.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MLog.d(TAG, "onCreateView");
        if (mRootView == null) {
            MLog.d(TAG, "newCreateView");
            mRootView = inflater.inflate(R.layout.fragment_error, container, false);
            initView();
        }
        mTvErrorMsg.setText(mStrErrorMsg);
        return mRootView;

    }

    private void initView() {
        //        TitleBarView mTitleBar = (TitleBarView) mRootView.findViewById(R.id.title_bar);
        //        mTitleBar.setTitle(getResources().getString(R.string.bbs));
        mTvErrorMsg = (TextView) mRootView.findViewById(R.id.tv_error_msg);
    }

    @Override
    public void onResume() {
        super.onResume();
        MLog.d(TAG, "onResume");
        UMengShare.countPageStart(getString(R.string.statistic_NfcServiceFragment));
    }

    @Override
    public void onPause() {
        super.onPause();
        MLog.d(TAG, "onPause");
        UMengShare.countPageEnd(getString(R.string.statistic_NfcServiceFragment));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MLog.d(TAG, "onDestroy");
        mRootView = null;
    }

    @Override
    public void onDestroyView() {
        ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        super.onDestroyView();
        MLog.d(TAG, "onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setErrorMsg(int errorCode, String msg) {
        MLog.d(TAG, "error msg = " + msg);
        if (!TextUtils.isEmpty(msg)) {
            mStrErrorMsg = msg;
            mErrorCode = errorCode;
            if (mTvErrorMsg != null)
                mTvErrorMsg.setText(mStrErrorMsg);
        }

    }
}
