package dblayout;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class SleepDataProvider extends ContentProvider {
	static final String PROVIDER_NAME = "com.example.provider.SleepData";
	static final String URL = "content://" + PROVIDER_NAME + "/sleepdata";
	public static final Uri CONTENT_URI = Uri.parse(URL);

	public static final String SLEEPDATAID = "SDID";
	public static final String STARTTIME = "StartTime";
	public static final String ENDTIME = "EndTime";

	DBHelper dbHelper;

	static final int SLEEPDATATABLE = 1;
	static final int SLEEPDATATABLE_ID = 2;

	private static HashMap<String, String> SleepDataMap;

	static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "sleepdata", SLEEPDATATABLE);
		uriMatcher.addURI(PROVIDER_NAME, "sleepdata/#", SLEEPDATATABLE_ID);
	}

	private SQLiteDatabase database;
	static final String DATABASE_NAME = "SleepDataDB";
	static final String TABLE_NAME = "SleepDataTable";
	static final int DATABASE_VERSION = 1;
	static final String CREATE_TABLE = 
			" CREATE TABLE " + TABLE_NAME +
			" (SDID INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			" StartTime LONG NOT NULL, " +
			" EndTime LONG NOT NULL)";


	private static class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(DBHelper.class.getName(),
					"Upgrading database from version " + oldVersion + " to "
							+ newVersion + ". Old data will be destroyed");
			db.execSQL("DROP TABLE IF EXISTS " +  TABLE_NAME);
			onCreate(db);
		}

	}

	@Override
	public boolean onCreate() {
		Context context = getContext();
		dbHelper = new DBHelper(context);
		database = dbHelper.getWritableDatabase();

		if(database == null)
			return false;
		else
			return true;	
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(TABLE_NAME);

		switch (uriMatcher.match(uri)) {
		case SLEEPDATATABLE:
			queryBuilder.setProjectionMap(SleepDataMap);
			break;
		case SLEEPDATATABLE_ID:
			queryBuilder.appendWhere( SLEEPDATAID + "=" + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		if (sortOrder == null || sortOrder == ""){
	         sortOrder = SLEEPDATAID;
	      }
		
		Cursor cursor = queryBuilder.query(database, projection, selection, 
				selectionArgs, null, null, sortOrder);

		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long row = database.insert(TABLE_NAME, "", values);

		if(row > 0) {
			Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
			getContext().getContentResolver().notifyChange(newUri, null);
			return newUri;
		}
		throw new SQLException("Fail to add a new record into " + uri);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;

		switch (uriMatcher.match(uri)){
		case SLEEPDATATABLE:
			count = database.update(TABLE_NAME, values, selection, selectionArgs);
			break;
		case SLEEPDATATABLE_ID:
			count = database.update(TABLE_NAME, values, SLEEPDATAID + 
					" = " + uri.getLastPathSegment() + 
					(!TextUtils.isEmpty(selection) ? " AND (" +
							selection + ')' : ""), selectionArgs);
			break;
		default: 
			throw new IllegalArgumentException("Unsupported URI " + uri );
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;

		switch (uriMatcher.match(uri)){
		case SLEEPDATATABLE:
			count = database.delete(TABLE_NAME, selection, selectionArgs);
			break;
		case SLEEPDATATABLE_ID:
			String id = uri.getLastPathSegment();	
			count = database.delete( TABLE_NAME, SLEEPDATAID +  " = " + id + 
					(!TextUtils.isEmpty(selection) ? " AND (" + 
							selection + ')' : ""), selectionArgs);
			break;
		default: 
			throw new IllegalArgumentException("Unsupported URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)){
		case SLEEPDATATABLE:
			return "vnd.android.cursor.dir/vnd.example.sleepdata";
		case SLEEPDATATABLE_ID:
			return "vnd.android.cursor.item/vnd.example.sleepdata";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}
}
