package org.bicsi.canada2014.fragment;

import org.bicsi.canada2017.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class EhallSchedDetailFragment2 extends Fragment {


	TextView sessionNameDetail;
	TextView scheduleDateDetail;
	TextView sessionTimeDetail;
	TextView descDetail;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		final View v = inflater.inflate(R.layout.fragment_ehschedule_detail, container, false);
		sessionNameDetail = (TextView)v. findViewById(R.id.sessionname_label);
		scheduleDateDetail = (TextView)v. findViewById(R.id.scheduledate_label);
		sessionTimeDetail = (TextView)v. findViewById(R.id.sessiontime_label);
		descDetail = (TextView)v. findViewById(R.id.desc_label);

		/////
		Bundle bundle = getArguments();
		if(bundle != null){

			String sessNameDet = bundle.getString("sessionName");
			sessionNameDetail.setText(sessNameDet);

			String schedDateDet = bundle.getString("scheduleDate");
			scheduleDateDetail.setText(schedDateDet);

			String sessTimeDet = bundle.getString("sessionTime");
			sessionTimeDetail.setText(sessTimeDet);

			String descDet = bundle.getString("desc");
			descDetail.setText(descDet);
		}

		return v;
	}


	/*public void updateNmeDetail(String sessNameDet) {
		sessionNameDetail.setText(sessNameDet);
	}

	public void updateDateDetail(String schedDateDet) {
		scheduleDateDetail.setText(schedDateDet);
	}

	public void updateTImeDetail(String sessTimeDet) {
		sessionTimeDetail.setText(sessTimeDet);
	}

	public void updateDescDetail(String descDet) {
		descDetail.setText(descDet);
	}*/


}


