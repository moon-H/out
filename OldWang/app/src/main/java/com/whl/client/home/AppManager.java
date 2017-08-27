package com.whl.client.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.whl.framework.preference.MSharedPreference;
import com.whl.client.R;
import com.whl.client.app.CommonToast;
import com.whl.client.dialog.NoticeDialog;
import com.whl.client.login.LoginActivity;

/**
 * Created by lenovo on 2016/9/27.
 */
public class AppManager {
    private static final String TAG = "AppManager";
    private Context context;

    public AppManager(Context context) {
        this.context = context;
    }

    public void showBindPhoneNumberDialog(final Activity activity) {
        NoticeDialog dialog = new NoticeDialog(activity, NoticeDialog.TWO_BUTTON);
        dialog.setButtonName(activity.getString(R.string.bind), activity.getString(R.string.cancel));
        dialog.setOnNoticeDialogClickListener(new NoticeDialog.OnNoticeDialogClickListener() {
            @Override
            public void onLeftButtonClicked(View view) {
            }

            @Override
            public void onRightButtonClicked(View view) {

            }
        });
        dialog.show(activity.getString(R.string.need_register_phone));
    }

    public void handleHttpFailed(final Activity activity, final int statusCode) {
        switch (statusCode) {
            case 401:
                NoticeDialog noticeDialog = new NoticeDialog(activity, NoticeDialog.ONE_BUTTON);

                noticeDialog.setButtonName(activity.getString(R.string.ok), "");
                noticeDialog.setOnNoticeDialogClickListener(new NoticeDialog.OnNoticeDialogClickListener() {

                    @Override
                    public void onRightButtonClicked(View view) {

                    }

                    @Override
                    public void onLeftButtonClicked(View view) {
                        switch (statusCode) {
                            case 401:
                                MSharedPreference.removeCookie(activity);
                                activity.startActivity(new Intent(activity, LoginActivity.class));
                                break;

                            default:
                                break;
                        }
                    }
                });
                noticeDialog.show(activity.getString(R.string.login_state_timeout));
                break;
            default:
                CommonToast.onHttpFailed(activity);
                break;
        }
    }
}
