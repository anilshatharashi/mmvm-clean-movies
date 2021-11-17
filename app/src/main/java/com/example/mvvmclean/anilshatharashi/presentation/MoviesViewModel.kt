package com.example.mvvmclean.anilshatharashi.presentation

import androidx.lifecycle.*
import com.example.mvvmclean.anilshatharashi.domain.DiscoverMovies
import com.example.mvvmclean.anilshatharashi.domain.mapper.Mapper
import com.example.mvvmclean.anilshatharashi.domain.usecase.GetMoviesUseCase
import com.example.mvvmclean.anilshatharashi.presentation.MoviesListState.*
import com.example.mvvmclean.anilshatharashi.presentation.mapper.MoviesListUiMapper
import com.example.mvvmclean.anilshatharashi.presentation.model.UiMovie
import com.example.mvvmclean.anilshatharashi.presentation.model.UiMovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val mapper: Mapper<DiscoverMovies, UiMovieModel>,
) : ViewModel() {

    val pageIndex = MutableLiveData(1)

    private val _isLastPage = MutableLiveData(false)
    val isLastPage: LiveData<Boolean> = _isLastPage

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _selectedMovieLiveData = SingleLiveEvent<UiMovie>()
    val selectedMovieLiveData: LiveData<UiMovie> = _selectedMovieLiveData

    private val _moviesListState = MutableStateFlow<MoviesListState>(Loading)
    val moviesListState: LiveData<MoviesListState> = _moviesListState.asLiveData()

    init {
        pageIndex.observeForever { fetchMoviesList() }
    }

    fun fetchMoviesList() {
        val currentPage = pageIndex.value ?: 1
        if (currentPage > 1) _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                getMoviesUseCase.execute(currentPage).collect { result ->
                    _moviesListState.value = result?.let { handleSuccess(it) }
                        ?: Failure(ErrorFetchingMoviesData)
                }
            } catch (exception: Exception) {
                _moviesListState.value = Failure(UnknownException)
            }
        }
    }

    private fun handleSuccess(discoverMovies: DiscoverMovies): Success {
        val uiModel = mapper.mapFrom(discoverMovies)
        _isLastPage.postValue(uiModel.page == uiModel.totalPages)
        _isLoading.postValue(false)
        return Success(uiModel)
    }

    fun onMovieItemSelected(id: UiMovie) {
        _selectedMovieLiveData.postValue(id)
    }
}
