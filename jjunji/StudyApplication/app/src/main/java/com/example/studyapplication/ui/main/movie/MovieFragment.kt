package com.example.studyapplication.ui.main.movie

import android.os.Bundle
import android.view.View
import com.example.studyapplication.R
import com.example.studyapplication.data.datasource.local.NaverLocalDataSourceImpl
import com.example.studyapplication.data.datasource.remote.NaverRemoteDataSourceImpl
import com.example.studyapplication.data.model.MovieInfo
import com.example.studyapplication.data.repository.NaverSearchRepository
import com.example.studyapplication.data.repository.NaverSearchRepositoryImpl
import com.example.studyapplication.ui.main.base.BaseSearchFragment
import com.example.studyapplication.ui.main.movie.adapter.MovieAdapter
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : BaseSearchFragment(R.layout.fragment_movie), MovieContract.View {
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var presenter: MovieContract.Presenter
    private val repository: NaverSearchRepository =
        NaverSearchRepositoryImpl(NaverRemoteDataSourceImpl(), NaverLocalDataSourceImpl())

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = MoviePresenter(this, repository)

        btnSearch.setOnClickListener(btnSearchClickListener())
        movieAdapter = MovieAdapter()
        recyclerView.adapter = movieAdapter

        presenter.checkCacheData()
    }

    // 검색 버튼 클릭 리스너
    private fun btnSearchClickListener(): View.OnClickListener {
        return View.OnClickListener {
            val movieTitle = etQuery.text.toString()
            presenter.clickSearchButton(movieTitle)
        }
    }

    override fun showList(items: ArrayList<MovieInfo>) {
        if (tvEmpty.visibility == View.VISIBLE) {
            tvEmpty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        movieAdapter.resetItem(items)
    }

    override fun showEmptyView() {
        if(recyclerView.visibility == View.VISIBLE) {
            recyclerView.visibility = View.GONE
        }
        tvEmpty.visibility = View.VISIBLE
    }

}
