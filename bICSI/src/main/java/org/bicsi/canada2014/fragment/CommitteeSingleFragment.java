package org.bicsi.canada2014.fragment;

import org.bicsi.canada2014.activities.MainActivity;
import org.bicsi.canada2017.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CommitteeSingleFragment extends Fragment {

	TextView title;
	TextView date;
	TextView start;
	TextView end;
	TextView location;
	
	@Override
 	public View onCreateView(LayoutInflater inflater, ViewGroup container,
 			Bundle savedInstanceState) {

 		super.onCreateView(inflater, container, savedInstanceState);
 		final View v = inflater.inflate(R.layout.fragment_committee_single, container, false);
 		
 		title = (TextView)v.findViewById(R.id.functiontitle);
		date = (TextView)v.findViewById(R.id.functiondate);
		start = (TextView)v.findViewById(R.id.functionstarttimestr);
		end = (TextView)v.findViewById(R.id.functionendtimestr);
		location = (TextView)v.findViewById(R.id.functionlocation);
		
		Bundle bundle = getArguments();
		if(bundle != null){
		
		String ftitle = bundle.getString("functiontitle");
		title.setText(ftitle);
		
		String fdate = bundle.getString("fucntioindate");
		date.setText(fdate);
		
		String fstart = bundle.getString("functionStartTimeStr");
		start.setText(fstart);
		
		String fend = bundle.getString("functionEndTimeStr");
		end.setText(fend);
		
		String floc = bundle.getString("LOCATIONNAME");
		location.setText(floc);
		
		
			}
 		
	return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		((MainActivity)getActivity()).updateTracker("Home Tab");
	}
}
