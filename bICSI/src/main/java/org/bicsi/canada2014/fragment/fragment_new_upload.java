package org.bicsi.canada2014.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.bicsi.canada2017.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.bicsi.canada2014.activities.NewMealActivity;
import org.bicsi.canada2014.common.MizeUtil.NavigateToTabFragmentListener;
import org.bicsi.canada2014.Meal;


public class fragment_new_upload extends Fragment {
	
	List<ParseObject> ob;
	
	private NavigateToTabFragmentListener mCallback;
	
	
	private ImageButton photoButton;
	private Button saveButton;
	private Button cancelButton;
	private TextView comment;
	//private Spinner mealRating;
	private ParseImageView photoPreview;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		ParseUser.getCurrentUser().saveInBackground();
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle SavedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_new_upload, parent, false);
	
		
		comment = ((EditText) v.findViewById(R.id.comment));
		
		photoButton = ((ImageButton) v.findViewById(R.id.photo_button));
		photoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(comment.getWindowToken(), 0);
				startCamera();
			}
		});
		
		saveButton = ((Button) v.findViewById(R.id.save_button));
        saveButton.setEnabled(false);
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				/*ParseQuery<ParseUser> query = ParseUser.getQuery();
				query.whereEqualTo("objectId", ParseUser.getCurrentUser()
		                .getObjectId());
				query.findInBackground(new FindCallback<ParseUser>()
				        {
				            
				            public void done(List<ParseUser> objects, ParseException e)
				            {
				            	if (e == null) {
				                    // The query was successful.
				            		for(ParseUser pu : objects){
				            		       //access the data associated with the ParseUser using the get method
				            		       //pu.getString("key") or pu.get("key") 
				            			String myUser = pu.getString("fullname");
				            			System.out.println("myUser is: " + myUser);
				            			
				            			Meal meal = ((NewMealActivity) getActivity()).getCurrentMeal();
				            			meal.setTitle(comment.getText().toString());
				            			meal.setAuthor(ParseUser.getCurrentUser());
				        				meal.setUser(myUser);
				            			
				        				meal.saveInBackground(new SaveCallback() {

				        					@Override
				        					public void done(ParseException e) {
				        						if (e == null) {
				        							getActivity().setResult(Activity.RESULT_OK);
				        							getActivity().finish();
				        						} else {
				        							Toast.makeText(
				        									getActivity().getApplicationContext(),
				        									"Error saving: " + e.getMessage(),
				        									Toast.LENGTH_SHORT).show();
				        						}
				        					}

				        				});
				            			
				            		} 
				            		
				                } else {
				                    // Something went wrong.
				                	
				                }
				                    }
				                });*/
				
				if (comment.getText().toString().equals("")) {
					Toast.makeText(getActivity(),
							"Please enter a comment.",
							Toast.LENGTH_LONG).show();}
				
				else{
				
				Meal meal = ((NewMealActivity) getActivity()).getCurrentMeal();

				// When the user clicks "Save," upload the meal to Parse
				// Add data to the meal object:
				meal.setTitle(comment.getText().toString());
				
				
				
				String struser = ParseUser.getCurrentUser().getString("fullname");
				
				// Associate the meal with the current user
				meal.setAuthor(ParseUser.getCurrentUser());
				meal.setUser(struser);

				// Add the rating
				//meal.setRating(mealRating.getSelectedItem().toString());

				// If the user added a photo, that data will be
				// added in the CameraFragment

				// Save the meal and return
				meal.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException e) {
						
						if (e == null) {
							getActivity().setResult(Activity.RESULT_OK);
							getActivity().finish();
							//postData(comment.getText().toString());
							new PostDataTask().execute();
						} else {
							Toast.makeText(
									getActivity().getApplicationContext(),
									"Error saving: " + e.getMessage(),
									Toast.LENGTH_SHORT).show();
						}
					}
					
				});
				}
			}
		});

		cancelButton = ((Button) v.findViewById(R.id.cancel_button));
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().setResult(Activity.RESULT_CANCELED);
				getActivity().finish();
			}
		});
		
		v.setOnTouchListener(new OnTouchListener()
		{
		    @Override
		    public boolean onTouch(View view, MotionEvent ev)
		    {
		        hideKeyboard(view);
		        return false;
		    }

		});

		// Until the user has taken a photo, hide the preview
		photoPreview = (ParseImageView) v.findViewById(R.id.meal_preview_image);
		photoPreview.setVisibility(View.INVISIBLE);
		
		return v;
	}
	
	public void startCamera() {
		Fragment cameraFragment = new CameraFragment();
		FragmentTransaction transaction = getActivity().getFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fragmentContainer, cameraFragment);
		transaction.addToBackStack("fragment_new_upload");
		transaction.commit();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		ParseFile photoFile = ((NewMealActivity) getActivity())
				.getCurrentMeal().getPhotoFile();
		if (photoFile != null) {
            saveButton.setEnabled(true);
			photoPreview.setParseFile(photoFile);
			photoPreview.loadInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] data, ParseException e) {
					photoPreview.setVisibility(View.VISIBLE);
				}
			});
		}

        /*else if (photoFile == null){

            Toast.makeText(getActivity(),
                    "No image found! Please provide an image.",
                    Toast.LENGTH_LONG).show();
        }*/
	}
	
	protected void hideKeyboard(View view)
	{
	    InputMethodManager in = (InputMethodManager) ((getActivity())).getSystemService(Context.INPUT_METHOD_SERVICE);
	    in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	private class PostDataTask extends AsyncTask<String, Void, Void> {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("https://speedyreference.com/newpicsubmitted.php");

		//This is the data to send
		String message = comment.getText().toString(); //any data to send
		
		/*protected void onPreExecute() {
		//NOT SURE
		}*/
		
		// Call after onPreExecute method
        protected Void doInBackground(String... urls){

		try {
		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("message", message));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		// Execute HTTP Post Request

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);

		//This is the response from a php application
		/*String reverseString = response;
		Toast.makeText(getActivity(), "response" + reverseString, Toast.LENGTH_LONG).show();*/
		
		} 
		
		catch (ClientProtocolException e) {
		Toast.makeText(getActivity(), "CPE response " + e.toString(), Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(getActivity(), "IOE response " + e.toString(), Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}
		return null;
		}//end postData()
}
	
	/*public void postData(String toPost) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("https://speedyreference.com/newpicsubmitted.php");

		//This is the data to send
		String message = comment.getText().toString(); //any data to send

		try {
		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("action", message));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		// Execute HTTP Post Request

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpclient.execute(httppost, responseHandler);

		//This is the response from a php application
		String reverseString = response;
		Toast.makeText(getActivity(), "response" + reverseString, Toast.LENGTH_LONG).show();

		} catch (ClientProtocolException e) {
		Toast.makeText(getActivity(), "CPE response " + e.toString(), Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		} catch (IOException e) {
		Toast.makeText(getActivity(), "IOE response " + e.toString(), Toast.LENGTH_LONG).show();
		// TODO Auto-generated catch block
		}

		}//end postData()*/
	
}
