package com.whl.client.app;

import android.content.Context;
import android.widget.Toast;

import com.whl.framework.http.model.Result;
import com.whl.client.R;

/**
 * Created by liwx on 2015/8/5.
 */
public class CommonToast {
    public static void onNoNetwork(Context context) {
        Toast.makeText(context, context.getString(R.string.network_exception), Toast.LENGTH_SHORT).show();
    }

    public static void onFailed(Context context, Result result) {
        if (result != null)
            Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public static void onHttpFailed(Context context) {
        Toast.makeText(context, context.getString(R.string.connect_server_failed), Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toast(String msg) {
        Toast.makeText(BizApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void onLogicFaild(Context context, Result result) {
        if (result != null)
            Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, context.getString(R.string.connect_server_failed), Toast.LENGTH_SHORT).show();

    }
}
