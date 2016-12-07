package com.example.rishabh.toimto;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.rishabh.toimto.Utilities.ParseResult;
import com.example.rishabh.toimto.Utilities.UrlHelper;
import com.example.rishabh.toimto.Utilities.VideoContract;
import com.example.rishabh.toimto.Utilities.VideoDbHelper;

import java.util.concurrent.ExecutionException;

public class Detail extends AppCompatActivity {
    private View v;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private VideoDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        v = findViewById(R.id.root_view);
        dbHelper = new VideoDbHelper(Detail.this);
        String data = getIntent().getExtras().getString(Intent.EXTRA_TEXT);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Detail");
        //collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        try {
            //Search for movie and update
            movieSearch(data);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }
    }

    private void movieSearch(String movieSearch) throws ExecutionException, InterruptedException {
        String[] params = {movieSearch};
        FetchResult fetchTask = new FetchResult(Detail.this);
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

    public void getVideoData(String tmdb_id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor resultSet = db.rawQuery("Select * from video",null);
        resultSet.moveToFirst();
        String username = resultSet.getString(1);
        String password = resultSet.getString(2);
    }

    private class FetchResult extends AsyncTask<String, Void, ParseResult> {

        private Context mContext;
        private ProgressDialog mDialog;

        public FetchResult(Context context) {
            mContext = context;
        }

        private void updateUI(ParseResult json) {
            final ImageView backdrop = (ImageView) collapsingToolbarLayout.findViewById(R.id.backdrop);
            TextView info_text = (TextView) findViewById(R.id.info_text);
            String description;
            description = json.get("overview");
            Glide.with(mContext)
                    .load("http://image.tmdb.org/t/p/w500" + json.get("backdrop_path"))
                    .asBitmap()
                    .into(new BitmapImageViewTarget(backdrop) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> anim) {
                            super.onResourceReady(bitmap, anim);
                            Palette p = Palette.from(bitmap).generate();
                            collapsingToolbarLayout.setBackgroundColor(p.getDarkMutedColor(0x000000));
                            collapsingToolbarLayout.setStatusBarScrimColor(p.getDarkMutedColor(0x000000));
                            collapsingToolbarLayout.setContentScrimColor(p.getLightMutedColor(0x000000));
                        }
                    });
            collapsingToolbarLayout.setTitle(json.get("title"));
            info_text.setText(description);
            //TODO: Update the detailView Here
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
                Log.e("tmdb", tmdbJsonString);

                String imdbId = json.get("imdb_id");
                String omdb = UrlHelper.getOmdbUrl(imdbId);
                imdbJsonString = UrlHelper.getRequest(omdb, mContext);
                Log.e("omdb", imdbJsonString);

                insertVideoData(tmdbJsonString, imdbJsonString, "movie", null);
            }
            return json;
        }
    }
}

/* TMDB JSON
{
   "adult":false,
   "backdrop_path":"/tFI8VLMgSTTU38i8TIsklfqS9Nl.jpg",
   "belongs_to_collection":null,
   "budget":165000000,
   "genres":[
      {
         "id":28,
         "name":"Action"
      },
      {
         "id":12,
         "name":"Adventure"
      },
      {
         "id":14,
         "name":"Fantasy"
      },
      {
         "id":878,
         "name":"Science Fiction"
      }
   ],
   "homepage":"http://marvel.com/doctorstrangepremiere",
   "id":284052,
   "imdb_id":"tt1211837",
   "original_language":"en",
   "original_title":"Doctor Strange",
   "overview":"After his career is destroyed, a brilliant but arrogant surgeon gets a new lease on life when a sorcerer takes him under his wing and trains him to defend the world against evil.",
   "popularity":18.319377,
   "poster_path":"/xfWac8MTYDxujaxgPVcRD9yZaul.jpg",
   "production_companies":[
      {
         "name":"Marvel Studios",
         "id":420
      }
   ],
   "production_countries":[
      {
         "iso_3166_1":"US",
         "name":"United States of America"
      }
   ],
   "release_date":"2016-10-25",
   "revenue":617025426,
   "runtime":115,
   "spoken_languages":[
      {
         "iso_639_1":"en",
         "name":"English"
      }
   ],
   "status":"Released",
   "tagline":"Open your mind. Change your reality.",
   "title":"Doctor Strange",
   "video":false,
   "vote_average":6.6,
   "vote_count":1322
}
 */

/* OMDB Json
{
   "Title":"Doctor Strange",
   "Year":"2016",
   "Rated":"PG-13",
   "Released":"04 Nov 2016",
   "Runtime":"115 min",
   "Genre":"Action, Adventure, Fantasy",
   "Director":"Scott Derrickson",
   "Writer":"Jon Spaihts, Scott Derrickson, C. Robert Cargill",
   "Actors":"Benedict Cumberbatch, Chiwetel Ejiofor, Rachel McAdams, Benedict Wong",
   "Plot":"Marvel's \"Doctor Strange\" follows the story of the talented neurosurgeon Doctor Stephen Strange who, after a tragic car accident, must put ego aside and learn the secrets of a hidden world of mysticism and alternate dimensions. Based in New York City's Greenwich Village, Doctor Strange must act as an intermediary between the real world and what lies beyond, utilising a vast array of metaphysical abilities and artifacts to protect the Marvel Cinematic Universe.",
   "Language":"English",
   "Country":"USA",
   "Awards":"1 win & 7 nominations.",
   "Poster":"https://images-na.ssl-images-amazon.com/images/M/MV5BMjM2ODA4MTM0M15BMl5BanBnXkFtZTgwNzE5OTYxMDI@._V1_SX300.jpg",
   "Metascore":"72",
   "imdbRating":"7.9",
   "imdbVotes":"145,669",
   "imdbID":"tt1211837",
   "Type":"movie",
   "tomatoMeter":"91",
   "tomatoImage":"certified",
   "tomatoRating":"7.3",
   "tomatoReviews":"264",
   "tomatoFresh":"239",
   "tomatoRotten":"25",
   "tomatoConsensus":"Doctor Strange artfully balances its outré source material against the blockbuster constraints of the MCU, delivering a thoroughly entertaining superhero origin story in the bargain.",
   "tomatoUserMeter":"90",
   "tomatoUserRating":"4.2",
   "tomatoUserReviews":"81260",
   "tomatoURL":"http://www.rottentomatoes.com/m/doctor_strange_2016/",
   "DVD":"N/A",
   "BoxOffice":"$205,778,872.00",
   "Production":"Walt Disney Pictures",
   "Website":"http://marvel.com/doctorstrange",
   "Response":"True"
}
 */