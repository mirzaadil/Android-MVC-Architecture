package com.mirzaadil.task.controller.rest;

import com.mirzaadil.task.R;
import com.mirzaadil.task.controller.utils.ApplicationConstants;
import com.mirzaadil.task.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mirzaadil on 8/23/17.
 */

public interface ApiInterface {
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);
    @GET("search/movie")
    Call<MoviesResponse> SearchMovie(@Query("api_key") String apiKey , @Query("query") String search_query);

}
