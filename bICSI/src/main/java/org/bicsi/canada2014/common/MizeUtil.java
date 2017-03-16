package org.bicsi.canada2014.common;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

public class MizeUtil {
	static ProgressDialog progress;
	public static void showToast(Activity activity, String message) {
		if (message != null) {
			Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
		}
	}
	
	public static void showToast(Activity activity, int stringId) {
		String message = activity.getString(stringId);
		if (message != null) {
			Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
		}
	}

    /** Container Activity must implement this interface
     * 
     */
    public interface NavigateToTabFragmentListener {
        public void navigateToTabFragment(Fragment newFragment, int tabIndex, Bundle bundle);

		public void navigateToTabFragment(Fragment newFragment, Bundle bundle);

		public void navigateToTabFragment(int i);

		public void navigateToTabFragment(Fragment newFragment);

		public void navigateToTabFragmentClearHistory(Fragment feedFragment, Bundle bundle1);

		public void goToMap(Bundle bundle);

    }
    
   
    @SuppressWarnings("deprecation")
	public static void showDialog(final Activity activity,final int stringId){
    	if(activity == null){
    		return;
    	}
    	try {
			final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
			String message = activity.getString(stringId);
			alertDialog.setTitle("Info");
			alertDialog.setMessage(message);
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int which) {
			      alertDialog.dismiss();
			      
			   }
			});
			
			alertDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @SuppressWarnings("deprecation")
	public static void showDialog(final Activity activity,final String stringMsg){
    	final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
    	String message =stringMsg;
    	alertDialog.setTitle("Info");
    	alertDialog.setMessage(message);
    	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
    	   public void onClick(DialogInterface dialog, int which) {
    	      alertDialog.dismiss();
    	      /*if(stringId == R.string.loggedout){
    	    	  ((MainActivity)activity).navigateToTabFragment(0);
    	      }*/
     	   }
    	});
    	
    	alertDialog.show();
    }
    @SuppressWarnings("deprecation")
	public static void showDialog(final Activity activity, final int stringId,final NavigateToTabFragmentListener mCallback){
    	final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
    	String message = activity.getString(stringId);
    	alertDialog.setTitle("Info");
    	alertDialog.setMessage(message);
    	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
    	   public void onClick(DialogInterface dialog, int which) {
    	      try {
				alertDialog.dismiss();
				  
			} catch (Exception e) {
				//Auto-generated catch block
				e.printStackTrace();
			}
    	   }
    	});
      	alertDialog.show();
    }
    
	public static String hashStringStatic(String stringToHash) {
		if(stringToHash == null){
			return null;
		}
		String hashword = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(stringToHash.getBytes());
			BigInteger hash = new BigInteger(1, md5.digest());
			hashword = hash.toString(16);
		} catch (Exception e) {
			// ignore
		}
		return hashword;
	}
	
	public static String formatDate(String timestamp) {
		String formatted = null;

		SimpleDateFormat timeStampFormat = new SimpleDateFormat(
				"MM-dd-yyyy hh:mm:ss");
		Date myDate;
		try {
			myDate = timeStampFormat.parse(timestamp);
			Calendar dateStamp = new GregorianCalendar(2008, 01, 01);
			dateStamp.setTime(myDate);
			
			/////////////////////////////////////////////////////////

			
			Calendar nowDate = Calendar.getInstance();
			if ((nowDate.get(Calendar.YEAR) == dateStamp.get(Calendar.YEAR)) && (nowDate.get(Calendar.MONTH) == dateStamp.get(Calendar.MONTH)) && (nowDate.get(Calendar.DAY_OF_MONTH) == dateStamp.get(Calendar.DAY_OF_MONTH))) {
				SimpleDateFormat newTimeStampFormat = new SimpleDateFormat("hh:mm a");
				TimeZone myTime = TimeZone.getDefault();
				Calendar cal = Calendar.getInstance();
				TimeZone tz = cal.getTimeZone();
				String s = tz.getDisplayName();
				newTimeStampFormat.setTimeZone(tz); 
				formatted = newTimeStampFormat.format(myDate);
				if(formatted != null){
					if(formatted.subSequence(0, 1) != null){
						if(formatted.subSequence(0, 1).equals("0")){
							formatted = formatted.substring(1);
						}
					}
				}
			} else {
				SimpleDateFormat newTimeStampFormat = new SimpleDateFormat("MMMM dd, yyyy");
				formatted = newTimeStampFormat.format(myDate);
			}
				
			


		} catch (ParseException e) {
			//Auto-generated catch block
			e.printStackTrace();
		}

		return formatted;
	}
	
	static public String customNumberFormat(String pattern, double value) {
		DecimalFormat myFormatter = new DecimalFormat(pattern);
		String output = myFormatter.format(value);
		return output;
	}
	
	
	
	public static void showProgressDialog(final Activity activity,String title, String message){
		progress = new ProgressDialog(activity);
		progress.setTitle(title);
		progress.setMessage(message);
		progress.show();
	}
	public static void dismiss(){
		progress.dismiss();
	}
	
    public static void showTwoButtonDialog(final Activity activity, final int stringId){
    	final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
    	String message = activity.getString(stringId);
    	alertDialog.setTitle("");
    	alertDialog.setMessage(message);    	
    	alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
    	   public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
    	   }
    	});    	
    	alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"", new DialogInterface.OnClickListener() {
     	   public void onClick(DialogInterface dialog, int which) {
     	      try {
 				alertDialog.dismiss();
 				
 			} catch (Exception e) {
 				//Auto-generated catch block
 				e.printStackTrace();
 			}
     	   }
     	});
      	alertDialog.show();
    }

	@SuppressWarnings("deprecation")
	public static void showDialog(Activity activity, int titleId, int messageId) {
		if(activity == null){
    		return;
    	}
    	try {
			final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
			String title = activity.getString(titleId);
			String message = activity.getString(messageId);
			String ok = activity.getString(R.string.ok);
			alertDialog.setTitle(title);
			alertDialog.setMessage(message);
			alertDialog.setButton(ok, new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int which) {
			      alertDialog.dismiss();
			   }
			});
			
			alertDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * setLinkedinDisconnected()
	 * 
	 */
	public static void setLinkedinDisconnected(Activity activity) {
		SharedPreferences app_preferences = activity.getSharedPreferences("MIZE_PREF", 0);
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putString("LinkedinConnect", "FALSE");
		editor.commit();
	}
	
    /** Container Activity must implement this interface
     * 
     */
    public interface PromptReturnListener {
        public void sendReturnValue(String value);

    }
    
	public String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		String a = Build.BOARD;
		String b = Build.BOOTLOADER;
		String c = Build.BRAND;
		String d = Build.CPU_ABI;
		String e = Build.CPU_ABI2;
		String f = Build.DEVICE;
		String g = Build.DISPLAY;
		String h = Build.FINGERPRINT;
		String i = Build.HARDWARE;
		String j = Build.HOST;
		String k = Build.ID;
		String l = Build.PRODUCT;
		String m = Build.RADIO;
		String n = Build.SERIAL;
		String o = Build.TAGS;
		String p = Build.TYPE;
		String q = Build.UNKNOWN;
		String r = Build.USER;

		if (model.startsWith(manufacturer)) {
			return model;
		} else {
			return manufacturer + " " + model;
		}
	}

	public String getPhoneCarrier(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		// Get IMEI Number of Phone
		String IMEINumber = tm.getDeviceId();

		// Get Subscriber ID
		String subscriberID = tm.getDeviceId();

		// Get SIM Serial Number
		String SIMSerialNumber = tm.getSimSerialNumber();

		// Get Network Country ISO Code
		String networkCountryISO = tm.getNetworkCountryIso();

		// Get SIM Country ISO Code
		String SIMCountryISO = tm.getSimCountryIso();

		// Get the device software version
		String softwareVersion = tm.getDeviceSoftwareVersion();

		// Get the Voice mail number
		String voiceMailNumber = tm.getVoiceMailNumber();

		// Get the Phone Type CDMA/GSM/NONE
		// /Get the type of network you are connected with
		int phoneType = tm.getPhoneType();

		switch (phoneType) {
		case (TelephonyManager.PHONE_TYPE_CDMA):
			// your code
			break;
		case (TelephonyManager.PHONE_TYPE_GSM):
			// your code
			break;
		case (TelephonyManager.PHONE_TYPE_NONE):
			// your code
			break;
		}

		// Find whether the Phone is in Roaming, returns true if in roaming
		/*
		 * boolean isRoaming=tm.isNetworkRoaming(); if(isRoaming) String
		 * phoneDetails+="\nIs In Roaming : "+"YES"; else //
		 * phoneDetails+="\nIs In Roaming : "+"NO";
		 */

		// Get the SIM state/Details
		int SIMState = tm.getSimState();
		switch (SIMState) {
		case TelephonyManager.SIM_STATE_ABSENT:
			// your code
			break;
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			// your code
			break;
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			// your code
			break;
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			// your code
			break;
		case TelephonyManager.SIM_STATE_READY:
			// your code
			break;
		case TelephonyManager.SIM_STATE_UNKNOWN:
			// your code
			break;

		}

		int simState = tm.getSimState();
		switch (simState) {
		case (TelephonyManager.SIM_STATE_ABSENT):
			break;
		case (TelephonyManager.SIM_STATE_NETWORK_LOCKED):
			break;
		case (TelephonyManager.SIM_STATE_PIN_REQUIRED):
			break;
		case (TelephonyManager.SIM_STATE_PUK_REQUIRED):
			break;
		case (TelephonyManager.SIM_STATE_UNKNOWN):
			break;
		case (TelephonyManager.SIM_STATE_READY): {
			// Get the SIM country ISO code
			String simCountry = tm.getSimCountryIso();
			// Get the operator code of the active SIM (MCC + MNC)
			String simOperatorCode = tm.getSimOperator();
			// Get the name of the SIM operator
			String simOperatorName = tm.getSimOperatorName();
			// -- Requires READ_PHONE_STATE uses-permission --
			// Get the SIM�s serial number
			String simSerial = tm.getSimSerialNumber();
		}
		}

		// Get the type of network you are connected with
		int networkType = tm.getNetworkType();
		switch (networkType) {
		case (TelephonyManager.NETWORK_TYPE_1xRTT):// "  Your Code ":
			break;
		case (TelephonyManager.NETWORK_TYPE_CDMA):// "  Your Code ":
			break;
		case (TelephonyManager.NETWORK_TYPE_EDGE):// "  Your Code ":
			break;
		case (TelephonyManager.NETWORK_TYPE_EVDO_0):// "  Your Code ":
			break;
		}

		// Get connected network country ISO code
		String networkCountry = tm.getNetworkCountryIso();

		// Get the connected network operator ID (MCC + MNC)
		String networkOperatorId = tm.getNetworkOperator();

		// Get the connected network operator name
		String networkName = tm.getNetworkOperatorName();

		return networkName;
	}

}
