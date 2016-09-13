package com.example.rishabh.toimto.Utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rishabh on 18/7/16.
 */
public class ParseResult {
    JSONObject reader;

    public ParseResult(String jsonStr){
        try {
            reader = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String get(String key){
        try {
            return(reader.getString(key));
        } catch (JSONException e) {
            return null;
        }
    }

    public int getInt(String key){
        try {
            return(reader.getInt(key));
        } catch (JSONException e) {
            return 0;
        }
    }

    public String getTitle(){
        return (this.get("Title") + " (" + this.get("Year") + ")");
    }

    public String getArrayElement(String arrays, int index){
        try {
            JSONArray array = new JSONArray(arrays);
            //Log.e("JSONObject", mainJson);
            return (array.get(index).toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
