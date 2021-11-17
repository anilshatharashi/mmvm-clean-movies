package com.example.mvvmclean.anilshatharashi

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvmclean.anilshatharashi.presentation.MoviesViewModel
import com.example.mvvmclean.anilshatharashi.presentation.ui.MovieDetailFragment
import com.example.mvvmclean.anilshatharashi.presentation.ui.MovieDetailFragment.Companion.FRAGMENT_TAG
import com.example.mvvmclean.anilshatharashi.presentation.ui.MoviesFragment
import com.example.mvvmclean.anilshatharashi.presentation.ui.addFragment
import com.example.mvvmclean.anilshatharashi.presentation.ui.replaceFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesActivity : AppCompatActivity() {

    private val moviesViewModel: MoviesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acivity_movies)

        if (savedInstanceState == null) {
            addFragment(MoviesFragment.newInstance(), null)
        }
        moviesViewModel.selectedMovieLiveData.observe(this, {
            if (supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) == null) {
                replaceFragment(MovieDetailFragment.newInstance(it), FRAGMENT_TAG)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}