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
import com.androidquery.AQuery;

import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {
    View v;
    private AQuery aq;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_detail, container, false);
        String data = getActivity().getIntent().getExtras().getString(Intent.EXTRA_TEXT);
        aq = new AQuery(getActivity(), v);

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
        aq.id(R.id.poster).image(json.get("Poster"), false, false);
        Log.e("Image", "Loaded");
    }
}
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