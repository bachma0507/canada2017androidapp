package org.bicsi.canada2014.fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.app.Activity;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import org.bicsi.canada2017.R;
import org.bicsi.canada2014.activities.MainActivity;
import org.bicsi.canada2014.adapter.AlertAdapter;
import org.bicsi.canada2014.adapter.AlertsAdapter;
import org.bicsi.canada2014.common.MizeUtil.NavigateToTabFragmentListener;
import org.bicsi.canada2014.models.Alert;
import org.bicsi.canada2014.models.AlertModel;
import org.bicsi.canada2014.web.AsyncTaskPost;


public class AlertsFragment extends ListFragment {

	private AsyncTaskPost mPost = null;
	private NavigateToTabFragmentListener mCallback;
	private AlertModel[] mDisplayList = null;//new AlertModel[1];
	private ArrayList<AsyncTaskPost> mPostArrayList ;
	private ListView alertList;
	private AlertAdapter mAdapter = null;
	private TextView txtAlertsUnreadCount;
	//private ArrayList<AlertModel> mDisplayList = new ArrayList<AlertModel>();
	


	/*********************************************************************************
	 * receiver
	 *********************************************************************************/
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			handleIntent(intent);
		}
	};
	private AlertsAdapter lAdapter;
	private List<ParseObject> todos;
	private ArrayList<Alert> larraylist;
	
	
	/**
	 * handleIntent
	 * 
	 */
	private void handleIntent(Intent intent) {
		   if (intent.getAction().equals(
				AsyncTaskPost.GET_UNREAD_COUNT_RETURN_INTENT)) {
			boolean success = intent.getBooleanExtra(
					AsyncTaskPost.GET_UNREAD_COUNT_SUCCESS_VALUE, false);
			if (success) {
				String jsonString = intent
						.getStringExtra(AsyncTaskPost.GET_UNREAD_COUNT_RETURN_VALUES);
				
				System.out.println("json response: "+jsonString);
				
					if(jsonString != null){
						if(!jsonString.startsWith("[")){
							jsonString = "["+jsonString+"]";
						}
						
					}		
				
			} else {
				//handleUnreadCounts();
			}
		}
	}

	
	private void reverseList() {
		if(mDisplayList != null){
			AlertModel[] temp = new AlertModel[mDisplayList.length];
			for(int i = 0; i < mDisplayList.length; i++){
				temp[i] = mDisplayList[mDisplayList.length - (i+1)];
			}
			mDisplayList = temp;
		}
	}


	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mCallback = (NavigateToTabFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement NavigateToListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.alerts_layout, container, false);
		txtAlertsUnreadCount = (TextView)getActivity().findViewById(R.id.txtAlertsUnreadCount);
		
		lAdapter = new AlertsAdapter(getActivity(), new ArrayList<Alert>());
		
		//mPostArrayList = new ArrayList<AsyncTaskPost>();
		alertList = (ListView) v.findViewById(android.R.id.list);
			
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		try {
			this.registerBR();
			getUnreadCount();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((MainActivity)getActivity()).updateTracker("Alerts Tab");
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterBRs();
	}
	
	/***********************************************************************************************************
	 * registerBRs()
	 * 
	 * this provides a one stop place to register any BR's
	 ***********************************************************************************************************/
	private void registerBR() {

		// ...................Register the BCR's for the
		// Activity...................
		getActivity().registerReceiver(receiver,
				new IntentFilter(AsyncTaskPost.GET_UNREAD_COUNT_RETURN_INTENT));
		
	}
	private void unregisterBRs() {
		try {
			getActivity().unregisterReceiver(receiver);
		} catch (Exception e) {
			//Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void getUnreadCount(){
		try {
//			mDisplayList = new Gson().fromJson(jsonString, AlertModel[].class);
//			System.out.println("mDisplayList[0]: "+mDisplayList);
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Alerts");
			query.orderByDescending("_created_at");							
			todos = query.find();										
			
			if(todos != null){
//				double alertDate = mDisplayList[mDisplayList.length-1].getCreateddate();
//				String dateString = Double.toString(alertDate);
//				setSharedPreferences(dateString);
//				hideUnreadCount();
				//reverseList();
//				mAdapter = new AlertAdapter(getActivity(), mDisplayList);
//				alertList.setAdapter(mAdapter);
				
				
				Date alertDate =todos.get(0).getUpdatedAt();							
				DateFormat df = new SimpleDateFormat("MMM dd,yyyy, HH:mm:ss");
				String dateString = df.format(alertDate);
			
				
				setSharedPreferences(dateString);
				hideUnreadCount();
				larraylist = new ArrayList<Alert>();
				
				lAdapter = new AlertsAdapter(getActivity(), larraylist);
				Alert alrt =null ;
		 
		        	for(int i=0; i<todos.size();i++){
		        		alrt = new Alert();						        		
		        		alrt.createdAt = todos.get(i).getCreatedAt();						        		
		        		alrt.objectId = todos.get(i).getObjectId();
		        		alrt.text = todos.get(i).getString("text");
		        		alrt.Updatedat = todos.get(i).getUpdatedAt();
		        		larraylist.add(alrt);						        		
		        	}        	
		   
				
		        alertList.setAdapter(lAdapter);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void hideUnreadCount() 
	{
		txtAlertsUnreadCount.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * setSharedPreferences
	 * 
	 */
	private void setSharedPreferences(String date) {
		SharedPreferences app_preferences = getActivity().getSharedPreferences("BICSI_PREF", 0);
		SharedPreferences.Editor editor = app_preferences.edit();		
		editor.putString("alert_date", date);
		editor.commit();
	}

}





