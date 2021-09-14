package com.example.mvvmclean.anilshatharashi.domain

import com.example.mvvmclean.anilshatharashi.domain.usecase.GetMoviesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetMoviesUseCaseTest {
    private lateinit var useCase: GetMoviesUseCase
    private lateinit var repository: MoviesRepository

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetMoviesUseCase(repository)
    }

    @Test
    fun `test execute calls the repository method only once`() {
        val flow = mockk<Flow<DiscoverMovies>>()
        coEvery { repository.getMoviesList(any()) } returns flow

        runBlocking { useCase.execute(1) }

        coVerify(exactly = 1) { repository.getMoviesList(1) }
    }

    @Test
    fun `test buildUseCase returns the correct data`() {
        val flow = mockk<Flow<DiscoverMovies>>()
        coEvery { repository.getMoviesList(any()) } returns flow

        val result = runBlocking { useCase.execute(1) }

        assertEquals(flow, result)
    }
}