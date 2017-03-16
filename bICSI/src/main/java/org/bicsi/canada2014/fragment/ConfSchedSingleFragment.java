package org.bicsi.canada2014.fragment;


import org.bicsi.canada2014.Planner;
import org.bicsi.canada2014.activities.NotesActivity;
import org.bicsi.canada2014.adapter.SQLiteDBAllData;
import org.bicsi.canada2014.adapter.SQLiteDBPlanner;
import org.bicsi.canada2014.common.MizeUtil.NavigateToTabFragmentListener;
import org.bicsi.canada2017.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ConfSchedSingleFragment extends Fragment  {
	
	private NavigateToTabFragmentListener mCallback;
	private Fragment myNoteFragment = new SessionNoteFragment();
	private SQLiteDBPlanner sqlite_obj;

	List<Planner> list = new ArrayList<Planner>();


	public String newFunctioncd;
	
	TextView title;
	TextView date;
	TextView start;
	TextView end;
	TextView desc;
	TextView location;
	TextView trainer1fname;
	TextView trainer1lname;
	TextView trainer2fname;
	TextView trainer2lname;
	TextView trainer3fname;
	TextView trainer3lname;
	TextView trainer4fname;
	TextView trainer4lname;
	TextView trainer5fname;
	TextView trainer5lname;
	TextView trainer6fname;
	TextView trainer6lname;
	TextView speakerslabel;
	Button surveybutton;
	Button notesbutton;
	Button plannerbutton;
	
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

		setRetainInstance(true);

		super.onCreateView(inflater, container, savedInstanceState);
		final View v = inflater.inflate(R.layout.fragment_confschedule_single, container, false);

		sqlite_obj = new SQLiteDBPlanner(getActivity());


		sqlite_obj.open();
		
		title = (TextView)v.findViewById(R.id.functiontitle);
		date = (TextView)v.findViewById(R.id.functiondate);
		start = (TextView)v.findViewById(R.id.functionstarttimestr);
		end = (TextView)v.findViewById(R.id.functionendtimestr);
		desc = (TextView)v.findViewById(R.id.functiondecsription);
		location = (TextView)v.findViewById(R.id.functionlocation);
		trainer1fname = (TextView)v.findViewById(R.id.trainer1fname);
		trainer1lname = (TextView)v.findViewById(R.id.trainer1lname);
		trainer2fname = (TextView)v.findViewById(R.id.trainer2fname);
		trainer2lname = (TextView)v.findViewById(R.id.trainer2lname);
		trainer3fname = (TextView)v.findViewById(R.id.trainer3fname);
		trainer3lname = (TextView)v.findViewById(R.id.trainer3lname);
		trainer4fname = (TextView)v.findViewById(R.id.trainer4fname);
		trainer4lname = (TextView)v.findViewById(R.id.trainer4lname);
		trainer5fname = (TextView)v.findViewById(R.id.trainer5fname);
		trainer5lname = (TextView)v.findViewById(R.id.trainer5lname);
		trainer6fname = (TextView)v.findViewById(R.id.trainer6fname);
		trainer6lname = (TextView)v.findViewById(R.id.trainer6lname);
		speakerslabel = (TextView)v.findViewById(R.id.speakers_label);
		surveybutton = (Button)v.findViewById(R.id.survey_button);
		notesbutton = (Button)v.findViewById(R.id.notes_button);
		plannerbutton = (Button)v.findViewById(R.id.planner_button);


		
		
		final Bundle bundle = getArguments();



				if(bundle != null){
				
				newFunctioncd = bundle.getString("_id");
					String funccd = newFunctioncd;


					String pfunctioncd = funccd.replace("'", "");

					System.out.println("newFunctioncd value is " + pfunctioncd);

					Cursor c = sqlite_obj.fetchPlannerByCode(pfunctioncd);

					if (c.moveToFirst() == false){

						plannerbutton.setText("Add to My Schedule");

					}
					else{

						plannerbutton.setText("Remove from My Schedule");

					}
				
				if(newFunctioncd.contains("CONCSES") || newFunctioncd.contains("PRECON") || newFunctioncd.contains("GS_TUES") || newFunctioncd.contains("GS_THURS") == true){
					speakerslabel.setVisibility(View.VISIBLE);
					surveybutton.setVisibility(View.VISIBLE);
				}
				else{
					speakerslabel.setVisibility(View.GONE);
					surveybutton.setVisibility(View.GONE);
				}
				
				String ftitle = bundle.getString("functiontitle");
				title.setText(ftitle);
				
				String fdate = bundle.getString("fucntioindate");
				date.setText(fdate);
				
				String fstart = bundle.getString("functionStartTimeStr");
				start.setText(fstart);
				
				String fend = bundle.getString("functionEndTimeStr");
				end.setText(fend);
				
				String fdesc = bundle.getString("functiondescription");
				desc.setText(fdesc);
				
				String floc = bundle.getString("LOCATIONNAME");
				location.setText(floc);
				
				String t1fname = bundle.getString("trainer1firstname");
				trainer1fname.setText(t1fname);
				
				String t1lname = bundle.getString("trainer1lastname");
				trainer1lname.setText(t1lname);
				
				String t2fname = bundle.getString("trainer2firstname");
				trainer2fname.setText(t2fname);
				
				String t2lname = bundle.getString("trainer2lastname");
				trainer2lname.setText(t2lname);
				
				if(t2fname.isEmpty() == true){
					trainer2fname.setVisibility(View.GONE);
					trainer2lname.setVisibility(View.GONE);
				}
				
				String t3lname = bundle.getString("trainer3lastname");
				trainer3lname.setText(t3lname);
				
				String t3fname = bundle.getString("trainer3firstname");
				trainer3fname.setText(t3fname);
				
				if(t3fname.isEmpty() == true){
					trainer3fname.setVisibility(View.GONE);
					trainer3lname.setVisibility(View.GONE);
				}
				
				String t4lname = bundle.getString("trainer4lastname");
				trainer4lname.setText(t4lname);
				
				String t4fname = bundle.getString("trainer4firstname");
				trainer4fname.setText(t4fname);
				
				if(t4fname.isEmpty() == true){
					trainer4fname.setVisibility(View.GONE);
					trainer4lname.setVisibility(View.GONE);
				}
				
				String t5lname = bundle.getString("trainer5lastname");
				trainer5lname.setText(t5lname);
				
				String t5fname = bundle.getString("trainer5firstname");
				trainer5fname.setText(t5fname);
				
				if(t5fname.isEmpty() == true){
					trainer5fname.setVisibility(View.GONE);
					trainer5lname.setVisibility(View.GONE);
				}
				
				String t6lname = bundle.getString("trainer6lastname");
				trainer6lname.setText(t6lname);
				
				String t6fname = bundle.getString("trainer6firstname");
				trainer6fname.setText(t6fname);
				
				if(t6fname.isEmpty() == true){
					trainer6fname.setVisibility(View.GONE);
					trainer6lname.setVisibility(View.GONE);
				}
					//System.out.println("The session title is: " + ftitle + ".");

					//final SessionNoteFragment myNoteFragment = new SessionNoteFragment();
					Bundle bundle2 = new Bundle();

					bundle2.putString("functiontitle", ftitle);
					bundle2.putString("_id", funccd);

					myNoteFragment.setArguments(bundle2);

					FragmentTransaction transaction = getActivity().getFragmentManager()
					.beginTransaction();
					transaction.addToBackStack(null);
					transaction.commit();

					//mCallback.navigateToTabFragment(myNoteFragment, null); //interface method


				//notesbutton.setOnClickListener(new View.OnClickListener() {
					//public void onClick(View v) {

						//mCallback.navigateToTabFragment(myNoteFragment, null);

					//sqlite_obj = new SQLiteDBAllData(getActivity());




					}
				//});

		notesbutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				//EditText et= (EditText)context.findViewById(R.id.txt_edit);
				String myTitle = bundle.getString("functiontitle");
				String myId = bundle.getString("_id");
//create an Intent object
				Intent intent = new Intent(getActivity(), NotesActivity.class);
//add data to the Intent object
				intent.putExtra("functiontitle",myTitle );
				intent.putExtra("_id", myId);
//start the second activity
				startActivity(intent);
			}

		});

		plannerbutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				if(plannerbutton.getText().equals("Add to My Schedule")) {

					sqlite_obj.open();
					String pfunctioncd = newFunctioncd.replace("'", "");
					String ptitle = bundle.getString("functiontitle");
					String pdesc = bundle.getString("functiondescription");
					String plocation = bundle.getString("LOCATIONNAME");
					String pdate = bundle.getString("fucntioindate");
					String pstart = bundle.getString("functionStartTimeStr");
					String pend = bundle.getString("functionEndTimeStr");

					Planner plannerItem = new Planner();

					plannerItem.code = pfunctioncd;
					plannerItem.title = ptitle;
					plannerItem.desc = pdesc;
					plannerItem.location = plocation;
					plannerItem.date = pdate;
					plannerItem.start = pstart;
					plannerItem.end = pend;

					sqlite_obj.insertPlanner(plannerItem);

					Toast.makeText(
							getActivity().getApplicationContext(),
							"Added to My Schedule!",
							Toast.LENGTH_SHORT).show();

					plannerbutton.setText("Remove from My Schedule");

					sqlite_obj.close();

					/*list = sqlite_obj.getAllPlannerItems();

					print(list);*/
				}
				else{
					sqlite_obj.open();
					String pfunctioncd = newFunctioncd.replace("'", "");
					sqlite_obj.DeletePlanner(pfunctioncd);

					Toast.makeText(
							getActivity().getApplicationContext(),
							"Deleted from My Schedule!",
							Toast.LENGTH_SHORT).show();

					plannerbutton.setText("Add to My Schedule");

					sqlite_obj.close();

				}
			}

		});
				
		surveybutton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						String urlEndStr = newFunctioncd.replace("'", "");

						Uri uri = Uri.parse("https://www.research.net/s/" + urlEndStr);
						Intent browserIntent = new Intent(Intent.ACTION_VIEW);
						browserIntent.setDataAndType(uri, "text/html");
						browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
						getActivity().startActivity(browserIntent);
						

					}
				});
				
				//}


				sqlite_obj.close();
				return v;

	}


	/*private void print(List<Planner> list) {
		// TODO Auto-generated method stub
		String value = "";
		for(Planner pl : list){
			value = value+"id: "+pl._id+", funccode: "+pl.code+" title: "+pl.title+" description: "+pl.desc+" location: "+pl.location+" date: "+pl.date+" start: "+pl.start+" end: "+pl.end+"\n";
		}
		System.out.println(value);

		sqlite_obj.close();


	}*/
	
}
