package org.bicsi.canada2014.fragment;

import org.bicsi.canada2014.activities.MainActivity;
import org.bicsi.canada2014.adapter.SQLiteDBAllData;
import org.bicsi.canada2014.common.MizeUtil.NavigateToTabFragmentListener;
import org.bicsi.canada2017.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class CommitteeFragment extends Fragment implements AdapterView.OnItemClickListener{
	
	private NavigateToTabFragmentListener mCallback;//interface from MizeUtil
	private SQLiteDBAllData sqlite_obj;
	private MyCursorAdapter dataAdapter;
	
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
 		final View v = inflater.inflate(R.layout.fragment_committee, container, false);
 		
 		sqlite_obj = new SQLiteDBAllData(getActivity());

		sqlite_obj.open();
 		
 		Cursor cursor = sqlite_obj.fetchAllSchedulesByFuncComm();
 		
 	// The desired columns to be bound
 		String[] columns = new String[] {
				SQLiteDBAllData.KEY_functiontitle,
				SQLiteDBAllData.KEY_fucntioindate,
				SQLiteDBAllData.KEY_functionStartTime,
				SQLiteDBAllData.KEY_functionStartTimeStr,
				SQLiteDBAllData.KEY_functionEndTime,
				SQLiteDBAllData.KEY_functionEndTimeStr,
				SQLiteDBAllData.KEY_ID,
				SQLiteDBAllData.KEY_functiondescription,
				SQLiteDBAllData.KEY_LOCATIONNAME
	};	
 	// the XML defined views which the data will be bound to
 		int[] to = new int[] { 
				R.id.textViewfunctiontitle,
				R.id.textViewfunctiondate,
				R.id.textViewfunctionStartTime,
				R.id.textViewfunctionStartTimeStr,
				R.id.textViewfunctionEndTime,
				R.id.textViewfunctionEndTimeStr,
				R.id.textViewFUNCTIONCD,
				R.id.textViewfunctiondescription,
				R.id.textViewlocationname

		};
 		
 	// create the adapter using the cursor pointing to the desired data 
 	 		//as well as the layout information
 	 		dataAdapter = new MyCursorAdapter(
 					
 	 				getActivity(), R.layout.committee_info_list, 
 	 				cursor, 
 	 				columns, 
 	 				to,
 	 				0);
 	 		
 	 		ListView listView = (ListView)v. findViewById(android.R.id.list);
 			// Assign adapter to ListView
 			listView.setAdapter(dataAdapter);
 			listView.setOnItemClickListener(this);
 			
 			EditText myFilter = (EditText)v. findViewById(R.id.myFilter);
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
 					return sqlite_obj.fetchCommByName(constraint.toString());
 					
 				}
 			});
 			
 			//sqlite_obj.close();
 			return v;
 		
	}

	@Override
	public void onItemClick(AdapterView<?> listView, View view, 
			int position, long id) {
		
		// Get the cursor, positioned to the corresponding row in the result set
		Cursor cursor = (Cursor) listView.getItemAtPosition(position);
		
		
		String functioncd = 
				cursor.getString(cursor.getColumnIndexOrThrow("_id"));
		
		String functionTitle = 
				cursor.getString(cursor.getColumnIndexOrThrow("functiontitle"));
		
		String functionDate = 
				cursor.getString(cursor.getColumnIndexOrThrow("fucntioindate"));
		
		String functionStart = 
				cursor.getString(cursor.getColumnIndexOrThrow("functionStartTimeStr"));
		
		String functionEnd = 
				cursor.getString(cursor.getColumnIndexOrThrow("functionEndTimeStr"));
		
		String functionDescription = 
				cursor.getString(cursor.getColumnIndexOrThrow("functiondescription"));
		
		String functionLocation = 
				cursor.getString(cursor.getColumnIndexOrThrow("LOCATIONNAME"));
		


		CommitteeSingleFragment mySingleFragment = new CommitteeSingleFragment();

		Bundle bundle = new Bundle();
		
		String newFunctioncd = new String("'" + functioncd + "'");

		bundle.putString("_id", newFunctioncd);
		bundle.putString("functiontitle", functionTitle);
		bundle.putString("fucntioindate", functionDate);
		bundle.putString("functionStartTimeStr", functionStart);
		bundle.putString("functionEndTimeStr", functionEnd);
		bundle.putString("functiondescription", functionDescription);
		bundle.putString("LOCATIONNAME", functionLocation);
		

		mySingleFragment.setArguments(bundle);

		mCallback.navigateToTabFragment(mySingleFragment, null); //interface method
		
		sqlite_obj.close();

	}
	
	
	
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
