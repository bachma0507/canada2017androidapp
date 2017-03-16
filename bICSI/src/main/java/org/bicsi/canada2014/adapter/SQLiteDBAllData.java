package org.bicsi.canada2014.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.Cursor;

public class SQLiteDBAllData {
	
	public static Date myTime;

	//public static final String KEY_ID = "_id";
	public static final String KEY_ID = "_id";
	//public static final String KEY_FUNCTIONCD= "FUNCTIONCD";
	public static final String KEY_functiontitle = "functiontitle";
	public static final String KEY_functiondescription = "functiondescription";
	public static final String KEY_LOCATIONNAME = "LOCATIONNAME";
	public static final String KEY_fucntioindate = "fucntioindate";
	public static final String KEY_functionStartTime = "functionStartTime";
	public static final String KEY_functionStartTimeStr = "functionStartTimeStr";
	public static final String KEY_functionEndTime = "functionEndTime";
	public static final String KEY_functionEndTimeStr = "functionEndTimeStr";
	public static final String KEY_trainer1firstname = "trainer1firstname";
	public static final String KEY_trainer1lastname = "trainer1lastname";
	public static final String KEY_trainer1org = "trainer1org";
	public static final String KEY_trainer1city = "trainer1city";
	public static final String KEY_trainer1state = "trainer1state";
	public static final String KEY_trainer1country = "trainer1country";
	public static final String KEY_trainer2firstname = "trainer2firstname";
	public static final String KEY_trainer2lastname = "trainer2lastname";
	public static final String KEY_trainer2org = "trainer2org";
	public static final String KEY_trainer2city = "trainer2city";
	public static final String KEY_trainer2state = "trainer2state";
	public static final String KEY_trainer2country = "trainer2country";
	public static final String KEY_trainer3firstname = "trainer3firstname";
	public static final String KEY_trainer3lastname = "trainer3lastname";
	public static final String KEY_trainer3org = "trainer3org";
	public static final String KEY_trainer3city = "trainer3city";
	public static final String KEY_trainer3state = "trainer3state";
	public static final String KEY_trainer3country = "trainer3country";
	public static final String KEY_trainer4firstname = "trainer4firstname";
	public static final String KEY_trainer4lastname = "trainer4lastname";
	public static final String KEY_trainer4org = "trainer4org";
	public static final String KEY_trainer4city = "trainer4city";
	public static final String KEY_trainer4state = "trainer4state";
	public static final String KEY_trainer4country = "trainer4country";
	public static final String KEY_trainer5firstname = "trainer5firstname";
	public static final String KEY_trainer5lastname = "trainer5lastname";
	public static final String KEY_trainer5org = "trainer5org";
	public static final String KEY_trainer5city = "trainer5city";
	public static final String KEY_trainer5state = "trainer5state";
	public static final String KEY_trainer5country = "trainer5country";
	public static final String KEY_trainer6firstname = "trainer6firstname";
	public static final String KEY_trainer6lastname = "trainer6lastname";
	public static final String KEY_trainer6org = "trainer6org";
	public static final String KEY_trainer6city = "trainer6city";
	public static final String KEY_trainer6state = "trainer6state";
	public static final String KEY_trainer6country = "trainer6country";

	public static final String KEY_planner = "planner";
	 
	 
	 private static final String TAG = "DbAdapter";
	 private DatabaseHelper DBHelper;
	 private SQLiteDatabase db;

	 private static final String DATABASE_NAME = "SQLiteDBAllData";
	 private static final String SQLITE_TABLE = "alldata";
	 private static final int DATABASE_VERSION = 1;

	 private final Context context;

	 private static final String DATABASE_CREATE = "create table alldata (_id text not null, functiontitle text not null, functiondescription  text,LOCATIONNAME text, fucntioindate text, functionStartTime text, functionStartTimeStr text, functionEndTime text, functionEndTimeStr text, trainer1firstname text, trainer1lastname text,trainer1org text, trainer1city text, trainer1state text, trainer1country text, trainer2firstname text, trainer2lastname text, trainer2org text, trainer2city text, trainer2state text, trainer2country text, trainer3firstname text, trainer3lastname text, trainer3org text, trainer3city text, trainer3state text, trainer3country text, trainer4firstname text, trainer4lastname text, trainer4org text, trainer4city text, trainer4state text, trainer4country text, trainer5firstname text, trainer5lastname text, trainer5org text, trainer5city text, trainer5state text, trainer5country text, trainer6firstname text, trainer6lastname text, trainer6org text, trainer6city text, trainer6state text, trainer6country text, planner text);";

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
	 
