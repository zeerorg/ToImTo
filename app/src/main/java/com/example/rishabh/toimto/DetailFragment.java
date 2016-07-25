package com.example.rishabh.toimto;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.example.rishabh.toimto.Utilities.FetchResult;
import com.example.rishabh.toimto.Utilities.ParseResult;
import com.example.rishabh.toimto.Utilities.UrlHelper;
import com.example.rishabh.toimto.Utilities.VideoContract;
import com.example.rishabh.toimto.Utilities.VideoDbHelper;

import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {
    View v;
    private AQuery aq;
    private VideoDbHelper dbHelper;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize global variables
        v = inflater.inflate(R.layout.fragment_detail, container, false);
        dbHelper = new VideoDbHelper(getContext());
        String data = getActivity().getIntent().getExtras().getString(Intent.EXTRA_TEXT);
        aq = new AQuery(getActivity(), v);

        try {
            //Search for movie and update
            movieSearch(data);
            return v;
            } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }
        return v;
    }

    private void updateUI(ParseResult json) {
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView imdbRating = (TextView) v.findViewById(R.id.imdbRating);
        TextView tomatoesRating = (TextView) v.findViewById(R.id.tomatoesRating);

        title.setText(json.getTitle());
        imdbRating.setText(json.get("imdbRating"));
        tomatoesRating.setText(json.get("tomatoRating"));
        aq.id(R.id.poster).image(json.get("Poster"), false, false);
        Log.e("Image", "Loaded");
    }

    public void insertVideoData(String tmdbJsonString, String omdbJsonString, String type, String tag){

        ParseResult tmdbJson = new ParseResult(tmdbJsonString);
        ParseResult omdbJson = new ParseResult(omdbJsonString);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VideoContract.VideoEntry._ID, Integer.parseInt(tmdbJson.get("id")));
        values.put(VideoContract.VideoEntry.COLUMN_IMDB_ID, tmdbJson.get("imdb_id"));
        values.put(VideoContract.VideoEntry.COLUMN_OMDB_JSON, omdbJsonString);
        values.put(VideoContract.VideoEntry.COLUMN_TMDB_JSON, tmdbJsonString);
        values.put(VideoContract.VideoEntry.COLUMN_YEAR, omdbJson.get("Year"));
        values.put(VideoContract.VideoEntry.COLUMN_TITLE, omdbJson.get("Title"));
        values.put(VideoContract.VideoEntry.COLUMN_POSTER, omdbJson.get("Poster"));
        values.put(VideoContract.VideoEntry.COLUMN_TYPE, type);
        values.put(VideoContract.VideoEntry.COLUMN_TAG, tag);
        values.put(VideoContract.VideoEntry.COLUMN_DATE_TIME, java.lang.System.currentTimeMillis());

        db.insert(VideoContract.VideoEntry.TABLE_NAME, null, values);
    }

    public void testAll() {
//        TextView dbString = (TextView) v.findViewById(R.id.dbStringTest);
        String result = "";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + VideoContract.VideoEntry.TABLE_NAME, null);
        while (cursor.moveToNext()) {
            result += cursor.getString(cursor.getColumnIndex(VideoContract.VideoEntry.COLUMN_TITLE));
        }
        db.close();
        cursor.close();
//        dbString.setText(result);
    }

    private void movieSearch(String movieSearch) throws ExecutionException, InterruptedException {
        FetchResult fetchTask = new FetchResult(getContext());
        String search = UrlHelper.movieSearch(movieSearch);
        String jsonString = null;
        String imdbJsonString = null;
        String tmdbJsonString = null;
        jsonString = fetchTask.execute(search).get();
        if (jsonString == null) {
            Toast.makeText(getContext(), "No Connection :(", Toast.LENGTH_LONG).show();
        } else {
            ParseResult json = new ParseResult(jsonString);
            String resultArray = json.get("results");
//                Log.e("JSONArray", resultArray);

            ParseResult result = json.getArrayElement(resultArray, 0);
            String movie = UrlHelper.getMovie(result.get("id"));
//                Log.e("URL", movie);

            fetchTask = new FetchResult(getContext());
            tmdbJsonString = fetchTask.execute(movie).get();
            json = new ParseResult(tmdbJsonString);
//                Log.e("JSONObject", jsonString);

            String imdbId = json.get("imdb_id");
            String omdb = UrlHelper.getOmdb(imdbId);
            fetchTask = new FetchResult(getContext());
            imdbJsonString = fetchTask.execute(omdb).get();
            json = new ParseResult(imdbJsonString);
            updateUI(json);
            insertVideoData(tmdbJsonString, imdbJsonString, "movie", "none");
            testAll();
        }
    }
}

    /*private String[] parseSearch(String data) {
        String[] search = new String[2];
        int start = data.indexOf("(");
        int end = data.indexOf(")");
        if (start == -1) {
            search[1] = null;
            search[0] = data;
        } else {
            String year = data.substring(start + 1, end);
            if (year.matches("^\\d+$")) {
                search[1] = year;
                search[0] = data.substring(0, start);
            } else {
                search[1] = null;
                search[0] = data.substring(0, start);
            }
        }
        return search;
    }*/

/*
{
   "Title":"Sultan",
   "Year":"2016",
   "Rated":"NOT RATED",
   "Released":"07 Jul 2016",
   "Runtime":"143 min",
   "Genre":"Action, Drama, Sport",
   "Director":"Ali Abbas Zafar",
   "Writer":"Ali Abbas Zafar",
   "Actors":"Salman Khan, Marko Zaror, Anushka Sharma, Randeep Hooda",
   "Plot":"Sultan is a story of Sultan Ali Khan - a local wrestling champion with the world at his feet as he dreams of representing India at the Olympics. It's a story of Aarfa - a feisty young girl from the same small town as Sultan with her own set of dreams. When the 2 local wrestling legends lock horns, romance blossoms and their dreams and aspirations become intertwined and aligned. However, the path to glory is a rocky one and one must fall several times before one stands victorious - More often than not, this journey can take a lifetime. Sultan is a classic underdog tale about a wrestlers journey, looking for a comeback by defeating all odds staked up against him. But when he has nothing to lose and everything to gain in this fight for his life match... Sultan must literally fight for his life. Sultan believes he's got what it takes... but this time, it's going to take everything he's got.",
   "Language":"Hindi",
   "Country":"India",
   "Awards":"N/A",
   "Poster":"http://ia.media-imdb.com/images/M/MV5BOWY3MmVmMGQtYTIyMS00ODc2LWI4N2YtMjA1MmY1YjA3MzVlXkEyXkFqcGdeQXVyMTkzOTcxOTg@._V1_SX300.jpg",
   "Metascore":"N/A",
   "imdbRating":"N/A",
   "imdbVotes":"N/A",
   "imdbID":"tt4832640",
   "Type":"movie",
   "tomatoMeter":"N/A",
   "tomatoImage":"N/A",
   "tomatoRating":"N/A",
   "tomatoReviews":"N/A",
   "tomatoFresh":"N/A",
   "tomatoRotten":"N/A",
   "tomatoConsensus":"N/A",
   "tomatoUserMeter":"N/A",
   "tomatoUserRating":"N/A",
   "tomatoUserReviews":"28",
   "tomatoURL":"http://www.rottentomatoes.com/m/771444213/",
   "DVD":"N/A",
   "BoxOffice":"N/A",
   "Production":"N/A",
   "Website":"N/A",
   "Response":"True"
}

 */