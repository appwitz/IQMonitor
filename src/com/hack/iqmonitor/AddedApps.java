package com.hack.iqmonitor;

import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.doomonafireball.betterpickers.hmspicker.HmsPickerBuilder;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerDialogFragment.HmsPickerDialogHandler;
import com.hack.iqmonitor.db.AppDetails;
import com.hack.iqmonitor.db.DataBaseHelper;

public class AddedApps extends Fragment implements OnItemClickListener {

	ListView listView;
	List<LauncherAppList> list;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.added_fragment_layout, container,
				false);

		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

		listView = (ListView) view.findViewById(R.id.listAddedApps);

		listView.setOnItemClickListener(this);
		new Background().execute();
		super.onViewCreated(view, savedInstanceState);

		super.onViewCreated(view, savedInstanceState);
	}

	private class Background extends
			AsyncTask<Void, Void, List<LauncherAppList>> {

		ProgressDialog dialog;

		@Override
		protected List<LauncherAppList> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			list = new LauncherAppUtil(getActivity()).getAppDetailsAdded();
			return list;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage("Loading....");
			dialog.show();
		}

		@Override
		protected void onPostExecute(List<LauncherAppList> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			listView.setAdapter(new AddedAppsBaseAdapter(result,
					getActivity()));
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

		
		
		final int pos = arg2;

		HmsPickerBuilder hpb = new HmsPickerBuilder()
				.setFragmentManager(getActivity().getSupportFragmentManager())
				.setStyleResId(R.style.BetterPickersDialogFragment)
				.setTargetFragment(this);

		//Log.e("timeeeeeeee1234", " " + list.get(arg2).getLimitUsageTime()) ;
		
		long seconds = list.get(arg2).getLimitUsageTime() ;
		int hr = (int) (seconds / 3600) ;
		int min = (int) ((seconds - (hr * 3600)) / 60) ;
		int min1 = min / 10 ;
		int min2 = min %10 ;
		
		
		Log.e("ti12345", " " + seconds ) ;
		
		hpb.show("crap", hr,min1, min2);

		hpb.addHmsPickerDialogHandler(new HmsPickerDialogHandler() {

			@Override
			public void onDialogHmsSet(int reference, int hours, int minutes) {
				// TODO Auto-generated method stub
//				Toast.makeText(getActivity(), "yip6546156", Toast.LENGTH_LONG)
//						.show();

				LauncherAppList launcherAppList = list.get(pos);
				AppDetails appDetails = new AppDetails();
				
				appDetails.setAppName(launcherAppList.getAppName());
				appDetails.setId(launcherAppList.getId()) ;
				appDetails.setCancellationId(0);
				appDetails.setCategory("Social");

				//Log.e("timefrompick", " " + hours + " " + launcherAppList.getUsageTime());
				if (hours == 0 && minutes == 0) {
					appDetails.setLimitUsageTime(launcherAppList.getLimitUsageTime());
				} else {
					//Log.e("timecdscdc ", " " + hours + " " + minutes);
					appDetails.setLimitUsageTime((minutes * 60)
							+ (hours * 3600));
				}

				appDetails.setPackageName(launcherAppList.getPackageName());
				//appDetails.setSessionId(0);
				appDetails.setTimeStamp(System.currentTimeMillis());
				//appDetails.setUsageTime(0);
				
				//// code to update the time
				
				new DataBaseHelper(getActivity()).updateLimitUsageTime(appDetails);
				
				new Background().execute();
				
//				Toast.makeText(getActivity(), "Added", Toast.LENGTH_LONG)
//						.show();
			

			}

			@Override
			public void onDialogCancel() {
				// TODO Auto-generated method stub
//				Toast.makeText(getActivity(), "yip", Toast.LENGTH_LONG).show();
				
				new DataBaseHelper(getActivity()).delete(list.get(pos)) ;
				
				new Background().execute();

				
			}
		});

	}

}