package org.bicsi.canada2014.activities;
import org.bicsi.canada2014.common.MizeUtil.NavigateToTabFragmentListener;
import org.bicsi.canada2017.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
 
public class WebViewActivity extends Fragment {
	
	private NavigateToTabFragmentListener mCallback;
 
	private WebView webView;
	
	public String urlEndStr;
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mCallback = (NavigateToTabFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement NavigateToListener");
		}
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		final View v = inflater.inflate(R.layout.webview_survey, container, false);
		
		Bundle bundle = getArguments();
        urlEndStr = bundle.getString("urlEndStr");
 
		webView = (WebView)v. findViewById(R.id.survey_webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("https://www.research.net/s/" + urlEndStr);
		
		return v;
 
	}
	
 
}