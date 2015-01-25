package com.hack.iqmonitor;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AddAddedAppsBaseAdapter extends BaseAdapter {

	List<LauncherAppList> list;
	Holder holder;
	Context context;

	public AddAddedAppsBaseAdapter(List<LauncherAppList> list, Context context) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View rootView, ViewGroup group) {

		if (rootView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rootView = layoutInflater.inflate(R.layout.add_added_apps_item,
					group, false);

			holder = new Holder();

			holder.imageView = (ImageView) rootView
					.findViewById(R.id.imageView1);
			holder.textView = (TextView) rootView.findViewById(R.id.textView1);

			rootView.setTag(holder);
		} else {
			holder = (Holder) rootView.getTag();
		}

		holder.imageView.setImageDrawable(list.get(pos).getIcon());
		holder.textView.setText(list.get(pos).getAppName());

		return rootView;
	}

	static class Holder {
		public TextView textView;
		public ImageView imageView;

	}

}
