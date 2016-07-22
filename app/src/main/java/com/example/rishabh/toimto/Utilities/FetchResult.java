package com.example.rishabh.toimto.Utilities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by rishabh on 17/7/16.
 *
 * This is a repetitve code used to get the json string.
 * NOTE: First you have to pass the url string this code "does not" build url (wouldn't be much reusable if it did that).
 */
public class FetchResult extends AsyncTask<String, Void, String> {
    private Context mContext = null;

    public FetchResult(Context context){
        mContext = context;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(!(networkInfo != null && networkInfo.isConnected())){
                return null;
            }

            URL url = new URL(params[0]);

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
