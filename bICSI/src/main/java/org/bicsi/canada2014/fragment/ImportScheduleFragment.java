package org.bicsi.canada2014.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.bicsi.canada2014.Planner;
import org.bicsi.canada2014.adapter.SQLiteDBAllData;
import org.bicsi.canada2014.adapter.SQLiteDBPlanner;
import org.bicsi.canada2014.common.MizeUtil;
import org.bicsi.canada2014.common.ServiceHandler;
import org.bicsi.canada2017.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by barry on 6/6/15.
 */
public class ImportScheduleFragment extends Fragment {

    private MizeUtil.NavigateToTabFragmentListener mCallback;

    private SQLiteDBPlanner sqlite_obj;

    Button importbutton;
    Button cancelbutton;
    EditText memberid;

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (MizeUtil.NavigateToTabFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NavigateToListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_import_schedule, container, false);

        sqlite_obj = new SQLiteDBPlanner(getActivity());


        //sqlite_obj.open();


        memberid = (EditText) v.findViewById(R.id.username);
        importbutton = (Button) v.findViewById(R.id.login);
        cancelbutton = (Button) v.findViewById(R.id.signup);





        importbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String mID = memberid.getText().toString();

                if (mID.isEmpty()){

                    AlertMessage();

                }
                else {
                    new GetJSONData().execute();
                    AlertMessage2();
                }

            }

        });

        cancelbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().onBackPressed();
            }
        });


        return v;
    }

    private class GetJSONData  extends AsyncTask<Void, Void, Void> {
        //call webservice
        private ProgressDialog pDialog;
        String memberID = memberid.getText().toString();


        private final String url = "https://webservice.bicsi.org/json/reply/MobFunctions?sess=CN-WINTER-FL-0117&custcd=" + memberID;



        //private final Uri uri = Uri.parse("https://webservice.bicsi.org/json/reply/MobFunctions?sess=CN-FALL-NV-0915&custcd=" + memberID);
        private String myLongStr;
        private String myLongStrEnd;

        private static final String TAG_FUNCTIONS = "Functions";

        //private SQLiteDBPlanner sqlite_obj = new SQLiteDBPlanner(mContext);
        private SQLiteDBAllData sqlite_obj2 = new SQLiteDBAllData(getActivity());

        List<String> list1;

        JSONArray functions = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Updating data...");
            pDialog.setCancelable(false);
            pDialog.show();

            //sqlite_obj.open();
            //sqlite_obj.deleteAll();
            //sqlite_obj.close();

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


                        list1.add(c.getString("FUNCTIONCD"));

                        sqlite_obj2.open();


                        for(int j=0; j<list1.size(); j++) {


                            Cursor cursor = sqlite_obj2.getSchedulesByFuncCode(list1.get(j).toString());

                            if(cursor.getCount() > 0){

                                int count = cursor.getCount();

                                System.out.println("Fetch by functioncd was successful: " + count);
                                System.out.println("Fetch by functioncd was successful: " + list1.get(j).toString());

                                String functioncd =
                                        cursor.getString(cursor.getColumnIndexOrThrow("_id"));

                                String functionTitle =
                                        cursor.getString(cursor.getColumnIndexOrThrow("functiontitle"));
                                System.out.println("Fetch by functioncd was successful: " + functionTitle);

                                String functionDate =
                                        cursor.getString(cursor.getColumnIndexOrThrow("fucntioindate"));

                                String functionStart =
                                        cursor.getString(cursor.getColumnIndexOrThrow("functionStartTimeStr"));

                                String functionEnd =
                                        cursor.getString(cursor.getColumnIndexOrThrow("functionEndTimeStr"));

                                String functionDescription =
                                        cursor.getString(cursor.getColumnIndexOrThrow("functiondescription"));

                                String functionLocation =
                                        cursor.getString(cursor.getColumnIndexOrThrow("LOCATIONNAME"));

                                sqlite_obj.open();

                                Planner plannerItem = new Planner();

                                plannerItem.code = functioncd;
                                plannerItem.title = functionTitle;
                                plannerItem.desc = functionDescription;
                                plannerItem.location = functionLocation;
                                plannerItem.date = functionDate;
                                plannerItem.start = functionStart;
                                plannerItem.end = functionEnd;

                                sqlite_obj.insertPlanner(plannerItem);

                                sqlite_obj.close();

                            }

                            else{

                                System.out.println("Fetch by functioncd was NOT successful!");
                            }

                        }

                        sqlite_obj2.close();



                    }

                    //System.out.println(OutputData);


                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Fetch by functioncd was NOT successful!");


                }
                //}
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

    private void AlertMessage(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set title
        alertDialogBuilder.setTitle("Error");

        // set dialog message
        alertDialogBuilder
                .setMessage("Please enter your MemberID.")
                .setCancelable(false)
                            /*.setPositiveButton("Import",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    ImportScheduleFragment myFragment = new ImportScheduleFragment();

                                    mCallback.navigateToTabFragment(myFragment, null); //interface method
                                    // if this button is clicked, close
                                    // current activity
                                    //getActivity().finish();
                                }
                            })*/
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    //}


    }

    private void AlertMessage2(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set title
        alertDialogBuilder.setTitle("Notification");

        // set dialog message
        alertDialogBuilder
                .setMessage("If you had sessions scheduled they have been imported. If not, please make sure your Member ID is valid and that you do have scheduled sessions to import.")
                .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    PlannerScheduleFragment myFragment = new PlannerScheduleFragment();

                                    mCallback.navigateToTabFragment(myFragment, null); //interface method
                                    // if this button is clicked, close
                                    // current activity
                                    //getActivity().finish();
                                }
                            });
                /*.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });*/

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        //}


    }

}