	 public SQLiteDBAllData(Context ctx) {
			
	 		this.context = ctx;
	 		DBHelper = new DatabaseHelper(context);
	 	}

	//---open SQLite DB---
	 public SQLiteDBAllData open() throws SQLException {
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
	 
	 	public long insert(String FUNCTIONCD, String functiontitle, String functiondescription, String LOCATIONNAME, String fucntioindate, String functionStartTime, String functionStartTimeStr, String functionEndTime, String functionEndTimeStr, String trainer1firstname, String trainer1lastname, String trainer1org, String trainer1city, String trainer1state, String trainer1country, String trainer2firstname, String trainer2lastname, String trainer2org, String trainer2city, String trainer2state, String trainer2country, String trainer3firstname, String trainer3lastname, String trainer3org, String trainer3city, String trainer3state, String trainer3country, String trainer4firstname, String trainer4lastname, String trainer4org, String trainer4city, String trainer4state, String trainer4country, String trainer5firstname, String trainer5lastname, String trainer5org, String trainer5city, String trainer5state, String trainer5country, String trainer6firstname, String trainer6lastname, String trainer6org, String trainer6city, String trainer6state, String trainer6country) {
		
	 		ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_ID, FUNCTIONCD);
				//initialValues.put(KEY_FUNCTIONCD, FUNCTIONCD2);
				initialValues.put(KEY_functiontitle, functiontitle);
				initialValues.put(KEY_functiondescription, functiondescription);
				initialValues.put(KEY_LOCATIONNAME, LOCATIONNAME);
				initialValues.put(KEY_fucntioindate, fucntioindate);
				initialValues.put(KEY_functionStartTime, functionStartTime);
			if (functionStartTimeStr.equals("08:01 AM")){
				String functionStartTimeStrNew = "08:00 AM";
				initialValues.put(KEY_functionStartTimeStr, functionStartTimeStrNew);
				System.out.println("***********Value for FUNCTIONSTARTTIMESTRNEW: " + functionStartTimeStrNew);
			}
			else if(functionStartTimeStr.equals("08:02 AM")){
				String functionStartTimeStrNew = "08:00 AM";
				initialValues.put(KEY_functionStartTimeStr, functionStartTimeStrNew);
				System.out.println("***********Value for FUNCTIONSTARTTIMESTRNEW: " + functionStartTimeStrNew);
			}
			else if(functionStartTimeStr.equals("08:59 AM")){
				String functionStartTimeStrNew = "09:00 AM";
				initialValues.put(KEY_functionStartTimeStr, functionStartTimeStrNew);
				System.out.println("***********Value for FUNCTIONSTARTTIMESTRNEW: " + functionStartTimeStrNew);
			}
			else if(functionStartTimeStr.equals("02:59 PM")){
				String functionStartTimeStrNew = "03:00 PM";
				initialValues.put(KEY_functionStartTimeStr, functionStartTimeStrNew);
				System.out.println("***********Value for FUNCTIONSTARTTIMESTRNEW: " + functionStartTimeStrNew);
			}
			else if(functionStartTimeStr.equals("07:29 AM")){
				String functionStartTimeStrNew = "07:30 AM";
				initialValues.put(KEY_functionStartTimeStr, functionStartTimeStrNew);
				System.out.println("***********Value for FUNCTIONSTARTTIMESTRNEW: " + functionStartTimeStrNew);
			}
			else if(functionStartTimeStr.equals("07:59 AM")){
				String functionStartTimeStrNew = "08:00 AM";
				initialValues.put(KEY_functionStartTimeStr, functionStartTimeStrNew);
				System.out.println("***********Value for FUNCTIONSTARTTIMESTRNEW: " + functionStartTimeStrNew);
			}
			else if(functionStartTimeStr.equals("09:01 AM")){
				String functionStartTimeStrNew = "09:00 AM";
				initialValues.put(KEY_functionStartTimeStr, functionStartTimeStrNew);
				System.out.println("***********Value for FUNCTIONSTARTTIMESTRNEW: " + functionStartTimeStrNew);
			}
			else if(functionStartTimeStr.equals("01:31 PM")){
				String functionStartTimeStrNew = "01:30 PM";
				initialValues.put(KEY_functionStartTimeStr, functionStartTimeStrNew);
				System.out.println("***********Value for FUNCTIONSTARTTIMESTRNEW: " + functionStartTimeStrNew);
			}
			else if(functionStartTimeStr.equals("02:01 PM")){
				String functionStartTimeStrNew = "02:00 PM";
				initialValues.put(KEY_functionStartTimeStr, functionStartTimeStrNew);
				System.out.println("***********Value for FUNCTIONSTARTTIMESTRNEW: " + functionStartTimeStrNew);
			}
			else if(functionStartTimeStr.equals("08:31 AM")){
				String functionStartTimeStrNew = "08:30 AM";
				initialValues.put(KEY_functionStartTimeStr, functionStartTimeStrNew);
				System.out.println("***********Value for FUNCTIONSTARTTIMESTRNEW: " + functionStartTimeStrNew);
			}
			else{
				initialValues.put(KEY_functionStartTimeStr, functionStartTimeStr);
			}
				System.out.println("***********Value for FUNCTIONSTARTTIMESTR: " + functionStartTimeStr);

				initialValues.put(KEY_functionEndTime, functionEndTime);
				initialValues.put(KEY_functionEndTimeStr, functionEndTimeStr);
				initialValues.put(KEY_trainer1firstname, trainer1firstname);
				initialValues.put(KEY_trainer1lastname, trainer1lastname);
				initialValues.put(KEY_trainer1org, trainer1org);
				initialValues.put(KEY_trainer1city, trainer1city);
				initialValues.put(KEY_trainer1state, trainer1state);
				initialValues.put(KEY_trainer1country, trainer1country);
				initialValues.put(KEY_trainer2firstname, trainer2firstname);
				initialValues.put(KEY_trainer2lastname, trainer2lastname);
				initialValues.put(KEY_trainer2org, trainer2org);
				initialValues.put(KEY_trainer2city, trainer2city);
				initialValues.put(KEY_trainer2state, trainer2state);
				initialValues.put(KEY_trainer2country, trainer2country);
				initialValues.put(KEY_trainer3firstname, trainer3firstname);
				initialValues.put(KEY_trainer3lastname, trainer3lastname);
				initialValues.put(KEY_trainer3org, trainer3org);
				initialValues.put(KEY_trainer3city, trainer3city);
				initialValues.put(KEY_trainer3state, trainer3state);
				initialValues.put(KEY_trainer3country, trainer3country);
				initialValues.put(KEY_trainer4firstname, trainer4firstname);
				initialValues.put(KEY_trainer4lastname, trainer4lastname);
				initialValues.put(KEY_trainer4org, trainer4org);
				initialValues.put(KEY_trainer4city, trainer4city);
				initialValues.put(KEY_trainer4state, trainer4state);
				initialValues.put(KEY_trainer4country, trainer4country);
				initialValues.put(KEY_trainer5firstname, trainer5firstname);
				initialValues.put(KEY_trainer5lastname, trainer5lastname);
				initialValues.put(KEY_trainer5org, trainer5org);
				initialValues.put(KEY_trainer5city, trainer5city);
				initialValues.put(KEY_trainer5state, trainer5state);
				initialValues.put(KEY_trainer5country, trainer5country);
				initialValues.put(KEY_trainer6firstname, trainer6firstname);
				initialValues.put(KEY_trainer6lastname, trainer6lastname);
				initialValues.put(KEY_trainer6org, trainer6org);
				initialValues.put(KEY_trainer6city, trainer6city);
				initialValues.put(KEY_trainer6state, trainer6state);
				initialValues.put(KEY_trainer6country, trainer6country);
				//initialValues.put(KEY_planner, planner);
			
	 		return db.insert(SQLITE_TABLE, null, initialValues);
	 	}

