package com.example.mvvmclean.anilshatharashi.data.mapper

import com.example.mvvmclean.anilshatharashi.data.repository.remote.model.DiscoverMoviesResponse
import com.example.mvvmclean.anilshatharashi.data.repository.remote.model.MovieListResponse
import com.example.mvvmclean.anilshatharashi.domain.DiscoverMovies
import com.example.mvvmclean.anilshatharashi.domain.Movie
import com.example.mvvmclean.anilshatharashi.domain.OriginalLanguage
import com.example.mvvmclean.anilshatharashi.domain.mapper.Mapper
import javax.inject.Inject

class DiscoverMoviesDomainMapper @Inject constructor()
    : Mapper<DiscoverMoviesResponse, DiscoverMovies> {
    override fun mapFrom(from: DiscoverMoviesResponse): DiscoverMovies =
        DiscoverMovies(
            from.page,
            mapToDomainMovieList(from.results),
            from.totalPages,
            from.totalResults
        )

    private fun mapToDomainMovieList(moviesList: List<MovieListResponse>?) = moviesList?.map {
        Movie(
            it.adult,
            it.backdropPath,
            it.genreIDS,
            it.id,
            OriginalLanguage.forValue(it.originalLanguage),
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

