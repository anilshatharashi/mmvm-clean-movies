package com.example.mvvmclean.anilshatharashi.data.repository.remote

import com.example.mvvmclean.anilshatharashi.data.repository.remote.model.DiscoverMoviesResponse
import kotlinx.coroutines.flow.Flow

interface MoviesRemoteDataSource {

    suspend fun fetchDiscoverMoviesData(pageIndex: Int): Flow<DiscoverMoviesResponse?>

}
