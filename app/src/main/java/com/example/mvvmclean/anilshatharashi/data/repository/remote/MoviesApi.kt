package com.example.mvvmclean.anilshatharashi.data.repository.remote

import com.example.mvvmclean.anilshatharashi.data.repository.remote.model.DiscoverMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface MoviesApi {

    @GET("/3/discover/movie")
    suspend fun fetchDiscoverMoviesData(
        @Query( "api_key") apiKey: String,
        @Query("page") pageIndex: Int,
        // Passing These default values as we don't have a requirement now to retrieve the MoviesData
        // based on certain conditions/parameters. When we have it we would pass these parameters
        // as an object from the Ui -> Domain -> Data/Network
        @Query("language") language: String = "en_US",
        @Query("include_video") includeVideo: Boolean = false,
        @Query("include_adult") includeAdult: Boolean = true,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("with_watch_monetization_types") withWatchMonetizationTypes: String = "free"
    ): Response<DiscoverMoviesResponse>
}
