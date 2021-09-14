package com.example.mvvmclean.anilshatharashi.presentation.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmclean.anilshatharashi.R
import com.example.mvvmclean.anilshatharashi.presentation.model.UiMovie
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_loading.view.*
import kotlinx.android.synthetic.main.view_item_movie.view.*

class MoviesListAdapter(private val movieItemClickListener: (UiMovie) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val movieList = mutableListOf<UiMovie?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LOADING -> LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            )
            else -> MovieItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.view_item_movie, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieItemViewHolder ->
                movieList[position]?.run {
                    holder.bindTo(this)
                    holder.itemView.setOnClickListener { movieItemClickListener.invoke(this) }
                }
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (movieList[position] == null) TYPE_LOADING else TYPE_CONTENT

    fun addProgressBar() {
        movieList.add(null)
        notifyItemInserted(movieList.size - 1)
    }

    fun removeProgressBar() {
        movieList.apply {
            if (size != 0) {
                removeAt(size - 1)
                notifyItemRemoved(size)
            }
        }
    }

    override fun getItemCount(): Int = movieList.size

    fun addMovieList(list: List<UiMovie>) {
        movieList.addAll(list)
        notifyDataSetChanged()
    }

    class MovieItemViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView), LayoutContainer {

        override val containerView: View
            get() = itemView

        fun bindTo(movieModel: UiMovie) {
            itemView.titleView.text = movieModel.title
            itemView.releaseDateView.text = movieModel.releaseDate
            movieModel.posterPath?.let {
                itemView.posterView.loadFromUrl(posterSize + it)
            }
        }
    }

    class LoadingViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {
        init {
            itemView.progressBar
        }
    }

    companion object {
        private const val TYPE_LOADING = 0
        private const val TYPE_CONTENT = 1

        // Hardcoding the posterSize as I am not maintaining the configuration
        // in local Database. Ideal way is to make a network request to fetch the Configuration and keep updating it
        // (for every two weeks or four weeks)
        private const val posterSize: String = "/w300/"
    }
}