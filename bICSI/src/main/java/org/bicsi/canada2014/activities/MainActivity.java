package org.bicsi.canada2014.activities;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bicsi.canada2014.fragment.PlannerScheduleFragment;
import org.bicsi.canada2017.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import org.bicsi.canada2014.adapter.SQLiteDB;
import org.bicsi.canada2014.adapter.SQLiteDBcShed;
import org.bicsi.canada2014.adapter.SQLiteDBAllData;
import org.bicsi.canada2014.common.ServiceHandler;
import org.bicsi.canada2014.common.MizeUtil;
import org.bicsi.canada2014.common.MizeUtil.PromptReturnListener;
import org.bicsi.canada2014.fragment.AboutUsFragment;
import org.bicsi.canada2014.fragment.AlertsFragment;
import org.bicsi.canada2014.fragment.GalleryLoginFragment;
import org.bicsi.canada2014.fragment.HomeFragment;
import org.bicsi.canada2014.fragment.WebviewFragment;
import org.bicsi.canada2014.web.AsyncTaskPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.pushio.manager.PushIOManager;

import com.pushwoosh.PushManager;
import com.pushwoosh.PushManager.RichPageListener;
import com.pushwoosh.BasePushMessageReceiver;
import com.pushwoosh.BaseRegistrationReceiver;

