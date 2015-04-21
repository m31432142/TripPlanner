package com.example.tripplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class SQLHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "trip.db";
	public static final int DATABASE_VERSION = 1;
	public static final String TBL_TRIP = "trip";
	public static final String TBL_ALERT = "alert";
	public static final String TBL_PACK = "pack";
	public static final String TBL_TODO = "todo";
	public static final String TBL_PLACE = "place";
	public static final String KEY_NAME = "name";
	public static final String KEY_NUM = "list_num";
	public static final String DEST = "destination";
	public static final String START_DT = "start_dt";
	public static final String END_DT = "end_dt";
	public static final String TRIP_ID = "trip_id";
	public static final String CHECK = "checked";
	private ContentValues values;

	public static final String KEY_ID = "id integer primary key autoincrement";

	public static final String CREATE_TBL_TRIP = "CREATE TABLE " + TBL_TRIP
			+ "(" + KEY_ID + "," + KEY_NAME + " text UNIQUE, " + DEST
			+ " text, " + START_DT + " date, " + END_DT + " date);";

	public static final String CREATE_TBL_TODO = "CREATE TABLE " + TBL_TODO
			+ "(" + TRIP_ID + " integer, " + KEY_NUM + " integer, " + KEY_NAME
			+ " text, " + CHECK + " text);";

	public static final String CREATE_TBL_PACK = "CREATE TABLE " + TBL_PACK
			+ "(" + TRIP_ID + " integer, " + KEY_NUM + " integer, " + KEY_NAME
			+ " text, " + CHECK + " text);";

	public SQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// called to create table
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TBL_TRIP);
		db.execSQL(CREATE_TBL_PACK);
		db.execSQL(CREATE_TBL_TODO);
	}

	// called when database version mismatch
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		if (oldVersion >= newVersion)
			return;

		Log.d("SQLiteDemo", "onUpgrade: Version = " + newVersion);
		db.execSQL("DROP TABLE IF EXISTS " + TBL_TRIP);
		db.execSQL("DROP TABLE IF EXISTS " + TBL_PACK);
		db.execSQL("DROP TABLE IF EXISTS " + TBL_TODO);

		onCreate(db);
	}

	/*********************************** Database Writing Methods ******************************/

	/*
	 * Method to add a new trip to the database. Only requirement is a Trip
	 * object
	 */
	public void addTrip(Trip trip) {
		SQLiteDatabase db = this.getWritableDatabase();
		values = new ContentValues();
		values.put(KEY_NAME, trip.getName());
		values.put(DEST, trip.getDestination());
		values.put(START_DT, trip.getDateStart().toString());
		values.put(END_DT, trip.getDateEnd().toString());
		db.insert(TBL_TRIP, null, values);
		Log.d("SQLite", trip.getName() + " added");
		db.close();
		trip.setTripID(this.getTripID(trip));
	}

	// This method is used for writing a list item to the database.
	public void addListItem(TripListItem item, int index) {
		SQLiteDatabase dbWrite = this.getWritableDatabase();
		item.setIndex(index);
		int checked = 0;

		if (item.isChecked()) {
			checked = 1;
		}

		values = new ContentValues();
		values.put(TRIP_ID, item.getTripID());
		values.put(KEY_NUM, index);
		values.put(KEY_NAME, item.getText());
		values.put(CHECK, checked);
		dbWrite.insert(item.getTable(), null, values);
		dbWrite.close();
	}

	// Deletes everything from the database
	public void deleteAll() {
		SQLiteDatabase dbWrite = this.getWritableDatabase();
		dbWrite.delete("trip", null, null);
		dbWrite.delete("todo", null, null);
		dbWrite.delete("pack", null, null);
		dbWrite.close();
	}

	// Deletes every item in the TripList object passed to it from the database
	public void deleteItems(TripList list) {
		SQLiteDatabase dbWrite = this.getWritableDatabase();
		String table = list.getTable();
		String whereClause = TRIP_ID + " = ?";
		String[] whereArgs = { Integer.toString(list.getTripID()) };
		dbWrite.delete(table, whereClause, whereArgs);
	}

	// Method used to save a trip list. This method first deletes all objects
	// for the TripList passed, then writes all items in the list
	public void saveList(TripList list) {
		TripList newList = list;

		this.deleteItems(list);

		for (int i = 0; i < newList.size(); i++) {
			this.addListItem(newList.get(i), i + 1);

		}

	}

	/**********************************************************************************/

	/***************************** Database Read Methods ********************************/

	// The trip database has an auto-incrementing id, this retrieves the id of
	// the trip passed in the argument
	public int getTripID(Trip trip) {
		String sql;
		int id;
		Cursor cursor;
		SQLiteDatabase dbRead = this.getReadableDatabase();

		sql = "Select id from " + TBL_TRIP + " where " + KEY_NAME + " = '"
				+ trip.getName() + "';";
		cursor = dbRead.rawQuery(sql, null);
		Log.d("SQL", sql);
		if (cursor.getCount() == 0) {
			id = 0;
		} else {
			cursor.moveToFirst();
			id = Integer
					.parseInt(cursor.getString(cursor.getColumnIndex("id")));
			dbRead.close();
		}
		return id;

	}

	// Checks that the Trip name is unique
	public boolean isUnique(Trip trip) {
		boolean unique = false;
		int id = this.getTripID(trip);

		if (id == 0) {
			unique = true;
		}

		return unique;
	}

	// Returns a Trip object from the database
	public Trip getTripByID(int id) {
		String[] args = new String[5];
		Cursor cursor;
		SQLiteDatabase dbRead = this.getReadableDatabase();
		String sql;

		sql = "select * from " + TBL_TRIP + " where id = " + id + ";";
		cursor = dbRead.rawQuery(sql, null);
		cursor.moveToFirst();

		for (int i = 0; i < 5; i++) {
			args[i] = cursor.getString(i);
		}

		dbRead.close();
		Trip trip = new Trip(args);

		return trip;
	}
	
	public Trip getNewestTrip(){
		String sql = "select max(id) from trip";
		Cursor cursor = null;
		SQLiteDatabase dbRead = this.getReadableDatabase();
		
		cursor.moveToFirst();
		int id = Integer.parseInt(cursor.getString(0));
		
		return this.getTripByID(id);				
	}

	public void loadPackList(Trip trip) {
		Cursor cursor;
		SQLiteDatabase dbRead = this.getReadableDatabase();
		String sql;
		PackList list = new PackList();
		String text;
		boolean checked = false;
		list.setTrip(trip);

		sql = "select * from " + TBL_PACK + " where " + TRIP_ID + " = "
				+ trip.getID() + ";";
		cursor = dbRead.rawQuery(sql, null);
		cursor.moveToFirst();
		for (int i = 0; i < cursor.getCount(); i++) {
			text = cursor.getString(2);

			if (cursor.getString(3).equals("1")) {
				checked = true;
			}

			list.add(new TripListItem(list, text, checked));
			cursor.moveToNext();
		}
		
		trip.setPackList(list);
		

	}

	public void loadToDoList(Trip trip) {
		Cursor cursor;
		SQLiteDatabase dbRead = this.getReadableDatabase();
		String sql;
		ToDoList list = new ToDoList();
		String text;
		boolean checked = false;
		list.setTrip(trip);

		sql = "select * from " + TBL_TODO + " where " + TRIP_ID + " = "
				+ trip.getID() + ";";
		cursor = dbRead.rawQuery(sql, null);
		cursor.moveToFirst();
		for (int i = 0; i < cursor.getCount(); i++) {
			text = cursor.getString(2);

			if (cursor.getString(3).equals("1")) {
				checked = true;
			}

			list.add(new TripListItem(list, text, checked));
			cursor.moveToNext();
		}
		
		trip.setToDoList(list);
		

	}

}
