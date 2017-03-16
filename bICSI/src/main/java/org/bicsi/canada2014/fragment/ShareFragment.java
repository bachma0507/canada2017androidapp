package org.bicsi.canada2014.fragment;


import org.bicsi.canada2017.R;
import org.bicsi.canada2014.activities.MainActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;


public class ShareFragment extends Fragment{
	public static final String TAG = "ShareInfoFragment";
	private Bundle theBundle = null;

	private String mMessage = "";
	private int mRating;
	private EditText mShareFeedbackView;
	private TextView mCharCountView;
	private boolean mFBChecked = false;
	private boolean mTwitterChecked = false;
	private String prevTitle = null;
    
    /*********************************************************************************
	 * receiver
	 *********************************************************************************/
	private BroadcastReceiver receiver = new BroadcastReceiver() {    	
		public void onReceive(Context context, Intent intent) {
			
		}
	};
    

	
	
	
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		this.registerBR();
		
		View v = inflater.inflate(R.layout.fragment_new_share, container, false);		
		setHasOptionsMenu(true);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		mShareFeedbackView = (EditText) v.findViewById(R.id.editShareFeedback);
		if(mMessage!=null){
			if(mRating > -1){
				mMessage = mMessage + " #BICSI #BICSIFALL";
			}
		}
		mShareFeedbackView.setText(mMessage);
		mCharCountView = (TextView) v.findViewById(R.id.txtLineNo);
		setupCharCount();
		updateCharCount(mMessage.length());
		return v;
	}
	
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	    inflater.inflate(R.menu.share_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	       case R.id.post:
	        	mMessage = mShareFeedbackView.getText().toString();
	        	((MainActivity)getActivity()).mPostMessage = mMessage;
	        	goToPreviousFragment();
	            return true;
	            

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	

	/**setUpSocial
	 * set listeners for social buttons
	 */
	/*private void setUpSocial() {
		
	}*/
	

	
	
	/**save the state bro
	 * gives us a chance to restore data from the saved fragment
	 */
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
		if(theBundle!=null){
			bundle = theBundle;
		} else {
			//Log.v(getTag(), "Error: Could not save state");
		}
    }

	@Override
	public void onPause() {
		super.onPause();
		((MainActivity) getActivity()).hideSoftKeyboard(R.id.editShareFeedback);
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle(prevTitle);
	}
    
	@Override
	public void onResume() {
		super.onResume();
		ActionBar actionBar = getActivity().getActionBar();
		prevTitle = (String) actionBar.getTitle();
	}

	
    /***********************************************************************************************************
	 * registerBRs()
	 * 
	 * this provides a one stop place to register any BR's
	 ***********************************************************************************************************/
	private void registerBR(){

		//...................Register the BCR's for the Activity................... 			

	}
	
	/***********************************************************************************************************
	 * unregisterBRs()
	 * 
	 * this provides a one stop place to unregister any BR's without tweaking the manifest
	 ***********************************************************************************************************/
	private void unregisterBRs(){
		try {
			getActivity().unregisterReceiver(receiver);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}   
	
	/*********************************************************************************
	 * onDestroy()  
	 *********************************************************************************/
	@Override
	public void onDestroyView(){
		super.onDestroy();
		this.unregisterBRs();
	}
	
	
	/**goToPreviousFragment
	 * 
	 */
	private void goToPreviousFragment(){
		getActivity().onBackPressed();
	}
	

	/**setupCharCount
	 * 
	 */
	private void setupCharCount(){
		mShareFeedbackView.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				updateCharCount(s.length());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable arg0) {

			}
		});
	}
	
	/**updateCharCount
	 * 
	 */
	private void updateCharCount(int count){
		mCharCountView.setText(count + "/140");
	}

}
