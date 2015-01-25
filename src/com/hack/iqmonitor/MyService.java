package com.hack.iqmonitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.hack.iqmonitor.db.AppDetails;
import com.hack.iqmonitor.db.DataBaseHelper;

public class MyService extends Service {

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
	Context context;
//	boolean activityStarted = false ;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		context = this;
		super.onCreate();
		dataBaseHelper = new DataBaseHelper(this);
		mPackageManager = getPackageManager();
		mHandler = new Handler();
		mActivity = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		GetLauncherApps();
		mHandler.postDelayed(mRunnable, 100);
	}

	boolean timerStarted = false;
	String mPreviousBlocApp = null;
	Runnable mRunnable = new Runnable() {
		PackageInfo mPackageInfo;
		boolean oneTimeSwitch = true;

		@Override
		public void run() {
			List<ActivityManager.RunningAppProcessInfo> info = mActivity
					.getRunningAppProcesses();
			// Log.v(null, " " +info.get(0).processName);
			clearYesterdayValues();
			mForeGroundPackageName = info.get(0).processName;
			try {
				if (mForeGroundPackageName.indexOf(":") != -1) {
					mForeGroundPackageName = mForeGroundPackageName.substring(
							0, mForeGroundPackageName.indexOf(":"));
				}
				mPackageInfo = mPackageManager.getPackageInfo(
						mForeGroundPackageName, 0);

				// mForeGroundRunningName = mPackageInfo.applicationInfo
				// .loadLabel(mPackageManager).toString();
				UpdateLimitList();
				GetLauncherApps();
				for (int i = 0; i < mLimitList.size(); i++) {
					if (mLimitList.get(i).getPackageName()
							.equalsIgnoreCase(mForeGroundPackageName)) {
						long limitTime = mLimitList.get(i).getLimitUsageTime();
						long usageTime = mLimitList.get(i).getUsageTime() / 1000;
						int sessionId = (int) mLimitList.get(i).getSessionId();

//						Log.e("prev", " " + mPrevOpenPackageName + " "
//								+ mForeGroundPackageName + "  " + limitTime
//								+ "   " + usageTime);

						if (usageTime > limitTime) {
							// Log.v(null, "Usage Limit exceed by limit");
							// Usage limit exceeds
							int noOfQuestions = (int) (usageTime / limitTime);
							// noOfQuestions = noOfMainIntervals ;

							// ////////////////////////////////////////// change
							// sub interval here = 5
							int noOfSubIntervals = (int) ((usageTime - (limitTime)) / (1 * 60));
						//	Log.v(null, " " + noOfSubIntervals + " " + sessionId);
							if (noOfSubIntervals >= sessionId) {
								
								
								sessionId = noOfSubIntervals+1;
								mLimitList.get(i).setSessionId(sessionId);
								new DataBaseHelper(context)
										.updateSessionId(mLimitList.get(i));
							//	Log.v(null, "Usage Limit exceeds");
								Intent  intent  = new Intent(context, IqActivity.class) ;
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
								startActivity(intent) ;
								Toast.makeText(context, "IQED",
										Toast.LENGTH_LONG).show();
							}

						}
					}
				}

				if (oneTimeSwitch) {
					mPrevOpenPackageName = mForeGroundPackageName;
					oneTimeSwitch = false;
				}

				// Log.e("prev", " " + mPrevOpenPackageName + " "
				// + mForeGroundPackageName);
				if (!mForeGroundPackageName
						.equalsIgnoreCase(mPrevOpenPackageName)) {
					if (timerStarted) {
//						for (int i = 0; i < mMonitorAppName.size(); i++) {
//							if (mMonitorAppNamel.get(i).getPackageName()
//									.equalsIgnoreCase(mPrevOpenPackageName)) {
//								Log.v(null, "Updated");
//								UpdateTime(mMonitorAppName.get(i),
//										(endTime - startTime));
//							}
//
//						}
						timerStarted = false;
					}
					mPrevOpenPackageName = mForeGroundPackageName;
					startTime = System.currentTimeMillis();
					timerStarted = true;
				} else {
					endTime = System.currentTimeMillis();
					for (int i = 0; i < mMonitorAppName.size(); i++) {
						if (mMonitorAppName.get(i).getPackageName()
								.equalsIgnoreCase(mPrevOpenPackageName)) {
							Log.v(null, "Updated");
							UpdateTime(mMonitorAppName.get(i),
									(endTime - startTime));
							startTime = System.currentTimeMillis();
						}

					}
				}

			} catch (NameNotFoundException e) {
				e.printStackTrace();

				mPrevOpenPackageName = "";
				if (mPrevOpenPackageName.isEmpty()) {
					endTime = System.currentTimeMillis();
					mPrevOpenPackageName = "b";
				}

			}
			mHandler.postDelayed(mRunnable, 100);
		}
	};

	public void GetLauncherApps() {
		mMonitorAppName = new ArrayList<AppDetails>();
		mMonitorAppName = dataBaseHelper.getAllEntries();
	}

	protected void clearYesterdayValues() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = (SharedPreferences) this
				.getSharedPreferences("clear", Context.MODE_PRIVATE);
		if (preferences.getString("date", "error").equalsIgnoreCase("error")) {
			Log.e("shit", "getting zero");
			preferences
					.edit()
					.putString(
							"date",
							new SimpleDateFormat("dd-MMM-yyyy")
									.format(new Date())).commit();
		}
		String sharedDate = preferences.getString("date", "error");
		try {
			Date date = new SimpleDateFormat("dd-MMM-yyyy").parse(sharedDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			Calendar currentDate = Calendar.getInstance();
			if (cal.compareTo(currentDate) > 0) {
				Log.e("shit", "getting zero");
				preferences
						.edit()
						.putString(
								"date",
								new SimpleDateFormat("dd-MMM-yyyy")
										.format(new Date())).commit();
				dataBaseHelper.clearYestrdayValues();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void UpdateTime(AppDetails appDetails, long currentUsageTime) {
		// TODO Auto-generated method stub
		long second = (currentUsageTime);
		dataBaseHelper.updateUsageTime(appDetails, second);
		Log.v(null, "Time " + second);
	}

	ArrayList<AppDetails> mUsageApp;

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
}
