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

    public String getTitle(){
        return (this.get("Title") + " (" + this.get("Year") + ")");
    }

    public ParseResult getArrayElement(String arrays, int index){
        try {
            JSONArray array = new JSONArray(arrays);
            String mainJson = array.get(index).toString();
            Log.e("JSONObject", mainJson);
            return (new ParseResult(mainJson));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
