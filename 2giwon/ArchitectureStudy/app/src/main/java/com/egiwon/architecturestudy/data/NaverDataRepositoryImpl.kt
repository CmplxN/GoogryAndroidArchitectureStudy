package com.egiwon.architecturestudy.data

import com.egiwon.architecturestudy.data.source.NaverDataSource
import com.egiwon.architecturestudy.data.source.remote.response.ContentResponse
import io.reactivex.Single

class NaverDataRepositoryImpl(
    private val naverRemoteDataSource: NaverDataSource.Remote,
    private val naverLocalDataSource: NaverDataSource.Local
) : NaverDataRepository {

    private fun loadRemoteContents(type: String, query: String): Single<ContentResponse> =
        naverRemoteDataSource.getContents(type, query)
            .doOnSuccess { response ->
                naverLocalDataSource.saveContents(
                    type,
                    query,
                    response
                ).toSingle { response }
            }

    override fun getContents(type: String, query: String): Single<ContentResponse> =
        loadRemoteContents(type, query)

    override fun getContentsByHistory(type: String, query: String): Single<ContentResponse> =
        naverLocalDataSource.getContents(type, query)

    override fun getContentQuerys(type: String): Single<List<String>> =
        naverLocalDataSource.getContentQuerys(type)


    override fun getCache(type: String): Single<ContentResponse> =
        naverLocalDataSource.getCacheContents(type)


    companion object {
        private var instance: NaverDataRepositoryImpl? = null

        fun getInstance(
            naverDataSource: NaverDataSource.Remote,
            naverLocalDataSource: NaverDataSource.Local
        ): NaverDataRepositoryImpl =
            instance
                ?: NaverDataRepositoryImpl(
                    naverDataSource,
                    naverLocalDataSource
                ).apply {
                    instance = this
                }
    }
}