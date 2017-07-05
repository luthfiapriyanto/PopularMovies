package com.example.luthfi.popularmovies.rest;

import com.example.luthfi.popularmovies.rest.model.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by luthfi on 6/27/17.
 */

public interface MainService {
    @GET("{type}?")
    Call<Movie> getMovies(@Path("type") String type, @Query("api_key") String api_key);
}
