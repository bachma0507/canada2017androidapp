package org.bicsi.canada2014.fragment;


import org.bicsi.canada2017.R;

import org.bicsi.canada2014.adapter.SQLiteDB;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import org.bicsi.canada2014.activities.MainActivity;


public class EhallSchedFragment2 extends Fragment /*implements AdapterView.OnItemClickListener*/{

	//private NavigateToTabFragmentListener mCallback;//interface from MizeUtil
	private SQLiteDB sqlite_obj;
	private MyCursorAdapter dataAdapter;

	public void onAttach(Activity activity) {
		super.onAttach(activity);


		try {
			//mCallback = (NavigateToTabFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement NavigateToListener");
		}
	}

	//}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		final View v = inflater.inflate(R.layout.fragment_ehschedule, container, false);


		sqlite_obj = new SQLiteDB(getActivity());



		sqlite_obj.open();

		Cursor cursor = sqlite_obj.fetchAllSchedules();

		// The desired columns to be bound
		String[] columns = new String[] {
				SQLiteDB.KEY_SESSIONNAME,
				SQLiteDB.KEY_SCHEDULEDATE,
				SQLiteDB.KEY_SESSIONTIME,
				SQLiteDB.KEY_DESC

		};

		// the XML defined views which the data will be bound to
		int[] to = new int[] { 
				R.id.textViewSessionName,
				R.id.textViewScheduleDate,
				R.id.textViewSessionTime,
				R.id.textViewDesc

		};

		// create the adapter using the cursor pointing to the desired data 
		//as well as the layout information
		dataAdapter = new MyCursorAdapter(
				getActivity(), R.layout.schedule_info, 
				cursor, 
				columns, 
				to,
				0);

		ListView listView = (ListView)v. findViewById(android.R.id.list);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);
		//listView.setOnItemClickListener(this);


		/*EditText myFilter = (EditText)v. findViewById(R.id.myFilter);
		myFilter.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, 
					int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, 
					int before, int count) {
				dataAdapter.getFilter().filter(s.toString());
			}
		});

		dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
			public Cursor runQuery(CharSequence constraint) {
				return sqlite_obj.fetchScheduleByDate(constraint.toString());
			}
		});*/
		
		sqlite_obj.close();

		return v;

	}

	/*@Override
	public void onItemClick(AdapterView<?> listView, View view, 
			int position, long id) {
		// Get the cursor, positioned to the corresponding row in the result set
		//Cursor cursor = (Cursor) listView.getItemAtPosition(position);

		//Toast.makeText(getActivity(), "Clicked "+ position, Toast.LENGTH_LONG).show();
		// Get the cursor, positioned to the corresponding row in the result set
		Cursor cursor = (Cursor) listView.getItemAtPosition(position);

		String exHallSessionName = 
				cursor.getString(cursor.getColumnIndexOrThrow("sessionName"));

		String exHallScheduleDate = 
				cursor.getString(cursor.getColumnIndexOrThrow("scheduleDate"));

		String exHallSessionTime = 
				cursor.getString(cursor.getColumnIndexOrThrow("sessionTime"));
		String exHallDesc = 
				cursor.getString(cursor.getColumnIndexOrThrow("desc"));

		//String allData = exHallScheduleDate + " \n" + exHallSessionName + "\n" + exHallSessionTime;

		//Toast.makeText(getActivity(), allData, Toast.LENGTH_LONG).show();


		EhallSchedDetailFragment2 myDetailFragment = new EhallSchedDetailFragment2();

		Bundle bundle = new Bundle();

		bundle.putString("sessionName", exHallSessionName);
		bundle.putString("scheduleDate", exHallScheduleDate);
		bundle.putString("sessionTime", exHallSessionTime);
		bundle.putString("desc", exHallDesc);
		myDetailFragment.setArguments(bundle);


		mCallback.navigateToTabFragment(myDetailFragment, null); //interface method

		//This interface method is not needed
		try{
		            ((OnSchedItemSelectedListener) getActivity()).onSchedItemPicked(position); 
		        }catch (ClassCastException cce){

		        }

		sqlite_obj.close();

	}*/
	//This interface is not needed
	/*public interface OnSchedItemSelectedListener{
		        public void onSchedItemPicked(int position);
		    }*/

	@Override
	public void onResume() {
		super.onResume();
		((MainActivity)getActivity()).updateTracker("Home Tab");
	}
	
	//extend the SimpleCursorAdapter to create a custom class where we 
	 //can override the getView to change the row colors
	 private class MyCursorAdapter extends SimpleCursorAdapter{
	 
	  public MyCursorAdapter(Context context, int layout, Cursor c,
	    String[] from, int[] to, int flags) {
	   super(context, layout, c, from, to, flags);
	  }  
	 
	  @Override 
	  public View getView(int position, View convertView, ViewGroup parent) {  
	 
	   //get reference to the row
	   View view = super.getView(position, convertView, parent); 
	   //check for odd or even to set alternate colors to the row background
	   if(position % 2 == 0){  
	    view.setBackgroundColor(Color.rgb(185, 197, 161));
	   }
	   else {
	    view.setBackgroundColor(Color.rgb(255, 255, 255));
	   }
	   return view;  
	  }  

	 }
}
