package org.bicsi.canada2014.fragment;

import org.bicsi.canada2017.R;
import org.bicsi.canada2014.activities.MainActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebviewFragment extends Fragment{
	public static final String TAG = "SupportInfoFragment";
	private Bundle theBundle = null;
    private String mURL = null;
    private WebView webView = null;
    private ProgressDialog progress;
	
    
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_webview, container, false);
		getDataFromBundle(savedInstanceState);


		webView = (WebView)v.findViewById(R.id.webView1);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setSupportZoom(true);
	    webView.getSettings().setBuiltInZoomControls(true);
	    webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
	    webView.setScrollbarFadingEnabled(true);
	    webView.getSettings().setLoadsImagesAutomatically(true);
	    webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
	    webView.getSettings().setRenderPriority(RenderPriority.HIGH);
	   	webView.setOnKeyListener(new OnKeyListener()
	    {
	        public boolean onKey(View v, int keyCode, KeyEvent event)
	        {
	            if(event.getAction() == KeyEvent.ACTION_DOWN)
	            {
	                WebView webView = (WebView) v;

	                switch(keyCode)
	                {
	                    case KeyEvent.KEYCODE_BACK:
	                        if(webView.canGoBack())
	                        {
	                        	WebBackForwardList history = webView.copyBackForwardList();
	                            if (history.getSize() > 1){
		                            webView.goBack();
		                            return true;
	                            } else {
	                            	return false;
	                            }
	                        }
	                        break;
	                }
	            }

	            return false;
	        }
	    });
		
		
		webView.setWebViewClient(new WebViewClient(){
		    @SuppressWarnings("deprecation")
			@Override
		    public boolean shouldOverrideUrlLoading(WebView wView, String url)
		    {
		    	System.out.println("shouldOverrideUrlLoading");
		    	if(url.startsWith("mailto:")){
	                MailTo mt = MailTo.parse(url);
	                Intent i = newEmailIntent(mt.getTo(), mt.getSubject(), mt.getBody(), mt.getCc());
	                startActivity(i);
	                wView.reload();
	                return true;
	            }   else if(url.endsWith(".pdf")){	           	
	            	Dialog dialog = new Dialog(getActivity());
	        		dialog.setTitle("Loading");
	        		dialog.show();
	            	 startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		        
		        return true;
	            }else{
	            	wView.getSettings().setJavaScriptEnabled(true); 
			    	wView.getSettings().setLoadWithOverviewMode(true);
			    	wView.getSettings().setUseWideViewPort(true);
			    	wView.getSettings().setPluginState(PluginState.ON);
	            	wView.loadUrl(url);
	            	 return true;
	            }
		    }
		    @Override
	        public void onPageFinished(WebView view, String url) {
		    	if(!url.contains("about:blank")){
		    		System.out.println("onPageFinished");
		    		progress.dismiss();
		    	}
	        }
		});
		loadURL();
		
		return v;
	}
	
	/**getDataFromBundle
	 * @param savedInstanceState 
	 * 
	 */
	private void getDataFromBundle(Bundle savedInstanceState) {
		if(getArguments()!= null){
			theBundle = getArguments();
			mURL = getArguments().getString("URL");
		} else if (savedInstanceState != null){
			theBundle = savedInstanceState;
			mURL = theBundle.getString("URL");
		}
	}

	/**save the state bro
	 * gives us a chance to restore data from the saved fragment
	 */
    
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    
     // Save the state of the WebView
        webView.saveState(bundle);
    }
    
    
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
      super.onSaveInstanceState(savedInstanceState);
   
      // Restore the state of the WebView
      webView.restoreState(savedInstanceState);
    }

	@Override
	public void onPause() {
		super.onPause();
		webView.clearHistory();
	}
	
	/**loadURL
	 * format and load the url
	 */
    private void loadURL(){
    	if(mURL!=null){
    		progress = new ProgressDialog(getActivity());
    		progress.setTitle("");
    		progress.setMessage("Loading...");
    		progress.show();
    		if(mURL.endsWith(".pdf")){
    			//webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+mURL);	
    			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mURL));
    			
            	startActivity(browserIntent);
    		}else{
    			//webView.getSettings().setJavaScriptEnabled(true); 
		    	webView.getSettings().setLoadWithOverviewMode(true);
		    	webView.getSettings().setUseWideViewPort(true);
		    
    			webView.loadUrl(mURL);	
    		}
    		
    		webView.clearHistory();
    	}
    }
    
    public static Intent newEmailIntent(String address, String subject, String body, String cc) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_CC, cc);
        intent.setType("message/rfc822");
        return intent;
  }

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onResume() {
		super.onResume();
		if(((MainActivity)getActivity()).mWebViewURL != null){
			if(mURL != ((MainActivity)getActivity()).mWebViewURL){
				mURL = ((MainActivity)getActivity()).mWebViewURL;
				loadURL();
				((MainActivity)getActivity()).mWebViewURL = null;
				webView.clearHistory();
				return;
			}
		} 
		progress.dismiss();
	}
    
	
    
}
