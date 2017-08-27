package com.whl.client.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.whl.framework.utils.MLog;
import com.whl.client.R;

public class ShowInfoDialog extends Dialog{
	private TextView tvInfo;
	private RotateAnimation animation;
	private static ShowInfoDialog dialog = null;

	private ShowInfoDialog(Context context) {
		super(context);
	}
	
	private ShowInfoDialog(Context context, int theme){
		super(context, theme);
	}
	
	public static ShowInfoDialog createDialog(Context context) {
		dialog = new ShowInfoDialog(context, R.style.DialogTheme);
		dialog.setContentView(R.layout.progressbar_inverse2);
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
		animation.setDuration(1300);
		animation.setRepeatMode(Animation.RESTART);
		animation.setRepeatCount(Animation.INFINITE);
		animation.setInterpolator(new LinearInterpolator());
		ImageView progress = (ImageView) dialog.findViewById(R.id.img_progress);
		progress.startAnimation(animation);

	}
	
	public boolean  isAnimationStart(){
		if(animation == null){
			return false;
		}
		return animation.hasStarted();
	}
	
	public void  startAnimation(){
		if (dialog == null) {
			return;
		}
		MLog.d("EnterActivity", "startAnimation");
		if(animation == null){
			animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			animation.setDuration(1300);
			animation.setRepeatMode(Animation.RESTART);
			animation.setRepeatCount(Animation.INFINITE);
			animation.setInterpolator(new LinearInterpolator());
		}
		ImageView progress = (ImageView) dialog.findViewById(R.id.img_progress);
		progress.startAnimation(animation);
	}
	
	
	public void setMessage(int resId){
		if(tvInfo == null){
			tvInfo = (TextView) dialog.findViewById(R.id.tv_pro_text);
		}
		tvInfo.setText(resId);
	}
	
	public void setMessage(String msg){
		if(tvInfo == null){
			tvInfo = (TextView) dialog.findViewById(R.id.tv_pro_text);
		}
		tvInfo.setText(msg);
	}
	
}
