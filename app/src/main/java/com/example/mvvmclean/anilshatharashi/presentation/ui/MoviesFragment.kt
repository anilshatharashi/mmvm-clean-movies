package com.example.mvvmclean.anilshatharashi.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmclean.anilshatharashi.MoviesActivity
import com.example.mvvmclean.anilshatharashi.R
import com.example.mvvmclean.anilshatharashi.presentation.ErrorFetchingMoviesData
import com.example.mvvmclean.anilshatharashi.presentation.MoviesListState.*
import com.example.mvvmclean.anilshatharashi.presentation.MoviesViewModel
import com.example.mvvmclean.anilshatharashi.presentation.NoInternet
import com.example.mvvmclean.anilshatharashi.presentation.UnknownException
import com.example.mvvmclean.anilshatharashi.presentation.model.UiMovieModel
import com.example.mvvmclean.anilshatharashi.presentation.ui.RecyclerViewPaginationListener.Companion.PAGE_START
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : BaseFragment() {

    private lateinit var moviesListAdapter: MoviesListAdapter
    private lateinit var moviesListRecyclerView: RecyclerView
    private val viewModel: MoviesViewModel by activityViewModels()

    private var pageIndex: Int = PAGE_START

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchMovieList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_movie_list, container, false)
        moviesListRecyclerView = rootView.findViewById(R.id.moviesListRecyclerView)
        val gridLayoutManager = GridLayoutManager(
            activity,
            GRID_LAYOUT_SPAN_COUNT,
            RecyclerView.VERTICAL,
            false
        )

        moviesListRecyclerView.layoutManager = gridLayoutManager
        moviesListAdapter = MoviesListAdapter { viewModel.onMovieItemSelected(it) }
        moviesListRecyclerView.adapter = moviesListAdapter

        moviesListRecyclerView.addOnScrollListener(object :
            RecyclerViewPaginationListener(gridLayoutManager) {

            override val isLastPage: Boolean
                get() = viewModel.isLastPage.value!!

            override val isLoading: Boolean
                get() = viewModel.isLoading.value!!

            override fun loadMoreItems() {
                viewModel.pageIndex.value = pageIndex++
                fetchMovieList()
            }
        })
        updateToolbar(getString(R.string.title_movies_fragment))
        return rootView
    }

    private fun fetchMovieList() {
        viewModel.fetchMoviesList(viewModel.pageIndex.value!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.moviesList.observe(viewLifecycleOwner) {
                when (it) {
                    is Loading -> showLoadingView()
                    is Success -> showContentView(it.uiModel)
                    is Failure -> handleFailure(it.error)
                }
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it == true) moviesListAdapter.addProgressBar()
        }
    }

    private fun showLoadingView() {
        progressBar.visibility = View.VISIBLE
        moviesListRecyclerView.visibility = View.GONE
    }

    private fun handleFailure(error: MovieListError) {
        when (error) {
            is NoInternet -> showErrorMessage(getString(R.string.no_internet_message))
            is ErrorFetchingMoviesData -> showErrorMessage(getString(R.string.error_message))
            is UnknownException -> showErrorMessage(getString(R.string.unknown_error_message))
        }
    }

    private fun showErrorMessage(message: String) {
        moviesListRecyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE
        movieDetail.visibility = View.VISIBLE
        movieDetail.text = message
    }

    private fun showContentView(uiModel: UiMovieModel) {
        if (pageIndex != PAGE_START) moviesListAdapter.removeProgressBar()
        moviesListAdapter.addMovieList(uiModel.moviesList)

        moviesListRecyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        movieDetail.visibility = View.GONE

    }

    private fun updateToolbar(toolbarTitle: String?) {
        val moviesActivity = activity as MoviesActivity
        moviesActivity.supportActionBar?.apply {
            title = toolbarTitle
            setDisplayShowHomeEnabled(false)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(true)
        }
    }

    override fun onContentError() {
        showErrorMessage(getString(R.string.no_internet_message))
    }

    override fun onContentAvailable() {
        fetchMovieList()
    }

    companion object {
        private const val GRID_LAYOUT_SPAN_COUNT = 2
        fun newInstance() = MoviesFragment()
    }
}
