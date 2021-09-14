package com.example.mvvmclean.anilshatharashi.di

import com.example.mvvmclean.anilshatharashi.data.mapper.DiscoverMoviesDomainMapper
import com.example.mvvmclean.anilshatharashi.data.repository.MoviesRepositoryImpl
import com.example.mvvmclean.anilshatharashi.data.repository.remote.MoviesRemoteDataSource
import com.example.mvvmclean.anilshatharashi.data.repository.remote.model.DiscoverMoviesResponse
import com.example.mvvmclean.anilshatharashi.domain.DiscoverMovies
import com.example.mvvmclean.anilshatharashi.domain.MoviesRepository
import com.example.mvvmclean.anilshatharashi.domain.mapper.Mapper
import com.example.mvvmclean.anilshatharashi.platform.NetworkHandler
import com.example.mvvmclean.anilshatharashi.presentation.mapper.DateFormatter
import com.example.mvvmclean.anilshatharashi.presentation.mapper.DateFormatterImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    private const val DATE_FORMAT_FROM_SERVER = "yyyy-MM-dd"

    @Singleton
    @Provides
    fun provideDateFormatter(): DateFormatter =
        DateFormatterImpl(SimpleDateFormat(DATE_FORMAT_FROM_SERVER, Locale.getDefault()))

    @Singleton
    @Provides
    fun provideMoviesListDomainMapper(dateFormatter: DateFormatter): Mapper<DiscoverMoviesResponse, DiscoverMovies> =
        DiscoverMoviesDomainMapper(dateFormatter)

    @Singleton
    @Provides
    fun provideMoviesRepository(
        networkHandler: NetworkHandler,
        remoteDataSource: MoviesRemoteDataSource,
        mapper: Mapper<DiscoverMoviesResponse?, DiscoverMovies?>
    ): MoviesRepository = MoviesRepositoryImpl(networkHandler, remoteDataSource, mapper)

}