		//Update planner field
	public long updatePlanner(String planner, String functioncd){

		ContentValues editValues = new ContentValues();
		editValues.put(KEY_planner, planner);

		return db.update(SQLITE_TABLE, editValues,KEY_ID + "='" + functioncd + "'",null);
	}

	 //---Delete All Data from table in SQLite DB---
	 	public void deleteAll() {
		
	 		db.delete(SQLITE_TABLE, null, null);
	 	}
	 	
	 	/*public long insert(String FUNCTIONCD, String functiontitle, String functiondescription){
	 		
	 		ContentValues initialValues = new ContentValues();
			//initialValues.put(KEY_ID, id);
				initialValues.put(KEY_FUNCTIONCD, FUNCTIONCD);
				initialValues.put(KEY_functiontitle, functiontitle);
				initialValues.put(KEY_functiondescription, functiondescription);
				
				return db.insert(SQLITE_TABLE, null, initialValues);
	 	}*/



	public Cursor fetchScheduleByDate(String inputText, String newConfDate) throws SQLException {
	   Log.w(TAG, inputText);
	   Cursor mCursor = null;
	   if (inputText == null  ||  inputText.length () == 0)  {
	    mCursor = db.query(SQLITE_TABLE, new String[] {
	    		KEY_ID, KEY_functiontitle, KEY_fucntioindate, KEY_functionStartTime, KEY_functionStartTimeStr, KEY_functionEndTime, KEY_functionEndTimeStr, KEY_functiondescription, KEY_LOCATIONNAME, KEY_trainer1firstname, KEY_trainer1lastname, KEY_trainer2firstname, KEY_trainer2lastname, KEY_trainer3firstname, KEY_trainer3lastname, KEY_trainer4firstname, KEY_trainer4lastname,KEY_trainer5firstname, KEY_trainer5lastname, KEY_trainer6firstname, KEY_trainer6lastname},
	    		KEY_fucntioindate + " = " + newConfDate + " AND (" + KEY_ID + " LIKE 'ATT%' OR " + KEY_ID + " LIKE 'BIC%' OR " + KEY_ID + " LIKE 'BREA%' OR " + KEY_ID + " LIKE 'COM%' OR " + KEY_ID + " LIKE 'CONC%' OR " + KEY_ID + " LIKE 'CONF%' OR " + KEY_ID + " LIKE 'CRED_E%' OR " + KEY_ID + " LIKE 'EH%' OR " + KEY_ID + " LIKE 'GS_TUES_%' OR " + KEY_ID + " LIKE 'GS_THURS_%' OR " + KEY_ID + " LIKE 'EX_REG%' OR " + KEY_ID + " LIKE 'EXAM_CHECK' OR " + KEY_ID + " LIKE 'CSC_%' OR " + KEY_ID + " LIKE 'EXV_REG%' OR " + KEY_ID + " LIKE 'BANQ_COMP' OR " + KEY_ID + " LIKE 'BANQ_RECEP' OR " + KEY_ID + " LIKE 'PRECON%') ORDER BY " + KEY_functionStartTime + ", " + KEY_functionEndTime + " ASC", null, null, null, null, null);
	    		//KEY_fucntioindate + " = " + newConfDate + " AND " + KEY_ID + " NOT LIKE 'EXHX%'", null, null, null, null, null);
	   }
	   else {
	    mCursor = db.query(true, SQLITE_TABLE, new String[] {
	    		KEY_ID, KEY_functiontitle, KEY_fucntioindate, KEY_functionStartTime, KEY_functionStartTimeStr, KEY_functionEndTime, KEY_functionEndTimeStr, KEY_functiondescription, KEY_LOCATIONNAME, KEY_trainer1firstname, KEY_trainer1lastname, KEY_trainer2firstname, KEY_trainer2lastname, KEY_trainer3firstname, KEY_trainer3lastname, KEY_trainer4firstname, KEY_trainer4lastname,KEY_trainer5firstname, KEY_trainer5lastname, KEY_trainer6firstname, KEY_trainer6lastname},
	    		KEY_functiontitle + " like '%" + inputText + "%' AND " + KEY_fucntioindate + " = " + newConfDate + " AND (" + KEY_ID + " LIKE 'ATT%' OR " + KEY_ID + " LIKE 'BIC%' OR " + KEY_ID + " LIKE 'BREA%' OR " + KEY_ID + " LIKE 'COM%' OR " + KEY_ID + " LIKE 'CONC%' OR " + KEY_ID + " LIKE 'CONF%' OR " + KEY_ID + " LIKE 'CRED_E%' OR " + KEY_ID + " LIKE 'EH%' OR " + KEY_ID + " LIKE 'GS_TUES_%' OR " + KEY_ID + " LIKE 'GS_THURS_%' OR " + KEY_ID + " LIKE 'EX_REG%' OR " + KEY_ID + " LIKE 'EXAM_CHECK' OR " + KEY_ID + " LIKE 'CSC_%' OR " + KEY_ID + " LIKE 'EXV_REG%' OR " + KEY_ID + " LIKE 'BANQ_COMP' OR " + KEY_ID + " LIKE 'BANQ_RECEP' OR " + KEY_ID + " LIKE 'PRECON%') ORDER BY " + KEY_functionStartTime + ", " + KEY_functionEndTime + " ASC", null, null, null, null, null);
	    		//KEY_functiontitle + " like '%" + inputText + "%' AND " + KEY_fucntioindate + " = " + newConfDate + " AND " + KEY_ID + " NOT LIKE 'EXHX%'", null, null, null, null, null);
	   }
	   if (mCursor != null) {
	    mCursor.moveToFirst();
	   }
	   return mCursor;

	  }

	 
	 /*public Cursor getAllSChedulesByConfDate(String newConfDate){
		 Cursor mCursor = db.query(SQLITE_TABLE,  new String[] {
				 KEY_ID, KEY_functiontitle, KEY_fucntioindate, KEY_functionStartTime, KEY_functionEndTime, KEY_functiondescription, KEY_LOCATIONNAME },
				 KEY_fucntioindate + " = " + newConfDate + " AND (" + KEY_ID + " NOT LIKE 'EXHX%' OR " + KEY_ID + " NOT LIKE 'EXHV%')", null, null, null, null, null);
				 //KEY_fucntioindate + " = " + newConfDate + " AND " + KEY_ID + " NOT LIKE 'EXHX%'", null, null, null, null, null);
				 //"(" + KEY_ID + " NOT LIKE 'EXHX%' OR " + KEY_ID + " NOT LIKE 'EXHV%') AND " + KEY_fucntioindate + " = " + newConfDate, null, null, null, null, null);
		 
		 if (mCursor != null) {
			 mCursor.moveToFirst(); 
		 }
		 return mCursor;
		 
	 }*/
	 
