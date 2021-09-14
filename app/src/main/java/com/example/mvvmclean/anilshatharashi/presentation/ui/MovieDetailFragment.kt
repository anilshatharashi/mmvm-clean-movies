package com.example.mvvmclean.anilshatharashi.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mvvmclean.anilshatharashi.MoviesActivity
import com.example.mvvmclean.anilshatharashi.R
import com.example.mvvmclean.anilshatharashi.presentation.model.UiMovie
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_detail.view.*

@AndroidEntryPoint
class MovieDetailFragment : BaseFragment() {

    private var movie: UiMovie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            movie = arguments?.getParcelable(SELECTED_MOVIE_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_movie_detail, container, false)
        movie?.let { renderDataOnUi(rootView, it) }
        return rootView
    }

    private fun renderDataOnUi(rootView: View?, model: UiMovie) {
        updateToolbar(model.title)
        rootView?.run {
            detailImageView.loadFromUrl(backdropSize + model.backdropPath)
            detailTitleView.text = model.title
            detailReleasedOnView.text = getString(R.string.detail_release_date, model.releaseDate)

            userRatingsValueView.text = model.voteAverage.toString()

            detailOverviewContent.text = model.overview
        }
    }

    private fun updateToolbar(toolbarTitle: String?) {
        val moviesActivity = activity as MoviesActivity
        moviesActivity.supportActionBar?.apply {
            title = toolbarTitle
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
        }
    }

    companion object {
         const val FRAGMENT_TAG = "movie_details_frag"
        private const val SELECTED_MOVIE_KEY = "selected_movie"
        fun newInstance(movie: UiMovie): MovieDetailFragment =
            MovieDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(SELECTED_MOVIE_KEY, movie)
                }
            }

        private const val backdropSize: String = "/w1280/"
    }

    override fun onContentError() {
       // showNoInternetMessage()
    }

    override fun onContentAvailable() {
        // showNoInternetMessage()
    }

}
