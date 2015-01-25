package com.hack.iqmonitor.db;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
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

	public void updateUsageTime(AppDetails appDetails,final long currentUsageTime) {
		RuntimeExceptionDao<AppDetails, Integer> dao = getSimpleDataDao();
		UpdateBuilder<AppDetails, Integer> updateBuilder = dao.updateBuilder();
		Log.e("time"," " + (appDetails.getUsageTime() + currentUsageTime)) ;
		appDetails.setUsageTime(appDetails.getUsageTime() + currentUsageTime);
		appDetails.setTimeStamp(System.currentTimeMillis());
		try {
			updateBuilder.updateColumnValue("usageTime",
					appDetails.getUsageTime());
			updateBuilder.updateColumnValue("timeStamp",
					appDetails.getTimeStamp());
			updateBuilder.where().eq("id", appDetails.getId());
		//	Log.e("id", "" +appDetails.getId()) ;
			
			updateBuilder.update() ;
			
			//Log.e("updt", "" + updateBuilder.update());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void updateLimitUsageTime(AppDetails appDetails) {
		RuntimeExceptionDao<AppDetails, Integer> dao = getSimpleDataDao();
		UpdateBuilder<AppDetails, Integer> updateBuilder = dao.updateBuilder();
	//	Log.e("time"," " + (appDetails.getUsageTime() + currentUsageTime)) ;
	//	appDetails.setUsageTime(appDetails.getUsageTime() + currentUsageTime);
		appDetails.setTimeStamp(System.currentTimeMillis());
		try {
			updateBuilder.updateColumnValue("limitUsageTime",
					appDetails.getLimitUsageTime());
			updateBuilder.updateColumnValue("timeStamp",
					appDetails.getTimeStamp());
			updateBuilder.where().eq("id", appDetails.getId());
			//Log.e("id", "" +appDetails.getId()) ;
			
			updateBuilder.update() ;
			
			//Log.e("updt", "" + updateBuilder.update());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateLimitUsageTimeAll(long seconds) {
		
		Log.e("secall", " " + seconds) ;
		RuntimeExceptionDao<AppDetails, Integer> dao = getSimpleDataDao();
		UpdateBuilder<AppDetails, Integer> updateBuilder = dao.updateBuilder();
	//	Log.e("time"," " + (appDetails.getUsageTime() + currentUsageTime)) ;
	//	appDetails.setUsageTime(appDetails.getUsageTime() + currentUsageTime);
		
		List<AppDetails> allAddedApps = getAllEntries() ;
		
		for (AppDetails appDetails : allAddedApps) {
			appDetails.setLimitUsageTime(seconds) ;
			appDetails.setTimeStamp(System.currentTimeMillis());
			try {
				updateBuilder.updateColumnValue("limitUsageTime",
						appDetails.getLimitUsageTime());
				updateBuilder.updateColumnValue("timeStamp",
						appDetails.getTimeStamp());
			
				updateBuilder.where().eq("id", appDetails.getId());
				
				updateBuilder.update() ;
				
				Log.e("updt", "" + updateBuilder.update());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	

	public void clearYestrdayValues() {
		// TODO Auto-generated method stub
		RuntimeExceptionDao<AppDetails, Integer> dao = getSimpleDataDao();
		UpdateBuilder<AppDetails, Integer> updateBuilder = dao.updateBuilder();
		List<AppDetails> appDetails = getAllEntries();
		for (AppDetails appDetails2 : appDetails) {
			appDetails2.setCancellationId(0);
			appDetails2.setSessionId(0);
			appDetails2.setTimeStamp(System.currentTimeMillis());
			appDetails2.setUsageTime(0);
			try {
				updateBuilder.updateColumnValue("usageTime",
						appDetails2.getUsageTime());
				updateBuilder.updateColumnValue("timeStamp",
						appDetails2.getTimeStamp());
				updateBuilder.updateColumnValue("cancellationId",
						appDetails2.getCancellationId());
				updateBuilder.updateColumnValue("sessionId",
						appDetails2.getSessionId());
				updateBuilder.where().eq("id", appDetails2.getId());
				updateBuilder.update();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateSessionId(AppDetails appDetails) {
		
		//Log.e("secall", " " + seconds) ;
		RuntimeExceptionDao<AppDetails, Integer> dao = getSimpleDataDao();
		UpdateBuilder<AppDetails, Integer> updateBuilder = dao.updateBuilder();
	//	Log.e("time"," " + (appDetails.getUsageTime() + currentUsageTime)) ;
	//	appDetails.setUsageTime(appDetails.getUsageTime() + currentUsageTime);
		
		//List<AppDetails> allAddedApps = getAllEntries() ;
		
	
			appDetails.setTimeStamp(System.currentTimeMillis());
			try {
				updateBuilder.updateColumnValue("sessionId",
						appDetails.getSessionId());
				updateBuilder.updateColumnValue("timeStamp",
						appDetails.getTimeStamp());
			
				updateBuilder.where().eq("id", appDetails.getId());
				
				updateBuilder.update() ;
				
				//Log.e("updt", "" + updateBuilder.update());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		
	}
}