	 public Cursor getAllSChedulesByConfDateNew(String newConfDate){
		 //Cursor mCursor = db.rawQuery("SELECT _id, functiontitle, fucntioindate, functionStartTime, functionEndTime, functiondescription, LOCATIONNAME FROM " + SQLITE_TABLE + " WHERE fucntioindate = " + newConfDate + " AND (_id LIKE 'EXHX%' OR _id LIKE 'EXHV%')", null);
		 Cursor mCursor = db.query(SQLITE_TABLE, new String[] {
		 KEY_ID, KEY_functiontitle, KEY_fucntioindate, KEY_functionStartTime, KEY_functionStartTimeStr, KEY_functionEndTime, KEY_functionEndTimeStr, KEY_functiondescription, KEY_LOCATIONNAME, KEY_trainer1firstname, KEY_trainer1lastname, KEY_trainer2firstname, KEY_trainer2lastname, KEY_trainer3firstname, KEY_trainer3lastname, KEY_trainer4firstname, KEY_trainer4lastname,KEY_trainer5firstname, KEY_trainer5lastname, KEY_trainer6firstname, KEY_trainer6lastname },
				 KEY_fucntioindate + " = " + newConfDate + " AND (" + KEY_ID + " LIKE 'ATT%' OR " + KEY_ID + " LIKE 'BIC%' OR " + KEY_ID + " LIKE 'BREA%' OR " + KEY_ID + " LIKE 'COM%' OR " + KEY_ID + " LIKE 'CONC%' OR " + KEY_ID + " LIKE 'CONF%' OR " + KEY_ID + " LIKE 'CRED_E%' OR " + KEY_ID + " LIKE 'EH%' OR " + KEY_ID + " LIKE 'GS_TUES_%' OR " + KEY_ID + " LIKE 'GS_THURS_%' OR " + KEY_ID + " LIKE 'EX_REG%' OR " + KEY_ID + " LIKE 'EXAM_CHECK' OR " + KEY_ID + " LIKE 'CSC_%' OR " + KEY_ID + " LIKE 'EXV_REG%' OR " + KEY_ID + " LIKE 'BANQ_COMP' OR " + KEY_ID + " LIKE 'BANQ_RECEP' OR " + KEY_ID + " LIKE 'PRECON%') ORDER BY " + KEY_functionStartTime + ", " + KEY_functionEndTime + " ASC", null, null, null, null, null);
				 //KEY_fucntioindate + " = " + newConfDate + " AND " + KEY_ID + " NOT LIKE 'EXHX%'", null, null, null, null, null);
				 //"(" + KEY_ID + " NOT LIKE 'EXHX%' OR " + KEY_ID + " NOT LIKE 'EXHV%') AND " + KEY_fucntioindate + " = " + newConfDate, null, null, null, null, null);
		 
		 if (mCursor != null) {
			 mCursor.moveToFirst(); 
		 }
		 return mCursor;
		 
	 }

	 
	 public Cursor fetchCommByName(String inputText) throws SQLException {
		   Log.w(TAG, inputText);
		   Cursor mCursor = null;
		   if (inputText == null  ||  inputText.length () == 0)  {
		    mCursor = db.query(SQLITE_TABLE, new String[] {
		    		KEY_ID, KEY_functiontitle, KEY_fucntioindate, KEY_functionStartTime, KEY_functionStartTimeStr, KEY_functionEndTime, KEY_functionEndTimeStr, KEY_functiondescription, KEY_LOCATIONNAME}, 
		    		KEY_ID + " LIKE 'COM%' ORDER BY " + KEY_fucntioindate + ", " + KEY_functionStartTime + ", " + KEY_functionEndTime + " ASC", null,
		      null, null, null, null);
		    		
		   }
		   else {
		    mCursor = db.query(true, SQLITE_TABLE, new String[] {
		    		KEY_ID, KEY_functiontitle, KEY_fucntioindate, KEY_functionStartTime, KEY_functionStartTimeStr, KEY_functionEndTime, KEY_functionEndTimeStr, KEY_functiondescription, KEY_LOCATIONNAME},
		    		KEY_functiontitle + " like '%" + inputText + "%' AND " + KEY_ID + " LIKE 'COM%' ORDER BY " + KEY_fucntioindate + ", " + KEY_functionStartTime + ", " + KEY_functionEndTime + " ASC", null, null, null, null, null);
		   }
		   if (mCursor != null) {
		    mCursor.moveToFirst();
		   }
		   return mCursor;

		  }
	 
