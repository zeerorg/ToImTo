package com.example.rishabh.toimto.Utilities;

import android.provider.BaseColumns;

/**
 * Created by rishabh on 22/7/16.
 */
public class VideoContract {

    public VideoContract(){}

    public static abstract class VideoEntry implements BaseColumns{
        public static final String TABLE_NAME           = "video";
        public static final String COLUMN_IMDB_ID       = "imdb_id";
        public static final String COLUMN_TITLE         = "title";
        public static final String COLUMN_YEAR          = "year";
        public static final String COLUMN_POSTER        = "poster";
        public static final String COLUMN_DATE_TIME     = "added_at";
        public static final String COLUMN_TYPE          = "type";
        /*public static final String COLUMN_PLOT          = "plot";
        public static final String COLUMN_IMDB_RATING   = "imdb_rating";
        public static final String COLUMN_TOMATOES_RATING = "tomatoes_rating";
        public static final String COLUMN_POSTER        = "poster";
        public static final String COLUMN_DIRECTOR      = "director";
        public static final String COLUMN_ACTORS        = "actors";
        public static final String COLUMN_LENGTH        = "length";*/
        public static final String COLUMN_TAG           = "tag";
        public static final String COLUMN_TMDB_JSON     = "tmdb_json";
        public static final String COLUMN_OMDB_JSON     = "omdb_json";
    }
}
