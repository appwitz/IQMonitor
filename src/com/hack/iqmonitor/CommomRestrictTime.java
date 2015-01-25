package com.hack.iqmonitor;

import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doomonafireball.betterpickers.hmspicker.HmsPickerBuilder;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerDialogFragment.HmsPickerDialogHandler;
import com.hack.iqmonitor.db.DataBaseHelper;

public class CommomRestrictTime extends Fragment {

	List<LauncherAppList> list;
	HmsPickerBuilder hpb;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.add_apps_fragment_layout,
				container, false);

		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

		new Background().execute();

		hpb = new HmsPickerBuilder()
				.setFragmentManager(getActivity().getSupportFragmentManager())
				.setStyleResId(R.style.BetterPickersDialogFragment)
				.setTargetFragment(this);
		hpb.show("crap", 0, 0, 0);

		hpb.addHmsPickerDialogHandler(new HmsPickerDialogHandler() {

			@Override
			public void onDialogHmsSet(int reference, int hours, int minutes) {

				long seconds = 3600 * hours + 60 * minutes;
				if (hours == 0 && minutes == 0) {
					seconds = 15 * 60;

				}

				new Background2(seconds).execute();
			}

			@Override
			public void onDialogCancel() {

			}
		});

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

			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
		}

	}

	private class Background2 extends
			AsyncTask<Void, Void, Void> {

		ProgressDialog dialog;
		long seconds ;

		public Background2(long seconds) {
			this.seconds = seconds ;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			new DataBaseHelper(getActivity()).updateLimitUsageTimeAll(seconds) ;
			
			return null;
			
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
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			Fragment fragment = new AddApps() ;
			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit() ;
		}

	}

}