	 public Cursor fetchAllSchedulesByFuncComm(){

		  Cursor mCursor = db.query(SQLITE_TABLE, new String[] {
		    		KEY_ID, KEY_functiontitle, KEY_fucntioindate, KEY_functionStartTime, KEY_functionStartTimeStr, KEY_functionEndTime, KEY_functionEndTimeStr, KEY_functiondescription, KEY_LOCATIONNAME}, 
		    		KEY_ID + " LIKE 'COM%' ORDER BY " + KEY_fucntioindate + ", " + KEY_functionStartTime + ", " + KEY_functionEndTime + " ASC", null,
		      null, null, null, null);

		  if (mCursor != null) {
		   mCursor.moveToFirst();
		  }
		  return mCursor;
		 }
	 
	 public Cursor fetchAllSchedulesByDate092814() {

		  Cursor mCursor = db.query(SQLITE_TABLE, new String[] {
		    		KEY_ID, KEY_functiontitle, KEY_fucntioindate, KEY_functionStartTime, KEY_functionEndTime}, 
		    		KEY_fucntioindate + " = '09-28-2014' ", null,
		      null, null, null, null);

		  if (mCursor != null) {
		   mCursor.moveToFirst();
		  }
		  return mCursor;
		 }
	 
	 public Cursor fetchAllSchedules() {

		  Cursor mCursor = db.query(SQLITE_TABLE, new String[] {
				  KEY_ID, KEY_functiontitle, KEY_fucntioindate, KEY_functionStartTime, KEY_functionEndTime}, 
		    null, null, null, null, null);

		  if (mCursor != null) {
		   mCursor.moveToFirst();
		  }
		  return mCursor;
		 }


