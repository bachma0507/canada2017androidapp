package org.bicsi.canada2014.adapter;

/**
 * Created by barry on 6/4/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.Cursor;

import com.facebook.internal.SessionTracker;

import java.util.ArrayList;
import java.util.List;
import org.bicsi.canada2014.Planner;

public class SQLiteDBPlanner {

    public static final String KEY_ID = "_id";
    public static final String KEY_FUNCCD = "code";
    public static final String KEY_FUNCTITLE = "title";
    public static final String KEY_FUNCDESC = "desc";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_DATE = "date";
    public static final String KEY_START = "start";
    public static final String KEY_END = "end";


    private static final String TAG = "DbAdapter";
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    private static final String DATABASE_NAME = "SQLiteDBPlanner";
    private static final String SQLITE_TABLE = "planner";
    private static final int DATABASE_VERSION = 1;

    private final Context context;

    private static final String DATABASE_CREATE =
            "create table planner (_id integer primary key autoincrement, code text not null, title text not null, desc text not null, location text not null, date text not null, start text not null, end text not null);";

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

    public SQLiteDBPlanner(Context ctx) {

        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    //---open SQLite DB---
    public SQLiteDBPlanner open() throws SQLException {
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
    public long insertPlanner(Planner plannerItem) {

        ContentValues initialValues = new ContentValues();
        //initialValues.put(KEY_ID, id);
        initialValues.put(KEY_FUNCCD, plannerItem.code);
        initialValues.put(KEY_FUNCTITLE, plannerItem.title);
        initialValues.put(KEY_FUNCDESC, plannerItem.desc);
        initialValues.put(KEY_LOCATION, plannerItem.location);
        initialValues.put(KEY_DATE, plannerItem.date);
        initialValues.put(KEY_START, plannerItem.start);
        initialValues.put(KEY_END, plannerItem.end);

        long insert = db.insert(SQLITE_TABLE, null, initialValues);

        return insert;
    }


    // Delete Database function
    public void DeletePlanner(String code) {
        //open();
        db.delete(SQLITE_TABLE, KEY_FUNCCD + "= '" + code + "'", null);
        //close();
    }

    //---Delete All Data from table in SQLite DB---
    public void deleteAll() {

        db.delete(SQLITE_TABLE, null, null);
    }


    //}
    public Cursor fetchPlannerByCode(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = db.query(SQLITE_TABLE, new String[] {
                            KEY_FUNCCD, KEY_FUNCTITLE, KEY_FUNCDESC, KEY_LOCATION, KEY_DATE, KEY_START, KEY_END},
                    null, null, null, null, null);

        }
        else {
            mCursor = db.query(true, SQLITE_TABLE, new String[] {
                            KEY_FUNCCD, KEY_FUNCTITLE, KEY_FUNCDESC, KEY_LOCATION, KEY_DATE, KEY_START, KEY_END},
                    KEY_FUNCCD + " = '" + inputText + "'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Planner getPlannerItem(String code) {

        String selectQuery = "SELECT  * FROM " + SQLITE_TABLE + " WHERE "
                + KEY_FUNCCD + " = '" + code + "'";
        Log.d(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Planner plannerItem = new Planner();
        plannerItem.code = c.getString(c.getColumnIndex(KEY_FUNCCD));
        plannerItem.title = c.getString(c.getColumnIndex(KEY_FUNCTITLE));
        plannerItem.desc = c.getString(c.getColumnIndex(KEY_FUNCDESC));
        plannerItem.location = c.getString(c.getColumnIndex(KEY_LOCATION));
        plannerItem.date = c.getString(c.getColumnIndex(KEY_DATE));
        plannerItem.start = c.getString(c.getColumnIndex(KEY_START));
        plannerItem.end = c.getString(c.getColumnIndex(KEY_END));

        return plannerItem;
    }


    public Cursor fetchAllPlannerItems() {

        Cursor mCursor = db.query(SQLITE_TABLE, new String[] {
                        KEY_ID,KEY_FUNCCD, KEY_FUNCTITLE, KEY_FUNCDESC, KEY_LOCATION, KEY_DATE, KEY_START, KEY_END},
                null, null, null, null, KEY_DATE+ " ASC," +KEY_START+ " ASC");

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public List<Planner> getAllPlannerItems() {
        List<Planner> plannerItemsArrayList = new ArrayList<Planner>();

        String selectQuery = "SELECT  * FROM " + SQLITE_TABLE;
        Log.d(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                Planner plannerItems = new Planner();
                plannerItems._id = c.getInt(c.getColumnIndex(KEY_ID));
                plannerItems.code = c.getString(c.getColumnIndex(KEY_FUNCCD));
                plannerItems.title = c.getString(c.getColumnIndex(KEY_FUNCTITLE));
                plannerItems.desc = c.getString(c.getColumnIndex(KEY_FUNCDESC));
                plannerItems.location = c.getString(c.getColumnIndex(KEY_LOCATION));
                plannerItems.date = c.getString(c.getColumnIndex(KEY_DATE));
                plannerItems.start = c.getString(c.getColumnIndex(KEY_START));
                plannerItems.end = c.getString(c.getColumnIndex(KEY_END));

                // adding to Planner Items list
                plannerItemsArrayList.add(plannerItems);
            } while (c.moveToNext());
        }

        return plannerItemsArrayList;
    }


    public Cursor getItem(String code) {

        String selectQuery = "SELECT  * FROM " + SQLITE_TABLE + " WHERE "
                + KEY_FUNCCD + " = '" + code + "'";
        Log.d(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Planner plannerItem = new Planner();
        plannerItem.code = c.getString(c.getColumnIndex(KEY_FUNCCD));
        plannerItem.title = c.getString(c.getColumnIndex(KEY_FUNCTITLE));
        plannerItem.desc = c.getString(c.getColumnIndex(KEY_FUNCDESC));
        plannerItem.location = c.getString(c.getColumnIndex(KEY_LOCATION));
        plannerItem.date = c.getString(c.getColumnIndex(KEY_DATE));
        plannerItem.start = c.getString(c.getColumnIndex(KEY_START));
        plannerItem.end = c.getString(c.getColumnIndex(KEY_END));

        return c;
    }

}
