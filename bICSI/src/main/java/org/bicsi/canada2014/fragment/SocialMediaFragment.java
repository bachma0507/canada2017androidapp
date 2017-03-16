package org.bicsi.canada2014.fragment;

import org.bicsi.canada2017.R;
import org.bicsi.canada2014.AndroidTwitterSample;
import org.bicsi.canada2014.IntentData;
import org.bicsi.canada2014.activities.MainActivity;
import org.bicsi.canada2014.common.MizeUtil;
import org.bicsi.canada2014.common.MizeUtil.NavigateToTabFragmentListener;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

public class SocialMediaFragment extends Fragment{
	private NavigateToTabFragmentListener mCallback;
	private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private static final String TAG = "SocialMediaFragment";
    
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
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.social_media_layout, container, false);
		ImageView imgFacebook = (ImageView)v.findViewById(R.id.imgFacebook);
		imgFacebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openInternalWebview("http://m.facebook.com/bicsi");
			}
		});
		ImageView imgTwitter = (ImageView)v.findViewById(R.id.imgTwitter);
		imgTwitter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openInternalWebview("http://mobile.twitter.com/bicsi");
			}
		});
		Button btnTwitterFeed = (Button)v.findViewById(R.id.btnTwitterFeed);
		btnTwitterFeed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openInternalWebview("http://speedyreference.com/twitterfeed.html");
			}
		});
		
		
		Button postFacebook = (Button)v.findViewById(R.id.btnPostToFB);
		postFacebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				postToFB();
			}
		});
		
		Button btnPostToTwitter = (Button)v.findViewById(R.id.btnPostToTwitter);
		btnPostToTwitter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Fragment newFragment = new ShareFragment();
				mCallback.navigateToTabFragment(newFragment, null);
			}
		});
		
		
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		if(checkForMessage()){
			postToTwitter();
		}
		((MainActivity)getActivity()).updateTracker("Social Tab");
	}
	
	private boolean checkForMessage() {
    	if(((MainActivity)getActivity()).mPostMessage != null){
    		return true;
    	}
		return false;
	}

	@Override
	public void onPause() {
        super.onPause();
        ((MainActivity)getActivity()).mPostMessage = null;
		uiHelper.onPause();
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}

	
	private void openInternalWebview(CharSequence urlToOpen) {
		if (urlToOpen == null) {
			return;
		}
		String urlString = urlToOpen.toString();
		Bundle bundle = new Bundle();
		bundle.putString("URL", urlString);
		Fragment newFragment = new WebviewFragment();
		mCallback.navigateToTabFragment(newFragment, bundle);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
	        @Override
	        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
	            Log.e("Activity", String.format("Error: %s", error.toString()));
	        }

	        @Override
	        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
	            Log.i("Activity", "Success!");
	        }
	    });
	}
	
	private void postToFB() {
		if (FacebookDialog.canPresentShareDialog(getActivity().getApplicationContext(),
				FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(getActivity()).build();//.setLink(
					//"https://developers.facebook.com/android").build();
			uiHelper.trackPendingDialogCall(shareDialog.present());

		}else{
			MizeUtil.showDialog(getActivity(), R.string.noFacebookApp);
		}
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	    }
	}
	
	
	/**
     * Login/post to Twitter
     */
    private void postToTwitter() {
    		String message = ((MainActivity)getActivity()).mPostMessage;
            Intent i = new Intent(getActivity(), AndroidTwitterSample.class);
            i.putExtra(IntentData.STRING, message);
            startActivity(i);
    }
 
}
