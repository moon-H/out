//package com.cssweb.shankephone.gateway.model.wristband;
//
//import android.app.Activity;
//import android.util.Log;
//
//import com.cssweb.framework.utils.MLog;
//import com.lakala.cswiper6.bluetooth.CSwiperController;
//import com.lakala.cswiper6.bluetooth.CSwiperController.CSwiperStateChangedListener;
//import com.lakala.cswiper6.bluetooth.CSwiperController.EmvControllerListener;
//import com.newland.mtype.conn.BluetoothConnectListener;
//import com.newland.mtype.module.common.iccard.ICCardSlot;
//import com.newland.mtype.util.ISOUtils;
//import com.xrz.lib.bluetooth.BTLinkerUtils;
//import com.xrz.lib.bluetooth.BlueToothDataListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class LakaLaManager {
//    private String TAG = LakaLaManager.class.getName();
//    public static final int OK = 100;
//    public static final int ERROR = 101;
//    public static CSwiperController cswiperController;
//    private BTLinkerUtils btLinkerUtils;
//
//    private Activity context;
//
//    private int status;
//
//    public LakaLaManager(Activity context2) throws Exception {
//        this.context = context2;
//        if (context instanceof CSwiperStateChangedListener && context instanceof EmvControllerListener) {
//            try {
//                cswiperController = new CSwiperController(context, (CSwiperStateChangedListener) context, (EmvControllerListener) context);
//                btLinkerUtils = new BTLinkerUtils(context);
//            } catch (Exception e) {
//                e.printStackTrace();
//                MLog.e(TAG, e);
//            }
//        } else {
//            throw new Exception(" ERRO:: must implements interface CSwiperStateChangedListener ," + "EmvControllerListener");
//        }
//
//    }
//
//    public void initController(final String address, BluetoothConnectListener listener) {
//        try {
//            cswiperController.setConnectParams(new String[]{"btaddr:" + address});
//            cswiperController.connectBLEBlueTooth(listener);
//        } catch (Exception e) {
//            Log.e(TAG, "failed to connect to:" + address, e);
//            listener.isConnected(false, 1000);
//            if (cswiperController != null) {
//                try {
//                    cswiperController.deleteCSwiper();
//                } catch (Exception ex) {
//                } finally {
//                    cswiperController = null;
//                }
//            }
//        }
//    }
//
//    private BlueToothDataListener btListener;
//
//    public void initBLEBluetooth(String mac, BlueToothDataListener btListener) {
//        this.btListener = btListener;
//        btLinkerUtils.getBackCall(btListener);
//        btLinkerUtils.connect(mac);
//        Log.i(TAG, "Trying to create a new connection.");
//    }
//
//    public void powerON() {
//        byte[] result = cswiperController.powerOn(ICCardSlot.SE);
//    }
//
//    public void powerOFF() {
//        if (cswiperController != null) {
//            cswiperController.powerOff(ICCardSlot.SE);
//        }
//    }
//
//    private List<String> resultList;
//
//    public List<String> icCommunication(String[] commads) {
//        resultList = new ArrayList<String>();
//        for (int i = 0; i < commads.length; i++) {
//            MLog.e(TAG, commads[i]);
//            byte[] result = cswiperController.communication(ICCardSlot.SE, ISOUtils.hex2byte(commads[i]));
//            resultList.add(ISOUtils.hexString(result));
//        }
//        return resultList;
//    }
//
//    public void deleteController() {
//        try {
//            if (cswiperController != null) {
//                cswiperController.deleteCSwiper();
//
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "deleteCSwiper failed!", e);
//        }
//
//        try {
//            if (btLinkerUtils != null) {
//                Log.i("deleteController", "in destory...");
//                btLinkerUtils.Close();
//                btLinkerUtils = null;
//            }
//        } catch (Exception e) {
//        }
//
//    }
//
//    public void stopCSwiper() {
//        if (cswiperController != null) {
//            cswiperController.stopCSwiper();
//        }
//
//    }
//
//}
