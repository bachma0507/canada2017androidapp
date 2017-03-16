package org.bicsi.canada2014.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;

import org.bicsi.canada2017.R;
import org.bicsi.canada2014.activities.MainActivity;
import org.bicsi.canada2014.common.MizeUtil.NavigateToTabFragmentListener;

public class HomeFragment extends Fragment {

	private NavigateToTabFragmentListener mCallback;
	private ImageView ivConfsch, ivContacts, ivPresent, ivSurvey, ivCec, ivExhall, ivExfloor, ivExhbit, ivHtinfo, ivCommeet, ivTrainexam, ivBicsiwelcome;
	private Fragment newFragment = new WebviewFragment();
	private Fragment newFragment2 = new EhallSchedFragment2();
	private Fragment newFragment3 = new ConfSchedFragment();
	private Fragment newFragment4 = new CommitteeFragment();

	
	
	

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
		View v = inflater.inflate(R.layout.home_layout, container, false);


		
		 	ivConfsch = (ImageView)v.findViewById(R.id.confsch);
	        ivContacts = (ImageView)v.findViewById(R.id.contacts);
	        ivPresent = (ImageView)v.findViewById(R.id.present);
	        ivSurvey = (ImageView)v.findViewById(R.id.survey);
	        ivCec = (ImageView)v.findViewById(R.id.cec);
	        ivExhall = (ImageView)v.findViewById(R.id.exhall);
	        ivExfloor = (ImageView)v.findViewById(R.id.exfloor);
	        ivExhbit = (ImageView)v.findViewById(R.id.exhbit);
	        ivHtinfo = (ImageView)v.findViewById(R.id.htinfo);
	        ivCommeet = (ImageView)v.findViewById(R.id.commeet);
	        ivTrainexam = (ImageView)v.findViewById(R.id.trainexam);
	        ivBicsiwelcome = (ImageView)v.findViewById(R.id.bicsiwelcome);
	       // iv11 = (ImageView)v.findViewById(R.id.contacts);
	             	     
		ivConfsch.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//openInternalWebview("http://www.bicsi.org/m/Schedule.aspx");
				
				mCallback.navigateToTabFragment(newFragment3, null);
			}
		});

		ivContacts.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				openInternalWebview("http://www.bicsi.org/m/content.aspx?id=6554");
			}
		});

		ivPresent.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				openInternalWebview("https://www.speedyreference.com/presentationsredirect.html");
			}
		});
		ivSurvey.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openInternalWebview("https://www.speedyreference.com/surveysredirect.html");
			}
		});
		ivCec.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				openInternalWebview("http://www.bicsi.org/m/content.aspx?id=8437");
			}
		});
		ivExhall.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				     //when a user selects an event from your list

				mCallback.navigateToTabFragment(newFragment2, null);

				//openInternalWebview("http://www.bicsi.org/m/content.aspx?id=7006");
				
				//Need here code to open EhallSchedFragment.java
				
				// Start NewActivity.class
                /*Intent myIntent = new Intent(getActivity(),
                        EhallSchedFragment.class);
                startActivity(myIntent);*/
			}
		});
		ivExfloor.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				openInternalWebview("http://speedyreference.com/floormap/boothinfowin17.htm");
				//openInternalWebview("http://speedyreference.com/bicsiappcms/floormaps.html");
			}
		});
		ivExhbit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openInternalWebview("http://www.bicsi.org/m/hall.aspx");
			}
		});
		ivHtinfo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				openInternalWebview("https://www.speedyreference.com/hotelredirect.html");
			}
		});
		ivCommeet.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//mCallback.navigateToTabFragment(newFragment4, null);
				openInternalWebview("https://www.speedyreference.com/nppredirect.html");
			}
		});
		ivTrainexam.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				openInternalWebview("http://www.bicsi.org/m/content.aspx?id=8450");
				
			}
		});
		ivBicsiwelcome.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"support@bicsi.org"});
				i.putExtra(Intent.EXTRA_SUBJECT, "Comments for Winter 2017 Conference");
				i.putExtra(Intent.EXTRA_TEXT   , "{Device - Android} Your Comments:");
				try {
				    startActivity(Intent.createChooser(i, "Send mail..."));
				} catch (android.content.ActivityNotFoundException ex) {
				    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
				}

				//openInternalWebview("http://microsite.anaheimoc.org/2014-bicsi-fall-conference-exhibition");
			}
		});
		return v;
	}
	private void openInternalWebview(CharSequence urlToOpen) {
		if (urlToOpen == null) {
			return;
		}
		String urlString = urlToOpen.toString();
		((MainActivity)getActivity()).mWebViewURL = urlString;
		mCallback.navigateToTabFragment(newFragment, null);
	}

	@Override
	public void onResume() {
		super.onResume();
		((MainActivity)getActivity()).updateTracker("Home Tab");
	}

	
	
	


	
}
