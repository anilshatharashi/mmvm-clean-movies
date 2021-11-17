package com.example.mvvmclean.anilshatharashi.presentation.mapper

import com.example.mvvmclean.anilshatharashi.domain.DiscoverMovies
import com.example.mvvmclean.anilshatharashi.domain.Movie
import com.example.mvvmclean.anilshatharashi.domain.mapper.Mapper
import com.example.mvvmclean.anilshatharashi.presentation.model.Language
import com.example.mvvmclean.anilshatharashi.presentation.model.UiMovie
import com.example.mvvmclean.anilshatharashi.presentation.model.UiMovieModel
import javax.inject.Inject

class MoviesListUiMapper @Inject constructor() : Mapper<DiscoverMovies?, UiMovieModel?> {

    private var thumbnailSize: String? = null
    private var backdropImageSize: String? = null

    var smallestWidth: Int = 0
        set(value) {
            field = value
            getThumbnailAndCoverPhotoSizes()
        }

    override fun mapFrom(from: DiscoverMovies?): UiMovieModel =
        UiMovieModel(
            from?.page?.toInt() ?: 1,
            from?.moviesList?.let { mapToUiMovieList(it) } ?: emptyList(),
            from?.totalPages?.toInt() ?: 1,
            from?.totalResults
        )

    private fun mapToUiMovieList(moviesList: List<Movie>?) = moviesList?.map {
        UiMovie(
            it.adult,
            backdropImageSize + it.backdropPath,
            it.genreIDS,
            it.id,
            Language.forValue(it.originalLanguage?.toValue()),
            it.originalTitle,
            it.overview,
            it.popularity,
            thumbnailSize + it.posterPath,
            it.releaseDate,
            it.title,
            it.video,
            it.voteAverage,
            it.voteCount,
        )
    }

    // Better way is getting the configuration information by making the initial configuration request and store it in local Database.
    private fun getThumbnailAndCoverPhotoSizes() {
        //320dp: a typical phone screen (240x320 ldpi, 320x480 mdpi, 480x800 hdpi, etc).
        //480dp: a large phone screen ~5" (480x800 mdpi).
        //600dp: a 7” tablet (600x1024 mdpi).
        //720dp: a 10” tablet (720x1280 mdpi, 800x1280 mdpi, etc).
        //  Retrieved from https://api.themoviedb.org/3/configuration?api_key=
        //  "logo_sizes": [
        //            "w45",
        //            "w92",
        //            "w154",
        //            "w185",
        //            "w300",
        //            "w500",
        //            "original"
        //        ],
        //        "poster_sizes": [
        //            "w92",
        //            "w154",
        //            "w185",
        //            "w342",
        //            "w500",
        //            "w780",
        //            "original"
        //        ],
        var thumbnailSize = "/w500/"
        var coverPhotoSize = "/w780/"
        when (smallestWidth) {
            in 320..479 -> {
                thumbnailSize = "/w185/"; coverPhotoSize = "/w500/"
            }
            in 480..599 -> {
                thumbnailSize = "/w300/"; coverPhotoSize = "/w780/"
            }
            in 600..719 -> {
                thumbnailSize = "/w500/"; coverPhotoSize = "/w780/"
            }
            in 720..1280 -> {
                thumbnailSize = "/original/"; coverPhotoSize = "/original/"
            }
        }
        this.thumbnailSize = thumbnailSize
        this.backdropImageSize = coverPhotoSize
    }
}
