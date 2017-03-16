package org.bicsi.canada2014.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;

import org.bicsi.canada2017.R;
import org.bicsi.canada2014.activities.NewMealActivity;

import android.widget.Button;

import org.bicsi.canada2014.common.MizeUtil.NavigateToTabFragmentListener;

public class LoginSuccessFragment extends Fragment {
	private NavigateToTabFragmentListener mCallback;
	
	private Fragment newFragment = new GalleryViewFragment();
	private Fragment newFragment2 = new fragment_new_upload();
	
	// Declare Variables
			Button viewGalleryButton;
			Button uploadPhotoButton;
			
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
				View v = inflater.inflate(R.layout.loginsuccess, container, false);
				
				// Locate Buttons in loginsuccess.xml
				viewGalleryButton = (Button) v.findViewById(R.id.viewgallerybtn);
				uploadPhotoButton = (Button) v.findViewById(R.id.uploadphotobtn);
				
				viewGalleryButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						//openInternalWebview("http://www.bicsi.org/m/Schedule.aspx");
						
						mCallback.navigateToTabFragment(newFragment, null);
					}
				});
				
				uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						
						Intent intent = new Intent(getActivity(), NewMealActivity.class);
						startActivity(intent);
						
						//mCallback.navigateToTabFragment(newFragment2, null);
					}
				});
				
				
				return v;
			}
			
			
}
