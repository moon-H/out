package com.whl.client.gateway.model.wristband;

import android.content.Context;

import com.whl.framework.utils.HexConverter;
import com.whl.framework.utils.MLog;

import cn.paycloud.quinticble.QuinticCallback;
import cn.paycloud.quinticble.QuinticDevice;
import cn.paycloud.quinticble.QuinticDeviceFactory;
import cn.paycloud.quinticble.QuinticException;

public class ShuaManager {
	private static final String TAG = "ShuaManager";
	private QuinticDevice mDevice;
	private QuinticDeviceFactory deviceFactory;
	private Context context;
	
	public ShuaManager(Context context) {
		this.context = context;
		deviceFactory = QuinticDeviceFactory.getInstance(context);
	}
	
	
	public void findDevice(String deviceAddress, final OnErrorListener listener){
		deviceFactory.findDevice(deviceAddress, new QuinticCallback<QuinticDevice>() {
			@Override
			public void onComplete(QuinticDevice device) {
				super.onComplete(device);
				mDevice =  device;
				MLog.d(TAG, "findDevice onComplete");
				if(mDevice != null){
					listener.onComplete();
				}else{
					listener.onError("连接设备失败！");
				}
				
			}
			@Override
			public void onError(QuinticException e) {
				super.onError(e);
				MLog.d(TAG, "findDevice onError" + e.getMessage());
				final String erro = e.getMessage();
				listener.onError(erro);
			}
			
			@Override
			public void onProgress(int arg0) {
				super.onProgress(arg0);
				MLog.d(TAG, "onProgress =" + arg0);
			}
		});
	}
	
	public void flashLed(QuinticCallback<Void> quinticCallback) {
		mDevice.flashLed(quinticCallback);
	}
	
	public void powerOn(QuinticCallback<Void> quinticCallback) {
		mDevice.smartCardPowerOn(quinticCallback);
	}
	
	public void powerOff(QuinticCallback<Void> quinticCallback) {
		mDevice.smartCardPowerOff(quinticCallback);
	}
	
	public void transmission(String hexString ,QuinticCallback<byte[]> quinticCallback){
		mDevice.smartCardTransmission(HexConverter.hexStringToBytes(hexString), quinticCallback);
	}
	
	public String getAddress() {
		if(mDevice!= null){
			return mDevice.getAddress();
		}
		return null;
	}
	
	private OnErrorListener onErrorListener;
	public interface OnErrorListener{
		void onError(String msg);
		void onComplete();
	}
}
