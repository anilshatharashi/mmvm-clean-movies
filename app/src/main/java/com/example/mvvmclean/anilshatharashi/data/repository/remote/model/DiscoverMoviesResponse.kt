package com.example.mvvmclean.anilshatharashi.data.repository.remote.model

import com.google.gson.annotations.SerializedName

data class DiscoverMoviesResponse(
    var page: Long = 0,
    @SerializedName("results")
    var results: List<MovieListResponse>? = null,
    @SerializedName("total_pages")
    var totalPages: Long = 0,
    @SerializedName("total_results")
    var totalResults: Long = 0,
)

data class MovieListResponse(
    var adult: Boolean = false,
    @SerializedName("backdrop_path")
    var backdropPath: String? = null,
    @SerializedName("genre_ids")
    var genreIDS: List<Int>? = null,
    var id: Long = 0,
    @SerializedName("original_language")
    var originalLanguage: String? = null,
    @SerializedName("original_title")
    var originalTitle: String? = null,
    var overview: String? = null,
    var popularity: Double = 0.0,
    @SerializedName("poster_path")
    var posterPath: String? = null,
    @SerializedName("release_date")
    var releaseDate: String? = null,
    var title: String? = null,
    var video: Boolean = false,
    @SerializedName("vote_average")
    var voteAverage: Double = 0.0,
    @SerializedName("vote_count")
    var voteCount: Long = 0
)

