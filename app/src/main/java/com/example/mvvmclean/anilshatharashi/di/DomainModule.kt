package com.example.mvvmclean.anilshatharashi.di

import com.example.mvvmclean.anilshatharashi.domain.DiscoverMovies
import com.example.mvvmclean.anilshatharashi.domain.MoviesRepository
import com.example.mvvmclean.anilshatharashi.domain.mapper.Mapper
import com.example.mvvmclean.anilshatharashi.domain.usecase.GetMoviesUseCase
import com.example.mvvmclean.anilshatharashi.presentation.mapper.MoviesListUiMapper
import com.example.mvvmclean.anilshatharashi.presentation.model.UiMovieModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
object DomainModule {

    @ViewModelScoped
    @Provides
    fun provideMoviesListUiMapper(): Mapper<DiscoverMovies?, UiMovieModel?> = MoviesListUiMapper()

    @Provides
    @ViewModelScoped
    fun provideGetMoviesUseCase(repository: MoviesRepository): GetMoviesUseCase =
        GetMoviesUseCase(repository)

}