package com.hack.iqmonitor;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.hack.iqmonitor.db.AppDetails;
import com.hack.iqmonitor.db.DataBaseHelper;

public class LauncherAppUtil {

	DataBaseHelper dataBaseHelper;
	Context context;
	PackageManager mPackageManager;
	List<LauncherAppList> launcherList;

	public LauncherAppUtil(Context context) {
		this.context = context;
		dataBaseHelper = new DataBaseHelper(context);
		mPackageManager = context.getPackageManager();
		launcherList = GetLauncherApps();
	}

	public List<LauncherAppList> getAppDetailsAdd() {
		List<LauncherAppList> appLists = new ArrayList<LauncherAppList>();
		List<AppDetails> details = dataBaseHelper.getAllEntries();
		for (AppDetails appDetails : details) {
			Log.v(null, appDetails.getAppName());
		}
		boolean matchFound = false;
		for (LauncherAppList appList : this.launcherList) {
			matchFound = false;
			if (details.isEmpty()) {
				appLists.add(appList);
				continue;
			}
			for (AppDetails appDetails : details) {
				if (appList.getPackageName().trim()
						.equalsIgnoreCase(appDetails.getPackageName().trim())) {
					matchFound = true;
					break;
				}
			}
			if (!matchFound) {
				appLists.add(appList);
				matchFound = false;
			}
		}
		return appLists;
	}

	public List<LauncherAppList> getAppDetailsAdded() {
		List<LauncherAppList> appLists = new ArrayList<LauncherAppList>();
		List<AppDetails> details = dataBaseHelper.getAllEntries();
		for (AppDetails appDetails : details) {
			for (LauncherAppList appList : this.launcherList) {
				if (appList.getPackageName().equalsIgnoreCase(
						appDetails.getPackageName())) {
					//Log.e("timeee", " " + appDetails.getUsageTime() ) ;
					appList.setUsageTime(appDetails.getUsageTime()) ;
					appList.setLimitUsageTime(appDetails.getLimitUsageTime());
					appList.setId(appDetails.getId()) ;
					appLists.add(appList);
					
					break;
				}
			}

		}
		return appLists;
	}

	public List<LauncherAppList> GetLauncherApps() {
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> mLauncherApps = mPackageManager
				.queryIntentActivities(mainIntent, 0);
		PackageInfo mPackageInfo = null;
		List<LauncherAppList> appLists = new ArrayList<LauncherAppList>();
		LauncherAppList list;
		for (ResolveInfo tempInfo : mLauncherApps) {
			try {
				list = new LauncherAppList();
				mPackageInfo = mPackageManager.getPackageInfo(
						tempInfo.activityInfo.packageName, 0);
				list.setAppName(mPackageInfo.applicationInfo.loadLabel(
						mPackageManager).toString());
				list.setPackageName(tempInfo.activityInfo.packageName);
				list.setIcon(mPackageInfo.applicationInfo
						.loadIcon(mPackageManager));
				appLists.add(list);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return appLists;
	}
}
