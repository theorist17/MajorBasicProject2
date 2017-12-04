package org.androidtown.MajorBasicProject2;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Hongin Lee on 2017-11-18.
 */



class HandlerDB extends AsyncTask<HashMap<String, String>,Void,String> {

    public DBResponse dbResponse = null;
    public String log = null;

    String serverUrl = "http://1.240.26.183/";
    String phpFileName;
    RequestHandler rh = new RequestHandler();
    ArrayList<HashMap<String, String >> dbResult = new ArrayList<HashMap<String, String>>();

    public HandlerDB(String phpFileName) {
        this.phpFileName = phpFileName;
    }

    public interface DBResponse {
        void onResepones(ArrayList<HashMap<String,String>> dbResult, String log);
    }

    public void setDbResponse(DBResponse dbResponse) {
        this.dbResponse = dbResponse;
    }

    @Override
    protected String doInBackground(HashMap<String, String>... mapInput) {
        String output = rh.sendPostRequest(serverUrl+phpFileName, mapInput[0]).trim();
        return output;
    }

    @Override
    protected void onPostExecute(String output) {
        super.onPostExecute(output);

        if(output.equals("")){
            dbResponse.onResepones(null, "php 출력결과 없음");
        }
        //{로 시작하는 JSON문자열 반환
        else if(isJSONValid(output)) {
            try {
                JSONObject json = new JSONObject(output);
                JSONArray results = json.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject joRow = results.getJSONObject(i);
                    Iterator it = joRow.keys();
                    HashMap<String, String> row = new HashMap<String, String>();
                    while (it.hasNext()) {
                        String n = (String) it.next();
                        row.put(n, joRow.getString(n));
                    }
                    dbResult.add(row);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dbResponse.onResepones(dbResult, "json");
        } else {
            dbResponse.onResepones(null, output);
        }
    }
    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}