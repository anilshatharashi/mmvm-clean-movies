package com.example.mvvmclean.anilshatharashi.presentation

import com.example.mvvmclean.anilshatharashi.presentation.model.UiMovieModel

sealed class MoviesListState {
    object Loading : MoviesListState()
    data class Success(val uiModel: UiMovieModel) : MoviesListState()
    data class Failure(val error: Exception) : MoviesListState()
}

object NoInternet : Exception()
object ErrorFetchingMoviesData : Exception()
object UnknownException : Exception()
