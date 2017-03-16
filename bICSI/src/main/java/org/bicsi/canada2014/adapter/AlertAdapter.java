package org.bicsi.canada2014.adapter;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.bicsi.canada2017.R;

import org.bicsi.canada2014.models.AlertModel;

public class AlertAdapter extends ArrayAdapter<AlertModel> {
	
	private Activity activity;
	private int rowLayout;
	private AlertModel[] mDisplayList;
	
	public AlertAdapter(Activity activity,AlertModel[] mDisplayList) {
		super(activity, R.layout.alert_rowlayout, mDisplayList);
		this.activity = activity;
		this.rowLayout = R.layout.alert_rowlayout;
		this.mDisplayList = mDisplayList;	
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (view == null) {
			view = activity.getLayoutInflater().inflate(rowLayout, parent,false);
		}		
		populateRow(view,mDisplayList[position]);
		return view;
	}


	private void populateRow(View view, AlertModel listItem)
	{
		if(listItem == null){
			return;
		}
		System.out.println("title: "+listItem.post);
		TextView txtTitle = (TextView) view.findViewById(R.id.title);
		txtTitle.setText(listItem.post);
		TextView txtDate = (TextView) view.findViewById(R.id.date);
		txtDate.setText(listItem.date);
	}
		
}
