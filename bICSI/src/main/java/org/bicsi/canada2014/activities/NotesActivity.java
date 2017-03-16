package org.bicsi.canada2014.activities;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.bicsi.canada2014.Meal;
import org.bicsi.canada2014.SessionNotes;
import org.bicsi.canada2014.adapter.SQLiteDBSessNotes;
import org.bicsi.canada2017.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by barry on 6/2/15.
 */
public class NotesActivity extends Activity{

    private SQLiteDBSessNotes sqlite_obj;
    private Context mContext = this;

    TextView title;
    EditText comment;
    Button savebutton;
    Button cancelbutton;
    List<SessionNotes> list = new ArrayList<SessionNotes>();
    List<SessionNotes> noteList = new ArrayList<SessionNotes>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_session_note);



        sqlite_obj = new SQLiteDBSessNotes(this);

        sqlite_obj.open();


        title = (TextView) findViewById(R.id.functiontitle);
        comment = (EditText) findViewById(R.id.comment);
        savebutton = (Button) findViewById(R.id.save_button);
        cancelbutton = (Button) findViewById(R.id.cancel_button);

        final String funccd = getIntent().getStringExtra("_id");
        String newFunccd = funccd.replace("'", "");
        System.out.println("The session code is: " + newFunccd + ".");


        Cursor note = sqlite_obj.fetchNoteByCode(newFunccd);

        if (note.moveToFirst() == false){

            comment.setText("");

        }

        else {

            comment.setText(note.getString(
                    note.getColumnIndexOrThrow(sqlite_obj.KEY_FUNCDESC)));
        }


        savebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //openInternalWebview("http://www.bicsi.org/m/Schedule.aspx");


                final String ftitle = getIntent().getStringExtra("functiontitle");

                final String funccd = getIntent().getStringExtra("_id");
                String newFunccd = funccd.replace("'", "");
                System.out.println("The session code is: " + newFunccd + ".");

                Cursor cNote = sqlite_obj.fetchNoteByCode(newFunccd);

                SessionNotes note = new SessionNotes();
                //SessionNotes note = ((NewNoteActivity) getActivity()).getCurrentSessionNotes();
                if (cNote.moveToFirst() == false) {
                    note.code = newFunccd;
                    note.title = ftitle;
                    note.desc = comment.getText().toString();
                    sqlite_obj.insertNote(note);

                    Toast.makeText(
                            mContext.getApplicationContext(),
                            "Note saved!",
                            Toast.LENGTH_SHORT).show();
                    sqlite_obj.close();
                    finish();
                } else {
                    note.code = newFunccd;
                    note.title = ftitle;
                    note.desc = comment.getText().toString();
                    sqlite_obj.UpdateNote(note);

                    Toast.makeText(
                            mContext.getApplicationContext(),
                            "Note updated!",
                            Toast.LENGTH_SHORT).show();
                    sqlite_obj.close();
                    finish();
                }

                /*list = sqlite_obj.getAllNotes();
                //System.out.println(print(list));
                print(list);*/

                //back();
            }
        });

        cancelbutton = ((Button) findViewById(R.id.cancel_button));
        cancelbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vew) {

                finish();
            }
        });

        View v = new View(this);
         v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                hideKeyboard(view);

                return false;
            }

        });
    }

    public void onStart(){
        super.onStart();
        //TextView tv=(TextView)findViewById(R.id.txt_view);
//show text in the Intent object in the TextView
        title.setText(getIntent().getStringExtra("functiontitle"));

        /*getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );*/


    }

    /*private void print(List<SessionNotes> list) {
        // TODO Auto-generated method stub
        String value = "";
        for(SessionNotes sn : list){
            value = value+"id: "+sn._id+", funccode: "+sn.code+" title: "+sn.title+" comment: "+sn.desc+"\n";
        }
        System.out.println(value);

        sqlite_obj.close();


    }*/

    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) (this.getSystemService(Context.INPUT_METHOD_SERVICE));
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


}
