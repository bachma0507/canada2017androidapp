package org.bicsi.canada2014.fragment;



import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.bicsi.canada2017.R;
import org.bicsi.canada2014.common.MizeUtil.NavigateToTabFragmentListener;


public class AboutUsFragment extends Fragment{
	public static final String TAG = "AboutUsFragment";
    private NavigateToTabFragmentListener mCallback;
    
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
		
		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (NavigateToTabFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NavigateToListener");
        }
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		String app_ver = null;
		try
		{
		    app_ver = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
		}
		catch (NameNotFoundException e)
		{
		    //Log.v(TAG, e.getMessage());
		}
		
		
		View v = inflater.inflate(R.layout.fragment_about_us, container, false);
		
		
		TextView versionView = (TextView) v.findViewById(R.id.versionView);
		if(app_ver != null){
			versionView.setText(getActivity().getString(R.string.version) + " " + app_ver);
		}
		
		
		
		return v;
	}
	
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	
	
	
	

	
	

	@Override
	public void onPause() {
		super.onPause();
		this.unregisterBRs();
	}
    
	@Override
	public void onResume() {
		super.onResume();
		this.registerBR();
	}

	
    /***********************************************************************************************************
	 * registerBRs()
	 * 
	 * this provides a one stop place to register any BR's
	 ***********************************************************************************************************/
	private void registerBR(){

		//...................Register the BCR's for the Activity................... 			
		//getActivity().registerReceiver(receiver,
				//new IntentFilter(AsyncTaskPost.POST_FEEDBACK_RETURN_INTENT));
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
			//Auto-generated catch block
			e.printStackTrace();
		}
	}   
	

	
	
	
	
	/**goToPreviousFragment
	 * 
	 */
	/*private void goToPreviousFragment(){
		getActivity().onBackPressed();
	}*/
	
}
