package org.bicsi.canada2014.fragment;

import java.util.List;

import org.bicsi.canada2017.R;
import org.bicsi.canada2014.adapter.SQLiteDB;



import android.app.Activity;
import android.database.Cursor;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.FilterQueryProvider;

public class EhallSchedFragment extends Activity{
	public static final String TAG = "ExHallScheduleFragment";
	private SQLiteDB sqlite_obj;
	 List<String> list1, list2, list3, list4, list5;
	 private SimpleCursorAdapter dataAdapter;
	
	 
    
	

	//@SuppressLint("SetJavaScriptEnabled")
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.fragment_ehschedule);
		//getDataFromBundle(savedInstanceState);
		
		sqlite_obj = new SQLiteDB(EhallSchedFragment.this);
		
		displayListView();


		
	}
	
	 private void displayListView(){
		
		sqlite_obj.open();
	     
	     Cursor cursor = sqlite_obj.fetchAllSchedules();

	     // The desired columns to be bound
	     String[] columns = new String[] {
	       SQLiteDB.KEY_SCHEDULEDATE,
	       SQLiteDB.KEY_SESSIONNAME,
	       SQLiteDB.KEY_SESSIONTIME
	       
	       /*CountriesDbAdapter.KEY_CONTINENT,
	       CountriesDbAdapter.KEY_REGION*/
	     };

	     // the XML defined views which the data will be bound to
	     int[] to = new int[] { 
	       R.id.textViewScheduleDate,
	       R.id.textViewSessionName,
	       R.id.textViewSessionTime,
	       
	       /*R.id.continent,
	       R.id.region,*/
	     };

	     // create the adapter using the cursor pointing to the desired data 
	     //as well as the layout information
	     dataAdapter = new SimpleCursorAdapter(
	    		 this, R.layout.schedule_info, 
			       cursor, 
			       columns, 
			       to,
			       0);
	     
	     ListView listView = (ListView) findViewById(android.R.id.list);
	     // Assign adapter to ListView
	     listView.setAdapter(dataAdapter);
	     
	     EditText myFilter = (EditText) findViewById(R.id.myFilter);
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
		     });
	}
    
}
