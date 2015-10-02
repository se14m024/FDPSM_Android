package com.fdpsm.exercise.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdpsm.exercise.model.Movie;
import com.fdpsm.exercise.R;
import com.fdpsm.exercise.tasks.MovieDetailTask;
import com.fdpsm.exercise.tasks.TaskListener;

public class MovieDetailFragment extends Fragment implements TaskListener {

    private View view;

    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        String item = getArguments().getString("item");

        MovieDetailTask task = new MovieDetailTask(this);
        task.execute(new String[]{item});

        return view;
    }

    @Override
    public void onTaskCompleted(Movie movie) {
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(movie.getTitle());
        TextView release = (TextView) view.findViewById(R.id.release);
        release.setText(movie.getReleaseDate());
        TextView runtime = (TextView) view.findViewById(R.id.runtime);
        runtime.setText(movie.getRuntime());
        ImageView poster = (ImageView) view.findViewById(R.id.poster);
        poster.setImageBitmap(movie.getPoster());
    }
}
