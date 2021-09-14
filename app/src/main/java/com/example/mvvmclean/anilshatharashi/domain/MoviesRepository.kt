package com.example.mvvmclean.anilshatharashi.domain

import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun getMoviesList(pageIndex: Int): Flow<DiscoverMovies?>

}
