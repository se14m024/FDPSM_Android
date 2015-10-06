package com.fdpsm.exercise.fragments;

import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdpsm.exercise.model.Movie;
import com.fdpsm.exercise.R;
import com.fdpsm.exercise.tasks.MovieDetailTask;
import com.fdpsm.exercise.tasks.TaskListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_movie_detail)
public class MovieDetailFragment extends Fragment implements TaskListener {

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.release)
    TextView release;

    @ViewById(R.id.runtime)
    TextView runtime;

    @ViewById(R.id.poster)
    ImageView poster;

    public MovieDetailFragment() {
    }

    @AfterViews
    public void init()
    {
        String item = getArguments().getString("item");
        System.out.println("Detailed Info for:" + item);
        MovieDetailTask task = new MovieDetailTask(this);
        task.execute(new String[]{item});
    }

    @Override
    public void onTaskCompleted(Movie movie) {
        title.setText(movie.getTitle());
        release.setText(movie.getReleaseDate());
        runtime.setText(movie.getRuntime());
        poster.setImageBitmap(movie.getPoster());
    }
}
