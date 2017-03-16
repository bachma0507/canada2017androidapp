package org.bicsi.canada2014.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.bicsi.canada2017.R;
import org.bicsi.canada2014.models.Alert;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AlertsAdapter extends ArrayAdapter<Alert>{

	private Activity activity;
	private int rowlayout;
	private List<Alert> displaylist;

	public AlertsAdapter(Activity activity,  ArrayList<Alert> arrayList) {
		super(activity, R.layout.alert_rowlayout, arrayList);
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.rowlayout = R.layout.alert_rowlayout;
		this.displaylist = arrayList;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(convertView==null){
			convertView = LayoutInflater.from(activity).inflate(R.layout.alert_rowlayout, parent,false);	
		}
		
		Alert alert = displaylist.get(position);
		
		DateFormat df = new SimpleDateFormat("MMM dd,yyyy, HH:mm");
		String reportDate = df.format(alert.Updatedat);	
		
		TextView Title = (TextView)convertView.findViewById(R.id.title);
		Title.setText(alert.text);
		TextView txtdate = (TextView)convertView.findViewById(R.id.date);
		txtdate.setText(String.valueOf(reportDate));		
		
		return convertView;
	}

	
}
