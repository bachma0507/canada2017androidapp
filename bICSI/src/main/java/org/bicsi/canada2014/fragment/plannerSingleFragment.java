package org.bicsi.canada2014.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.bicsi.canada2014.activities.NotesActivity;
import org.bicsi.canada2014.adapter.SQLiteDBPlanner;
import org.bicsi.canada2014.common.MizeUtil;
import org.bicsi.canada2017.R;

import java.net.URI;

/**
 * Created by barry on 6/5/15.
 */
public class plannerSingleFragment extends Fragment {

    private MizeUtil.NavigateToTabFragmentListener mCallback;

    private Fragment myNoteFragment = new SessionNoteFragment();

    public String newFunctioncd;

    private SQLiteDBPlanner sqlite_obj;

    TextView title;
    TextView date;
    TextView start;
    TextView end;
    TextView desc;
    TextView location;

    Button surveybutton;
    Button notesbutton;
    Button plannerbutton;

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

        setRetainInstance(true);

        super.onCreateView(inflater, container, savedInstanceState);

        super.onCreateView(inflater, container, savedInstanceState);
        final View v = inflater.inflate(R.layout.fragment_planner_single, container, false);

        sqlite_obj = new SQLiteDBPlanner(getActivity());


        sqlite_obj.open();

        title = (TextView)v.findViewById(R.id.functiontitle);
        date = (TextView)v.findViewById(R.id.functiondate);
        start = (TextView)v.findViewById(R.id.functionstarttimestr);
        end = (TextView)v.findViewById(R.id.functionendtimestr);
        desc = (TextView)v.findViewById(R.id.functiondecsription);
        location = (TextView)v.findViewById(R.id.functionlocation);
        surveybutton = (Button)v.findViewById(R.id.survey_button);
        notesbutton = (Button)v.findViewById(R.id.notes_button);
        plannerbutton = (Button)v.findViewById(R.id.planner_button);

        final Bundle bundle = getArguments();

        if(bundle != null){
            newFunctioncd = bundle.getString("code");
            String funccd = newFunctioncd;

            if(newFunctioncd.contains("CONCSES") || newFunctioncd.contains("PRECON") || newFunctioncd.contains("GS_TUES") || newFunctioncd.contains("GS_THURS") == true){

                surveybutton.setVisibility(View.VISIBLE);
            }
            else{

                surveybutton.setVisibility(View.GONE);
            }

            String ftitle = bundle.getString("title");
            title.setText(ftitle);

            String fdate = bundle.getString("date");
            date.setText(fdate);

            String fstart = bundle.getString("start");
            start.setText(fstart);

            String fend = bundle.getString("end");
            end.setText(fend);

            String fdesc = bundle.getString("desc");
            desc.setText(fdesc);

            String floc = bundle.getString("location");
            location.setText(floc);


            Bundle bundle2 = new Bundle();

            bundle2.putString("functiontitle", ftitle);
            bundle2.putString("_id", funccd);

            myNoteFragment.setArguments(bundle2);

            FragmentTransaction transaction = getActivity().getFragmentManager()
                    .beginTransaction();
            transaction.addToBackStack(null);
            transaction.commit();



        }

        notesbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String myTitle = bundle.getString("title");
                String myId = bundle.getString("code");
//create an Intent object
                Intent intent = new Intent(getActivity(), NotesActivity.class);
//add data to the Intent object
                intent.putExtra("functiontitle",myTitle );
                intent.putExtra("_id", myId);
//start the second activity
                startActivity(intent);
            }

        });

        surveybutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String urlEndStr = newFunctioncd.replace("'", "");

                Uri uri = Uri.parse("https://www.research.net/s/" + urlEndStr);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setDataAndType(uri, "text/html");
                browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                getActivity().startActivity(browserIntent);


            }
        });

        plannerbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                sqlite_obj.open();
                String pfunctioncd = newFunctioncd.replace("'", "");
                sqlite_obj.DeletePlanner(pfunctioncd);

                Toast.makeText(
                        getActivity().getApplicationContext(),
                        "Deleted from My Schedule!",
                        Toast.LENGTH_SHORT).show();

                sqlite_obj.close();
                //finish();
                getActivity().onBackPressed();

            }


    });


        return v;
    }

    }


