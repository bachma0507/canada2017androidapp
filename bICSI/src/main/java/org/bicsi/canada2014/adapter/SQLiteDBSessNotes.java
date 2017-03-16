package org.bicsi.canada2014.adapter;

/**
 * Created by barry on 5/5/15.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

import org.bicsi.canada2014.SessionNotes;

public class SQLiteDBSessNotes {

    public static final String KEY_ID = "_id";
    public static final String KEY_FUNCCD = "code";
    public static final String KEY_FUNCTITLE = "title";
    public static final String KEY_FUNCDESC = "desc";


    private static final String TAG = "DbAdapter";
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    private static final String DATABASE_NAME = "SQLiteDBSessNotes";
    private static final String SQLITE_TABLE = "sessnotes";
    private static final int DATABASE_VERSION = 1;

    private final Context context;

    private static final String DATABASE_CREATE =
            "create table sessnotes (_id integer primary key autoincrement, code text not null, title text not null, desc text not null);";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                Log.w(TAG, DATABASE_CREATE);
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS sample ");
            onCreate(db);
        }
    }

	 /*public SQLiteDB(Context ctx) {
	  this.context = ctx;
	 }*/

    public SQLiteDBSessNotes(Context ctx) {

        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    //---open SQLite DB---
    public SQLiteDBSessNotes open() throws SQLException {
	  /*mDbHelper = new DatabaseHelper(mCtx);*/
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---close SQLite DB---
    public void close() {
	  /*if (mDbHelper != null) {
	   mDbHelper.close();
	  }*/
        DBHelper.close();
    }

    //---insert data into SQLite DB---
    public long insertNote(SessionNotes note) {

        ContentValues initialValues = new ContentValues();
        //initialValues.put(KEY_ID, id);
        initialValues.put(KEY_FUNCCD, note.code);
        initialValues.put(KEY_FUNCTITLE, note.title);
        initialValues.put(KEY_FUNCDESC, note.desc);

        long insert = db.insert(SQLITE_TABLE, null, initialValues);

        return insert;
    }

    // Update Database function
    public long UpdateNote(SessionNotes note) {
        ContentValues editValues = new ContentValues();
        //editValues.put(KEY_FUNCCD, note.code);
        //editValues.put(KEY_FUNCTITLE, note.title);
        editValues.put(KEY_FUNCDESC, note.desc);

        //open();
        long update = db.update(SQLITE_TABLE, editValues, KEY_FUNCCD + " = '" + note.code + "'", null);
        //close();
        return update;
    }

    // Delete Database function
    public void DeleteNote(String code) {
        //open();
        db.delete(SQLITE_TABLE, KEY_FUNCCD + "=" + code, null);
        //close();
    }

    //---Delete All Data from table in SQLite DB---
    public void deleteAll() {

        db.delete(SQLITE_TABLE, null, null);
    }

    public List<SessionNotes> getAllNotes() {
        List<SessionNotes> notesArrayList = new ArrayList<SessionNotes>();

        String selectQuery = "SELECT  * FROM " + SQLITE_TABLE;
        Log.d(TAG, selectQuery);

        //db = DBHelper.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                SessionNotes note = new SessionNotes();
                note._id = c.getInt(c.getColumnIndex(KEY_ID));
                note.code = c.getString(c
                        .getColumnIndex(KEY_FUNCCD));
                note.title = c.getString(c.getColumnIndex(KEY_FUNCTITLE));
                note.desc = c.getString(c.getColumnIndex(KEY_FUNCDESC));

                // adding to Students list
                notesArrayList.add(note);
            } while (c.moveToNext());
        }

        return notesArrayList;
    }
//}
    public Cursor fetchNoteByCode(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = db.query(SQLITE_TABLE, new String[] {
                            KEY_FUNCCD, KEY_FUNCTITLE, KEY_FUNCDESC},
                    null, null, null, null, null);

        }
        else {
            mCursor = db.query(true, SQLITE_TABLE, new String[] {
                            KEY_FUNCCD, KEY_FUNCTITLE, KEY_FUNCDESC},
                    KEY_FUNCCD + " = '" + inputText + "'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public SessionNotes getNote(String code) {
        //SQLiteDatabase db = this.getReadableDatabase();

        // SELECT * FROM students WHERE id = ?;
        String selectQuery = "SELECT  * FROM " + SQLITE_TABLE + " WHERE "
                + KEY_FUNCCD + " = '" + code + "'";
        Log.d(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        SessionNotes note = new SessionNotes();
        note.code = c.getString(c.getColumnIndex(KEY_FUNCCD));
        note.title = c.getString(c.getColumnIndex(KEY_FUNCTITLE));
        note.desc = c.getString(c.getColumnIndex(KEY_FUNCDESC));

        return note;
    }


    public Cursor fetchAllNotes() {

        Cursor mCursor = db.query(SQLITE_TABLE, new String[] {
                        KEY_FUNCCD, KEY_FUNCTITLE, KEY_FUNCDESC},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }





}
