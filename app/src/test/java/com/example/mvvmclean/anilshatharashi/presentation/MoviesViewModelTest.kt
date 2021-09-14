package com.example.mvvmclean.anilshatharashi.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.mvvmclean.anilshatharashi.domain.DiscoverMovies
import com.example.mvvmclean.anilshatharashi.domain.Movie
import com.example.mvvmclean.anilshatharashi.domain.mapper.Mapper
import com.example.mvvmclean.anilshatharashi.domain.usecase.GetMoviesUseCase
import com.example.mvvmclean.anilshatharashi.presentation.mapper.MoviesListUiMapper
import com.example.mvvmclean.anilshatharashi.presentation.model.UiMovieModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MoviesViewModelTest {

    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var getMoviesUseCase: GetMoviesUseCase
    private lateinit var uiMapper: Mapper<DiscoverMovies, UiMovieModel>

    @get:Rule
    val taskExecutor = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        getMoviesUseCase = mockk()
        uiMapper = MoviesListUiMapper() as Mapper<DiscoverMovies, UiMovieModel>
        moviesViewModel = MoviesViewModel(getMoviesUseCase, uiMapper)
    }

    @Test
    fun `page should be loading page at the beginning`() {
        observeState {
            verify { it.onChanged(MoviesListState.Loading) }
        }
    }

    @Test
    fun `page should be error page if null is returned`() {
        val moviesFlow = MutableSharedFlow<DiscoverMovies?>()
        coEvery { getMoviesUseCase.execute(1) } returns moviesFlow

        observeState {
            moviesViewModel.fetchMoviesList(1)
            runBlocking { moviesFlow.emit(null) }
            verify { it.onChanged(MoviesListState.Failure(ErrorFetchingMoviesData)) }
        }
    }

    @Test
    fun `getMoviesData should push moviess to the ui`() {
        // GIVEN I select a movies id
        val moviesFlow = MutableSharedFlow<DiscoverMovies?>()
        coEvery { getMoviesUseCase.execute(1) } returns moviesFlow

        observeState {
            moviesViewModel.fetchMoviesList(1)

            // WHEN I push a movies
            val movies = DiscoverMovies(
                moviesList = listOf(
                    Movie(adult = false, id = 82),
                    Movie(adult = false, id = 42),
                    Movie(adult = false, id = 52),
                )
            )
            runBlocking { moviesFlow.emit(movies) }

            // THEN it should be pushed to the ui
            verify(exactly = 1) { it.onChanged(MoviesListState.Success(uiMapper.mapFrom(movies))) }
        }
    }

    @Test
    fun `getMoviesData should only push moviess for the last requested id`() {
        // GIVEN there are two movies ids
        val firstPageFlow = MutableSharedFlow<DiscoverMovies?>()
        val secondPageFlow = MutableSharedFlow<DiscoverMovies?>()

        coEvery { getMoviesUseCase.execute(1) } returns firstPageFlow
        coEvery { getMoviesUseCase.execute(2) } returns secondPageFlow

        observeState {
            // WHEN I subscribe first to one, then to the second
            moviesViewModel.fetchMoviesList(1)
            moviesViewModel.fetchMoviesList(2)

            // AND there is a movies for both ids
            val list1 = DiscoverMovies(
                moviesList = listOf(
                    Movie(adult = false, id = 82),
                    Movie(adult = false, id = 42),
                    Movie(adult = false, id = 52),
                )
            )
            val list2 = DiscoverMovies(
                moviesList = listOf(
                    Movie(adult = false, id = 382),
                    Movie(adult = false, id = 242),
                    Movie(adult = false, id = 2542),
                )
            )
            runBlocking {
                firstPageFlow.emit(list1)
                secondPageFlow.emit(list2)
            }

            // THEN the ui should only get the movies from the last subscribed id
            verify(exactly = 1) {
                it.onChanged(MoviesListState.Success(uiMapper.mapFrom(list2)))
            }
            verify(exactly = 0) {
                it.onChanged(MoviesListState.Success(uiMapper.mapFrom(list1)))
            }
        }
    }

    private fun observeState(block: (Observer<MoviesListState>) -> Unit) {
        val observer = mockk<Observer<MoviesListState>>(relaxUnitFun = true)
        moviesViewModel.moviesList.observeForever(observer)
        try {
            block(observer)
        } finally {
            moviesViewModel.moviesList.removeObserver(observer)
        }
    }
}