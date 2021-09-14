package com.example.mvvmclean.anilshatharashi.data.repository.remote

import com.example.mvvmclean.anilshatharashi.BuildConfig.API_KEY
import com.example.mvvmclean.anilshatharashi.data.repository.remote.model.DiscoverMoviesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MoviesRemoteDataSourceImpl @Inject constructor(
    private val moviesApi: MoviesApi,
) : MoviesRemoteDataSource {

    override suspend fun fetchDiscoverMoviesData(pageIndex: Int): Flow<DiscoverMoviesResponse?> =
        flow {
            val fetchDiscoverMoviesData = moviesApi.fetchDiscoverMoviesData(API_KEY, pageIndex)
            emit(fetchDiscoverMoviesData.body())
        }

//  I would use  a mapper to map this object to Entity(Room DB) when we need to cache the data.
//  (We don't use the same DiscoverMoviesData object to save the data in DB)
}