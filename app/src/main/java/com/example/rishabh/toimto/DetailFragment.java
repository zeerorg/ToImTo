package com.example.rishabh.toimto;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rishabh.toimto.Utilities.ParseResult;
import com.example.rishabh.toimto.Utilities.UrlHelper;
import com.example.rishabh.toimto.Utilities.VideoContract;
import com.example.rishabh.toimto.Utilities.VideoDbHelper;

import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 *
 * Just pass the movie name to get t details.
 *
 */
public class DetailFragment extends Fragment {
    private View v;
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

    private void movieSearch(String movieSearch) throws ExecutionException, InterruptedException {
        String[] params = {movieSearch};
        FetchResult fetchTask = new FetchResult(getContext());
        fetchTask.execute(params);
    }

    public void insertVideoData(String tmdbJsonString, String omdbJsonString, String type, String tag) {

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

    private class FetchResult extends AsyncTask<String, Void, ParseResult> {

        private Context mContext;
        private ProgressDialog mDialog;

        public FetchResult(Context context) {
            mContext = context;
        }

        private void updateUI(ParseResult json) {
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView imdbRating = (TextView) v.findViewById(R.id.imdbRating);
            TextView tomatoesRating = (TextView) v.findViewById(R.id.tomatoesRating);

            title.setText(json.getTitle());
            imdbRating.setText(json.get("imdbRating"));
            tomatoesRating.setText(json.get("tomatoRating"));
            //aq.id(R.id.poster).image(json.get("Poster"), false, false);
            Glide.with(mContext)
                    .load(json.get("Poster"))
                    .into((ImageView) v.findViewById(R.id.poster));
            Log.e("Image", "Loaded");
        }

        @Override
        protected void onPostExecute(ParseResult json) {
            mDialog.dismiss();
            if (json == null)
                Toast.makeText(mContext, "No Connection :(", Toast.LENGTH_LONG).show();
            else
                updateUI(json);
            super.onPostExecute(json);
        }

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage("Please wait...");
            mDialog.show();
            super.onPreExecute();
        }

        @Override
        protected ParseResult doInBackground(String... strings) {

            String search = UrlHelper.movieSearchUrl(strings[0]);
            String jsonString;
            String imdbJsonString;
            String tmdbJsonString;
            ParseResult json;

            jsonString = UrlHelper.getRequest(search, mContext);
            if (jsonString == null) {
                return null;
            } else {
                json = new ParseResult(jsonString);
                String resultArray = json.get("results");
//                Log.e("JSONArray", resultArray);

                ParseResult result = new ParseResult(json.getArrayElement(resultArray, 0));
                String movie = UrlHelper.getMovieUrl(result.get("id"));
//                Log.e("URL", movie);

                tmdbJsonString = UrlHelper.getRequest(movie, mContext);
                json = new ParseResult(tmdbJsonString);
//                Log.e("JSONObject", jsonString);

                String imdbId = json.get("imdb_id");
                String omdb = UrlHelper.getOmdbUrl(imdbId);
                imdbJsonString = UrlHelper.getRequest(omdb, mContext);
                json = new ParseResult(imdbJsonString);

                insertVideoData(tmdbJsonString, imdbJsonString, "movie", null);
            }
            return json;
        }
    }
}