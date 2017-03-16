package org.bicsi.canada2014.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.bicsi.canada2014.web.RequestTypes.SearchType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;


public class AsyncTaskPost extends AsyncTask<Void, Void, Void> {
	private String TAG = "AsyncTaskPost";
	// intents and extras


	
	public static String GET_UNREAD_COUNT_RETURN_INTENT = "org.bicsi.canada2014.get_unread_count_return_intent";
	public static String GET_UNREAD_COUNT_SUCCESS_VALUE = "org.bicsi.canada2014.get_unread_count_success_value";
	public static String GET_UNREAD_COUNT_RETURN_VALUES = "org.bicsi.canada2014.get_unread_count_return_values";
	public static Intent sGetUnreadCountCompletedIntent = new Intent(GET_UNREAD_COUNT_RETURN_INTENT);

	
	
	// return objects
	private String mReturnString = null;

	// Http ref
	private HttpClient mClient;
	// params
	private String mParam = "";
	private int mParamInt;
	private boolean mParamBoolean;
	private RequestTypes.SearchType mSearchType = null;
	private Bundle mParameters = null;
	private boolean mSuccess = false;


	//public static final String PRODUCTION_URL = "http://api.stackmob.com";
	public static final String PRODUCTION_URL = "https://api.parse.com/1/classes";
	//NOTE: We are on production URL now, it can be changed to test in settings screen
	public static String BASE_DEV_URL = PRODUCTION_URL;


	private final String ALERTS_URL = BASE_DEV_URL + "/alerts/";


	
	public static final CharSequence ACCESS_TOKEN_INVALID = "ACCESS_TOKEN_INVALID";
	
	// listener 
	private static Context sListener = null;


	public AsyncTaskPost(Context context, String productCode) {
		sListener = context;
	}

	public AsyncTaskPost(Context context, RequestTypes.SearchType searchType) {
		sListener = context;
		mSearchType = searchType;
	}

	public AsyncTaskPost(Context context, RequestTypes.SearchType searchType, boolean param) {
		sListener = context;
		mSearchType = searchType;
		mParamBoolean = param;
	}

	public AsyncTaskPost(Context context, RequestTypes.SearchType searchType, String param) {

		sListener = context;
		mSearchType = searchType;
		mParam = param;
	}

	public AsyncTaskPost(Context context, RequestTypes.SearchType searchType, Bundle parameters) {
		sListener = context;
		mSearchType = searchType;
		mParameters = parameters;
	}

	public AsyncTaskPost(Context context, RequestTypes.SearchType searchType, String param, int paramInt) {
		sListener = context;
		mSearchType = searchType;
		mParam = param;
		mParamInt = paramInt;
	}

	public AsyncTaskPost(Context context, SearchType searchType, int paramInt) {
		sListener = context;
		mSearchType = searchType;
		mParamInt = paramInt;
	}



	/*********************************************************************************
	 * doInBackground()
	 * 
	 **********************************************************************************/
	@Override
	protected Void doInBackground(Void... unused) {
		if ( isCancelled()){
			return null;
		}
		// based on the RequestType
		if(mSearchType == null){
			//Log.e(TAG, "mSearchType == null");
			return null;
		}
		switch (mSearchType) {
		case NONE:
			break;

		case GET_UNREAD_COUNT:
			this.doGetUnreadCount();
			break;

		default:
			break;
		}
		return null;
	}



	/*********************************************************************************
	 * onPreExecute()
	 * 
	 **********************************************************************************/
	@Override
	protected void onPreExecute() {
	}

	/*********************************************************************************
	 * onProgressUpdate(Progress[])
	 * 
	 **********************************************************************************/
	@Override
	protected void onProgressUpdate(Void... values) {
	}

	ArrayList<NameValuePair> mNameValuePairs = new ArrayList<NameValuePair>();


	/*********************************************************************************
	 * onPostExecute(Void result)
	 * 
	 * 
	 **********************************************************************************/
	@Override
	public void onPostExecute(Void result) {
		super.onPostExecute(result);
		switch (this.mSearchType) {
		
		case GET_UNREAD_COUNT:
			sGetUnreadCountCompletedIntent.putExtra(
					GET_UNREAD_COUNT_SUCCESS_VALUE, mSuccess);
			sGetUnreadCountCompletedIntent.putExtra(
					GET_UNREAD_COUNT_RETURN_VALUES, mReturnString);
			sListener.sendBroadcast(sGetUnreadCountCompletedIntent);
			break;

		default:
			break;
		}
	}


	

	

	
	

	/**
	 * getSuccess
	 * 
	 */
	public boolean getSuccess() {
		return mSuccess;
	}

	/**
	 * getReturnString
	 * 
	 */
	public String getReturnString() {
		return mReturnString;
	}


	private void doGetUnreadCount(){
		mClient = CustomHttpClient.getHttpClient();
		try {
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Alerts");
			query.orderByDescending("_created_at");							
			List<ParseObject> todos;
			todos = query.find();	
			
			
			
			String str = null;
			if (str != null) {
				try {
					
					JSONObject jsonObject = new  JSONObject(str);
					JSONArray jsonArray = jsonObject.optJSONArray("results");
					
					mSuccess = true;
					mReturnString = str;
				} catch (JSONException e) {
					mSuccess = false;
					System.out.println("Error parsing JSON: " + str);
				}
				
			} else {
				mSuccess = false;
			}
		} catch (Exception e) {
			System.out.println("Network error doGetUnreadCount");
		}
	}
	
	
	

}
