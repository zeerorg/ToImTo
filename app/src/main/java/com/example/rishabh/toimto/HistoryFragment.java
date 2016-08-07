package com.example.rishabh.toimto;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rishabh.toimto.Utilities.VideoContract;
import com.example.rishabh.toimto.Utilities.VideoDbHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("History Fragment", "Started");
        VideoDbHelper dbHelper = new VideoDbHelper(getContext());
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        TextView dbString = (TextView) v.findViewById(R.id.history);
        String result = "";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + VideoContract.VideoEntry.TABLE_NAME, null);
        while (cursor.moveToNext()) {
            result += cursor.getString(cursor.getColumnIndex(VideoContract.VideoEntry.COLUMN_TITLE));
            result += "\n";
        }
        db.close();
        cursor.close();
        dbString.setText(result);

        return v;
    }

}
