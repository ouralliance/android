package com.mechinn.android.ouralliance.providers;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.mechinn.android.ouralliance.DatabaseConnection;
import com.mechinn.android.ouralliance.SchemaArray;

public class MatchScoutingProvider extends ContentProvider {
    public static final String DBTable = "matchScouting";
    
    public static final String keyCompetition = "competition";
    public static final String keyMatchNum = "matchNum";
    public static final String keyTeam = "team";
    public static final String keySlot = "slot";
    public static final String keyBroke = "broke";
    public static final String keyAuto = "autonomous";
    public static final String keyBalance = "balanced";
    public static final String keyShooter = "shooter";
    public static final String keyTop = "top";
    public static final String keyMid = "mid";
    public static final String keyBot = "bot";
    public static final String keyMiss = "miss";
    public static final String keyNotes = "notes";
    
    public static final String keyAutoBridge = "autoBridge";
    public static final String keyAutoShooter = "autoShooter";
    public static final String keyTopAuto = "topAuto";
    public static final String keyMidAuto = "midAuto";
    public static final String keyBotAuto = "botAuto";
    public static final String keyMissAuto = "missAuto";
    
    public static final SchemaArray v5schemaArray = new SchemaArray(new String[] {DatabaseConnection._ID, DatabaseConnection._LASTMOD, 
		keyCompetition, keyMatchNum, keyTeam, keySlot, keyBroke, keyBalance, keyShooter, 
		keyTop, keyMid, keyBot, keyNotes});

    public static final SchemaArray schemaArray = new SchemaArray(new String[] {DatabaseConnection._ID, DatabaseConnection._LASTMOD, 
		keyCompetition, keyMatchNum, keyTeam, keySlot, keyBroke, keyBalance, keyShooter, 
		keyTop, keyMid, keyBot, keyNotes, keyAutoBridge, keyAutoShooter, keyMiss, 
		keyTopAuto, keyMidAuto, keyBotAuto, keyMissAuto});

    private static final String logTag = "MatchScoutingProvider";
    private static final String authority = "com.mechinn.android.ouralliance.providers."+logTag;
    private static final String type = ContentResolver.CURSOR_DIR_BASE_TYPE+"/com.mechinn."+DBTable;
    public static final Uri mUri = Uri.parse("content://" + authority + "/"+DBTable);
    private static final int sig = 1;
    private static final UriMatcher sUriMatcher;
    private static HashMap<String, String> matchesProjectionMap;
    
    public static final String DATABASE_CREATE = "CREATE TABLE "+ DBTable +" ("+
    		DatabaseConnection._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
    		DatabaseConnection._LASTMOD+" INTEGER NOT NULL, "+
			keyCompetition+" TEXT NOT NULL, " +
			keyMatchNum+" INTEGER, " +
			keyTeam+" INTEGER, " +
			keySlot+" TEXT, " +
			keyBroke+" INTEGER, " +
			keyAuto+" INTEGER, " +
			keyBalance+" INTEGER, "+
			keyShooter+" INTEGER, " +
			keyTop+" INTEGER, " +
			keyMid+" INTEGER, " +
			keyBot+" INTEGER, " +
			keyNotes+" TEXT);";

    private DatabaseConnection mDB;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(authority, DBTable, sig);

        matchesProjectionMap = new HashMap<String, String>();
        for(String schema : schemaArray) {
            matchesProjectionMap.put(schema, schema);
        }
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mDB.getWritableDatabase();
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case sig:
                count = db.delete(DBTable, where, whereArgs);
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
            case sig:
                return type;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
    	switch(sUriMatcher.match(uri)) {
    		case sig: {
    			ContentValues values;
    	        if (initialValues != null) {
    	            values = new ContentValues(initialValues);
    	        } else {
    	            values = new ContentValues();
    	        }

    	        SQLiteDatabase db = mDB.getWritableDatabase();
    	        long rowId = db.insert(DBTable, null, values);
    	        if (rowId > 0) {
    	            Uri teamUri = ContentUris.withAppendedId(mUri, rowId);
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
    	mDB = new DatabaseConnection(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case sig:
                qb.setTables(DBTable);
                qb.setProjectionMap(matchesProjectionMap);
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
            case sig:
                count = db.update(DBTable, values, where, whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
