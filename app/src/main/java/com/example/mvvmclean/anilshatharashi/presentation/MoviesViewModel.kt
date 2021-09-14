package com.example.mvvmclean.anilshatharashi.presentation

import android.util.Log
import androidx.lifecycle.*
import com.example.mvvmclean.anilshatharashi.domain.DiscoverMovies
import com.example.mvvmclean.anilshatharashi.domain.mapper.Mapper
import com.example.mvvmclean.anilshatharashi.domain.usecase.GetMoviesUseCase
import com.example.mvvmclean.anilshatharashi.presentation.MoviesListState.*
import com.example.mvvmclean.anilshatharashi.presentation.model.UiMovie
import com.example.mvvmclean.anilshatharashi.presentation.model.UiMovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val mapper: Mapper<DiscoverMovies, UiMovieModel>
) : ViewModel() {

    val pageIndex = MutableLiveData(1)

    private val _isLastPage = MutableLiveData(false)
    val isLastPage: LiveData<Boolean> = _isLastPage

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var previousLiveData: LiveData<DiscoverMovies?>? = null

    private val _selectedMovieLiveData = SingleLiveEvent<UiMovie>()
    val selectedMovieLiveData: LiveData<UiMovie> = _selectedMovieLiveData

    private val _moviesListState = MediatorLiveData<MoviesListState>().apply { postValue(Loading) }
    val moviesList: LiveData<MoviesListState> = _moviesListState


    fun fetchMoviesList(pageIndex: Int) {
        if (pageIndex > 1) _isLoading.postValue(true)

        viewModelScope.launch {
            previousLiveData?.let {
                _moviesListState.removeSource(it)
            }
            try {
                previousLiveData = getMoviesUseCase.execute(pageIndex)
                    .asLiveData()
                    .also { liveData ->
                        _moviesListState.addSource(liveData) { handleResponse(it) }
                    }
            } catch (exception: Exception) {
                Log.e(
                    "***MoviesViewModel",
                    "exception = $exception\nstacktrace =${exception.printStackTrace()}"
                )
                _moviesListState.value = if (exception is MovieListError) Failure(exception)
                else Failure(UnknownException)
            }
        }
    }

    private fun handleResponse(discoverMovies: DiscoverMovies?) {
        discoverMovies?.let {
            val uiModel = mapper.mapFrom(it)
            _moviesListState.value = Success(uiModel)
            Log.d("***MoviesViewModel", " uiModel = $uiModel")
            _isLastPage.postValue(uiModel.page == uiModel.totalPages)
            _isLoading.postValue(false)
        } ?: run {
            Log.d("***MoviesViewModel", " uiModel = $discoverMovies")
            _moviesListState.value = Failure(ErrorFetchingMoviesData)
        }
    }

    fun onMovieItemSelected(id: UiMovie) {
        _selectedMovieLiveData.postValue(id)
    }
}
