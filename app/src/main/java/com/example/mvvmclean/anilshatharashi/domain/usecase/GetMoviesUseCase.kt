package com.example.mvvmclean.anilshatharashi.domain.usecase

import com.example.mvvmclean.anilshatharashi.domain.DiscoverMovies
import com.example.mvvmclean.anilshatharashi.domain.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val repository: MoviesRepository) :
    UseCase<Int, DiscoverMovies?>() {

    override suspend fun buildUseCase(params:Int): Flow<DiscoverMovies?> =
        repository.getMoviesList(params)

}
