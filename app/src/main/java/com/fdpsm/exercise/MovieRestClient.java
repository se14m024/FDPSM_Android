package com.fdpsm.exercise;

import com.fdpsm.exercise.model.Movie;
import com.fdpsm.exercise.model.Search;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

@Rest(rootUrl = "http://www.omdbapi.com", converters = {GsonHttpMessageConverter.class})
public interface MovieRestClient {

    @Get("/?t={title}&y=&plot=short&r=json")
    Movie getMovieDetail(String title);

    @Get("/?s={search}&y=&plot=full&r=json")
    Search getMovieList(String search);
}
