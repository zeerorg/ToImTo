package com.example.rishabh.toimto.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.rishabh.toimto.Utilities.VideoContract.VideoEntry;

/**
 * Created by rishabh on 24/7/16.
 */
public class VideoDbHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Video.db";

    public VideoDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createEntries = "CREATE TABLE " + VideoEntry.TABLE_NAME + " ( " +
                VideoEntry._ID              + " INTEGER PRIMARY KEY " + " , " +
                VideoEntry.COLUMN_IMDB_ID   + " TEXT " + " , " +
                VideoEntry.COLUMN_TITLE     + " TEXT " + " , " +
                VideoEntry.COLUMN_TAG       + " TEXT " + " , " +
                VideoEntry.COLUMN_TYPE      + " TEXT " + " , " +
                VideoEntry.COLUMN_POSTER    + " TEXT " + " , " +
                VideoEntry.COLUMN_YEAR      + " TEXT " + " , " +
                VideoEntry.COLUMN_DATE_TIME + " DATETIME " + " , " +
                VideoEntry.COLUMN_TMDB_JSON + " TEXT " + " , " +
                VideoEntry.COLUMN_OMDB_JSON + " TEXT " +
                " );";
        sqLiteDatabase.execSQL(createEntries);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + VideoEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /*public void insertVideoData(String tmdbJsonString, String omdbJsonString, String type, String tag){

        ParseResult tmdbJson = new ParseResult(tmdbJsonString);
        ParseResult omdbJson = new ParseResult(omdbJsonString);

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VideoEntry._ID, Integer.parseInt(tmdbJson.get("id")));
        values.put(VideoEntry.COLUMN_IMDB_ID, tmdbJson.get("imdb_id"));
        values.put(VideoEntry.COLUMN_OMDB_JSON, omdbJsonString);
        values.put(VideoEntry.COLUMN_TMDB_JSON, tmdbJsonString);
        values.put(VideoEntry.COLUMN_YEAR, omdbJson.get("Year"));
        values.put(VideoEntry.COLUMN_TITLE, omdbJson.get("Title"));
        values.put(VideoEntry.COLUMN_POSTER, omdbJson.get("Poster"));
        values.put(VideoEntry.COLUMN_TYPE, type);
        values.put(VideoEntry.COLUMN_TAG, tag);
        values.put(VideoEntry.COLUMN_DATE_TIME, java.lang.System.currentTimeMillis());

        db.insert(VideoEntry.TABLE_NAME, null, values);
    }

    public String testAll(){
        String result = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + VideoEntry.TABLE_NAME, null);
        while(cursor.moveToNext()){
            result += cursor.getString(cursor.getColumnIndex(VideoEntry.COLUMN_TITLE));
        }
        db.close();
        cursor.close();
        return result;
    }*/

}
