package org.bicsi.canada2014.fragment;

/**
 * Created by barry on 6/3/15.
 */

import org.bicsi.canada2014.activities.MainActivity;
import org.bicsi.canada2014.adapter.SQLiteDBPlanner;
import org.bicsi.canada2017.R;
import org.bicsi.canada2014.adapter.SQLiteDBAllData;
import org.bicsi.canada2014.common.MizeUtil.NavigateToTabFragmentListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.FilterQueryProvider;
import android.content.DialogInterface;

import android.view.MenuItem;

import android.view.ContextMenu.ContextMenuInfo;
import android.widget.TextView;


public class PlannerScheduleFragment extends Fragment implements AdapterView.OnItemClickListener {

    private NavigateToTabFragmentListener mCallback;//interface from MizeUtil
    private SQLiteDBPlanner sqlite_obj;
    private MyCursorAdapter dataAdapter;
    //public String planner;


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
        final View v = inflater.inflate(R.layout.planner_schedule, container, false);

        Bundle bundle = getArguments();
        if(bundle != null){

            //planner = bundle.getString("planner");

        }

        sqlite_obj = new SQLiteDBPlanner(getActivity());

        try {
            sqlite_obj.open();

            //Cursor cursor = sqlite_obj.getAllSChedulesByPlanner();
            Cursor cursor = sqlite_obj.fetchAllPlannerItems();


            if (cursor.getCount() == 0){

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());

                // set title
                alertDialogBuilder.setTitle("Notification");

                // set dialog message
                alertDialogBuilder
                        .setMessage("You have not added any items to your schedule. Please tap Add to My Schedule when viewing Schedule or Session details. If you signed up for sessions or other items when you registered, please tap the Import button below to import your sessions.")
                        .setCancelable(false)
                        .setPositiveButton("Import",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                ImportScheduleFragment myFragment = new ImportScheduleFragment();

                                mCallback.navigateToTabFragment(myFragment, null); //interface method
                                // if this button is clicked, close
                                // current activity
                                //getActivity().finish();
                            }
                        })
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
            }

            //}
            else {
                System.out.println("Table row count is: " + cursor.getCount());

                String[] columns = new String[]{
                        SQLiteDBPlanner.KEY_FUNCCD,
                        SQLiteDBPlanner.KEY_FUNCTITLE,
                        SQLiteDBPlanner.KEY_FUNCDESC,
                        SQLiteDBPlanner.KEY_LOCATION,
                        SQLiteDBPlanner.KEY_DATE,
                        SQLiteDBPlanner.KEY_START,
                        SQLiteDBPlanner.KEY_END


                };

                int[] to = new int[]{
                        R.id.textViewFUNCTIONCD,
                        R.id.textViewfunctiontitle,
                        R.id.textViewfunctiondescription,
                        R.id.textViewlocationname,
                        R.id.textViewfunctiondate,
                        R.id.textViewfunctionStartTimeStr,
                        R.id.textViewfunctionEndTimeStr

                };

                dataAdapter = new MyCursorAdapter(

                        getActivity(), R.layout.planner_schedule_info_list,
                        cursor,
                        columns,
                        to,
                        0);

                ListView listView = (ListView) v.findViewById(android.R.id.list);
                // Assign adapter to ListView
                listView.setAdapter(dataAdapter);
                listView.setOnItemClickListener(this);

                //registerForContextMenu(listView);

            /*EditText myFilter = (EditText)v. findViewById(R.id.myFilter);
            myFilter.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    dataAdapter.getFilter().filter(s.toString());
                }
            });

            dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                public Cursor runQuery(CharSequence constraint) {
                    return sqlite_obj.fetchScheduleByDate(constraint.toString(), newConfDate);

                }
            });*/

            }
            return v;

        } finally {
            sqlite_obj.close();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> listView, View view,
                            int position, long id) {
        try {
            sqlite_obj.open();

            //Toast.makeText(getActivity(), "Clicked "+ position, Toast.LENGTH_LONG).show();
            // Get the cursor, positioned to the corresponding row in the result set
            Cursor cursor = (Cursor) listView.getItemAtPosition(position);

            String functioncd =
                    cursor.getString(cursor.getColumnIndexOrThrow("code"));

            String functionTitle =
                    cursor.getString(cursor.getColumnIndexOrThrow("title"));

            String functionDate =
                    cursor.getString(cursor.getColumnIndexOrThrow("date"));

            String functionStart =
                    cursor.getString(cursor.getColumnIndexOrThrow("start"));

            String functionEnd =
                    cursor.getString(cursor.getColumnIndexOrThrow("end"));

            String functionDescription =
                    cursor.getString(cursor.getColumnIndexOrThrow("desc"));

            String functionLocation =
                    cursor.getString(cursor.getColumnIndexOrThrow("location"));





            plannerSingleFragment mySingleFragment = new plannerSingleFragment();

            Bundle bundle = new Bundle();

            String newFunctioncd = new String("'" + functioncd + "'");

            bundle.putString("code", newFunctioncd);
            bundle.putString("title", functionTitle);
            bundle.putString("date", functionDate);
            bundle.putString("start", functionStart);
            bundle.putString("end", functionEnd);
            bundle.putString("desc", functionDescription);
            bundle.putString("location", functionLocation);



            mySingleFragment.setArguments(bundle);

            mCallback.navigateToTabFragment(mySingleFragment, null); //interface method



        } finally{

            sqlite_obj.close();
        }


    }

    private class MyCursorAdapter extends SimpleCursorAdapter{

        public MyCursorAdapter(Context context, int layout, Cursor c,
                               String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //get reference to the row
            View view = super.getView(position, convertView, parent);
            //check for odd or even to set alternate colors to the row background
            if(position % 2 == 0){
                view.setBackgroundColor(Color.rgb(252, 240, 217));
            }
            else {
                view.setBackgroundColor(Color.rgb(255, 255, 255));
            }
            return view;
        }


    }

    // We want to create a context Menu when the user long click on an item
    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;

        // We know that each row in the adapter is a Map
        //HashMap map =  (HashMap)
                dataAdapter.getItem(aInfo.position);

        menu.setHeaderTitle("Options for ");
        menu.add(1, 1, 1, "Details");
        menu.add(1, 2, 2, "Delete");

    }*/



}
