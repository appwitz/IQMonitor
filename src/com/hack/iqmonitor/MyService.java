package com.hack.iqmonitor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hack.iqmonitor.db.AppDetails;
import com.hack.iqmonitor.db.DataBaseHelper;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MyService extends Service {
	Button button;
	Handler mHandler;
	ActivityManager mActivity;
	RunningTaskInfo mRunningInfo;
	String mForeGroundPackageName;
	PackageManager mPackageManager;
	List<AppDetails> mMonitorAppName;
	long startTime = 0, endTime = 0;
	String mPrevOpenPackageName = null;
	public static boolean isActivtyOpen = false;
	DataBaseHelper dataBaseHelper;
	List<AppDetails> mLimitList;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		dataBaseHelper = new DataBaseHelper(this);
		mPackageManager = getPackageManager();
		mHandler = new Handler();
		mActivity = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		mHandler.postDelayed(mRunnable, 100);
	}

	boolean timerStarted = false;
	String mPreviousBlocApp = null;
	Runnable mRunnable = new Runnable() {
		PackageInfo mPackageInfo;
		boolean oneTimeSwitch = true;
		String mForeGroundRunningName;

		@Override
		public void run() {
			List<ActivityManager.RunningAppProcessInfo> info = mActivity
					.getRunningAppProcesses();
			// Log.v(null, " " +info.get(0).processName);
			mForeGroundPackageName = info.get(0).processName;
			try {
				mPackageInfo = mPackageManager.getPackageInfo(
						mForeGroundPackageName, 0);
				mForeGroundRunningName = mPackageInfo.applicationInfo
						.loadLabel(mPackageManager).toString();
				UpdateLimitList();
				for (int i = 0; i < mLimitList.size(); i++) {
					if (mLimitList.get(i).getPackageName()
							.equalsIgnoreCase(mForeGroundPackageName)) {
						long limitTime = mLimitList.get(i).getLimitUsageTime();
						long usageTime = mLimitList.get(i).getUsageTime();
						if (usageTime > limitTime) {
							Log.v(null, "Usage Limit exceeds");
							// Usage limit exceeds
						}
					}
				}
				if (oneTimeSwitch) {
					mPrevOpenPackageName = mForeGroundPackageName;
					oneTimeSwitch = false;
				}
				for (int i = 0; i < mMonitorAppName.size(); i++) {
					if (mForeGroundRunningName.equalsIgnoreCase(mMonitorAppName
							.get(i))
							&& (!mPrevOpenPackageName
									.equalsIgnoreCase(mForeGroundPackageName))) {
						if (timerStarted) {
							String day = new SimpleDateFormat("dd MMM yyyy")
									.format(new Date());
							UpdateTime(day, mPrevOpenPackageName,
									(endTime - startTime));
							timerStarted = false;
						}
						mPrevOpenPackageName = mForeGroundPackageName;
						startTime = System.currentTimeMillis();
						timerStarted = true;
					} else {
						endTime = System.currentTimeMillis();
					}
				}

			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mHandler.postDelayed(mRunnable, 100);
		}
	};

	public void GetLauncherApps() {
		mMonitorAppName = new ArrayList<AppDetails>();
		mMonitorAppName = dataBaseHelper.getAllEntries();
	}

	ArrayList<AppDetails> mUsageApp;

	protected void UpdateTime(long timeStamp, String mPrevOpenPackageName,
			long currentUsageTime) {
		// TODO Auto-generated method stub
		long second = (currentUsageTime / 1000);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mHandler.removeCallbacks(mRunnable);
	}

	public void Kill(String name) {
		ActivityManager mManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		mManager.killBackgroundProcesses(name);
	}

	public void UpdateLimitList() {
		mLimitList = dataBaseHelper.getAllEntries();
	}

	private class Application {
		String appName, mLimit;
	}
}