	public Cursor getAllSChedulesByPlanner(){
		Cursor mCursor = db.query(SQLITE_TABLE, new String[] {
						KEY_ID, KEY_functiontitle, KEY_fucntioindate, KEY_functionStartTime, KEY_functionStartTimeStr, KEY_functionEndTime, KEY_functionEndTimeStr, KEY_functiondescription, KEY_LOCATIONNAME, KEY_trainer1firstname, KEY_trainer1lastname, KEY_trainer2firstname, KEY_trainer2lastname, KEY_trainer3firstname, KEY_trainer3lastname, KEY_trainer4firstname, KEY_trainer4lastname,KEY_trainer5firstname, KEY_trainer5lastname, KEY_trainer6firstname, KEY_trainer6lastname, KEY_planner },
				KEY_planner + " = 'yes' AND (" + KEY_ID + " LIKE 'ATT%' OR " + KEY_ID + " LIKE 'BIC%' OR " + KEY_ID + " LIKE 'BREA%' OR " + KEY_ID + " LIKE 'COM%' OR " + KEY_ID + " LIKE 'CONC%' OR " + KEY_ID + " LIKE 'CONF%' OR " + KEY_ID + " LIKE 'CRED_E%' OR " + KEY_ID + " LIKE 'EH%' OR " + KEY_ID + " LIKE 'GS_TUES_%' OR " + KEY_ID + " LIKE 'GS_THURS_%' OR " + KEY_ID + " LIKE 'EX_REG%' OR " + KEY_ID + " LIKE 'EXAM_CHECK' OR " + KEY_ID + " LIKE 'CSC_%' OR " + KEY_ID + " LIKE 'EXV_REG%' OR " + KEY_ID + " LIKE 'BANQ_COMP' OR " + KEY_ID + " LIKE 'BANQ_RECEP' OR " + KEY_ID + " LIKE 'PRECON%') ORDER BY " + KEY_functionStartTime + ", " + KEY_functionEndTime + " ASC", null, null, null, null, null);


		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}


