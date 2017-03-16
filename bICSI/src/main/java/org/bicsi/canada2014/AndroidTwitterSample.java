package org.bicsi.canada2014;

import org.bicsi.canada2014.activities.PrepareRequestTokenActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;


public class AndroidTwitterSample extends Activity {
	public static final int TWITTER_ACTION = 000000005;
	public static final int RESULT_LOGGED_OUT = 000000004;
	public static final int  RESULT_LOGGED_IN = 000000003;
	private SharedPreferences prefs;
	private final Handler mTwitterHandler = new Handler();
	private boolean mLogout = false;
	private String mScreenName = null;

	String brandType = "";

	final Runnable mUpdateTwitterNotification = new Runnable() {
		public void run() {
			Toast.makeText(getBaseContext(), "Tweet sent !", Toast.LENGTH_LONG)
					.show();
			finish();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

		checkForLogout();
		if(mLogout){
			mScreenName = null;
			setScreenName();
			setResult(RESULT_LOGGED_OUT);
			finish();
		} else {
			
			//////////////////
			
			
			new AsyncTask<SharedPreferences,Object, Boolean>() {

		        @Override
		        protected Boolean doInBackground(SharedPreferences... params) {
		            return TwitterUtils.isAuthenticated(params[0]);
		        }

		        @Override
		        protected void onPostExecute(Boolean isAuthenticated) {
		            if (isAuthenticated) {
		                // Do processing after successful authentication
		                    //sendTweet();
		        		if (getTweetMsg() != null) {
							sendTweet();
						}
						if (getOptions() == 0) {
							getScreenName();
						}
						setResult(RESULT_LOGGED_IN);
						finish();
		            }
		            else {
		                // Do processing after authentication failure
		                    //Intent i = new Intent(getApplicationContext(), PrepareRequestTokenActivity.class);
		                    //i.putExtra("tweet_msg",getTweetMsg());
		                    //startActivity(i);
		            	Intent i = new Intent(getApplicationContext(), PrepareRequestTokenActivity.class);
						i.putExtra("tweet_msg", getTweetMsg());
						startActivityForResult(i, TWITTER_ACTION);
						finish();
		            }
		        }
		    }.execute(prefs);
			
			
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

	}


	private String getTweetMsg() {
		return getIntent().getStringExtra(IntentData.STRING);
	}
	
	private int getOptions() {
		return getIntent().getIntExtra(IntentData.INT, -1);
	}

	public void sendTweet() {
		Thread t = new Thread() {
			public void run() {

				try {
					TwitterUtils.sendTweet(prefs, getTweetMsg());
					mTwitterHandler.post(mUpdateTwitterNotification);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		};
		t.start();
	}
	
	public void getScreenName() {
		Thread t = new Thread() {
			public void run() {

				try {
					String screenName = "";//TwitterUtils.getScreenName(prefs);
					mScreenName = screenName;
					returnWithScreenName();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		t.start();
	}

	private void clearCredentials() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		//TwitterUtils.clearCredentials(prefs);
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == TWITTER_ACTION) {
            if (resultCode == RESULT_LOGGED_IN) {
            	setResult(RESULT_LOGGED_IN);
            	finish();
            }
        }
    }
	
	private void checkForLogout(){
		mLogout = getIntent().getBooleanExtra(IntentData.BOOLEAN, false);
		if(mLogout){
			clearCredentials();
		}
	}
	
	private void returnWithScreenName(){
		setScreenName();
		setResult(RESULT_LOGGED_OUT);
		finish();
	}
	
	/**setScreenName
	 * 
	 */
	private void setScreenName(){
		SharedPreferences app_preferences = getSharedPreferences(
				"MIZE_PREF", 0);
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putString("TwitterScreenName", mScreenName);
		editor.commit();
	}
}
