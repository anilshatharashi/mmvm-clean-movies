package com.example.mvvmclean.anilshatharashi.data

import com.example.mvvmclean.anilshatharashi.data.mapper.DiscoverMoviesDomainMapper
import com.example.mvvmclean.anilshatharashi.data.repository.MoviesRepositoryImpl
import com.example.mvvmclean.anilshatharashi.data.repository.remote.MoviesRemoteDataSource
import com.example.mvvmclean.anilshatharashi.data.repository.remote.model.DiscoverMoviesResponse
import com.example.mvvmclean.anilshatharashi.domain.DiscoverMovies
import com.example.mvvmclean.anilshatharashi.domain.mapper.Mapper
import com.example.mvvmclean.anilshatharashi.platform.NetworkHandler
import com.example.mvvmclean.anilshatharashi.presentation.NoInternet
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class MoviesRepositoryImplTest {

    private lateinit var networkHandler: NetworkHandler
    private lateinit var mapper: Mapper<DiscoverMoviesResponse?, DiscoverMovies?>
    private lateinit var moviesRemoteDataSource: MoviesRemoteDataSource
    private lateinit var repository: MoviesRepositoryImpl

    @Before
    fun setUp() {
        moviesRemoteDataSource = mockk()
        networkHandler = mockk()
        mapper =
            DiscoverMoviesDomainMapper() as Mapper<DiscoverMoviesResponse?, DiscoverMovies?>
        repository = MoviesRepositoryImpl(networkHandler, moviesRemoteDataSource, mapper)
    }

    @Test
    fun `getMoviesList calls a correct function`() = runBlocking {
        val moviesFlow = MutableSharedFlow<DiscoverMoviesResponse?>()
        coEvery { networkHandler.isConnected() } returns true
        coEvery { moviesRemoteDataSource.fetchDiscoverMoviesData(1) } returns moviesFlow

        repository.getMoviesList(1)

        coVerify(exactly = 1) { moviesRemoteDataSource.fetchDiscoverMoviesData(1) }
    }

    @Test
    fun `throw NoInternet Exception when it's not connected to internet`() {
        val moviesFlow = MutableSharedFlow<DiscoverMoviesResponse?>()

        coEvery { networkHandler.isConnected() } returns false
        coEvery { moviesRemoteDataSource.fetchDiscoverMoviesData(any()) } returns  moviesFlow

        runBlocking {
            try {
                repository.getMoviesList(1)
            } catch (e: Exception) {
                assertTrue(e is NoInternet)
            }
        }
    }

    @Test
    fun `test exception thrown from the network call should not be caught inside repository`() {
        coEvery { networkHandler.isConnected() } returns true
        coEvery { moviesRemoteDataSource.fetchDiscoverMoviesData(any()) } throws UnknownHostException()
        runBlocking {
            try {
                repository.getMoviesList(1)
            } catch (e: Exception) {
                assertTrue(e is UnknownHostException)
            }
        }
    }
}