package com.example.rishabh.toimto;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rishabh.toimto.Utilities.ParseResult;
import com.example.rishabh.toimto.Utilities.UrlHelper;
import com.example.rishabh.toimto.Utilities.MyRecyclerView;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 *
 * 'simple' HAHAHAHA
 */
public class MainActivityFragment extends Fragment implements MyRecyclerView.ListAdapter.ItemClickCallback{

    private RecyclerView recyclerView;
    private MyRecyclerView.ListAdapter adapter;
    private List<MyRecyclerView.ListItem> listData;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        listData = MyRecyclerView.ListData.getListData();

        recyclerView = (RecyclerView) v.findViewById(R.id.rec_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        adapter = new MyRecyclerView.ListAdapter(listData, getContext());
        recyclerView.setAdapter(adapter);
        adapter.setItemClickCallback(this);

        FetchMovies updater = new FetchMovies(getContext());
        updater.execute();
        return v;
    }

    @Override
    public void onItemClick(int p) {
        MyRecyclerView.ListItem item = MyRecyclerView.ListData.getListData().get(p);
        Intent intent = new Intent(getContext(), Detail.class);
        intent.putExtra(Intent.EXTRA_TEXT, item.getText());
        startActivity(intent);
    }

    private class FetchMovies extends AsyncTask<Void, Void, String>
    {
        Context mContext;
        String title[] = new String[10];
        String image[] = new String[10];

        FetchMovies(Context context){
            mContext = context;
        }

        @Override
        protected String doInBackground(Void... voids) {

            if(MyRecyclerView.ListData.getListData().size() == 10)
                return null;

            String link = "http://api.themoviedb.org/3/movie/now_playing?api_key=0202ead4ea370b743c58c0e863ff6bd9";
            return UrlHelper.getRequest(link, mContext);
        }

        @Override
        protected void onPostExecute(String data) {
            ParseResult json;
            String result;
            String rawMovieData;

            if(data == null)
                return;

            for(int x=0; x<10; x++){
                json = new ParseResult(data);
                result = json.get("results");
                rawMovieData = json.getArrayElement(result, x);
                json = new ParseResult(rawMovieData);
                Log.e("Movie "+x, (json.get("title")));
                title[x] = json.get("title");
                image[x] = "http://image.tmdb.org/t/p/w500"+json.get("poster_path");
            }
            MyRecyclerView.ListData.setListData(title, image);
            listData = MyRecyclerView.ListData.getListData();
            adapter.setListData(listData);
            adapter.notifyDataSetChanged();
        }
    }

}