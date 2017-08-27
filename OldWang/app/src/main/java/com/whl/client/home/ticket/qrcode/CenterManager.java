/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.whl.client.home.ticket.qrcode;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.whl.framework.utils.HexConverter;
import com.whl.client.app.CssConstant;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
@SuppressLint("NewApi")
public class CenterManager {
    private final static String TAG = "CenterManager";

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;
    private int mFindServiceState = FIND_SERVICE_STATE_START;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_CONNECT_TIME_OUT = 3;

    private static final int FIND_SERVICE_STATE_START = 1;
    private static final int FIND_SERVICE_STATE_SUCCESS = 2;
    private static final int FIND_SERVICE_STATE_FAILED = 3;
    private static final int FIND_SERVICE_STATE_TIME_OUT = 4;

    public static final int WRITE_DATA_RESULT_SUCCESS = 1;
    public static final int WRITE_DATA_RESULT_FAILED_GATT_IS_NULL = 2;
    public static final int WRITE_DATA_RESULT_FAILED_SERVICE_IS_NULL = 3;
    public static final int WRITE_DATA_RESULT_FAILED_ADAPTER_NOT_INITIALIZED = 4;
    public static final int WRITE_DATA_RESULT_FAILED_UNKOWN = 5;


    public final static String ACTION_GATT_CONNECTTING = "com.example.bluetooth.le.ACTION_GATT_CONNECTTING";
    public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_CONNECT_TIME_OUT = "com.example.bluetooth.le.ACTION_GATT_CONNECT_TIME_OUT";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_GATT_SERVICES_DISCOVER_FAILED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVER_FAILED";
    public final static String ACTION_GATT_SERVICES_DISCOVER_COMPLETE = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVER_COMPLETE";
    public final static String ACTION_STOP_SCAN = "com.example.bluetooth.le.ACTION_STOP_SCAN";
    public final static String ACTION_START_SCAN = "com.example.bluetooth.le.ACTION_START_SCAN";
    public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String ACTION_FIND_TARGET_DEVICE = "com.example.bluetooth.le.ACTION_FIND_TARGET_DEVICE";
    public final static String ACTION_SCAN_DEVICE_COMPLETE = "com.example.bluetooth.le.ACTION_SCAN_DEVICE_COMPLETE";
    public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
    public final static String ACTION_WRITE_DATA_RESULT = "com.example.bluetooth.le.ACTION_WRITE_DATA_RESULT";
    public final static String ACTION_RECEIVE_DATA = "com.example.bluetooth.le.ACTION_RECEIVE_DATA";

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private Context mContext;

    CountDownTimer mConnectCountTimer;
    CountDownTimer mFindServiceTimer;

    private BluetoothLeScanner mBluetoothLeScanner;
    BluetoothManager bluetoothManager;
    private boolean isConnecting;
    private BluetoothDevice mCurrentScanDevice;
    private int mTempDeviceNumber;
    private OnCenterCallback mCallback;
    private Handler mHandler = new Handler();

    private ArrayList<ScanResult> mScanResultArrayList = new ArrayList<>();


