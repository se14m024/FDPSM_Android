package com.fdpsm.exercise.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("Title")
    private String title;
    @SerializedName("Year")
    private String releaseDate;
    @SerializedName("Runtime")
    private String runtime;
    @SerializedName("Poster")
    private String posterUrl;

    private Bitmap poster;

    public Movie() {

    }

    public Movie(String title, String releaseDate, String runtime, String posterUrl) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public Bitmap getPoster() {
        return poster;
    }

    public void setPoster(Bitmap poster) {
        this.poster = poster;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", runtime='" + runtime + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                '}';
    }
}
