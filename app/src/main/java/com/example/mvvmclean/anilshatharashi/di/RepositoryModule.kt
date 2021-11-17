package com.example.mvvmclean.anilshatharashi.di

import com.example.mvvmclean.anilshatharashi.data.mapper.DiscoverMoviesDomainMapper
import com.example.mvvmclean.anilshatharashi.data.repository.MoviesRepositoryImpl
import com.example.mvvmclean.anilshatharashi.data.repository.remote.MoviesRemoteDataSource
import com.example.mvvmclean.anilshatharashi.data.repository.remote.model.DiscoverMoviesResponse
import com.example.mvvmclean.anilshatharashi.domain.DiscoverMovies
import com.example.mvvmclean.anilshatharashi.domain.MoviesRepository
import com.example.mvvmclean.anilshatharashi.domain.mapper.Mapper
import com.example.mvvmclean.anilshatharashi.platform.NetworkHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMoviesListDomainMapper(): Mapper<DiscoverMoviesResponse, DiscoverMovies> =
        DiscoverMoviesDomainMapper()

    @Singleton
    @Provides
    fun provideMoviesRepository(
        networkHandler: NetworkHandler,
        remoteDataSource: MoviesRemoteDataSource,
        mapper: Mapper<DiscoverMoviesResponse?, DiscoverMovies?>,
    ): MoviesRepository = MoviesRepositoryImpl(networkHandler, remoteDataSource, mapper)

}