    public CenterManager(Context context) {
        mContext = context;
        bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothLeScanner = bluetoothManager.getAdapter().getBluetoothLeScanner();

        initialize();

        mConnectCountTimer = new CountDownTimer(10000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                //                Log.d(TAG, "mConnectCountTimer tick");
                if (mConnectionState == STATE_CONNECTED || mConnectionState == STATE_DISCONNECTED) {
                    Log.d(TAG, "mConnectCountTimer tick cancel");
                    mConnectCountTimer.cancel();
                }
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "center连接超时");
                //                sendBleBroadCast(ACTION_GATT_CONNECT_TIME_OUT);
                isConnecting = false;
                if (mCallback != null)
                    mCallback.onConnectFailed(STATE_CONNECT_TIME_OUT);
            }
        };

        mFindServiceTimer = new CountDownTimer(10000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                //                Log.d(TAG, "mConnectCountTimer tick");
                if (mFindServiceState == FIND_SERVICE_STATE_SUCCESS || mFindServiceState == FIND_SERVICE_STATE_FAILED) {
                    Log.d(TAG, "mFindServiceTimer tick cancel");
                    mFindServiceTimer.cancel();
                }
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "center连接超时");
                //                sendBleBroadCast(ACTION_GATT_CONNECT_TIME_OUT);
                isConnecting = false;
                if (mCallback != null)
                    mCallback.onGattServiceDiscoveredFailed(FIND_SERVICE_STATE_TIME_OUT);
            }
        };

    }


    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d(TAG, "### onConnectionStateChange status = " + status + " new status = " + newState);
            String intentAction;
            mConnectCountTimer.cancel();
            mFindServiceTimer.cancel();
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                Log.d(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                mFindServiceState = FIND_SERVICE_STATE_START;
                mFindServiceTimer.start();
                if (mCallback != null)
                    mCallback.onConnectSuccess(gatt);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.d(TAG, "Disconnected from GATT server.");
                //                sendBleBroadCast(intentAction);
                if (mCallback != null)
                    mCallback.onConnectFailed(STATE_DISCONNECTED);
            } else {
                if (mCallback != null)
                    mCallback.onConnectFailed(newState);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(TAG, "onServicesDiscovered status = " + status);
            mFindServiceTimer.cancel();
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onServicesDiscovered success = " + status);
                isConnecting = false;
                mFindServiceState = FIND_SERVICE_STATE_SUCCESS;
                if (mCallback != null)
                    mCallback.onGattServiceDiscovered(gatt);
            } else {
                mFindServiceState = FIND_SERVICE_STATE_FAILED;
                Log.w(TAG, "onServicesDiscovered failed: " + status);
                //                sendBleBroadCast(ACTION_GATT_SERVICES_DISCOVERED);
                if (mCallback != null)
                    mCallback.onGattServiceDiscoveredFailed(FIND_SERVICE_STATE_FAILED);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG, "onCharacteristicRead");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                sendBleBroadCast(ACTION_DATA_AVAILABLE, "");
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            //中心收到消息
            String receiveData = HexConverter.bytesToHexString(characteristic.getValue());
            Log.d(TAG, "onCharacteristicChanged = " + receiveData);
            if (mCallback != null) {
                mCallback.onCenterReceiveData(receiveData);
            }
            //            sendBleBroadCast(ACTION_RECEIVE_DATA, receiveData);
            //            sendBleBroadCast(ACTION_DATA_AVAILABLE, receiveData);
            //            ((BleMainActivity) mContext).runOnUiThread(new Runnable() {
            //                @Override
            //                public void run() {
            //                    CommonToast.toast(mContext, "中心收到消息了");
            //                }
            //            });
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            //发送消息结果通知
            try {
                String writeValue = HexConverter.bytesToHexString(descriptor.getValue());
                Log.d(TAG, "onDescriptorWrite state = " + status + " data = " + writeValue);
            } catch (Exception e) {
                Log.d("TAG", "");
            }
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (mCallback != null)
                    mCallback.onWriteDataSuccess();
            } else {
                if (mCallback != null)
                    mCallback.onWriteDataFailed(status);
            }

            //            sendBleBroadCast(ACTION_WRITE_DATA_RESULT, "" + status);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Log.d(TAG, "onDescriptorRead");
        }
    };

    private void sendBleBroadCast(final String action) {
        final Intent intent = new Intent(action);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        //        sendBroadcast(intent);
    }

    private void sendBleBroadCast(final String action, String data) {
        final Intent intent = new Intent(action);
        intent.putExtra("value", data);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    private void sendBleBroadCast(final String action, ScanResult scanResult) {
        final Intent intent = new Intent(action);
        intent.putExtra("key_ble_scan_result", scanResult);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public CenterManager getService() {
            return CenterManager.this;
        }
    }

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(@NonNull final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        isConnecting = true;
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            isConnecting = false;
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
        //        sendBleBroadCast(ACTION_GATT_CONNECTTING);
        Log.d(TAG, "Trying to create a new connection.");
        mConnectCountTimer.start();
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        Log.d(TAG, "disconnect");
        isConnecting = false;
        if (!isBluetoothOpened()) {
            Log.d(TAG, "bluetooth not open");
            return;
        }
        if (mBluetoothGatt == null) {
            //            Log.w(TAG, "BluetoothAdapter not initialized");
            Log.w(TAG, "mBluetoothGatt == null");
            return;
        }
        mBluetoothGatt.disconnect();
        if (mBluetoothGatt != null) {
            refreshDeviceCache(mBluetoothGatt);
        }
    }

    public boolean refreshDeviceCache(BluetoothGatt bluetoothGatt) {
        try {
            final Method refresh = BluetoothGatt.class.getMethod("refresh");
            if (refresh != null) {
                final boolean success = (Boolean) refresh.invoke(bluetoothGatt);
                Log.d(TAG, "refreshDeviceCache, is success:  " + success);
                return success;
            }
        } catch (Exception e) {
            Log.d(TAG, "exception occur while refreshing device: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        Log.d(TAG, "close");
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled, byte[] value) {

        if (mBluetoothAdapter == null) {
            Log.d(TAG, "BluetoothAdapter not initialized");
            if (mCallback != null) {
                mCallback.onWriteDataFailed(WRITE_DATA_RESULT_FAILED_ADAPTER_NOT_INITIALIZED);
            }
            return;
        }

        //        boolean mtuResult = mBluetoothGatt.requestMtu(24);
        //        Log.d(TAG, "### mtuResult = " + mtuResult);
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        // This is specific to Heart Rate Measurement.
        //        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CssConstant.SINGLE_TICKET.UUID_CHARACTERISTIC_DATA_SHARE);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        Log.d(TAG, "### UUID = " + characteristic.getUuid());
        descriptor.setValue(value);
        final boolean result = mBluetoothGatt.writeDescriptor(descriptor);
        Log.d(TAG, "writeDescriptor result = " + result);
        if (!result) {
            if (mCallback != null) {
                mCallback.onWriteDataFailed(WRITE_DATA_RESULT_FAILED_UNKOWN);
            }
        }
        //        characteristic.setValue(value);
        //        boolean chracterResult = mBluetoothGatt.writeCharacteristic(characteristic);
        //        Utils.runOnUiThread(new Runnable() {
        //            @Override
        //            public void run() {
        //
        //            }
        //        });
        //        }

    }

    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null)
            return null;

        return mBluetoothGatt.getServices();
    }


    public void writeCustomData(UUID serviceUUID, byte[] data) {
        if (mBluetoothGatt != null) {
            BluetoothGattService service = mBluetoothGatt.getService(serviceUUID);
            if (service != null) {
                final BluetoothGattCharacteristic characteristic = service.getCharacteristic(CssConstant.SINGLE_TICKET.UUID_CHARACTERISTIC_DATA_SHARE);
                //                characteristic.setValue(HexConverter.hexStringToBytes(data));
                setCharacteristicNotification(characteristic, true, data);
            } else {
                Log.d(TAG, "writeCustomData Failed service is null");
                if (mCallback != null) {
                    mCallback.onWriteDataFailed(WRITE_DATA_RESULT_FAILED_SERVICE_IS_NULL);
                }
            }
        } else {
            Log.d(TAG, "writeCustomData Failed is null");
            if (mCallback != null) {
                mCallback.onWriteDataFailed(WRITE_DATA_RESULT_FAILED_GATT_IS_NULL);
            }
        }
        //        Log.d(TAG, "serviceUUID = " + serviceUUID.toString());

    }

    ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.d(TAG, "onScanResult");
            mScanResultArrayList.add(result);

            //            if (mCallback != null)
            //                mCallback.onFindTargetDevice(result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.d(TAG, " onBatchScanResults " + results.size());
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.d(TAG, " onScanFailed " + errorCode);
            if (mCallback != null)
                mCallback.onScanFailed(errorCode);
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startScanLeDevice(UUID uuid, long scanTime) {
        Log.d(TAG, "### start scan " + uuid.toString());
        if (mBluetoothAdapter != null && mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON && mBluetoothLeScanner != null) {
            mScanResultArrayList.clear();
            mHandler.postDelayed(mStopScanRunnable, scanTime);
            List<ScanFilter> bleScanFilters = new ArrayList<>();
            //            for (UUID uuid : uuidList) {
            bleScanFilters.add(new ScanFilter.Builder().setServiceUuid(new ParcelUuid(uuid)).build());
            //            }
            ScanSettings bleScanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setReportDelay(0).build();
            mBluetoothLeScanner.startScan(bleScanFilters, bleScanSettings, mScanCallback);
        } else {
            Log.d(TAG, "blue tooth is not open or mBluetoothLeScanner is null = " + (mBluetoothLeScanner != null));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void stopScan() {
        Log.d(TAG, "stopScan");
        isConnecting = false;
        mHandler.removeCallbacks(mStopScanRunnable);
        if (!isBluetoothOpened()) {
            Log.d(TAG, "bluetooth not open");
            return;
        }
        if (mBluetoothAdapter != null && mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
            mBluetoothLeScanner.stopScan(mScanCallback);
        }
    }

    public BluetoothDevice getFindDevice() {
        return mCurrentScanDevice;
    }

    private Runnable mStopScanRunnable = new Runnable() {
        @Override
        public void run() {
            if (isBluetoothOpened())
                mBluetoothLeScanner.stopScan(mScanCallback);
            if (mCallback != null)
                mCallback.onScanComplete(mScanResultArrayList);
        }
    };


    public void startScanOldApi() {
        UUID[] uuids = new UUID[]{CssConstant.SINGLE_TICKET.UUID_SERVICE_ENTRY_GATE};
        //        sendBleBroadCast(ACTION_START_SCAN);
        //        stopScanOldApi();
        mBluetoothAdapter.startLeScan(uuids, mLeScanCallback);
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            //            stopScanOldApi();
            sendBleBroadCast(ACTION_FIND_TARGET_DEVICE, "111111");
        }
    };


    public interface OnCenterCallback {

        void onStartScan();

        void onScanComplete(ArrayList<ScanResult> list);

        void onScanFailed(int code);

        //        void onFindTargetDevice(ScanResult scanResult);

        void onGattServiceDiscovered(BluetoothGatt gatt);

        void onGattServiceDiscoveredFailed(int code);

        void onConnectSuccess(BluetoothGatt gatt);

        void onConnectFailed(int code);

        void onWriteDataSuccess();

        void onWriteDataFailed(int code);

        void onCenterReceiveData(String data);

    }


    public void setOnCenterCallback(OnCenterCallback callback) {
        mCallback = callback;
    }

    private boolean isBluetoothOpened() {
        if (mBluetoothAdapter != null && mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
            return true;
        }
        return false;
    }
}

