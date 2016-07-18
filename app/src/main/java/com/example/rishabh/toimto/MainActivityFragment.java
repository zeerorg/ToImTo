package com.example.rishabh.toimto;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.rishabh.toimto.Utilities.FetchResult;

/**
 * A placeholder fragment containing a simple view.
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

        search_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Detail.class);
                intent.putExtra(Intent.EXTRA_TEXT, search_text.getText().toString());
                startActivity(intent);
            }
        });
        return v;
    }
}
