package org.bicsi.canada2014.fragment;

import android.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import org.bicsi.canada2014.ListViewAdapter;
import org.bicsi.canada2014.WorldPopulation;
import org.bicsi.canada2017.R;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class GalleryViewFragment extends Fragment {
	
	// Declare Variables
		ListView listview;
		List<ParseObject> ob;
		ProgressDialog mProgressDialog;
		ListViewAdapter adapter;
		private List<WorldPopulation> worldpopulationlist = null;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);
			View v = inflater.inflate(R.layout.gallery_view, container, false);
			// Execute RemoteDataTask AsyncTask
			new RemoteDataTask().execute();
			
			
			return v;
		}
		
		// RemoteDataTask AsyncTask
					private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
						@Override
						protected void onPreExecute() {
							super.onPreExecute();
							// Create a progressdialog
							mProgressDialog = new ProgressDialog(getActivity());
							// Set progressdialog title
							mProgressDialog.setTitle("Photo Gallery");
							// Set progressdialog message
							mProgressDialog.setMessage("Loading...");
							mProgressDialog.setIndeterminate(false);
							// Show progressdialog
							mProgressDialog.show();
						}

						@Override
						protected Void doInBackground(Void... params) {
							// Create the array
							worldpopulationlist = new ArrayList<WorldPopulation>();
							try {
								// Locate the class table named "Country" in Parse.com
								ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
										"WallImageObject");
								// Locate the column named "ranknum" in Parse.com and order list
								// by ascending
								query.whereEqualTo("Status", "F14approved");
								query.orderByDescending("createdAt");
								ob = query.find();
								for (ParseObject wallimageobject : ob) {
									// Locate images in flag column
									ParseFile image = (ParseFile) wallimageobject.get("image");

									WorldPopulation map = new WorldPopulation();
									map.setRank((String) wallimageobject.get("user"));
									map.setCountry((String) wallimageobject.get("Status"));
									map.setPopulation((String) wallimageobject.get("comment"));
									map.setFlag(image.getUrl());
									worldpopulationlist.add(map);
								}
							} catch (ParseException e) {
								Log.e("Error", e.getMessage());
								e.printStackTrace();
							}
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							
							// Locate the listview in gallery_view.xml
							listview = (ListView) getActivity().findViewById(R.id.listview);
							// Pass the results into ListViewAdapter.java
							adapter = new ListViewAdapter(getActivity(),
									worldpopulationlist);
							// Binds the Adapter to the ListView
							listview.setAdapter(adapter);
							// Close the progressdialog
							mProgressDialog.dismiss();
						}
					}
					
					
}
