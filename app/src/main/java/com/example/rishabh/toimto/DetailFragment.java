package com.example.rishabh.toimto;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rishabh.toimto.Utilities.FetchResult;
import com.example.rishabh.toimto.Utilities.ParseResult;

import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {
    View v;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_detail, container, false);
        String data = getActivity().getIntent().getExtras().getString(Intent.EXTRA_TEXT);

        String[] search = parseSearch(data);
        Uri.Builder buildURL = new Uri.Builder();
        buildURL.scheme("http")
                .authority("www.omdbapi.com")
                .appendQueryParameter("t", search[0])
                .appendQueryParameter("tomatoes", "true")
                .appendQueryParameter("y", search[1])
                .appendQueryParameter("plot", "full");

        FetchResult fetchTask = new FetchResult();
        String jsonString = null;
        try {
            jsonString = fetchTask.execute(buildURL.build().toString()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ParseResult json = new ParseResult(jsonString);
        updateUI(json);

        return v;
    }

    private String[] parseSearch(String data){
        String[] search = new String[2];
        int start = data.indexOf("(");
        int end = data.indexOf(")");
        if (start == -1){
            search[1] = null;
            search[0] = data;
        }
        else{
            String year = data.substring(start+1, end);
            if(year.matches("^\\d+$")){
                search[1] = year;
                search[0] = data.substring(0, start);
            }
            else{
                search[1] = null;
                search[0] = data.substring(0, start);
            }
        }
        return search;
    }

    private void updateUI(ParseResult json){
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView imdbRating = (TextView) v.findViewById(R.id.imdbRating);
        TextView tomatoesRating = (TextView) v.findViewById(R.id.tomatoesRating);

        title.setText(json.getTitle());
        imdbRating.setText(json.get("imdbRating"));
        tomatoesRating.setText(json.get("tomatoRating"));
    }
}
