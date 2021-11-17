package com.example.mvvmclean.anilshatharashi.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmclean.anilshatharashi.MoviesActivity
import com.example.mvvmclean.anilshatharashi.R
import com.example.mvvmclean.anilshatharashi.databinding.FragmentMovieListBinding
import com.example.mvvmclean.anilshatharashi.presentation.ErrorFetchingMoviesData
import com.example.mvvmclean.anilshatharashi.presentation.MoviesListState.*
import com.example.mvvmclean.anilshatharashi.presentation.MoviesViewModel
import com.example.mvvmclean.anilshatharashi.presentation.NoInternet
import com.example.mvvmclean.anilshatharashi.presentation.UnknownException
import com.example.mvvmclean.anilshatharashi.presentation.model.UiMovieModel
import com.example.mvvmclean.anilshatharashi.presentation.ui.RecyclerViewPaginationListener.Companion.PAGE_START
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_list.*

@AndroidEntryPoint
class MoviesFragment : BaseFragment() {

    private lateinit var moviesListAdapter: MoviesListAdapter
    private val viewModel: MoviesViewModel by activityViewModels()
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private var pageIndex: Int = PAGE_START

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.smallestWidth = arguments?.getInt(SMALLEST_WIDTH) ?: 0
        Log.i("**", "Width = ${viewModel.smallestWidth}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        binding.run {
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
                    get() = viewModel.isLastPage.value ?: false

                override val isLoading: Boolean
                    get() = viewModel.isLoading.value ?: false

                override fun loadMoreItems() {
                    viewModel.pageIndex.value = pageIndex++
                }
            })
            updateToolbar(getString(R.string.title_movies_fragment))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.moviesListState.observe(viewLifecycleOwner) {
            when (it) {
                is Loading -> showLoadingView()
                is Success -> showContentView(it.uiModel)
                is Failure -> handleFailure(it.error)
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it == true) moviesListAdapter.addProgressBar()
        }
    }

    private fun showLoadingView() {
        binding.run {
            progressBar.visibility = View.VISIBLE
            moviesListRecyclerView.visibility = View.GONE
        }
    }

    private fun handleFailure(exception: Exception) {
        when (exception) {
            is NoInternet -> showErrorMessage(getString(R.string.no_internet_message))
            is ErrorFetchingMoviesData -> showErrorMessage(getString(R.string.error_message))
            is UnknownException -> showErrorMessage(getString(R.string.unknown_error_message))
        }
    }

    private fun showErrorMessage(message: String) {
        binding.run {
            moviesListRecyclerView.visibility = View.GONE
            progressBar.visibility = View.GONE
            emptyStateView.visibility = View.VISIBLE
            emptyStateView.text = message
        }
    }

    private fun showContentView(uiModel: UiMovieModel) {
        if (pageIndex != PAGE_START) moviesListAdapter.removeProgressBar()
        moviesListAdapter.addMovieList(uiModel.moviesList)

        hideLoadingView()
        emptyStateView.visibility = View.GONE
    }

    private fun hideLoadingView() {
        binding.run {
            progressBar.visibility = View.GONE
            moviesListRecyclerView.visibility = View.VISIBLE
        }
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
        if (emptyStateView.visibility != View.VISIBLE) return
        viewModel.fetchMoviesList()
    }

    companion object {
        private const val GRID_LAYOUT_SPAN_COUNT = 2
        private const val SMALLEST_WIDTH = "width"

        fun newInstance(width: Int) = MoviesFragment().apply {
            arguments = bundleOf(SMALLEST_WIDTH to width)
        }
    }
}
