package com.example.architecture.data.repository

import android.content.Context
import com.example.architecture.data.model.MovieModel
import com.example.architecture.data.source.local.NaverLocalDataSourceImpl
import com.example.architecture.data.source.remote.NaverRemoteDataSourceImpl

class NaverRepositoryImpl(context: Context) : NaverRepository {

    private val naverRemoteDataSourceImpl = NaverRemoteDataSourceImpl()
    private val naverLocalDataSourceImpl = NaverLocalDataSourceImpl(context)

    override fun getMovieList(
        keyword: String,
        onSuccess: (movieList: List<MovieModel>) -> Unit,
        onFailure: (t: Throwable) -> Unit
    ) {

        naverLocalDataSourceImpl.getMovieList(keyword, onSuccess)

        naverRemoteDataSourceImpl.getMovieList(
            keyword,
            onSuccess,
            onFailure,
            this::saveMovieList
        )
    }


    override fun clearCacheData() {
        naverLocalDataSourceImpl.clearData()
    }

    private fun saveMovieList(
        keyword: String,
        movieList: List<MovieModel>
    ) {
        naverLocalDataSourceImpl.saveMovieList(keyword, movieList)
    }

}