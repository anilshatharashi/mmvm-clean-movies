package com.example.mvvmclean.anilshatharashi.data.repository

import com.example.mvvmclean.anilshatharashi.data.repository.remote.MoviesRemoteDataSource
import com.example.mvvmclean.anilshatharashi.data.repository.remote.model.DiscoverMoviesResponse
import com.example.mvvmclean.anilshatharashi.domain.DiscoverMovies
import com.example.mvvmclean.anilshatharashi.domain.MoviesRepository
import com.example.mvvmclean.anilshatharashi.domain.mapper.Mapper
import com.example.mvvmclean.anilshatharashi.platform.NetworkHandler
import com.example.mvvmclean.anilshatharashi.presentation.NoInternet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val remoteDataSource: MoviesRemoteDataSource,
    private val mapper: Mapper<DiscoverMoviesResponse?, DiscoverMovies?>
) : MoviesRepository {

    override suspend fun getMoviesList(pageIndex: Int): Flow<DiscoverMovies?> {
        if (!networkHandler.isConnected()) throw NoInternet
        else return remoteDataSource.fetchDiscoverMoviesData(pageIndex).map { mapper.mapFrom(it) }
    }
}