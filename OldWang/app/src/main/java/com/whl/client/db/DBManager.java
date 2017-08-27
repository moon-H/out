package com.whl.client.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.whl.framework.utils.MLog;
import com.whl.client.gateway.model.wristband.Device;

public class DBManager {
    private static final String TAG = "DBManager";

    private Context context;

    public static final String COLUMN_DEVICE_ID = "DEVICE_ID";
    public static final String COLUMN_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String COLUMN_DEVICE_DEFAULT_LAUNCH = "DEVICE_DEFAULT_LAUNCH";


    public DBManager(Context context) {
        this.context = context;
    }
    public String getCreateTableSql(String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append(tableName).append(" (")
            .append(COLUMN_DEVICE_ID).append(" VARCHAR PRIMARY KEY,")
            .append(COLUMN_DEVICE_ADDRESS).append(" TEXT,")
            .append(COLUMN_DEVICE_DEFAULT_LAUNCH).append(" VARCHAR)");
        MLog.d(TAG, "Create DB: " + sb.toString());
        return sb.toString();
    }
    public StringBuilder getSelectBluetoothDeviceSQL(String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
            .append(COLUMN_DEVICE_ID).append(",")
            .append(COLUMN_DEVICE_ADDRESS).append(",")
            .append(COLUMN_DEVICE_DEFAULT_LAUNCH).append(" FROM ")
            .append(tableName);
        return sb;
    }
    public ContentValues getBluetoothDeviceContentValues(Device mDevice) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DEVICE_ID, mDevice.getName());
        values.put(COLUMN_DEVICE_ADDRESS, mDevice.getAddress());
        values.put(COLUMN_DEVICE_DEFAULT_LAUNCH, mDevice.getDefaultLanuch());
        return values;
    }

    public Device buildBluetoothDevice(Cursor c) {
    	Device mDevice = new Device(c.getString(0),c.getString(1),c.getString(2));
        MLog.d(TAG, "id = " + mDevice.getName() + " address = " + mDevice.getAddress() + " defaultLanuch = " + mDevice.getDefaultLanuch());
        return mDevice;
    }
    public String getDropTableSql(String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE IF EXISTS ").append(tableName);
        MLog.d(TAG, "Drop table: " + sb.toString());
        return sb.toString();
    }
}
