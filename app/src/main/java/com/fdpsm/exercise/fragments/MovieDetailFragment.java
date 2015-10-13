package com.fdpsm.exercise.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdpsm.exercise.ExerciseApplication;
import com.fdpsm.exercise.MovieRestClient;
import com.fdpsm.exercise.R;
import com.fdpsm.exercise.dao.DaoSession;
import com.fdpsm.exercise.dao.MovieDetail;
import com.fdpsm.exercise.dao.MovieDetailDao;
import com.fdpsm.exercise.model.Movie;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


@EFragment(R.layout.fragment_movie_detail)
public class MovieDetailFragment extends Fragment {

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.release)
    TextView release;

    @ViewById(R.id.runtime)
    TextView runtime;

    @ViewById(R.id.poster)
    ImageView poster;

    @RestService
    MovieRestClient restClient;

    public MovieDetailFragment() {
    }

    @AfterViews
    public void init() {
        String item = getArguments().getString("item");
        this.getMovieDetailTask(item);
    }

    @Background
    public void getMovieDetailTask(String item) {

        Movie movie = restClient.getMovieDetail(item);

        createDBEntry(movie);

        if (movie != null) {
            if (!movie.getPosterUrl().equalsIgnoreCase("N/A")) {
                movie.setPoster(loadPoster(movie.getPosterUrl()));
            }
        }

        updateUIDetail(movie);
    }

    @UiThread
    public void updateUIDetail(Movie movie) {
        title.setText(movie.getTitle());
        release.setText(movie.getReleaseDate());
        runtime.setText(movie.getRuntime());
        poster.setImageBitmap(movie.getPoster());
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


    private void createDBEntry(Movie m) {
        DaoSession daoSession = ((ExerciseApplication) getContext().getApplicationContext()).getDaoSession();
        MovieDetailDao movieDetailDao = daoSession.getMovieDetailDao();

        MovieDetail md = new MovieDetail();
        md.setTitle(m.getTitle());
        md.setReleaseDate(m.getReleaseDate());
        md.setRuntime(m.getRuntime());
        md.setPosterUrl(m.getPosterUrl());

        movieDetailDao.insertOrReplace(md);
    }

}
