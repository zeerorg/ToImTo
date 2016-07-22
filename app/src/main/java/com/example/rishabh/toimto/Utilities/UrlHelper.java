package com.example.rishabh.toimto.Utilities;

import android.net.Uri;

/**
 * Created by rishabh on 22/7/16.
 */
public class UrlHelper {
    public static Uri.Builder buildURL = new Uri.Builder();

    public static String movieSearch(String search){
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

    public static String getMovie(String id){
        buildURL = new Uri.Builder();
        buildURL.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(id)
                .appendQueryParameter("api_key", "0202ead4ea370b743c58c0e863ff6bd9");

        return buildURL.build().toString();
    }

    public static String getOmdb(String imdbId){
        buildURL = new Uri.Builder();
        buildURL.scheme("http")
                .authority("www.omdbapi.com")
                .appendQueryParameter("i", imdbId)
                .appendQueryParameter("tomatoes", "true")
                .appendQueryParameter("plot", "full");

        return buildURL.build().toString();
    }
}
