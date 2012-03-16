package com.mechinn.android.myalliance.providers;

import java.util.HashMap;

import com.mechinn.android.myalliance.GeneralSchema;

import android.content.ContentProvider;
import android.content.ContentResolver;
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
import android.provider.BaseColumns;
import android.util.Log;

public class TeamInfoProvider extends ContentProvider implements GeneralSchema  {
    public static final String DBTable = "teams";
    public static final String DBTableReset = "teamsReset";
    private static final int DBVersion = 2;
    
    public static final String keyTeam = "team";
    public static final String keyOrientation = "orientation";
    public static final String keyNumWheels = "numWheels";
    public static final String keyWheelTypes = "wheelTypes";
    public static final String keyDeadWheel = "deadWheel";
    public static final String keyWheel1Type = "wheel1Type";
    public static final String keyWheel1Diameter = "wheel1Diameter";
    public static final String keyWheel2Type = "wheel2Type";
    public static final String keyWheel2Diameter = "wheel2Diameter";
    public static final String keyDeadWheelType = "deadWheelType";
    public static final String keyTurret = "turret";
    public static final String keyTracking = "tracking";
    public static final String keyFenderShooter = "fendershooter";
    public static final String keyKeyShooter = "keyshooter";
    public static final String keyBarrier = "barrier";
    public static final String keyClimb = "climb";
    public static final String keyNotes = "notes";
    public static final String keyAutonomous = "autonomous";

    private static final String TAG = "TeamInfoContentProvider";
    private static final String AUTHORITY = "com.mechinn.android.myalliance.providers.TeamInfoProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/teams");
    public static final Uri CONTENT_URI_RESET = Uri.parse("content://" + AUTHORITY + "/teamsReset");
    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/com.mechinn.teaminfo";
    private static final int TEAMINFO = 1;
    private static final int TEAMINFORESET = 2;
    private static final UriMatcher sUriMatcher;
    private static HashMap<String, String> teamInfoProjectionMap;
    
    private static class TeamInfoDB extends SQLiteOpenHelper {
    	private static final String DATABASE_CREATE = "CREATE TABLE "+ DBTable +" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
    			keyLastMod+" int not null, "+
    			keyTeam+" int not null unique, " +
    			keyOrientation+" text, " +
    			keyNumWheels+" int, " +
    			keyWheelTypes+" int, " +
    			keyDeadWheel+" int, " +
    			keyWheel1Type+" text, " +
    			keyWheel1Diameter+" int, " +
    			keyWheel2Type+" text, " +
    			keyWheel2Diameter+" int, " +
    			keyDeadWheelType+" text, " +
    			keyTurret+" int, " +
    			keyTracking+" int, " +
    			keyFenderShooter+" int, " +
    			keyKeyShooter+" int, " +
    			keyBarrier+" int, " +
    			keyClimb+" int, " +
    			keyNotes+" text);";

    	TeamInfoDB(Context context) {
            super(context, DBName, null, DBVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	//setup original db and upgrade to latest
        	Log.d("onCreate",DATABASE_CREATE);
        	db.execSQL(DATABASE_CREATE);
        	onUpgrade(db,1,DBVersion);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        	switch(oldVersion+1){
    	    	default: //case 1
    	    		Log.i(TAG, "v1 original team info table");
    	    		db.execSQL("DROP TABLE IF EXISTS "+DBTable);
    	    		Log.d("onCreate",DATABASE_CREATE);
    	    		db.execSQL(DATABASE_CREATE);
        		case 2:
        			Log.i(TAG, "v2 added autonomous column");
            		db.execSQL("alter table "+DBTable+" add column "+keyAutonomous+" int;");
        		case 3:
//        			Log.i(TAG, "v3 ");
        	}
        }
    }

    private TeamInfoDB mDB;

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mDB.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        int count;
        switch (sUriMatcher.match(uri)) {
            case TEAMINFO:
                count = db.delete(DBTable, where, whereArgs);
                break;
            case TEAMINFORESET:
                qb.setTables(DBTable);
                qb.setProjectionMap(teamInfoProjectionMap);
                count = qb.query(db, null, null, null, null, null, null).getCount();
            	mDB.onUpgrade(db,0,1);
            	break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case TEAMINFO:
                return CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
    	switch(sUriMatcher.match(uri)) {
    		case TEAMINFO: {
    			ContentValues values;
    	        if (initialValues != null) {
    	            values = new ContentValues(initialValues);
    	        } else {
    	            values = new ContentValues();
    	        }

    	        SQLiteDatabase db = mDB.getWritableDatabase();
    	        long rowId = db.insert(DBTable, null, values);
    	        if (rowId > 0) {
    	            Uri teamUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
    	            getContext().getContentResolver().notifyChange(teamUri, null);
    	            return teamUri;
    	        }

    	        throw new SQLException("Failed to insert row into " + uri);
    		}
    		default: {
    			throw new IllegalArgumentException("Unknown URI " + uri);
    		}
    	}
        
    }

    @Override
    public boolean onCreate() {
    	mDB = new TeamInfoDB(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case TEAMINFO:
                qb.setTables(DBTable);
                qb.setProjectionMap(teamInfoProjectionMap);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mDB.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = mDB.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case TEAMINFO:
                count = db.update(DBTable, values, where, whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, DBTable, TEAMINFO);
        sUriMatcher.addURI(AUTHORITY, DBTableReset, TEAMINFORESET);

        teamInfoProjectionMap = new HashMap<String, String>();
        teamInfoProjectionMap.put(_ID, _ID);
        teamInfoProjectionMap.put(keyLastMod, keyLastMod);
        teamInfoProjectionMap.put(keyTeam, keyTeam);
        teamInfoProjectionMap.put(keyOrientation, keyOrientation);
        teamInfoProjectionMap.put(keyNumWheels, keyNumWheels);
        teamInfoProjectionMap.put(keyWheelTypes, keyWheelTypes);
        teamInfoProjectionMap.put(keyDeadWheel, keyDeadWheel);
        teamInfoProjectionMap.put(keyWheel1Type, keyWheel1Type);
        teamInfoProjectionMap.put(keyWheel1Diameter, keyWheel1Diameter);
        teamInfoProjectionMap.put(keyWheel2Type, keyWheel2Type);
        teamInfoProjectionMap.put(keyWheel2Diameter, keyWheel2Diameter);
        teamInfoProjectionMap.put(keyDeadWheelType, keyDeadWheelType);
        teamInfoProjectionMap.put(keyTurret, keyTurret);
        teamInfoProjectionMap.put(keyTracking, keyTracking);
        teamInfoProjectionMap.put(keyFenderShooter, keyFenderShooter);
        teamInfoProjectionMap.put(keyKeyShooter, keyKeyShooter);
        teamInfoProjectionMap.put(keyBarrier, keyBarrier);
        teamInfoProjectionMap.put(keyClimb, keyClimb);
        teamInfoProjectionMap.put(keyNotes, keyNotes);
        teamInfoProjectionMap.put(keyAutonomous, keyAutonomous);

    }
}
