
package com.whl.client.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.whl.framework.utils.MLog;
import com.whl.client.R;


public class ProgressDialog extends Dialog {

	private static ProgressDialog dialog = null;
	private RotateAnimation animation;
	private static final String TAG = "ProgressDialog";

	public ProgressDialog(Context context) {
		super(context);
	}

	public ProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static ProgressDialog createDialog(Context context) {
		dialog = new ProgressDialog(context, R.style.ProgressDialog);
		dialog.setContentView(R.layout.progress_dialog_inverse);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		dialog.setCancelable(false);
		return dialog;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (dialog == null) {
			return;
		}
		animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(1000);
		animation.setRepeatMode(Animation.RESTART);
		animation.setRepeatCount(Animation.INFINITE);
		animation.setInterpolator(new LinearInterpolator());
		ImageView progress = (ImageView) dialog.findViewById(R.id.loadImg);
		progress.startAnimation(animation);

	}

	public void show() {
		if (dialog != null)
			super.show();
		else
			MLog.e(TAG, "dialog is null");
	}

	public void dismiss() {
		if (dialog != null)
			super.dismiss();
	}

}
