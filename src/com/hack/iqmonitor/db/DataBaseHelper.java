package com.hack.iqmonitor.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Customer";
	private RuntimeExceptionDao<AppDetails, Integer> DemoORMLiteRuntimeDao = null;

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			Log.i(DataBaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, AppDetails.class);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
			Log.e(DataBaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		try {
			Log.i(DataBaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, AppDetails.class, true);
			onCreate(arg0, connectionSource);
		} catch (SQLException e) {
			Log.e(DataBaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() {
		super.close();
	}

	public void insert(AppDetails database) {
		RuntimeExceptionDao<AppDetails, Integer> dao = getSimpleDataDao();
		dao.create(database);
	}

	private RuntimeExceptionDao<AppDetails, Integer> getSimpleDataDao() {
		if (DemoORMLiteRuntimeDao == null) {
			DemoORMLiteRuntimeDao = getRuntimeExceptionDao(AppDetails.class);
		}
		return DemoORMLiteRuntimeDao;
	}

	public void delete(AppDetails database) {
		RuntimeExceptionDao<AppDetails, Integer> dao = getSimpleDataDao();
		DeleteBuilder<AppDetails, Integer> deleteBuilder = dao.deleteBuilder();
		try {
			deleteBuilder.where().eq("id", database.getId());
			deleteBuilder.delete();
		} catch (Exception e) {
			Log.e(DataBaseHelper.class.getName(), "Can't drop databases", e);
		}
	}

	public List<AppDetails> getAllEntries() {
		RuntimeExceptionDao<AppDetails, Integer> dao = getSimpleDataDao();
		return dao.queryForAll();
	}

	
}
