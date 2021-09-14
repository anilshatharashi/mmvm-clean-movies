package com.example.mvvmclean.anilshatharashi.presentation.mapper

import com.example.mvvmclean.anilshatharashi.domain.DiscoverMovies
import com.example.mvvmclean.anilshatharashi.domain.Movie
import com.example.mvvmclean.anilshatharashi.domain.mapper.Mapper
import com.example.mvvmclean.anilshatharashi.presentation.model.Language
import com.example.mvvmclean.anilshatharashi.presentation.model.UiMovie
import com.example.mvvmclean.anilshatharashi.presentation.model.UiMovieModel

class MoviesListUiMapper : Mapper<DiscoverMovies?, UiMovieModel?> {
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
            it.backdropPath,
            it.genreIDS,
            it.id,
            Language.forValue(it.originalLanguage?.toValue()),
            it.originalTitle,
            it.overview,
            it.popularity,
            it.posterPath,
            it.releaseDate,
            it.title,
            it.video,
            it.voteAverage,
            it.voteCount,
        )
    }
}
