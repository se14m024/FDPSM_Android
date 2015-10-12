package com.fdpsm.exercise.model;

import com.google.gson.annotations.SerializedName;

public class Search {

    @SerializedName("Search")
    private Result[] result;

    public Result[] getResult() {
        return result;
    }

    public class Result
    {
        @SerializedName("Title")
        String title;

        public String getTitle() {
            return title;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}


