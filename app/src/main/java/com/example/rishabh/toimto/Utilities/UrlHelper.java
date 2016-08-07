package com.example.rishabh.toimto.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rishabh on 22/7/16.
 */
public class UrlHelper {
    public static Uri.Builder buildURL = new Uri.Builder();

    public static String movieSearchUrl(String search){
        buildURL = new Uri.Builder();
        buildURL.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("search")
                .appendPath("movie")
                .appendQueryParameter("api_key", "0202ead4ea370b743c58c0e863ff6bd9")
                .appendQueryParameter("query", search);

        return buildURL.build().toString();
    }

    public static String getMovieUrl(String id){
        buildURL = new Uri.Builder();
        buildURL.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(id)
                .appendQueryParameter("api_key", "0202ead4ea370b743c58c0e863ff6bd9");

        return buildURL.build().toString();
    }

    public static String getOmdbUrl(String imdbId){
        buildURL = new Uri.Builder();
        buildURL.scheme("http")
                .authority("www.omdbapi.com")
                .appendQueryParameter("i", imdbId)
                .appendQueryParameter("tomatoes", "true")
                .appendQueryParameter("plot", "full");

        return buildURL.build().toString();
    }

    public static String getRequest(String param, Context mContext) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(!(networkInfo != null && networkInfo.isConnected())){
                return null;
            }

            URL url = new URL(param);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);   //Took so much to find this
            urlConnection.setReadTimeout(10000);    // and this. It's a must have.
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                Log.e("FetchData", "inputStream is null");
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e("FetchData", "IOException: " + e.toString());
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    //DOing Nothing
                }
            }
        }
        return forecastJsonStr;
    }
}
