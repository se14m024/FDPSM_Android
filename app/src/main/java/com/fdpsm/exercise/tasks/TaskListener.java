package com.fdpsm.exercise.tasks;

import com.fdpsm.exercise.model.Movie;

public interface TaskListener {

    void onTaskCompleted(Movie movie);
}
