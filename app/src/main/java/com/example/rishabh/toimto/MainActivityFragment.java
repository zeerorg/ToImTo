package com.example.rishabh.toimto;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 *
 * 'simple' HAHAHAHA
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        final EditText search_text = (EditText) v.findViewById(R.id.search_text);
        Button search_button = (Button) v.findViewById(R.id.search_button);

        // On pressing the 'Enter' key
        search_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.e("Search", "Using enter key");
                    startIntent(search_text.getText().toString());
                    return true;
                }
                return false;
            }
        });

        //On clicking button
        search_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.e("Search", "Using button");
                startIntent(search_text.getText().toString());
            }
        });


        return v;
    }

    private void startIntent(String text){
        Intent intent = new Intent(getActivity(), Detail.class);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
    }
}
