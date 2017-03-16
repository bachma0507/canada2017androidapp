package org.bicsi.canada2014.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.content.Intent;

import org.bicsi.canada2017.R;

import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.bicsi.canada2014.common.MizeUtil.NavigateToTabFragmentListener;

public class GallerySignupFragment extends Fragment {
	
private NavigateToTabFragmentListener mCallback;
	
	private Fragment newFragment = new LoginSuccessFragment();
	
	// Declare Variables
			Button loginbutton;
			Button signup;
			String usernametxt;
			String passwordtxt;
			String emailtxt;
			EditText password;
			EditText username;
			EditText email;
			TextView privacytextview;
			
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
				View v = inflater.inflate(R.layout.gallery_signup, container, false);
				
				privacytextview = (TextView)v.findViewById(R.id.privacytextview);
				username = (EditText) v.findViewById(R.id.username);
				password = (EditText) v.findViewById(R.id.password);
				email = (EditText) v.findViewById(R.id.email);

				// Locate Buttons in gallery_signup.xml
				signup = (Button) v.findViewById(R.id.signup);
				
				// Sign up Button Click Listener
				signup.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						// Retrieve the text entered from the EditText
						usernametxt = username.getText().toString();
						emailtxt = email.getText().toString();
						passwordtxt = password.getText().toString();
						
						InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		                imm.hideSoftInputFromWindow(password.getWindowToken(), 0); 
						
						// Force user to fill up the form
						if (usernametxt.equals("") || passwordtxt.equals("") || emailtxt.equals("")) {
							Toast.makeText(getActivity(),
									"Please complete the sign up form",
									Toast.LENGTH_LONG).show();

						} else {
							// Save new user data into Parse.com Data Storage
							ParseUser user = new ParseUser();
							user.setUsername(usernametxt);
							user.setEmail(emailtxt);
							user.setPassword(passwordtxt);
							user.put("emailCopy", (emailtxt));
							user.put("fullname", (usernametxt));
							user.put("fullname_lower", (usernametxt));
							user.signUpInBackground(new SignUpCallback() {
								public void done(ParseException e) {
									if (e == null) {
										// Show a simple Toast message upon successful registration
										mCallback.navigateToTabFragment(newFragment, null);
										ParseUser currentUser = ParseUser.getCurrentUser();
										String struser = currentUser.getString("fullname");
										
										Toast.makeText(getActivity(),
												"Successfully Signed up, you're now logged in " + struser,
												Toast.LENGTH_LONG).show();
									} else {
										Toast.makeText(getActivity(),
												"Sign up Error", Toast.LENGTH_LONG)
												.show();
									}
								}
							});
						}

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
				
				
				privacytextview.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						
						Uri uri = Uri.parse("http://www.speedyreference.com/bicsi/androidappprivacy.html");
						Intent browserIntent = new Intent(Intent.ACTION_VIEW);
						browserIntent.setDataAndType(uri, "text/html");
						browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
						getActivity().startActivity(browserIntent);
						
					}
				});

	
				return v;
			}

			
			/**
			* Hides virtual keyboard
			*
			* @author kvarela
			*/
			protected void hideKeyboard(View view)
			{
			    InputMethodManager in = (InputMethodManager) ((getActivity())).getSystemService(Context.INPUT_METHOD_SERVICE);
			    in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
}
