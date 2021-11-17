package com.example.mvvmclean.anilshatharashi.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class UiMovieModel(
    var page: Int = 1,
    var moviesList: List<UiMovie> = emptyList(),
    var totalPages: Int = 1,
    var totalResults: Long? = 0,
)

@Parcelize
data class UiMovie(
    var adult: Boolean = false,
    var backdropPath: String, // used as Backdrop in Details screen
    var genreIDS: List<Int>? = emptyList(),
    var id: Long = 0,
    var language: Language = Language.EN,
    var originalTitle: String? = "",
    var overview: String? = "",
    var popularity: Double = 0.0,
    var posterPath: String? = null, // used as logo in list screen
    var releaseDate: String? = null,
    var title: String? = "",
    var video: Boolean = false,
    var voteAverage: Double = 0.0,
    var voteCount: Long = 0
): Parcelable

enum class Language {
    EN, ES, NL;

    fun toValue(): String = when (this) {
        EN -> "en"
        ES -> "es"
        NL -> "nl"
    }

    companion object {
        fun forValue(value: String?): Language = when (value) {
            "en" -> EN
            "es" -> ES
            "nl" -> NL
            else -> EN
        }
    }
}
