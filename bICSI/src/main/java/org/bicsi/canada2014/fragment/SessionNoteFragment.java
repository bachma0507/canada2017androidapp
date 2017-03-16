package org.bicsi.canada2014.fragment;

/**
 * Created by barry on 5/4/15.
 */



import org.bicsi.canada2014.common.MizeUtil.NavigateToTabFragmentListener;
import org.bicsi.canada2017.R;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.bicsi.canada2014.SessionNotes;
import org.bicsi.canada2014.adapter.SQLiteDBSessNotes;
import java.util.ArrayList;
import java.util.List;


public class SessionNoteFragment extends Fragment {

    private NavigateToTabFragmentListener mCallback;
    private SQLiteDBSessNotes sqlite_obj;

    //public String newFunctioncd;

    TextView title;
    EditText comment;
    Button savebutton;
    Button cancelbutton;
    List<SessionNotes> list = new ArrayList<SessionNotes>();
    List<SessionNotes> noteList = new ArrayList<SessionNotes>();

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

        //setRetainInstance(true);

        super.onCreateView(inflater, container, savedInstanceState);
        final View v = inflater.inflate(R.layout.fragment_session_note, container, false);

        sqlite_obj = new SQLiteDBSessNotes(getActivity());

        sqlite_obj.open();


        title = (TextView) v.findViewById(R.id.functiontitle);
        comment = (EditText) v.findViewById(R.id.comment);
        savebutton = (Button) v.findViewById(R.id.save_button);
        cancelbutton = (Button) v.findViewById(R.id.cancel_button);



        Bundle bundle = getArguments();

        final String ftitle = bundle.getString("functiontitle");
        title.setText(ftitle);

        final String funccd = bundle.getString("_id");
        System.out.println("The session code is: " + funccd + ".");

        //SessionNotes myNote = sqlite_obj.getNote(funccd);
        //printNote(myNote);

        savebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //openInternalWebview("http://www.bicsi.org/m/Schedule.aspx");



                SessionNotes note = new SessionNotes();
                //SessionNotes note = ((NewNoteActivity) getActivity()).getCurrentSessionNotes();
                note.code = funccd;
                note.title = ftitle;
                note.desc = comment.getText().toString();
                sqlite_obj.insertNote(note);

                list = sqlite_obj.getAllNotes();
                //System.out.println(print(list));
                print(list);

                //back();
            }
        });

        cancelbutton = ((Button) v.findViewById(R.id.cancel_button));
        cancelbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });

        return v;
    }

    private void printNote(SessionNotes myNote){

        String value = "";

            value = value+"id: "+myNote._id+", funccode: "+myNote.code+" title: "+myNote.title+" comment: "+myNote.desc+"\n";

        System.out.println(value);

    }
    private void print(List<SessionNotes> list) {
        // TODO Auto-generated method stub
        String value = "";
        for(SessionNotes sn : list){
            value = value+"id: "+sn._id+", funccode: "+sn.code+" title: "+sn.title+" comment: "+sn.desc+"\n";
        }
        System.out.println(value);
    }

    /*private void back() {
        FragmentManager fm = getActivity().getFragmentManager();
        fm.popBackStack("ConfSchedSingleFragment",
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }*/

    //FragmentManager fragmentManager = getFragmentManager();
    //FragmentTransaction fragmentTransaction;
    //fragmentTransaction.addToBackStack(null);
    /*@Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).updateTracker("Home Tab");
    }*/

}
