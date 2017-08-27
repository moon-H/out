
package com.whl.framework.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.whl.framework.utils.MLog;

import java.util.concurrent.locks.ReentrantLock;


public abstract class BaseSQLiteOpenHelper extends SQLiteOpenHelper {

	private static final String TAG = "BaseSQLiteOpenHelper";

	protected Context mContext;
	protected ReentrantLock lock = new ReentrantLock();

	protected SQLiteDatabase writableDB;
	protected SQLiteDatabase readableDB;

	public BaseSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		mContext = context;
	}

	protected long insert(String table, String nullColumnHack, ContentValues values) {
		long row = -1l;
		lock.lock();
		try {

			writableDB = getWritableDatabase();
			row = writableDB.insert(table, nullColumnHack, values);
			if (row == -1) {
				MLog.e(TAG, "insert: " + table + " insert fails!!");
			}
			return row;
		} catch (Exception e) {
			e.printStackTrace();
			return row;
		} finally {
			closeWritableDatabase();
			lock.unlock();
		}
	}

	protected int delete(String table, String whereClause, String[] whereArgs) {
		int rows = 0;
		lock.lock();
		try {

			writableDB = getWritableDatabase();
			rows = writableDB.delete(table, whereClause, whereArgs);
			return rows;
		} catch (Exception e) {
			MLog.e(TAG, "delete: " + table + " delete fails!!");
			e.printStackTrace();
			return rows;
		} finally {
			closeWritableDatabase();
			lock.unlock();
		}
	}

	protected int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		int rows = 0;
		lock.lock();
		try {

			writableDB = getWritableDatabase();
			rows = writableDB.update(table, values, whereClause, whereArgs);
			return rows;
		} catch (Exception e) {
			MLog.e(TAG, "update: " + table + " update fails!!");
			e.printStackTrace();
			return rows;
		} finally {
			closeWritableDatabase();
			lock.unlock();
		}
	}

	protected Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit) {
		Cursor c = null;
		try {
			readableDB = getReadableDatabase();
			c = readableDB.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
			return c;
		} catch (Exception e) {
			MLog.e(TAG, "query: " + table + " query fails!!");
			e.printStackTrace();
			return c;
		}
	}

	protected Cursor rawQuery(String sql, String[] selectionArgs) {
		Cursor c = null;
		try {
			readableDB = getReadableDatabase();
			c = readableDB.rawQuery(sql, selectionArgs);
			return c;
		} catch (Exception e) {
			MLog.e(TAG, "rawQuery: " + sql + " fails!!");
			e.printStackTrace();
			return c;
		}
	}

	private void closeWritableDatabase() {
		if (writableDB != null) {
			writableDB.close();
			writableDB = null;
			MLog.d(TAG, "closeWritableDatabase");
		}
	}

	protected void closeReadableDatabase() {
		if (readableDB != null) {
			readableDB.close();
			readableDB = null;
			MLog.d(TAG, "closeReadableDatabase");
		}
	}
}
