package com.example.rishabh.toimto.Utilities;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rishabh on 17/7/16.
 */
public class FetchResult extends AsyncTask<String, Void, String> {
    String JSON = null;

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;

        try {
            Uri.Builder buildURL = new Uri.Builder();
            buildURL.scheme("http")
                    .authority("www.omdbapi.com")
                    .appendQueryParameter("t", params[0])
                    .appendQueryParameter("tomatoes", "true")
                    .appendQueryParameter("y", params[1])
                    .appendQueryParameter("plot", "full");

            URL url = new URL(buildURL.build().toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(10000);
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
                }
            }
        }
        return forecastJsonStr;
    }

}
