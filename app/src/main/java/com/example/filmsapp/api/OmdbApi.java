package com.example.filmsapp.api;

import com.example.filmsapp.models.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OmdbApi
{
    @GET("/")
    Call<Movie> getMovieByTitle(
            @Query("apikey") String apiKey,
            @Query("t") String title,
            @Query("plot") String plot,
            @Query("r") String responseType
    );

    @GET("/")
    Call<Movie> getMovieById(
            @Query("apikey") String apiKey,
            @Query("i") String imdbId,
            @Query("plot") String plot,
            @Query("r") String responseType
    );
}