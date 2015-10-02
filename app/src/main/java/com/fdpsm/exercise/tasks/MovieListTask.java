package com.fdpsm.exercise.tasks;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.fdpsm.exercise.MovieConstants;
import com.fdpsm.exercise.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieListTask extends AsyncTask<String, String, List<String>> {

    private ArrayAdapter<String> listAdapter;

    public MovieListTask(ArrayAdapter<String> listAdapter) {
        this.listAdapter = listAdapter;
    }

    @Override
    protected List<String> doInBackground(String... params) {
        return this.getJSONStringFromUrl(Util.createRequestURL(params[0], MovieConstants.API_URL_BY_SEARCH));
    }

    @Override
    protected void onPostExecute(List<String> titleList) {
        listAdapter.clear();
        listAdapter.addAll(titleList);
    }

    private List<String> getJSONStringFromUrl(String urlText) {

        ArrayList<String> titleList = null;
        URL url = null;

        try {
            url = new URL(urlText);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String response = readStream(con.getInputStream());
            JSONObject obj = new JSONObject(response);

            titleList = new ArrayList<String>();

            JSONArray arr = obj.getJSONArray("Search");
            for (int i = 0; i < arr.length(); i++) {
                titleList.add(arr.getJSONObject(i).getString("Title"));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return titleList;
    }

    private String readStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String nextLine = "";
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
