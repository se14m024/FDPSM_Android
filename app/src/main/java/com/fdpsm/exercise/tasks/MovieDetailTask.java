package com.fdpsm.exercise.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.fdpsm.exercise.model.Movie;
import com.fdpsm.exercise.MovieConstants;
import com.fdpsm.exercise.Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MovieDetailTask extends AsyncTask<String, String, Movie> {

    private TaskListener listener;

    public MovieDetailTask(TaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected Movie doInBackground(String... params) {
        return this.getJSONStringFromUrl(Util.createRequestURL(params[0], MovieConstants.API_URL_BY_TITLE));
    }

    @Override
    protected void onPostExecute(Movie movie) {
        listener.onTaskCompleted(movie);
    }


    private Movie getJSONStringFromUrl(String urlText) {

        Movie movie = null;
        URL url = null;

        try {
            url = new URL(urlText);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            movie = new Gson().fromJson(new InputStreamReader(con.getInputStream(), "UTF-8"), Movie.class);

            if (movie != null) {
                if (!movie.getPosterUrl().equalsIgnoreCase("N/A")) {
                    movie.setPoster(loadPoster(movie.getPosterUrl()));
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movie;
    }

    private Bitmap loadPoster(String posterURL) {

        Bitmap bitmap = null;
        URL url = null;

        try {
            url = new URL(posterURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            bitmap = BitmapFactory.decodeStream(con.getInputStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
