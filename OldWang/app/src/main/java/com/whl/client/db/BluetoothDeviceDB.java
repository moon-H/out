package com.whl.client.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.whl.framework.db.BaseSQLiteOpenHelper;
import com.whl.framework.utils.MLog;
import com.whl.client.gateway.model.wristband.Device;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class BluetoothDeviceDB extends BaseSQLiteOpenHelper {
    private static final String TAG = "BluetoothDeviceDB";
    private static final String DB_TABLE_NAME = "bluetoothDevice";
    private static final int DB_VERSION = 1;
    private DBManager dbManager;

    public BluetoothDeviceDB(Context context) {
        super(context, DB_TABLE_NAME, null, DB_VERSION);
        dbManager = new DBManager(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        MLog.d(TAG, "onCreate");
        db.execSQL(dbManager.getCreateTableSql(DB_TABLE_NAME));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MLog.d(TAG, "onUpgrade");
        db.execSQL(dbManager.getDropTableSql(DB_TABLE_NAME));
        onCreate(db);
    }

    public synchronized List<Device> getDeviceList() {
        MLog.d(TAG, "getDeviceList");
        ArrayList<Device> mDeviceList = new ArrayList<Device>();
        mDeviceList.addAll(DataSupport.findAll(Device.class));
        MLog.d(TAG, "getDeviceList = " + mDeviceList.size());
        return mDeviceList;
    }


    public Device getDeviceByName(String deviceName) {
        MLog.d(TAG, "getDeviceByName : " + deviceName);
        ArrayList<Device> mDeviceList = new ArrayList<Device>();
        mDeviceList.addAll(DataSupport.where("name LIKE ?", "%" + deviceName + "%").find(Device.class));
        if (mDeviceList != null && mDeviceList.size() > 0) {
            return mDeviceList.get(0);
        } else
            return null;
    }

    public Device getDeviceDefault() {
        MLog.d(TAG, "getDeviceDefault ");
        ArrayList<Device> mDeviceList = new ArrayList<Device>();
        mDeviceList.addAll(DataSupport.where("defaultLanuch = ?", "Y").find(Device.class));
        if (mDeviceList != null && mDeviceList.size() > 0) {
            return mDeviceList.get(0);
        } else
            return null;
    }

    public Device getDevice(String name, String address) {
        MLog.d(TAG, "getDevice : " + name + "  address = " + address);
        ArrayList<Device> mDeviceList = new ArrayList<Device>();
        mDeviceList.addAll(DataSupport.where("name LIKE ? and address = ?", "%" + name + "%", address).find
                (Device.class));
        if (mDeviceList != null && mDeviceList.size() > 0) {
            return mDeviceList.get(0);
        } else
            return null;
    }

    public synchronized boolean insertDevice(Device device) {
        MLog.d(TAG, "insertOrUpdateMyService: " + device.toString());
        Device d = getDeviceByName(device.getName());
        if ( d != null) {
            if(d.getAddress().equals(device.getAddress())){
                MLog.e(TAG, "This service is already exist!");
                return false;
            }else {
                removeDevice(device.getName());
            }
        }
        boolean isSaved = device.save();
        MLog.d(TAG, "insert rows: " + isSaved);
        return isSaved;
    }

    public synchronized int updateDevice(Device device) {
        MLog.d(TAG, "updateDevice = " + device.toString());
        int rows = device.updateAll("name = ?", device.getName());
        MLog.d(TAG, "updateMyservice rows = " + rows);
        return rows;
    }

    public synchronized long setDeviceDefault(Device device) {
        MLog.d(TAG, "updateDevice = " + device.toString());
        if (getDeviceByName(device.getName()) == null) {
            return 0;
        } else {
            return updateDevice(device);
        }
    }

    public synchronized int removeDevice(String deviceName) {
        MLog.d(TAG, "removeDevice: device deviceName=" + deviceName);
        int rows = 0;
        Device d = getDeviceByName(deviceName);
        if(d!=null){
            rows= d.delete();
        }
        MLog.d(TAG, "removeDevice: rows=" + rows);
        return rows;
    }

    public synchronized int clearDevice() {
        MLog.d(TAG, "clearDevice");
        int rows = DataSupport.deleteAll(Device.class);
        MLog.d(TAG, "clearDevice rows = " + rows);
        return rows;
    }
}
