package org.bicsi.canada2014;

import oauth.signpost.OAuth;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.content.SharedPreferences;

public class TwitterUtils {

	 public static boolean isAuthenticated(SharedPreferences prefs) {

	  String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
	  String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
	  
	  Twitter twitter;
	try {
		AccessToken a = new AccessToken(token,secret);
		  twitter = new TwitterFactory().getInstance();
		  twitter.setOAuthConsumer(org.bicsi.canada2014.Contstants.CONSUMER_KEY, org.bicsi.canada2014.Contstants.CONSUMER_SECRET);
		  twitter.setOAuthAccessToken(a);
	} catch (Exception e1) {
		return false;
	}
	  
	  try {
	   twitter.getAccountSettings();
	   return true;
	  } catch (TwitterException e) {
	   return false;
	  }
	 }
	 
	 public static void sendTweet(SharedPreferences prefs,String msg) throws Exception {
	  String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
	  String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
	  
	  AccessToken a = new AccessToken(token,secret);
	  Twitter twitter = new TwitterFactory().getInstance();
	  twitter.setOAuthConsumer(org.bicsi.canada2014.Contstants.CONSUMER_KEY, org.bicsi.canada2014.Contstants.CONSUMER_SECRET);
	  twitter.setOAuthAccessToken(a);
	        try {
				twitter.updateStatus(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
	 } 
	}