	public Cursor getSchedulesByFuncCd(String functioncd){

		Cursor mCursor = db.query(SQLITE_TABLE, new String[] {
						KEY_ID, KEY_functiontitle, KEY_fucntioindate, KEY_functionStartTime, KEY_functionStartTimeStr, KEY_functionEndTime, KEY_functionEndTimeStr, KEY_functiondescription, KEY_LOCATIONNAME, KEY_trainer1firstname, KEY_trainer1lastname, KEY_trainer2firstname, KEY_trainer2lastname, KEY_trainer3firstname, KEY_trainer3lastname, KEY_trainer4firstname, KEY_trainer4lastname,KEY_trainer5firstname, KEY_trainer5lastname, KEY_trainer6firstname, KEY_trainer6lastname, KEY_planner },
				KEY_ID + " = '" + functioncd + "'", null, null, null, null, null);


		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public Cursor getSchedulesByFuncCode(String functioncd){

		Cursor mCursor = db.query(SQLITE_TABLE, new String[] {
						KEY_ID, KEY_functiontitle, KEY_fucntioindate, KEY_functionStartTime, KEY_functionStartTimeStr, KEY_functionEndTime, KEY_functionEndTimeStr, KEY_functiondescription, KEY_LOCATIONNAME },
				KEY_ID + " = '" + functioncd + "'", null, null, null, null, null);


		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}
}

