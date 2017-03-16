package org.bicsi.canada2014;

public class Contstants {
	public static String URL = "URL";

	public static final String CONSUMER_KEY = "xEVNUzlUsNS22FQC0JvjVA";
	public static final String CONSUMER_SECRET= "HphuyiwQD142egBOXXMXG7F8OsMoRREQevrdwUIekBc";

	public static final String REQUEST_URL = "https://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL = "https://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";

	public static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-twitter";
	public static final String OAUTH_CALLBACK_HOST = "callback";
	public static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
}