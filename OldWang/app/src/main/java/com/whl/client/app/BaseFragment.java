package com.whl.client.app;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.whl.framework.http.model.Result;
import com.whl.client.home.AppManager;
import com.whl.client.view.CssProgressDialog;


public class BaseFragment extends Fragment {
    private CssProgressDialog mProgressDialog;
    private AppManager appManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appManager = new AppManager(getActivity());
    }

    public void showBindPhoneNumberDialog(final Activity activity) {
        appManager.showBindPhoneNumberDialog(activity);
    }

    /**
     * 显示加载框
     */
    public void showProgress(String msg) {
        showProgressDialog(msg, false);
    }


    /**
     * 显示加载信息
     */

    public void showProgress() {
        showProgressDialog("", false);
    }

    /**
     * 显示加载信息
     */
    public void showProgress(String msg, boolean cancelAble) {
        showProgressDialog(msg, cancelAble);
    }

    private void showProgressDialog(String msg, boolean cancelAble) {
        if (mProgressDialog == null) {
            mProgressDialog = new CssProgressDialog(getActivity());
        }
        mProgressDialog.setContent(msg);
        if (cancelAble)
            mProgressDialog.setCancelable(true);
        else
            mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    /**
     * 关闭dialog
     */
    public void dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public void handleHttpFailed(Activity activity, int statusCode) {
        appManager.handleHttpFailed(activity, statusCode);
    }

    public void handleAutoLoginFailed(Result result) {
        if (result != null) {
            Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
        }
        //        logoutApp();
    }
}
