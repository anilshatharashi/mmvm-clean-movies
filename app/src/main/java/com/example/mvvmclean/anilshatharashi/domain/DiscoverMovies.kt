package com.example.mvvmclean.anilshatharashi.domain

data class DiscoverMovies(
    var page: Long = 0,
    var moviesList: List<Movie>? = null,
    var totalPages: Long = 0,
    var totalResults: Long = 0,
)

data class Movie(
    var adult: Boolean = false,
    var backdropPath: String? = null,
    var genreIDS: List<Int>? = null,
    var id: Long = 0,
    var originalLanguage: OriginalLanguage? = null,
    var originalTitle: String? = null,
    var overview: String? = null,
    var popularity: Double = 0.0,
    var posterPath: String? = null,
    var releaseDate: String? = null,
    var title: String? = null,
    var video: Boolean = false,
    var voteAverage: Double = 0.0,
    var voteCount: Long = 0
)

enum class OriginalLanguage {
    EN, ES, NL;

    fun toValue(): String = when (this) {
        EN -> "en"
        ES -> "es"
        NL -> "nl"
    }

    companion object {
        fun forValue(value: String?): OriginalLanguage = when (value) {
            "en" -> EN
            "es" -> ES
            "nl" -> NL
            else -> EN
        }
    }
}
