package com.example.mvvmclean.anilshatharashi.presentation

import com.example.mvvmclean.anilshatharashi.presentation.model.UiMovieModel

sealed class MoviesListState {
    object Loading : MoviesListState()
    data class Success(val uiModel: UiMovieModel) : MoviesListState()
    data class Failure(val error: MovieListError) : MoviesListState()

    abstract class MovieListError : Exception()

}
object NoInternet : MoviesListState.MovieListError()
object ErrorFetchingMoviesData : MoviesListState.MovieListError()
object UnknownException : MoviesListState.MovieListError()