public class MainActivity extends Activity implements
        MizeUtil.NavigateToTabFragmentListener, PromptReturnListener, OnClickListener /*EhallSchedFragment2.OnSchedItemSelectedListener*/{

    private static final String TAG = "MainActivity";
    private static final int SECOND = 1000;
    private static final int WAIT_TIME = 120 * SECOND;
    private LinearLayout tab1;
    private LinearLayout tab2;
    private LinearLayout tab3;
    private LinearLayout tab4;
    private LinearLayout tab5;
    private TextView txtTab1;
    private TextView txtTab2;
    private TextView txtTab3;
    private TextView txtTab4;
    private TextView txtTab5;
    private ImageView imgTab1;
    private ImageView imgTab2;
    private ImageView imgTab3;
    private ImageView imgTab4;
    private ImageView imgTab5;
    private ArrayList<Fragment> tabList1 = new ArrayList<Fragment>();
    private ArrayList<Fragment> tabList2 = new ArrayList<Fragment>();
    private ArrayList<Fragment> tabList3 = new ArrayList<Fragment>();
    private ArrayList<Fragment> tabList4 = new ArrayList<Fragment>();
    private ArrayList<Fragment> tabList5 = new ArrayList<Fragment>();
    private AsyncTaskPost mPost = null;
    private int mPreviousTabIndex = 0;
    private int mCurrentTabIndex = 0;
    private Context mContext = this;
    public String mCategoryName = null;
    public int mCategoryId = 0;
    public String mProductQuery = null;
    public int mSearchType = 0;
    public String mFriendQuery = null;
    public String mBrandQuery = "";
    public String mDepartmentName = null;
    public String mPushNotificationCode = null;
    private TextView txtHomeUnreadCount;
    public String mPostMessage = null;
    private Runnable mMyRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                getUnreadCount();
                Handler myHandler = new Handler();
                myHandler.postDelayed(mMyRunnable, WAIT_TIME);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    };

    private PushIOManager mPushIOManager;
    public String mWebViewURL = null;

    /*********************************************************************************
     * receiver
     *********************************************************************************/
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            handleIntent(intent);
        }
    };

    //begin PushWoosh receiver
    //Registration receiver
    BroadcastReceiver mBroadcastReceiver = new BaseRegistrationReceiver()
    {
        @Override
        public void onRegisterActionReceive(Context context, Intent intent)
        {
            checkMessage(intent);
        }
    };

    //Push message receiver
    private BroadcastReceiver mReceiver = new BasePushMessageReceiver()
    {
        @Override
        protected void onMessageReceive(Intent intent)
        {
            //JSON_DATA_KEY contains JSON payload of push notification.
            showMessage("push message is " + intent.getExtras().getString(JSON_DATA_KEY));
        }
    };

    //Registration of the receivers
    public void registerReceivers()
    {
        IntentFilter intentFilter = new IntentFilter(getPackageName() + ".action.PUSH_MESSAGE_RECEIVE");

        registerReceiver(mReceiver, intentFilter, getPackageName() +".permission.C2D_MESSAGE", null);

        registerReceiver(mBroadcastReceiver, new IntentFilter(getPackageName() + "." + PushManager.REGISTER_BROAD_CAST_ACTION));
    }

    public void unregisterReceivers()
    {
        //Unregister receivers on pause
        try
        {
            unregisterReceiver(mReceiver);
        }
        catch (Exception e)
        {
            // pass.
        }

        try
        {
            unregisterReceiver(mBroadcastReceiver);
        }
        catch (Exception e)
        {
            //pass through
        }
    }

    //end pushwoosh receiver

    /**
     * handleIntent
     *
     */
    private void handleIntent(Intent intent) {
        if (intent.getAction().equals(
                AsyncTaskPost.GET_UNREAD_COUNT_RETURN_INTENT)) {
            boolean success = intent.getBooleanExtra(
                    AsyncTaskPost.GET_UNREAD_COUNT_SUCCESS_VALUE, false);
            if (success) {
                String jsonString = intent
                        .getStringExtra(AsyncTaskPost.GET_UNREAD_COUNT_RETURN_VALUES);

                System.out.println("json response: "+jsonString);

                if(jsonString != null){
                    if(!jsonString.startsWith("[")){
                        jsonString = "["+jsonString+"]";
                    }

                }


            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
        EasyTracker.getInstance(mContext).activityStart(this);
    }

    //private SQLiteDB sqlite_obj;
    //List<String> list1, list2, list3, list4, list5;
    private SimpleCursorAdapter dataAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sqlite_obj = new SQLiteDB(MainActivity.this);

        //pushwoosh begin
        //Register receivers for push notifications
        registerReceivers();

        //Create and start push manager
        PushManager pushManager = PushManager.getInstance(this);

        //Start push manager, this will count app open for Pushwoosh stats as well
        try {
            pushManager.onStartup(this);
        }
        catch(Exception e)
        {
            //push notifications are not available or AndroidManifest.xml is not configured properly
        }

        //Register for push!
        pushManager.registerForPushNotifications();

        checkMessage(getIntent());

        // other code
        //pushwoosh end


        GetURL();

        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseAnalytics.trackAppOpened(getIntent());

        Crittercism.initialize(getApplicationContext(), "b77b31a420b041e69e8bf800a852089000555300");

        setupTabs();
        resetTabs();

        Handler myHandler = new Handler();
        myHandler.postDelayed(mMyRunnable, 1);

        GetURLCSched();

        new GetJSONData().execute();

    }

    ///
    private class LongOperation  extends AsyncTask<String, Void, Void> {

        private SQLiteDB sqlite_obj = new SQLiteDB(mContext);
        List<String> list1, list2, list3, list4, list5;


        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
        String data ="";

        int sizeData = 0;



        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)

            Dialog.setMessage("Updating data...");
            Dialog.show();

            try{
                // Set Request parameter
                data +="&" + URLEncoder.encode("data", "UTF-8");

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            /************ Make Post Call To Web Server ***********/
            BufferedReader reader=null;

            // Send data
            try
            {

                // Defined URL  where to send data
                URL url = new URL(urls[0]);

                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( data );
                wr.flush();

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + " ");
                }

                // Append Server Response To Content String
                Content = sb.toString();
            }
            catch(Exception ex)
            {
                Error = ex.getMessage();
            }
            finally
            {
                try
                {

                    reader.close();
                }

                catch(Exception ex) {}
            }

            /*****************************************************/
            return null;
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            if (Error != null) {

                System.out.println("Output : "+Error);

            } else {

                // Show Response Json On Screen (activity)
                System.out.println( Content );

                /****************** Start Parse Response JSON Data *************/

                String OutputData = "";
                  /*JSONObject jsonResponse;*/

                try {



                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                       /*jsonResponse = new JSONObject(Content);*/

                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    /*******  Returns null otherwise.  *******/
                       /*JSONArray jsonMainNode = jsonResponse.optJSONArray("");*/

                    JSONArray jsonMainNode = new JSONArray(Content);

                    /*********** Process each JSON Node ************/

                    int lengthJsonArr = jsonMainNode.length();

                    list1 = new ArrayList<String>();
                    list2 = new ArrayList<String>();
                    list3 = new ArrayList<String>();
                    list4 = new ArrayList<String>();
                    list5 = new ArrayList<String>();

                    for(int i=0; i < lengthJsonArr; i++)
                    {
                        /****** Get Object for each JSON node.***********/
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        /******* Fetch node values **********/
                        //String id       = jsonChildNode.optString("id").toString();
                        String id = jsonChildNode.optString("id").toString();
                        String scheduleDate     = jsonChildNode.optString("scheduleDate").toString();
                        String sessionName = jsonChildNode.optString("sessionName").toString();
                        String sessionTime = jsonChildNode.optString("sessionTime").toString();
                        String desc = jsonChildNode.optString("desc").toString();

                        list1.add(jsonChildNode.getString("id"));
                        list2.add(jsonChildNode.getString("scheduleDate"));
                        list3.add(jsonChildNode.getString("sessionName"));
                        list4.add(jsonChildNode.getString("sessionTime"));
                        list5.add(jsonChildNode.getString("desc"));




                        OutputData += "ID: "+ id +" "
                                + "ScheduleDate: "+ scheduleDate +" "
                                + "ID sessionname: "+ sessionName +" "
                                + "SessionTime: "+ sessionTime +" "
                                + "Desc: "+ desc +" "
                                +"\n";

                        sqlite_obj.open();

                        sqlite_obj.deleteAll();

                        for(int j=0; j<list1.size(); j++) {

                            sqlite_obj.insert(list1.get(j).toString(), list2.get(j).toString(), list3.get(j).toString(), list4.get(j).toString(), list5.get(j).toString());


                        }

                        sqlite_obj.close();

                    }
                    /****************** End Parse Response JSON Data *************/

                    //Show Parsed Output on screen (activity)
                       /*jsonParsed.setText( OutputData );*/
                    System.out.println(OutputData);

                    //Generate ListView from SQLite Database
                    //displayListView();





                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }

        }

    }

    private class LongOperationCSched  extends AsyncTask<String, Void, Void> {

        private SQLiteDBcShed sqlite_obj = new SQLiteDBcShed(mContext);
        List<String> list1, list2, list3;

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
        String data ="";

        int sizeData = 0;



        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)

            Dialog.setMessage("Updating data...");
            Dialog.show();

            try{
                // Set Request parameter
                data +="&" + URLEncoder.encode("data", "UTF-8");

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            /************ Make Post Call To Web Server ***********/
            BufferedReader reader=null;

            // Send data
            try
            {

                // Defined URL  where to send data
                URL url = new URL(urls[0]);

                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( data );
                wr.flush();

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + " ");
                }

                // Append Server Response To Content String
                Content = sb.toString();
            }
            catch(Exception ex)
            {
                Error = ex.getMessage();
            }
            finally
            {
                try
                {

                    reader.close();
                }

                catch(Exception ex) {}
            }

            /*****************************************************/
            return null;
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            if (Error != null) {

                System.out.println("Output : "+Error);

            } else {

                // Show Response Json On Screen (activity)
                System.out.println( Content );

                /****************** Start Parse Response JSON Data *************/

                String OutputData = "";
                /*JSONObject jsonResponse;*/

                try {



                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                     /*jsonResponse = new JSONObject(Content);*/

                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    /*******  Returns null otherwise.  *******/
                     /*JSONArray jsonMainNode = jsonResponse.optJSONArray("");*/

                    JSONArray jsonMainNode = new JSONArray(Content);

                    /*********** Process each JSON Node ************/

                    int lengthJsonArr = jsonMainNode.length();

                    list1 = new ArrayList<String>();
                    list2 = new ArrayList<String>();
                    list3 = new ArrayList<String>();


                    for(int i=0; i < lengthJsonArr; i++)
                    {
                        /****** Get Object for each JSON node.***********/
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        /******* Fetch node values **********/
                        String id       = jsonChildNode.optString("id").toString();
                        String day     = jsonChildNode.optString("day").toString();
                        String date = jsonChildNode.optString("date").toString();

                        list1.add(jsonChildNode.getString("id"));
                        list2.add(jsonChildNode.getString("day"));
                        list3.add(jsonChildNode.getString("date"));


                        OutputData += "ID: "+ id +" "
                                + "Day: "+ day +" "
                                + "Date: "+ date +" "
                                +"\n";

                        sqlite_obj.open();

                        sqlite_obj.deleteAll();

                        for(int j=0; j<list1.size(); j++) {

                            sqlite_obj.insert(list1.get(j).toString(), list2.get(j).toString(), list3.get(j).toString());


                        }

                        sqlite_obj.close();

                    }
                    /****************** End Parse Response JSON Data *************/

                    //Show Parsed Output on screen (activity)
                     /*jsonParsed.setText( OutputData );*/
                    System.out.println(OutputData);

                    //Generate ListView from SQLite Database
                    //displayListView();





                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }

        }

    }

    private class GetJSONData  extends AsyncTask<Void, Void, Void> {
        //call webservice
        private ProgressDialog pDialog;
        private static final String url = "https://webservice.bicsi.org/json/reply/MobSession?SessionAltCd=CN-WINTER-FL-0117";
        //private final Uri uri = Uri.parse("https://webservice.bicsi.org/json/reply/MobSession?SessionAltCd=CN-FALL-NV-0915");
        private String myLongStr;
        private String myLongStrEnd;

        private static final String TAG_FUNCTIONS = "Functions";

        private SQLiteDBAllData sqlite_obj = new SQLiteDBAllData(mContext);

        List<String> list1,list2,list3,list4,list5,list6,list7,list8,list9,list10,list11,list12,list13,list14,list15,list16,list17,list18,list19,list20,list21,list22,list23,list24,list25,list26,list27,list28,list29,list30,list31,list32,list33,list34,list35,list36,list37,list38,list39,list40,list41,list42,list43;

        JSONArray functions = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Updating data...");
            pDialog.setCancelable(false);
            pDialog.show();

            sqlite_obj.open();
            sqlite_obj.deleteAll();
            sqlite_obj.close();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            //String jsonStr = sh.makeServiceCall(uri, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            String OutputData = "";

            if (jsonStr != null) {


                try {


                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    functions = jsonObj.getJSONArray(TAG_FUNCTIONS);

                    // looping through All Contacts
                    for (int i = 0; i < functions.length(); i++) {
                        JSONObject c = functions.getJSONObject(i);

                        list1= new ArrayList<String>();
                        list2= new ArrayList<String>();
                        list3= new ArrayList<String>();
                        list4= new ArrayList<String>();
                        list5= new ArrayList<String>();
                        list6= new ArrayList<String>();
                        list7= new ArrayList<String>();
                        list8= new ArrayList<String>();
                        list9= new ArrayList<String>();
                        list10= new ArrayList<String>();
                        list11= new ArrayList<String>();
                        list12= new ArrayList<String>();
                        list13= new ArrayList<String>();
                        list14= new ArrayList<String>();
                        list15= new ArrayList<String>();
                        list16= new ArrayList<String>();
                        list17= new ArrayList<String>();
                        list18= new ArrayList<String>();
                        list19= new ArrayList<String>();
                        list20= new ArrayList<String>();
                        list21= new ArrayList<String>();
                        list22= new ArrayList<String>();
                        list23= new ArrayList<String>();
                        list24= new ArrayList<String>();
                        list25= new ArrayList<String>();
                        list26= new ArrayList<String>();
                        list27= new ArrayList<String>();
                        list28= new ArrayList<String>();
                        list29= new ArrayList<String>();
                        list30= new ArrayList<String>();
                        list31= new ArrayList<String>();
                        list32= new ArrayList<String>();
                        list33= new ArrayList<String>();
                        list34= new ArrayList<String>();
                        list35= new ArrayList<String>();
                        list36= new ArrayList<String>();
                        list37= new ArrayList<String>();
                        list38= new ArrayList<String>();
                        list39= new ArrayList<String>();
                        list40= new ArrayList<String>();
                        list41= new ArrayList<String>();
                        list42= new ArrayList<String>();
                        list43= new ArrayList<String>();
                        //list44= new ArrayList<String>();





					    /*String FUNCTIONCD= c.optString("FUNCTIONCD").toString();
						String functiontitle= c.optString("functiontitle").toString();
						String functiondescription= c.optString("functiondescription").toString();
						String LOCATIONNAME= c.optString("LOCATIONNAME").toString();
						String fucntioindate= c.optString("fucntioindate").toString();
						String functionStartTime= c.optString("functionStartTime").toString();
						String functionEndTime= c.optString("functionEndTime").toString();
						String trainer1firstname= c.optString("trainer1firstname").toString();
						String trainer1lastname= c.optString("trainer1lastname").toString();
						String trainer1org= c.optString("trainer1org").toString();
						String trainer1city= c.optString("trainer1city").toString();
						String trainer1state= c.optString("trainer1state").toString();
						String trainer1country= c.optString("trainer1country").toString();
						String trainer2firstname= c.optString("trainer2firstname").toString();
						String trainer2lastname= c.optString("trainer2lastname").toString();
						String trainer2org= c.optString("trainer2org").toString();
						String trainer2city= c.optString("trainer2city").toString();
						String trainer2state= c.optString("trainer2state").toString();
						String trainer2country= c.optString("trainer2country").toString();
						String trainer3firstname= c.optString("trainer3firstname").toString();
						String trainer3lastname= c.optString("trainer3lastname").toString();
						String trainer3org= c.optString("trainer3org").toString();
						String trainer3city= c.optString("trainer3city").toString();
						String trainer3state= c.optString("trainer3state").toString();
						String trainer3country= c.optString("trainer3country").toString();
						String trainer4firstname= c.optString("trainer4firstname").toString();
						String trainer4lastname= c.optString("trainer4lastname").toString();
						String trainer4org= c.optString("trainer4org").toString();
						String trainer4city= c.optString("trainer4city").toString();
						String trainer4state= c.optString("trainer4state").toString();
						String trainer4country= c.optString("trainer4country").toString();
						String trainer5firstname= c.optString("trainer5firstname").toString();
						String trainer5lastname= c.optString("trainer5lastname").toString();
						String trainer5org= c.optString("trainer5org").toString();
						String trainer5city= c.optString("trainer5city").toString();
						String trainer5state= c.optString("trainer5state").toString();
						String trainer5country= c.optString("trainer5country").toString();
						String trainer6firstname= c.optString("trainer6firstname").toString();
						String trainer6lastname= c.optString("trainer6lastname").toString();
						String trainer6org= c.optString("trainer6org").toString();
						String trainer6city= c.optString("trainer6city").toString();
						String trainer6state= c.optString("trainer6state").toString();
						String trainer6country= c.optString("trainer6country").toString();*/



                        list1.add(c.getString("FUNCTIONCD"));
                        list2.add(c.getString("functiontitle"));
                        list3.add(c.getString("functiondescription"));
                        list4.add(c.getString("LOCATIONNAME"));
                        list5.add(c.getString("fucntioindate"));
                        list6.add(c.getString("functionStartTime"));
                        list7.add(c.getString("functionEndTime"));
                        list8.add(c.getString("trainer1firstname"));
                        list9.add(c.getString("trainer1lastname"));
                        list10.add(c.getString("trainer1org"));
                        list11.add(c.getString("trainer1city"));
                        list12.add(c.getString("trainer1state"));
                        list13.add(c.getString("trainer1country"));
                        list14.add(c.getString("trainer2firstname"));
                        list15.add(c.getString("trainer2lastname"));
                        list16.add(c.getString("trainer2org"));
                        list17.add(c.getString("trainer2city"));
                        list18.add(c.getString("trainer2state"));
                        list19.add(c.getString("trainer2country"));
                        list20.add(c.getString("trainer3firstname"));
                        list21.add(c.getString("trainer3lastname"));
                        list22.add(c.getString("trainer3org"));
                        list23.add(c.getString("trainer3city"));
                        list24.add(c.getString("trainer3state"));
                        list25.add(c.getString("trainer3country"));
                        list26.add(c.getString("trainer4firstname"));
                        list27.add(c.getString("trainer4lastname"));
                        list28.add(c.getString("trainer4org"));
                        list29.add(c.getString("trainer4city"));
                        list30.add(c.getString("trainer4state"));
                        list31.add(c.getString("trainer4country"));
                        list32.add(c.getString("trainer5firstname"));
                        list33.add(c.getString("trainer5lastname"));
                        list34.add(c.getString("trainer5org"));
                        list35.add(c.getString("trainer5city"));
                        list36.add(c.getString("trainer5state"));
                        list37.add(c.getString("trainer5country"));
                        list38.add(c.getString("trainer6firstname"));
                        list39.add(c.getString("trainer6lastname"));
                        list40.add(c.getString("trainer6org"));
                        list41.add(c.getString("trainer6city"));
                        list42.add(c.getString("trainer6state"));
                        list43.add(c.getString("trainer6country"));
                        //list44.add(c.getString("planner"));


                        String fucntioindate= c.optString("fucntioindate").toString();
                        String functionStartTime= c.optString("functionStartTime").toString();
                        String functionEndTime= c.optString("functionEndTime").toString();

                        String myDate = fucntioindate + ", " + functionStartTime;
                        String myEndDate = fucntioindate + ", " + functionEndTime;

                        Long myLDate = longDate(myDate);
                        Long myLEndDate = longDate(myEndDate);

                        myLongStr = String.valueOf(myLDate);
                        myLongStrEnd = String.valueOf(myLEndDate);


	                       /*OutputData += "FUNCTIONCD: "+ FUNCTIONCD +" "
	                                   + "Title: "+ functiontitle +" "
	                                   + "Desc: "+ functiondescription +" "
	                                   +"\n";*/

                        sqlite_obj.open();


                        for(int j=0; j<list1.size(); j++) {


                            sqlite_obj.insert(list1.get(j).toString(), list2.get(j).toString(), list3.get(j).toString(), list4.get(j).toString(), list5.get(j).toString(), myLongStr, list6.get(j).toString(), myLongStrEnd, list7.get(j).toString(), list8.get(j).toString(), list9.get(j).toString(), list10.get(j).toString(), list11.get(j).toString(), list12.get(j).toString(), list13.get(j).toString(), list14.get(j).toString(), list15.get(j).toString(), list16.get(j).toString(), list17.get(j).toString(), list18.get(j).toString(), list19.get(j).toString(), list20.get(j).toString(), list21.get(j).toString(), list22.get(j).toString(), list23.get(j).toString(), list24.get(j).toString(), list25.get(j).toString(), list26.get(j).toString(), list27.get(j).toString(), list28.get(j).toString(), list29.get(j).toString(), list30.get(j).toString(), list31.get(j).toString(), list32.get(j).toString(), list33.get(j).toString(), list34.get(j).toString(), list35.get(j).toString(), list36.get(j).toString(), list37.get(j).toString(), list38.get(j).toString(), list39.get(j).toString(), list40.get(j).toString(), list41.get(j).toString(), list42.get(j).toString(), list43.get(j).toString());


                        }

                        sqlite_obj.close();

                    }

                    System.out.println(OutputData);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

        }


    }

    public long longDate (String sdate){

        //int intDate = (int) System.currentTimeMillis(); //Initializes var with current time in case parsing fails
        long longDate = System.currentTimeMillis(); //Initializes var with current time in case parsing fails
        SimpleDateFormat format = new SimpleDateFormat("mm-dd-yyyy, hh:mm a", Locale.US); //Describes date pattern
        format.setLenient(false); //enforces strict pattern matching

        try {
            Date date = format.parse(sdate); //converts string to a date object
            longDate = date.getTime();
        } catch (ParseException e) {
        }

        return longDate;
    }


    public void GetURL(){
        // WebServer Request URL
        String serverURL = "http://speedyreference.com/ehscheduleW17.php";

        // Use AsyncTask execute Method To Prevent ANR Problem
        new LongOperation().execute(serverURL);
    }

    public void GetURLCSched(){
        // WebServer Request URL
        String serverURL = "http://speedyreference.com/cscheduleW17.php";

        // Use AsyncTask execute Method To Prevent ANR Problem
        new LongOperationCSched().execute(serverURL);
    }


    ////
    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }


    private void setupTabs()
    {

        tab1 = (LinearLayout) findViewById(R.id.layoutTab1);
        ((ImageView)findViewById(R.id.imgTab1)).setImageResource(R.drawable.icons_home_sel);
        ((TextView)findViewById(R.id.txtHome)).setTextColor(getResources().getColor(R.color.bicsi_yellow));
        tab2 = (LinearLayout) findViewById(R.id.layoutTab2);
        tab3 = (LinearLayout) findViewById(R.id.layoutTab3);
        tab4 = (LinearLayout) findViewById(R.id.layoutTab4);
        tab5 = (LinearLayout) findViewById(R.id.layoutTab5);


        txtTab1 = (TextView)findViewById(R.id.txtHome);

        txtTab2 = (TextView)findViewById(R.id.txtAlerts);
        txtTab3 = (TextView)findViewById(R.id.txtSocial);
        txtTab4 = (TextView)findViewById(R.id.txtMyBicsi);
        txtTab5 = (TextView) findViewById(R.id.txtMySched);

        imgTab1 = (ImageView)findViewById(R.id.imgTab1);
        imgTab2 = (ImageView)findViewById(R.id.imgTab2);
        imgTab3 = (ImageView)findViewById(R.id.imgTab3);
        imgTab4 = (ImageView)findViewById(R.id.imgTab4);
        txtHomeUnreadCount = (TextView)findViewById(R.id.txtAlertsUnreadCount);
        imgTab5 = (ImageView) findViewById(R.id.imgTab5);

        tab1.setOnClickListener(textView_listener);
        tab2.setOnClickListener(textView_listener);
        tab3.setOnClickListener(textView_listener);
        tab4.setOnClickListener(textView_listener);
        tab5.setOnClickListener(textView_listener);

    }

    public static void trimCache(Context context) {
        File dir = context.getFilesDir();
        if(dir!= null && dir.isDirectory()){
            File[] children = dir.listFiles();
            if (children == null) {
                // Either dir does not exist or is not a directory
            } else {
                File temp;
                for (int i = 0; i < children.length; i++) {
                    temp = children[i];
                    temp.delete();
                }
            }
        }
    }

    /**resetTabs
     *
     */
    private void resetTabs()
    {
        trimCache(this);
        Fragment oldTab = null;
        if (tabList1.size() > 0) {
            oldTab = tabList1.remove(tabList1.size() - 1);
            if (oldTab != null) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(oldTab);
                fragmentTransaction.commit();
            }
        }
        if (tabList2.size() > 0) {
            oldTab = tabList2.remove(tabList2.size() - 1);
            if (oldTab != null) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(oldTab);
                fragmentTransaction.commit();
            }
        }
        if (tabList3.size() > 0) {
            oldTab = tabList3.remove(tabList3.size() - 1);
            if (oldTab != null) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(oldTab);
                fragmentTransaction.commit();
            }
        }
        if (tabList4.size() > 0) {
            oldTab = tabList4.remove(tabList4.size() - 1);
            if (oldTab != null) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(oldTab);
                fragmentTransaction.commit();
            }
        }
        if (tabList5.size() > 0) {
            oldTab = tabList5.remove(tabList5.size() - 1);
            if (oldTab != null) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(oldTab);
                fragmentTransaction.commit();
            }
        }



        ActionBar actionBar = getActionBar();
        tabList1.clear();
        tabList2.clear();
        tabList3.clear();
        tabList4.clear();
        tabList5.clear();

        actionBar.setDisplayShowTitleEnabled(true);

        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.t_bckgrd));

        tabList1.add(new HomeFragment());
        tabList2.add(new AlertsFragment());
        tabList3.add(new GalleryLoginFragment());
        tabList5.add(new PlannerScheduleFragment());


        Bundle bundlem = new Bundle();
        //bundlem.putString("URL", "http://www.bicsi.org/m/mybicsi.aspx");
        bundlem.putString("URL", "http://www.bicsi.org/m/Login_App.aspx");
        Fragment newFragmentm = new WebviewFragment();
        newFragmentm.setArguments(bundlem);
        tabList4.add(newFragmentm);


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment tab = null;
        tab = tabList1.get(tabList1.size() - 1);

        fragmentTransaction.add(R.id.content, tab);
        fragmentTransaction.commit();
        //addToTabList(tab, tabIndex);


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    @Override
    protected void onNewIntent(Intent intent) {


        super.onNewIntent(intent);
        setIntent(intent);
        checkMessage(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public void sendReturnValue(String value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void goToMap(Bundle bundle) {
        // TODO Auto-generated method stub

    }

    public void navigateToTabFragment(Fragment newFragment, int tabIndex,
                                      Bundle bundle) {
        if (newFragment == null) {
            return;
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment oldFragment = null;
        switch(mCurrentTabIndex){
            case 0:
                if (tabList1.size() > 0) {
                    oldFragment = tabList1.get(tabList1.size() - 1);
                } else {
                    finish();
                }
                break;
            case 1:
                if (tabList2.size() > 0) {
                    oldFragment = tabList2.get(tabList2.size() - 1);
                } else {
                    finish();
                }
                break;
            case 2:
                if (tabList3.size() > 0) {
                    oldFragment = tabList3.get(tabList3.size() - 1);
                } else {
                    finish();
                }
                break;
            case 3:
                if (tabList4.size() > 0) {
                    oldFragment = tabList4.get(tabList4.size() - 1);
                } else {
                    finish();
                }
                break;
            case 4:
                if (tabList5.size() > 0) {
                    oldFragment = tabList5.get(tabList5.size() - 1);
                } else {
                    finish();
                }
                break;
        }
        fragmentTransaction.remove(oldFragment);
        fragmentTransaction.commit();


        mPreviousTabIndex = mCurrentTabIndex;
        mCurrentTabIndex = tabIndex;

        fragmentTransaction = fragmentManager.beginTransaction();
        // You can then add a fragment using the add() method, specifying the
        // fragment to add and the view in which to insert it. For example:
        if(bundle != null){
            newFragment.setArguments(bundle);
        }
        if(newFragment.isAdded()){
            fragmentTransaction.show(newFragment);
            newFragment.onResume();
        } else {
            fragmentTransaction.add(R.id.content, newFragment);
        }
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        addToTabList(newFragment, tabIndex);


    }

    @Override
    public void navigateToTabFragment(Fragment newFragment, Bundle bundle) {

        int tabIndex = mCurrentTabIndex;
        navigateToTabFragment(newFragment, tabIndex, bundle);
    }

    @Override
    public void navigateToTabFragment(int tabIndex) {
        if(tabIndex == mCurrentTabIndex){
            resetTab(mCurrentTabIndex);
            return;
        }
        if (tabList1.size() == 0)
            return;
        if (tabList2.size() == 0)
            return;
        if (tabList3.size() == 0)
            return;
        if (tabList4.size() == 0)
            return;
        if (tabList5.size() == 0)
            return;
        if (tabIndex == mCurrentTabIndex)
            return;

        Fragment newTab = null;
        switch (tabIndex) {
            case 0:
                newTab = this.tabList1.get(tabList1.size() - 1);
                break;
            case 1:
                newTab = this.tabList2.get(tabList2.size() - 1);
                break;
            case 2:
                newTab = this.tabList3.get(tabList3.size() - 1);
                break;
            case 3:
                newTab = this.tabList4.get(tabList4.size() - 1);
                break;
            case 4:
                newTab = this.tabList5.get(tabList5.size() - 1);
                break;
            default:
                return;
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment oldTab = null;
        switch (mCurrentTabIndex) {
            case 0:
                if (tabList1.size() > 0) {
                    oldTab = tabList1.get(tabList1.size() - 1);
                } else {
                    finish();
                }
                break;
            case 1:
                if (tabList2.size() > 0) {
                    oldTab = tabList2.get(tabList2.size() - 1);
                } else {
                    finish();
                }
                break;
            case 2:
                if (tabList3.size() > 0) {
                    oldTab = tabList3.get(tabList3.size() - 1);
                } else {
                    finish();
                }
                break;
            case 3:
                if (tabList4.size() > 0) {
                    oldTab = tabList4.get(tabList4.size() - 1);
                } else {
                    finish();
                }
                break;
            case 4:
                if (tabList5.size() > 0) {
                    oldTab = tabList5.get(tabList5.size() - 1);
                } else {
                    finish();
                }
                break;
            default:
                finish();
        }
        if ((newTab == null) || (oldTab == null)) {
            return;
        }
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(oldTab);
        fragmentTransaction.commit();

        mPreviousTabIndex = mCurrentTabIndex;
        mCurrentTabIndex = tabIndex;

        // You can then add a fragment using the add() method, specifying the
        // fragment to add and the view in which to insert it. For example:
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, newTab);
        fragmentTransaction.commit();
        resetTabIcons(tabIndex);
    }
    private void resetTabIcons(int tabIndex){
        switch (tabIndex) {
            case 0:
                imgTab1.setImageResource(R.drawable.icons_home_sel);
                txtTab1.setTextColor(getResources().getColor(R.color.bicsi_yellow));
                imgTab2.setImageResource(R.drawable.icons_alerts);
                txtTab2.setTextColor(Color.WHITE);
                imgTab3.setImageResource(R.drawable.icon_gallery);
                txtTab3.setTextColor(Color.WHITE);
                imgTab4.setImageResource(R.drawable.icons_mybicsi);
                txtTab4.setTextColor(Color.WHITE);
                imgTab5.setImageResource(R.drawable.icons_social);
                txtTab5.setTextColor(Color.WHITE);

                break;
            case 1:
                imgTab1.setImageResource(R.drawable.icons_home);
                txtTab1.setTextColor(Color.WHITE);
                imgTab2.setImageResource(R.drawable.icons_alerts_sel);
                txtTab2.setTextColor(getResources().getColor(R.color.bicsi_yellow));
                imgTab3.setImageResource(R.drawable.icon_gallery);
                txtTab3.setTextColor(Color.WHITE);
                imgTab4.setImageResource(R.drawable.icons_mybicsi);
                txtTab4.setTextColor(Color.WHITE);
                imgTab5.setImageResource(R.drawable.icons_social);
                txtTab5.setTextColor(Color.WHITE);
                break;
            case 2:
                imgTab1.setImageResource(R.drawable.icons_home);
                txtTab1.setTextColor(Color.WHITE);
                imgTab2.setImageResource(R.drawable.icons_alerts);
                txtTab2.setTextColor(Color.WHITE);
                imgTab3.setImageResource(R.drawable.icon_gallery_sel);
                txtTab3.setTextColor(getResources().getColor(R.color.bicsi_yellow));
                imgTab4.setImageResource(R.drawable.icons_mybicsi);
                txtTab4.setTextColor(Color.WHITE);
                imgTab5.setImageResource(R.drawable.icons_social);
                txtTab5.setTextColor(Color.WHITE);
                break;
            case 3:
                imgTab1.setImageResource(R.drawable.icons_home);
                txtTab1.setTextColor(Color.WHITE);
                imgTab2.setImageResource(R.drawable.icons_alerts);
                txtTab2.setTextColor(Color.WHITE);
                imgTab3.setImageResource(R.drawable.icon_gallery);
                txtTab3.setTextColor(Color.WHITE);
                imgTab4.setImageResource(R.drawable.icons_mybicsi_sel);
                txtTab4.setTextColor(getResources().getColor(R.color.bicsi_yellow));
                imgTab5.setImageResource(R.drawable.icons_social);
                txtTab5.setTextColor(Color.WHITE);
                break;
            case 4:
                imgTab1.setImageResource(R.drawable.icons_home);
                txtTab1.setTextColor(Color.WHITE);
                imgTab2.setImageResource(R.drawable.icons_alerts);
                txtTab2.setTextColor(Color.WHITE);
                imgTab3.setImageResource(R.drawable.icon_gallery);
                txtTab3.setTextColor(Color.WHITE);
                imgTab4.setImageResource(R.drawable.icons_mybicsi);
                txtTab4.setTextColor(Color.WHITE);
                imgTab5.setImageResource(R.drawable.icons_social_sel);
                txtTab5.setTextColor(getResources().getColor(R.color.bicsi_yellow));
                break;
            default:
                break;
        }

    }

    /**
     * getCurrentTab
     *
     * @return
     */
    public int getCurrentTab() {
        int currentTab = 0;//TODO:get current tab index
        return currentTab;
    }



    @Override
    public void navigateToTabFragment(Fragment newFragment) {

        navigateToTabFragment(newFragment, mCurrentTabIndex, null);

    }


    private void addToTabList(Fragment tab, int tabIndex) {
        switch (tabIndex) {
            case 0:
                tabList1.add(tab);
                break;
            case 1:
                tabList2.add(tab);
                break;
            case 2:
                tabList3.add(tab);
                break;
            case 3:
                tabList4.add(tab);
                break;
            case 4:
                tabList5.add(tab);
                break;
        }
    }

    private OnClickListener textView_listener = new OnClickListener() {
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.layoutTab1) {
                navigateToTabFragment(0);
            } else if (id == R.id.layoutTab2) {
                navigateToTabFragment(1);
            } else if (id == R.id.layoutTab3) {
                navigateToTabFragment(2);
            } else if (id == R.id.layoutTab4) {
                navigateToTabFragment(3);
            } else if (id == R.id.layoutTab5)
                navigateToTabFragment(4);
        }

    };


    //Restore this once home tab fragment has been added
    @Override
    public void onBackPressed() {

        onBackPressed(null);
    }

    public void onBackPressed(Bundle bundle) {

        int tabIndex = mCurrentTabIndex;

        Fragment newTab = null;
        Fragment oldTab = null;
        switch (tabIndex) {
            case 0:
                if (tabList1.size() > 1) {
                    newTab = tabList1.get(tabList1.size() - 2);
                    oldTab = tabList1.remove(tabList1.size() - 1);
                } else {
                    finish();
                }
                break;
            case 1:
                if (tabList2.size() > 1) {
                    newTab = tabList2.get(tabList2.size() - 2);
                    oldTab = tabList2.remove(tabList2.size() - 1);
                } else {
                    finish();
                }
                break;
            case 2:
                if (tabList3.size() > 1) {
                    newTab = tabList3.get(tabList3.size() - 2);
                    oldTab = tabList3.remove(tabList3.size() - 1);
                } else {
                    finish();
                }
                break;
            case 3:
                if (tabList4.size() > 1) {
                    newTab = tabList4.get(tabList4.size() - 2);
                    oldTab = tabList4.remove(tabList4.size() - 1);
                } else {
                    finish();
                }
                break;
            case 4:
                if (tabList5.size() > 1) {
                    newTab = tabList5.get(tabList5.size() - 2);
                    oldTab = tabList5.remove(tabList5.size() - 1);
                } else {
                    finish();
                }
                break;
            default:
                finish();
        }
        if((newTab == null) || (oldTab == null)){
            return;
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(oldTab.getClass().toString().contains("Webview")){
            fragmentTransaction.hide(oldTab);
            oldTab.onPause();
        } else {
            fragmentTransaction.remove(oldTab);
        }
        fragmentTransaction.commit();


        mPreviousTabIndex = mCurrentTabIndex;
        mCurrentTabIndex = tabIndex;


        newTab.setArguments(bundle);


        fragmentTransaction = fragmentManager.beginTransaction();
        // You can then add a fragment using the add() method, specifying the
        // fragment to add and the view in which to insert it. For example:
        fragmentTransaction.add(R.id.content, newTab);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @SuppressLint("NewApi")
    private void resetTab(int index) {
        Fragment oldTab = null;
        Fragment tab = null;
        switch (index) {

            case 0:
                if (tabList1.size() > 0) {
                    oldTab = tabList1.remove(tabList1.size() - 1);
                    tabList1.clear();
                    tabList1.add(new HomeFragment());
                    tab = tabList1.get(tabList1.size() - 1);
                }
                break;
            case 1:
                if (tabList2.size() > 0) {
                    oldTab = tabList2.remove(tabList2.size() - 1);
                    tabList2.clear();
                    tabList2.add(new AlertsFragment());
                    tab = tabList2.get(tabList2.size() - 1);
                }
                break;
            case 2:
                if (tabList3.size() > 0) {
                    oldTab = tabList3.remove(tabList3.size() - 1);
                    tabList3.clear();
                    tabList3.add(new GalleryLoginFragment());
                    tab = tabList3.get(tabList3.size() - 1);
                }
                break;
            case 3:
                if (tabList4.size() > 0) {
                    oldTab = tabList4.remove(tabList4.size() - 1);
                    tabList4.clear();
                    Bundle bundlem = new Bundle();
                    bundlem.putString("URL", "http://www.bicsi.org/m/mybicsi.aspx");
                    Fragment newFragmentm = new WebviewFragment();
                    newFragmentm.setArguments(bundlem);
                    tabList4.add(newFragmentm);
                    tab = tabList4.get(tabList4.size() - 1);
                }
                break;
            case 4:
                if (tabList5.size() > 0) {
                    oldTab = tabList5.remove(tabList5.size() - 1);
                    tabList5.clear();
                    tabList5.add(new PlannerScheduleFragment());
                    tab = tabList5.get(tabList5.size() - 1);
                }
                break;

        }

        if (oldTab != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(oldTab);
            fragmentTransaction.commit();
        }


        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setIcon(R.drawable.ic_launcher_web);
        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.t_bckgrd));

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        fragmentTransaction.add(R.id.content, tab);
        fragmentTransaction.commit();

    }

    @Override
    public void navigateToTabFragmentClearHistory(Fragment newFragment, Bundle bundle) {
        if (newFragment == null) {
            return;
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment oldFragment = null;
        switch(mCurrentTabIndex){
            case 0:
                if (tabList1.size() > 0) {
                    oldFragment = tabList1.get(tabList1.size() - 1);
                    tabList1.clear();
                } else {
                    finish();
                }
                break;
            case 1:
                if (tabList2.size() > 0) {
                    oldFragment = tabList2.get(tabList2.size() - 1);
                    tabList2.clear();
                } else {
                    finish();
                }
                break;
            case 2:
                if (tabList3.size() > 0) {
                    oldFragment = tabList3.get(tabList3.size() - 1);
                    tabList3.clear();
                } else {
                    finish();
                }
                break;
            case 3:
                if (tabList4.size() > 0) {
                    oldFragment = tabList4.get(tabList4.size() - 1);
                    tabList4.clear();
                } else {
                    finish();
                }
                break;
            case 4:
                if (tabList5.size() > 0) {
                    oldFragment = tabList5.get(tabList5.size() - 1);
                    tabList5.clear();
                } else {
                    finish();
                }
                break;

        }
        fragmentTransaction.remove(oldFragment);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        // You can then add a fragment using the add() method, specifying the
        // fragment to add and the view in which to insert it. For example:
        newFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.content, newFragment);
        fragmentTransaction.commit();

        addToTabList(newFragment, mCurrentTabIndex);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about_us:
                navigateToTabFragment(new AboutUsFragment());
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }
    public void showHomeUnreadCount(int unreadCount)
    {
        if(unreadCount>=0)
        {
            if(unreadCount==0)
            {
                txtHomeUnreadCount.setVisibility(View.INVISIBLE);
            }
            else
            {
                txtHomeUnreadCount.setText(""+unreadCount);
                txtHomeUnreadCount.setVisibility(View.VISIBLE);
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        getUnreadCount();
        registerBR();
        //Re-register receivers on resume
        registerReceivers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterBRs();
        //Unregister receivers on pause
        unregisterReceivers();
    }


    /***********************************************************************************************************
     * registerBRs()
     *
     * this provides a one stop place to register any BR's
     ***********************************************************************************************************/
    private void registerBR() {

        // ...................Register the BCR's for the
        // Activity...................
        registerReceiver(receiver,
                new IntentFilter(AsyncTaskPost.GET_UNREAD_COUNT_RETURN_INTENT));


    }
    private void unregisterBRs() {
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            //Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void getUnreadCount(){
        try {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Alerts");
            query.orderByDescending("_created_at");
            List<ParseObject> todoslist = query.find();

            //AlertModel[] alertList = new Gson().fromJson(jsonString, AlertModel[].class);
            if(todoslist != null){
                Date newDate = todoslist.get(0).getUpdatedAt();
                //alertList[alertList.length-1].getCreateddate();
                String dateString = getSharedPreferences();

                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy, HH:mm:ss");
                Date oldDate = sdf.parse(dateString);
                //double oldDate = Double.parseDouble(dateString);
                if((newDate.getTime() - oldDate.getTime())>5000){
                    showHomeUnreadCount(1);
                }

            }
        } catch (Exception e) {
            showHomeUnreadCount(1);
            e.printStackTrace();
        }
    }

    /**
     * getSharedPreferences
     *
     */
    private String getSharedPreferences() {
        SharedPreferences app_preferences = getSharedPreferences("BICSI_PREF", 0);
        String alertDate = app_preferences.getString("alert_date", null);
        return alertDate;
    }
    /**
     * hideSoftKeyboard hide the keyboard
     */
    public void hideSoftKeyboard(int id) {
        try {
            ((InputMethodManager) MainActivity.this
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(findViewById(id).getWindowToken(),
                            0);
        } catch (Exception e) {
            //Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * send to Google Analytics
     */
    public void updateTracker(String label){
        // May return null if a EasyTracker has not yet been initialized with a
        // property ID.
        EasyTracker easyTracker = EasyTracker.getInstance(this);

        // MapBuilder.createEvent().build() returns a Map of event fields and values
        // that are set and sent with the hit.
        easyTracker.send(MapBuilder
                        .createEvent("ui_action",     // Event category (required)
                                "button_press",  // Event action (required)
                                label,   // Event label
                                null)            // Event value
                        .build()
        );
    }
    //No need to override this interface method since the interface no longer exists
	/*@Override
	public void onSchedItemPicked(int position) {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "Clicked "+ position, Toast.LENGTH_LONG).show();

	}*/


    private void checkMessage(Intent intent)
    {
        if (null != intent)
        {
            if (intent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
            {
                showMessage("push message is " + intent.getExtras().getString(PushManager.PUSH_RECEIVE_EVENT));
            }
            else if (intent.hasExtra(PushManager.REGISTER_EVENT))
            {
                showMessage("register");
            }
            else if (intent.hasExtra(PushManager.UNREGISTER_EVENT))
            {
                showMessage("unregister");
            }
            else if (intent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
            {
                showMessage("register error");
            }
            else if (intent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
            {
                showMessage("unregister error");
            }

            resetIntentValues();
        }
    }

    /**
     * Will check main Activity intent and if it contains any PushWoosh data, will clear it
     */
    private void resetIntentValues()
    {
        Intent mainAppIntent = getIntent();

        if (mainAppIntent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.PUSH_RECEIVE_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.REGISTER_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.REGISTER_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.UNREGISTER_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.REGISTER_ERROR_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.UNREGISTER_ERROR_EVENT);
        }

        setIntent(mainAppIntent);
    }

    private void showMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /*@Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);

        checkMessage(intent);
    }*/

}

/*
public class MainActivity extends Activity implements
MizeUtil.NavigateToTabFragmentListener, PromptReturnListener, OnClickListener */
/*EhallSchedFragment2.OnSchedItemSelectedListener*//*
{
	
	private static final String TAG = "MainActivity";
	private static final int SECOND = 1000;
	private static final int WAIT_TIME = 120 * SECOND;
	private LinearLayout tab1;
	private LinearLayout tab2;
	private LinearLayout tab3;
	private LinearLayout tab4;
	private LinearLayout tab5;
	private TextView txtTab1;
	private TextView txtTab2;
	private TextView txtTab3;
	private TextView txtTab4;
	private TextView txtTab5;
	private ImageView imgTab1;
	private ImageView imgTab2;
	private ImageView imgTab3;
	private ImageView imgTab4;
	private ImageView imgTab5;
	private ArrayList<Fragment> tabList1 = new ArrayList<Fragment>();
	private ArrayList<Fragment> tabList2 = new ArrayList<Fragment>();
	private ArrayList<Fragment> tabList3 = new ArrayList<Fragment>();
	private ArrayList<Fragment> tabList4 = new ArrayList<Fragment>();
	private ArrayList<Fragment> tabList5 = new ArrayList<Fragment>();
	private AsyncTaskPost mPost = null;
	private int mPreviousTabIndex = 0;
	private int mCurrentTabIndex = 0;
	private Context mContext = this;
	public String mCategoryName = null;
	public int mCategoryId = 0;
	public String mProductQuery = null;
	public int mSearchType = 0;
	public String mFriendQuery = null;
	public String mBrandQuery = "";
	public String mDepartmentName = null;
	public String mPushNotificationCode = null;
	private TextView txtHomeUnreadCount;
	public String mPostMessage = null;
	private Runnable mMyRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				getUnreadCount();
				Handler myHandler = new Handler();
				myHandler.postDelayed(mMyRunnable, WAIT_TIME);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};
	
	private PushIOManager mPushIOManager;
	public String mWebViewURL = null;
	
	*/
/*********************************************************************************
	 * receiver
	 *********************************************************************************//*

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
		
			handleIntent(intent);
		}
	};
	
	*/
/**
	 * handleIntent
	 * 
	 *//*

	private void handleIntent(Intent intent) {
		   if (intent.getAction().equals(
				AsyncTaskPost.GET_UNREAD_COUNT_RETURN_INTENT)) {
			boolean success = intent.getBooleanExtra(
					AsyncTaskPost.GET_UNREAD_COUNT_SUCCESS_VALUE, false);
			if (success) {
				String jsonString = intent
						.getStringExtra(AsyncTaskPost.GET_UNREAD_COUNT_RETURN_VALUES);
				
				System.out.println("json response: "+jsonString);
				
					if(jsonString != null){
						if(!jsonString.startsWith("[")){
							jsonString = "["+jsonString+"]";
						}
						
					}
				
				
			}
		}
	}
	 
	@Override
    public void onStart() {
      super.onStart();
      	EasyTracker.getInstance(this).activityStart(this);  
      	EasyTracker.getInstance(mContext).activityStart(this); 
    }

	//private SQLiteDB sqlite_obj;
	 //List<String> list1, list2, list3, list4, list5;
	 private SimpleCursorAdapter dataAdapter;
    
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);    
      
      //sqlite_obj = new SQLiteDB(MainActivity.this);
      
      
      GetURL();
      
      ParseInstallation.getCurrentInstallation().saveInBackground();
      ParseAnalytics.trackAppOpened(getIntent());   		
        
        Crittercism.initialize(getApplicationContext(), "5543d5958172e25e67906b49");
        
        setupTabs();
		resetTabs();		
		
		Handler myHandler = new Handler();
		myHandler.postDelayed(mMyRunnable, 1);
		
		GetURLCSched();
		
		new GetJSONData().execute();
		
		 }
    
    ///
  private class LongOperation  extends AsyncTask<String, Void, Void> {
	  
	  	private SQLiteDB sqlite_obj = new SQLiteDB(mContext);
	  	List<String> list1, list2, list3, list4, list5;
	  	

  		private final HttpClient Client = new DefaultHttpClient();
          private String Content;
          private String Error = null;
          private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
          String data =""; 
        
          int sizeData = 0;  
        
         
         
          protected void onPreExecute() {
              // NOTE: You can call UI Element here.
              
              //Start Progress Dialog (Message)
            
              Dialog.setMessage("Updating data...");
              Dialog.show();
             
              try{
                  // Set Request parameter
                  data +="&" + URLEncoder.encode("data", "UTF-8");
                     
              } catch (UnsupportedEncodingException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              } 
             
          }
  
          // Call after onPreExecute method
          protected Void doInBackground(String... urls) {
             
              */
/************ Make Post Call To Web Server ***********//*

              BufferedReader reader=null;
    
                   // Send data 
                  try
                  { 
                   
                     // Defined URL  where to send data
                     URL url = new URL(urls[0]);
                      
                    // Send POST data request
        
                    URLConnection conn = url.openConnection(); 
                    conn.setDoOutput(true); 
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
                    wr.write( data ); 
                    wr.flush(); 
               
                    // Get the server response 
                    
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                 
                      // Read Server Response
                      while((line = reader.readLine()) != null)
                          {
                                 // Append server response in string
                                 sb.append(line + " ");
                          }
                     
                      // Append Server Response To Content String 
                     Content = sb.toString();
                  }
                  catch(Exception ex)
                  {
                      Error = ex.getMessage();
                  }
                  finally
                  {
                      try
                      {
          
                          reader.close();
                      }
        
                      catch(Exception ex) {}
                  }
             
              */
/*****************************************************//*

              return null;
          }
          
          protected void onPostExecute(Void unused) {
              // NOTE: You can call UI Element here.
              
              // Close progress dialog
              Dialog.dismiss();
              
              if (Error != null) {
                  
                  System.out.println("Output : "+Error);
                  
              } else {
               
                  // Show Response Json On Screen (activity)
              	System.out.println( Content );
                 
               */
/****************** Start Parse Response JSON Data *************//*

                 
                  String OutputData = "";
                  */
/*JSONObject jsonResponse;*//*

                       
                  try {
                       
                	
                	
                       */
/****** Creates a new JSONObject with name/value mappings from the JSON string. ********//*

                       */
/*jsonResponse = new JSONObject(Content);*//*

                       
                       */
/***** Returns the value mapped by name if it exists and is a JSONArray. ***//*

                       */
/*******  Returns null otherwise.  *******//*

                       */
/*JSONArray jsonMainNode = jsonResponse.optJSONArray("");*//*

                	
                  	JSONArray jsonMainNode = new JSONArray(Content);
                       
                       */
/*********** Process each JSON Node ************//*

   
                       int lengthJsonArr = jsonMainNode.length();  
                     
                     list1 = new ArrayList<String>();
           			 list2 = new ArrayList<String>();
           			 list3 = new ArrayList<String>();
           			 list4 = new ArrayList<String>();
           			 list5 = new ArrayList<String>();
   
                       for(int i=0; i < lengthJsonArr; i++) 
                       {
                           */
/****** Get Object for each JSON node.***********//*

                           JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                           
                           */
/******* Fetch node values **********//*

                           //String id       = jsonChildNode.optString("id").toString();
                           String id = jsonChildNode.optString("id").toString();
                           String scheduleDate     = jsonChildNode.optString("scheduleDate").toString();
                           String sessionName = jsonChildNode.optString("sessionName").toString();
                           String sessionTime = jsonChildNode.optString("sessionTime").toString();
                           String desc = jsonChildNode.optString("desc").toString();
                         
                           list1.add(jsonChildNode.getString("id"));
                           list2.add(jsonChildNode.getString("scheduleDate"));
                           list3.add(jsonChildNode.getString("sessionName"));
                           list4.add(jsonChildNode.getString("sessionTime"));
                           list5.add(jsonChildNode.getString("desc"));
                           
                           
                           
                         
                           OutputData += "ID: "+ id +" "
                                       + "ScheduleDate: "+ scheduleDate +" "
                                       + "ID sessionname: "+ sessionName +" "
                                       + "SessionTime: "+ sessionTime +" "
                                       + "Desc: "+ desc +" "
                                       +"\n";
                         
                           sqlite_obj.open();
                     	
                       	sqlite_obj.deleteAll();
                     	
                       	for(int j=0; j<list1.size(); j++) {
                     		
                       		sqlite_obj.insert(list1.get(j).toString(), list2.get(j).toString(), list3.get(j).toString(), list4.get(j).toString(), list5.get(j).toString());

                       		
                       	}
                       	
                       	sqlite_obj.close();
                          
                      }
                   */
/****************** End Parse Response JSON Data *************//*

                      
                       //Show Parsed Output on screen (activity)
                       */
/*jsonParsed.setText( OutputData );*//*

                       System.out.println(OutputData);
                       
                     //Generate ListView from SQLite Database
                       //displayListView();
                      
                       
                       
                      
                       
                   } catch (JSONException e) {
           
                       e.printStackTrace();
                   }
   
                  
               }
            
          }

 }
  
  private class LongOperationCSched  extends AsyncTask<String, Void, Void> {
	  
	  	private SQLiteDBcShed sqlite_obj = new SQLiteDBcShed(mContext);
	  	List<String> list1, list2, list3;
		
		private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
        String data =""; 
      
        int sizeData = 0;  
      
       
       
        protected void onPreExecute() {
            // NOTE: You can call UI Element here.
            
            //Start Progress Dialog (Message)
          
            Dialog.setMessage("Updating data...");
            Dialog.show();
           
            try{
                // Set Request parameter
                data +="&" + URLEncoder.encode("data", "UTF-8");
                   
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
           
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
           
            */
/************ Make Post Call To Web Server ***********//*

            BufferedReader reader=null;
  
                 // Send data 
                try
                { 
                 
                   // Defined URL  where to send data
                   URL url = new URL(urls[0]);
                    
                  // Send POST data request
      
                  URLConnection conn = url.openConnection(); 
                  conn.setDoOutput(true); 
                  OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
                  wr.write( data ); 
                  wr.flush(); 
             
                  // Get the server response 
                  
                  reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                  StringBuilder sb = new StringBuilder();
                  String line = null;
               
                    // Read Server Response
                    while((line = reader.readLine()) != null)
                        {
                               // Append server response in string
                               sb.append(line + " ");
                        }
                   
                    // Append Server Response To Content String 
                   Content = sb.toString();
                }
                catch(Exception ex)
                {
                    Error = ex.getMessage();
                }
                finally
                {
                    try
                    {
        
                        reader.close();
                    }
      
                    catch(Exception ex) {}
                }
           
            */
/*****************************************************//*

            return null;
        }
        
        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.
            
            // Close progress dialog
            Dialog.dismiss();
            
            if (Error != null) {
                
                System.out.println("Output : "+Error);
                
            } else {
             
                // Show Response Json On Screen (activity)
            	System.out.println( Content );
               
             */
/****************** Start Parse Response JSON Data *************//*

               
                String OutputData = "";
                */
/*JSONObject jsonResponse;*//*

                     
                try {
                     
              	
              	
                     */
/****** Creates a new JSONObject with name/value mappings from the JSON string. ********//*

                     */
/*jsonResponse = new JSONObject(Content);*//*

                     
                     */
/***** Returns the value mapped by name if it exists and is a JSONArray. ***//*

                     */
/*******  Returns null otherwise.  *******//*

                     */
/*JSONArray jsonMainNode = jsonResponse.optJSONArray("");*//*

              	
                	JSONArray jsonMainNode = new JSONArray(Content);
                     
                     */
/*********** Process each JSON Node ************//*

 
                     int lengthJsonArr = jsonMainNode.length();  
                   
                   list1 = new ArrayList<String>();
         			 list2 = new ArrayList<String>();
         			 list3 = new ArrayList<String>();
         			 
 
                     for(int i=0; i < lengthJsonArr; i++) 
                     {
                         */
/****** Get Object for each JSON node.***********//*

                         JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                         
                         */
/******* Fetch node values **********//*

                         String id       = jsonChildNode.optString("id").toString();
                         String day     = jsonChildNode.optString("day").toString();
                         String date = jsonChildNode.optString("date").toString();
                       
                         list1.add(jsonChildNode.getString("id"));
                         list2.add(jsonChildNode.getString("day"));
                         list3.add(jsonChildNode.getString("date"));
                         
                       
                         OutputData += "ID: "+ id +" "
                                     + "Day: "+ day +" "
                                     + "Date: "+ date +" "
                                     +"\n";
                       
                         sqlite_obj.open();
                   	
                     	sqlite_obj.deleteAll();
                   	
                     	for(int j=0; j<list1.size(); j++) {
                   		
                     		sqlite_obj.insert(list1.get(j).toString(), list2.get(j).toString(), list3.get(j).toString());
                     		
                     		
                     	}
                     	
                     	sqlite_obj.close();
                        
                    }
                 */
/****************** End Parse Response JSON Data *************//*

                    
                     //Show Parsed Output on screen (activity)
                     */
/*jsonParsed.setText( OutputData );*//*

                     System.out.println(OutputData);
                     
                   //Generate ListView from SQLite Database
                     //displayListView();
                    
                     
                     
                    
                     
                 } catch (JSONException e) {
         
                     e.printStackTrace();
                 }
 
                
             }
          
        }

}
	 
  private class GetJSONData  extends AsyncTask<Void, Void, Void> {
	  //call webservice
	  private ProgressDialog pDialog;
	  private static final String url = "https://webservice.bicsi.org/json/reply/MobSession?SessionAltCd=CN-FALL-NV-0915";
	  private String myLongStr;
	  private String myLongStrEnd;
	  
	  private static final String TAG_FUNCTIONS = "Functions";
	  
	  	private SQLiteDBAllData sqlite_obj = new SQLiteDBAllData(mContext);
	  	
	  	List<String> list1,list2,list3,list4,list5,list6,list7,list8,list9,list10,list11,list12,list13,list14,list15,list16,list17,list18,list19,list20,list21,list22,list23,list24,list25,list26,list27,list28,list29,list30,list31,list32,list33,list34,list35,list36,list37,list38,list39,list40,list41,list42,list43;
	  	
	  	JSONArray functions = null;
	  	
	  	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Updating data...");
			pDialog.setCancelable(false);
			pDialog.show();
			
			sqlite_obj.open();
			sqlite_obj.deleteAll();
			sqlite_obj.close();

		}
	  	
		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);
			
			String OutputData = "";

			if (jsonStr != null) {
				
				
				try {
					
					
					JSONObject jsonObj = new JSONObject(jsonStr);
					
					// Getting JSON Array node
					functions = jsonObj.getJSONArray(TAG_FUNCTIONS);

					// looping through All Contacts
					for (int i = 0; i < functions.length(); i++) {
						JSONObject c = functions.getJSONObject(i);
						
						list1= new ArrayList<String>();
						list2= new ArrayList<String>();
						list3= new ArrayList<String>();
						list4= new ArrayList<String>();
						list5= new ArrayList<String>();
						list6= new ArrayList<String>();
						list7= new ArrayList<String>();
						list8= new ArrayList<String>();
						list9= new ArrayList<String>();
						list10= new ArrayList<String>();
						list11= new ArrayList<String>();
						list12= new ArrayList<String>();
						list13= new ArrayList<String>();
						list14= new ArrayList<String>();
						list15= new ArrayList<String>();
						list16= new ArrayList<String>();
						list17= new ArrayList<String>();
						list18= new ArrayList<String>();
						list19= new ArrayList<String>();
						list20= new ArrayList<String>();
						list21= new ArrayList<String>();
						list22= new ArrayList<String>();
						list23= new ArrayList<String>();
						list24= new ArrayList<String>();
						list25= new ArrayList<String>();
						list26= new ArrayList<String>();
						list27= new ArrayList<String>();
						list28= new ArrayList<String>();
						list29= new ArrayList<String>();
						list30= new ArrayList<String>();
						list31= new ArrayList<String>();
						list32= new ArrayList<String>();
						list33= new ArrayList<String>();
						list34= new ArrayList<String>();
						list35= new ArrayList<String>();
						list36= new ArrayList<String>();
						list37= new ArrayList<String>();
						list38= new ArrayList<String>();
						list39= new ArrayList<String>();
						list40= new ArrayList<String>();
						list41= new ArrayList<String>();
						list42= new ArrayList<String>();
						list43= new ArrayList<String>();
						
						
						
						
		       			 
					    */
/*String FUNCTIONCD= c.optString("FUNCTIONCD").toString();
						String functiontitle= c.optString("functiontitle").toString();
						String functiondescription= c.optString("functiondescription").toString();
						String LOCATIONNAME= c.optString("LOCATIONNAME").toString();
						String fucntioindate= c.optString("fucntioindate").toString();
						String functionStartTime= c.optString("functionStartTime").toString();
						String functionEndTime= c.optString("functionEndTime").toString();
						String trainer1firstname= c.optString("trainer1firstname").toString();
						String trainer1lastname= c.optString("trainer1lastname").toString();
						String trainer1org= c.optString("trainer1org").toString();
						String trainer1city= c.optString("trainer1city").toString();
						String trainer1state= c.optString("trainer1state").toString();
						String trainer1country= c.optString("trainer1country").toString();
						String trainer2firstname= c.optString("trainer2firstname").toString();
						String trainer2lastname= c.optString("trainer2lastname").toString();
						String trainer2org= c.optString("trainer2org").toString();
						String trainer2city= c.optString("trainer2city").toString();
						String trainer2state= c.optString("trainer2state").toString();
						String trainer2country= c.optString("trainer2country").toString();
						String trainer3firstname= c.optString("trainer3firstname").toString();
						String trainer3lastname= c.optString("trainer3lastname").toString();
						String trainer3org= c.optString("trainer3org").toString();
						String trainer3city= c.optString("trainer3city").toString();
						String trainer3state= c.optString("trainer3state").toString();
						String trainer3country= c.optString("trainer3country").toString();
						String trainer4firstname= c.optString("trainer4firstname").toString();
						String trainer4lastname= c.optString("trainer4lastname").toString();
						String trainer4org= c.optString("trainer4org").toString();
						String trainer4city= c.optString("trainer4city").toString();
						String trainer4state= c.optString("trainer4state").toString();
						String trainer4country= c.optString("trainer4country").toString();
						String trainer5firstname= c.optString("trainer5firstname").toString();
						String trainer5lastname= c.optString("trainer5lastname").toString();
						String trainer5org= c.optString("trainer5org").toString();
						String trainer5city= c.optString("trainer5city").toString();
						String trainer5state= c.optString("trainer5state").toString();
						String trainer5country= c.optString("trainer5country").toString();
						String trainer6firstname= c.optString("trainer6firstname").toString();
						String trainer6lastname= c.optString("trainer6lastname").toString();
						String trainer6org= c.optString("trainer6org").toString();
						String trainer6city= c.optString("trainer6city").toString();
						String trainer6state= c.optString("trainer6state").toString();
						String trainer6country= c.optString("trainer6country").toString();*//*

						 

	                     
						list1.add(c.getString("FUNCTIONCD"));
						list2.add(c.getString("functiontitle"));
						list3.add(c.getString("functiondescription"));
						list4.add(c.getString("LOCATIONNAME"));
						list5.add(c.getString("fucntioindate"));
						list6.add(c.getString("functionStartTime"));
						list7.add(c.getString("functionEndTime"));
						list8.add(c.getString("trainer1firstname"));
						list9.add(c.getString("trainer1lastname"));
						list10.add(c.getString("trainer1org"));
						list11.add(c.getString("trainer1city"));
						list12.add(c.getString("trainer1state"));
						list13.add(c.getString("trainer1country"));
						list14.add(c.getString("trainer2firstname"));
						list15.add(c.getString("trainer2lastname"));
						list16.add(c.getString("trainer2org"));
						list17.add(c.getString("trainer2city"));
						list18.add(c.getString("trainer2state"));
						list19.add(c.getString("trainer2country"));
						list20.add(c.getString("trainer3firstname"));
						list21.add(c.getString("trainer3lastname"));
						list22.add(c.getString("trainer3org"));
						list23.add(c.getString("trainer3city"));
						list24.add(c.getString("trainer3state"));
						list25.add(c.getString("trainer3country"));
						list26.add(c.getString("trainer4firstname"));
						list27.add(c.getString("trainer4lastname"));
						list28.add(c.getString("trainer4org"));
						list29.add(c.getString("trainer4city"));
						list30.add(c.getString("trainer4state"));
						list31.add(c.getString("trainer4country"));
						list32.add(c.getString("trainer5firstname"));
						list33.add(c.getString("trainer5lastname"));
						list34.add(c.getString("trainer5org"));
						list35.add(c.getString("trainer5city"));
						list36.add(c.getString("trainer5state"));
						list37.add(c.getString("trainer5country"));
						list38.add(c.getString("trainer6firstname"));
						list39.add(c.getString("trainer6lastname"));
						list40.add(c.getString("trainer6org"));
						list41.add(c.getString("trainer6city"));
						list42.add(c.getString("trainer6state"));
						list43.add(c.getString("trainer6country")); 
						
						
						String fucntioindate= c.optString("fucntioindate").toString();
						String functionStartTime= c.optString("functionStartTime").toString();
						String functionEndTime= c.optString("functionEndTime").toString();
						
						String myDate = fucntioindate + ", " + functionStartTime;
						String myEndDate = fucntioindate + ", " + functionEndTime;
						
						Long myLDate = longDate(myDate);
						Long myLEndDate = longDate(myEndDate);
					
						myLongStr = String.valueOf(myLDate);
						myLongStrEnd = String.valueOf(myLEndDate);
						
						
	                       */
/*OutputData += "FUNCTIONCD: "+ FUNCTIONCD +" "
	                                   + "Title: "+ functiontitle +" "
	                                   + "Desc: "+ functiondescription +" "
	                                   +"\n";*//*

	                     
	                       sqlite_obj.open();
	                       
						
	                       for(int j=0; j<list1.size(); j++) {
	                    	   
	                    		
	                      		sqlite_obj.insert(list1.get(j).toString(), list2.get(j).toString(), list3.get(j).toString(), list4.get(j).toString(), list5.get(j).toString(), myLongStr, list6.get(j).toString(), myLongStrEnd, list7.get(j).toString(), list8.get(j).toString(), list9.get(j).toString(), list10.get(j).toString(), list11.get(j).toString(), list12.get(j).toString(), list13.get(j).toString(), list14.get(j).toString(), list15.get(j).toString(), list16.get(j).toString(), list17.get(j).toString(), list18.get(j).toString(), list19.get(j).toString(), list20.get(j).toString(), list21.get(j).toString(), list22.get(j).toString(), list23.get(j).toString(), list24.get(j).toString(), list25.get(j).toString(), list26.get(j).toString(), list27.get(j).toString(), list28.get(j).toString(), list29.get(j).toString(), list30.get(j).toString(), list31.get(j).toString(), list32.get(j).toString(), list33.get(j).toString(), list34.get(j).toString(), list35.get(j).toString(), list36.get(j).toString(), list37.get(j).toString(), list38.get(j).toString(), list39.get(j).toString(), list40.get(j).toString(), list41.get(j).toString(), list42.get(j).toString(), list43.get(j).toString());
	                      		
	                      		
	                      	}
	                      	
	                      	sqlite_obj.close();
	                         
	                     }
					
	                   System.out.println(OutputData);
						
						
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			
		}
		
		
}
  
  public long longDate (String sdate){
	   
	  //int intDate = (int) System.currentTimeMillis(); //Initializes var with current time in case parsing fails
	  long longDate = System.currentTimeMillis(); //Initializes var with current time in case parsing fails
	  SimpleDateFormat format = new SimpleDateFormat("mm-dd-yyyy, hh:mm a", Locale.US); //Describes date pattern
	  format.setLenient(false); //enforces strict pattern matching  
	     
	  try {
	   Date date = format.parse(sdate); //converts string to a date object
	   longDate = date.getTime(); 
	  } catch (ParseException e) {
	  }
	     
	  return longDate;  
	  }

  
  public void GetURL(){
		// WebServer Request URL
       String serverURL = "http://speedyreference.com/ehscheduleC15.php";
       
       // Use AsyncTask execute Method To Prevent ANR Problem
       new LongOperation().execute(serverURL);
	}
  
  public void GetURLCSched(){
		// WebServer Request URL
     String serverURL = "http://speedyreference.com/cscheduleC15.php";
     
     // Use AsyncTask execute Method To Prevent ANR Problem
     new LongOperationCSched().execute(serverURL);
	}
  
  
    ////
    @Override
    public void onStop() {
      super.onStop();
      EasyTracker.getInstance(this).activityStop(this);  
    }
    
    
    private void setupTabs() 
	{
		
		tab1 = (LinearLayout) findViewById(R.id.layoutTab1);
		((ImageView)findViewById(R.id.imgTab1)).setImageResource(R.drawable.icons_home_sel);
		((TextView)findViewById(R.id.txtHome)).setTextColor(getResources().getColor(R.color.bicsi_yellow));
		tab2 = (LinearLayout) findViewById(R.id.layoutTab2);
		tab3 = (LinearLayout) findViewById(R.id.layoutTab3);
		tab4 = (LinearLayout) findViewById(R.id.layoutTab4);
		
		
		txtTab1 = (TextView)findViewById(R.id.txtHome);
		
		txtTab2 = (TextView)findViewById(R.id.txtAlerts);
		txtTab3 = (TextView)findViewById(R.id.txtSocial);
		txtTab4 = (TextView)findViewById(R.id.txtMyBicsi);
				
		imgTab1 = (ImageView)findViewById(R.id.imgTab1);
		imgTab2 = (ImageView)findViewById(R.id.imgTab2);
		imgTab3 = (ImageView)findViewById(R.id.imgTab3);
		imgTab4 = (ImageView)findViewById(R.id.imgTab4);
		txtHomeUnreadCount = (TextView)findViewById(R.id.txtAlertsUnreadCount);
		
		tab1.setOnClickListener(textView_listener);
		tab2.setOnClickListener(textView_listener);
		tab3.setOnClickListener(textView_listener);
		tab4.setOnClickListener(textView_listener);
		
	}

    public static void trimCache(Context context) {
	    File dir = context.getFilesDir();
	    if(dir!= null && dir.isDirectory()){
	        File[] children = dir.listFiles();
	        if (children == null) {
	            // Either dir does not exist or is not a directory
	        } else {
	            File temp;
	            for (int i = 0; i < children.length; i++) {
	                temp = children[i];
	                temp.delete();
	            }
	        }
	    }
	} 
 
	*/
/**resetTabs
	 * 
	 *//*

	private void resetTabs()
	{
		trimCache(this);
		Fragment oldTab = null;
		if (tabList1.size() > 0) {
			oldTab = tabList1.remove(tabList1.size() - 1);
			if (oldTab != null) {
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.remove(oldTab);
				fragmentTransaction.commit();
			}
		}
		if (tabList2.size() > 0) {
			oldTab = tabList2.remove(tabList2.size() - 1);
			if (oldTab != null) {
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.remove(oldTab);
				fragmentTransaction.commit();
			}
		}
		if (tabList3.size() > 0) {
			oldTab = tabList3.remove(tabList3.size() - 1);
			if (oldTab != null) {
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.remove(oldTab);
				fragmentTransaction.commit();
			}
		}
		if (tabList4.size() > 0) {
			oldTab = tabList4.remove(tabList4.size() - 1);
			if (oldTab != null) {
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.remove(oldTab);
				fragmentTransaction.commit();
			}
		}
		

		
		ActionBar actionBar = getActionBar();
		tabList1.clear();
		tabList2.clear();
		tabList3.clear();
		tabList4.clear();
		//tabList5.clear();
		
		actionBar.setDisplayShowTitleEnabled(true);
		
		//actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.t_bckgrd));

		tabList1.add(new HomeFragment());
		tabList2.add(new AlertsFragment());
		tabList3.add(new GalleryLoginFragment());
		
		
		Bundle bundlem = new Bundle();
		bundlem.putString("URL", "http://www.bicsi.org/m/mybicsi.aspx");
		Fragment newFragmentm = new WebviewFragment();
		newFragmentm.setArguments(bundlem);
		tabList4.add(newFragmentm);
		
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment tab = null;
		tab = tabList1.get(tabList1.size() - 1);
		
		fragmentTransaction.add(R.id.content, tab);
		fragmentTransaction.commit();
		//addToTabList(tab, tabIndex);
		

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
	}

 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
	

	@Override
	public void sendReturnValue(String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goToMap(Bundle bundle) {
		// TODO Auto-generated method stub
		
	}
	
	public void navigateToTabFragment(Fragment newFragment, int tabIndex,
			Bundle bundle) {
		if (newFragment == null) {
			return;
		}
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment oldFragment = null;
		switch(mCurrentTabIndex){
		case 0:
			if (tabList1.size() > 0) {
				oldFragment = tabList1.get(tabList1.size() - 1);
			} else {
				finish();
			}
			break;
		case 1:
			if (tabList2.size() > 0) {
				oldFragment = tabList2.get(tabList2.size() - 1);
			} else {
				finish();
			}
			break;
		case 2:
			if (tabList3.size() > 0) {
				oldFragment = tabList3.get(tabList3.size() - 1);
			} else {
				finish();
			}
			break;
		case 3:
			if (tabList4.size() > 0) {
				oldFragment = tabList4.get(tabList4.size() - 1);
			} else {
				finish();
			}
			break;
		}
		fragmentTransaction.remove(oldFragment);
		fragmentTransaction.commit();

		
		mPreviousTabIndex = mCurrentTabIndex;
		mCurrentTabIndex = tabIndex;

		fragmentTransaction = fragmentManager.beginTransaction();
		// You can then add a fragment using the add() method, specifying the
		// fragment to add and the view in which to insert it. For example:
		if(bundle != null){
			newFragment.setArguments(bundle);
		}
		if(newFragment.isAdded()){
			fragmentTransaction.show(newFragment);
			newFragment.onResume();
		} else {
			fragmentTransaction.add(R.id.content, newFragment);
		}
		//fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();

		addToTabList(newFragment, tabIndex);
		

	}
	
	@Override
	public void navigateToTabFragment(Fragment newFragment, Bundle bundle) {

		int tabIndex = mCurrentTabIndex;
		navigateToTabFragment(newFragment, tabIndex, bundle);



	}

	@Override
	public void navigateToTabFragment(int tabIndex) {
		if(tabIndex == mCurrentTabIndex){
			resetTab(mCurrentTabIndex);
			return;
		}
		if (tabList1.size() == 0)
			return;
		if (tabList2.size() == 0)
			return;
		if (tabList3.size() == 0)
			return;
		if (tabList4.size() == 0)
			return;
		if (tabIndex == mCurrentTabIndex)
			return;

		Fragment newTab = null;
		switch (tabIndex) {
		case 0:
			newTab = this.tabList1.get(tabList1.size() - 1);
			break;
		case 1:
			newTab = this.tabList2.get(tabList2.size() - 1);
			break;
		case 2:
			newTab = this.tabList3.get(tabList3.size() - 1);
			break;
		case 3:
			newTab = this.tabList4.get(tabList4.size() - 1);
			break;
		default:
			return;
		}
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment oldTab = null;
		switch (mCurrentTabIndex) {
		case 0:
			if (tabList1.size() > 0) {
				oldTab = tabList1.get(tabList1.size() - 1);
			} else {
				finish();
			}
			break;
		case 1:
			if (tabList2.size() > 0) {
				oldTab = tabList2.get(tabList2.size() - 1);
			} else {
				finish();
			}
			break;
		case 2:
			if (tabList3.size() > 0) {
				oldTab = tabList3.get(tabList3.size() - 1);
			} else {
				finish();
			}
			break;
		case 3:
			if (tabList4.size() > 0) {
				oldTab = tabList4.get(tabList4.size() - 1);
			} else {
				finish();
			}
			break;
		default:
			finish();
		}
		if ((newTab == null) || (oldTab == null)) {
			return;
		}
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.remove(oldTab);
		fragmentTransaction.commit();
		
		mPreviousTabIndex = mCurrentTabIndex;
		mCurrentTabIndex = tabIndex;

		// You can then add a fragment using the add() method, specifying the
		// fragment to add and the view in which to insert it. For example:
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.content, newTab);
		fragmentTransaction.commit();
		resetTabIcons(tabIndex);
	}
	private void resetTabIcons(int tabIndex){
		switch (tabIndex) {
		case 0:
			imgTab1.setImageResource(R.drawable.icons_home_sel);
			txtTab1.setTextColor(getResources().getColor(R.color.bicsi_yellow));
			imgTab2.setImageResource(R.drawable.icons_alerts);
			txtTab2.setTextColor(Color.WHITE);
			imgTab3.setImageResource(R.drawable.icon_gallery);
			txtTab3.setTextColor(Color.WHITE);
			imgTab4.setImageResource(R.drawable.icons_mybicsi);
			txtTab4.setTextColor(Color.WHITE);
			
			break;
		case 1:
			imgTab1.setImageResource(R.drawable.icons_home);
			txtTab1.setTextColor(Color.WHITE);
			imgTab2.setImageResource(R.drawable.icons_alerts_sel);
			txtTab2.setTextColor(getResources().getColor(R.color.bicsi_yellow));
			imgTab3.setImageResource(R.drawable.icon_gallery);
			txtTab3.setTextColor(Color.WHITE);
			imgTab4.setImageResource(R.drawable.icons_mybicsi);
			txtTab4.setTextColor(Color.WHITE);
			break;
		case 2:
			imgTab1.setImageResource(R.drawable.icons_home);
			txtTab1.setTextColor(Color.WHITE);
			imgTab2.setImageResource(R.drawable.icons_alerts);
			txtTab2.setTextColor(Color.WHITE);
			imgTab3.setImageResource(R.drawable.icon_gallery_sel);
			txtTab3.setTextColor(getResources().getColor(R.color.bicsi_yellow));
			imgTab4.setImageResource(R.drawable.icons_mybicsi);
			txtTab4.setTextColor(Color.WHITE);
			
			break;
		case 3:
			imgTab1.setImageResource(R.drawable.icons_home);
			txtTab1.setTextColor(Color.WHITE);
			imgTab2.setImageResource(R.drawable.icons_alerts);
			txtTab2.setTextColor(Color.WHITE);
			imgTab3.setImageResource(R.drawable.icon_gallery);
			txtTab3.setTextColor(Color.WHITE);
			imgTab4.setImageResource(R.drawable.icons_mybicsi_sel);
			txtTab4.setTextColor(getResources().getColor(R.color.bicsi_yellow));
			break;
		default:
			break;
		}
		
	}

	*/
/**
	 * getCurrentTab
	 * 
	 * @return
	 *//*

	public int getCurrentTab() {
		int currentTab = 0;//TODO:get current tab index
		return currentTab;
	}



	@Override
	public void navigateToTabFragment(Fragment newFragment) {

		navigateToTabFragment(newFragment, mCurrentTabIndex, null);

	}


	private void addToTabList(Fragment tab, int tabIndex) {
		switch (tabIndex) {
		case 0:
			tabList1.add(tab);
			break;
		case 1:
			tabList2.add(tab);
			break;
		case 2:
			tabList3.add(tab);
			break;
		case 3:
			tabList4.add(tab);
			break;
		case 4:
			tabList5.add(tab);
			break;
		}
	}

	private OnClickListener textView_listener = new OnClickListener() {
		public void onClick(View v) {
			int id = v.getId();
			if(id == R.id.layoutTab1){
				navigateToTabFragment(0);
			}else if(id == R.id.layoutTab2){
				navigateToTabFragment(1);
			}else if(id == R.id.layoutTab3){
				navigateToTabFragment(2);
			}else if(id == R.id.layoutTab4)
				navigateToTabFragment(3);
			}
	
	};
	
	
	//Restore this once home tab fragment has been added
	@Override
	public void onBackPressed() {
		
		onBackPressed(null);
	}
	
	public void onBackPressed(Bundle bundle) {

		int tabIndex = mCurrentTabIndex;

		Fragment newTab = null;
		Fragment oldTab = null;
		switch (tabIndex) {
		case 0:
			if (tabList1.size() > 1) {
				newTab = tabList1.get(tabList1.size() - 2);
				oldTab = tabList1.remove(tabList1.size() - 1);
			} else {
				finish();
			}
			break;
		case 1:
			if (tabList2.size() > 1) {
				newTab = tabList2.get(tabList2.size() - 2);
				oldTab = tabList2.remove(tabList2.size() - 1);
			} else {
				finish();
			}
			break;
		case 2:
			if (tabList3.size() > 1) {
				newTab = tabList3.get(tabList3.size() - 2);
				oldTab = tabList3.remove(tabList3.size() - 1);
			} else {
				finish();
			}
			break;
		case 3:
			if (tabList4.size() > 1) {
				newTab = tabList4.get(tabList4.size() - 2);
				oldTab = tabList4.remove(tabList4.size() - 1);
			} else {
				finish();
			}
			break;
		case 4:
			if (tabList5.size() > 1) {
				newTab = tabList5.get(tabList5.size() - 2);
				oldTab = tabList5.remove(tabList5.size() - 1);
			} else {
				finish();
			}
			break;
		default:
			finish();
		}
		if((newTab == null) || (oldTab == null)){
			return;
		}
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if(oldTab.getClass().toString().contains("Webview")){
			fragmentTransaction.hide(oldTab);
			oldTab.onPause();
		} else {
			fragmentTransaction.remove(oldTab);
		}
		fragmentTransaction.commit();

		
		mPreviousTabIndex = mCurrentTabIndex;
		mCurrentTabIndex = tabIndex;
		

		newTab.setArguments(bundle);


		fragmentTransaction = fragmentManager.beginTransaction();
		// You can then add a fragment using the add() method, specifying the
		// fragment to add and the view in which to insert it. For example:
		fragmentTransaction.add(R.id.content, newTab);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	@SuppressLint("NewApi")
	private void resetTab(int index) {
		Fragment oldTab = null;
		Fragment tab = null;
		switch (index) {

		case 0:
			if (tabList1.size() > 0) {
				oldTab = tabList1.remove(tabList1.size() - 1);
				tabList1.clear();
				tabList1.add(new HomeFragment());
				tab = tabList1.get(tabList1.size() - 1);
			}
			break;
		case 1:
			if (tabList2.size() > 0) {
				oldTab = tabList2.remove(tabList2.size() - 1);
				tabList2.clear();
				tabList2.add(new AlertsFragment());
				tab = tabList2.get(tabList2.size() - 1);
			}
			break;
		case 2:
			if (tabList3.size() > 0) {
				oldTab = tabList3.remove(tabList3.size() - 1);
				tabList3.clear();
				tabList3.add(new GalleryLoginFragment());
				tab = tabList3.get(tabList3.size() - 1);
			}
			break;
		case 3:
			if (tabList4.size() > 0) {
				oldTab = tabList4.remove(tabList4.size() - 1);
				tabList4.clear();
				Bundle bundlem = new Bundle();
				bundlem.putString("URL", "http://www.bicsi.org/m/mybicsi.aspx");
				Fragment newFragmentm = new WebviewFragment();
				newFragmentm.setArguments(bundlem);
				tabList4.add(newFragmentm);
				tab = tabList4.get(tabList4.size() - 1);
			}
			break;
		
		}
		
		if (oldTab != null) {
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.remove(oldTab);
			fragmentTransaction.commit();
		}

		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		//actionBar.setIcon(R.drawable.ic_launcher_web);
		//actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.t_bckgrd));
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


		fragmentTransaction.add(R.id.content, tab);
		fragmentTransaction.commit();
		
	}

	@Override
	public void navigateToTabFragmentClearHistory(Fragment newFragment, Bundle bundle) {
		if (newFragment == null) {
			return;
		}
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment oldFragment = null;
		switch(mCurrentTabIndex){
		case 0:
			if (tabList1.size() > 0) {
				oldFragment = tabList1.get(tabList1.size() - 1);
				tabList1.clear();
			} else {
				finish();
			}
			break;
		case 1:
			if (tabList2.size() > 0) {
				oldFragment = tabList2.get(tabList2.size() - 1);
				tabList2.clear();
			} else {
				finish();
			}
			break;
		case 2:
			if (tabList3.size() > 0) {
				oldFragment = tabList3.get(tabList3.size() - 1);
				tabList3.clear();
			} else {
				finish();
			}
			break;
		case 3:
			if (tabList4.size() > 0) {
				oldFragment = tabList4.get(tabList4.size() - 1);
				tabList4.clear();
			} else {
				finish();
			}
			break;
		case 4:
			if (tabList5.size() > 0) {
				oldFragment = tabList5.get(tabList5.size() - 1);
				tabList5.clear();
			} else {
				finish();
			}
			break;
		}
		fragmentTransaction.remove(oldFragment);
		fragmentTransaction.commit();

		fragmentTransaction = fragmentManager.beginTransaction();
		// You can then add a fragment using the add() method, specifying the
		// fragment to add and the view in which to insert it. For example:
		newFragment.setArguments(bundle);
		fragmentTransaction.add(R.id.content, newFragment);
		fragmentTransaction.commit();

		addToTabList(newFragment, mCurrentTabIndex);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// Handle item selection
	    switch (item.getItemId()) {
	case R.id.about_us:
		navigateToTabFragment(new AboutUsFragment());
        return true;
    default:
		return super.onMenuItemSelected(featureId, item);
	    }
	}
	public void showHomeUnreadCount(int unreadCount) 
	{
		if(unreadCount>=0)
		{
			if(unreadCount==0)
			{
				txtHomeUnreadCount.setVisibility(View.INVISIBLE);
			}
			else
			{
				txtHomeUnreadCount.setText(""+unreadCount);
				txtHomeUnreadCount.setVisibility(View.VISIBLE);
			}
		}
	}



	@Override
	protected void onResume() {
		super.onResume();
		getUnreadCount();
		registerBR();			
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterBRs();
	}
	
	
	*/
/***********************************************************************************************************
	 * registerBRs()
	 * 
	 * this provides a one stop place to register any BR's
	 ***********************************************************************************************************//*

	private void registerBR() {

		// ...................Register the BCR's for the
		// Activity...................
		registerReceiver(receiver,
				new IntentFilter(AsyncTaskPost.GET_UNREAD_COUNT_RETURN_INTENT));
		
		
	}
	private void unregisterBRs() {
		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {
			//Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getUnreadCount(){
		try {
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Alerts");
			query.orderByDescending("_created_at");							
			List<ParseObject> todoslist = query.find();
			
			//AlertModel[] alertList = new Gson().fromJson(jsonString, AlertModel[].class);
			if(todoslist != null){
				Date newDate = todoslist.get(0).getUpdatedAt();
						//alertList[alertList.length-1].getCreateddate();
				String dateString = getSharedPreferences();
				
				SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy, HH:mm:ss");
				Date oldDate = sdf.parse(dateString);
				//double oldDate = Double.parseDouble(dateString);
				if((newDate.getTime() - oldDate.getTime())>5000){
					showHomeUnreadCount(1);
				}
				
			}
		} catch (Exception e) {
			showHomeUnreadCount(1);
			e.printStackTrace();
		}
	}
	
	*/
/**
	 * getSharedPreferences
	 * 
	 *//*

	private String getSharedPreferences() {
		SharedPreferences app_preferences = getSharedPreferences("BICSI_PREF", 0);
		String alertDate = app_preferences.getString("alert_date", null);
		return alertDate;
	}
	*/
/**
	 * hideSoftKeyboard hide the keyboard
	 *//*

	public void hideSoftKeyboard(int id) {
		try {
			((InputMethodManager) MainActivity.this
					.getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(findViewById(id).getWindowToken(),
							0);
		} catch (Exception e) {
			//Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	*/
/**
	 * send to Google Analytics
	 *//*

	public void updateTracker(String label){
		  // May return null if a EasyTracker has not yet been initialized with a
		  // property ID.
		  EasyTracker easyTracker = EasyTracker.getInstance(this);

		  // MapBuilder.createEvent().build() returns a Map of event fields and values
		  // that are set and sent with the hit.
		  easyTracker.send(MapBuilder
		      .createEvent("ui_action",     // Event category (required)
		                   "button_press",  // Event action (required)
		                   label,   // Event label
		                   null)            // Event value
		      .build()
		  );
	}
	//No need to override this interface method since the interface no longer exists
	*/
/*@Override
	public void onSchedItemPicked(int position) {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "Clicked "+ position, Toast.LENGTH_LONG).show();
		
	}*//*

	
	
	
}

*/
