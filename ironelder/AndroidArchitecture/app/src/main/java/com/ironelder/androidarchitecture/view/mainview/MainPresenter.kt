package com.ironelder.androidarchitecture.view.mainview

import android.util.Log
import com.google.gson.Gson
import com.ironelder.androidarchitecture.data.ResultItem
import com.ironelder.androidarchitecture.data.SearchResult
import com.ironelder.androidarchitecture.data.TotalModel
import com.ironelder.androidarchitecture.data.dao.SearchResultDao
import com.ironelder.androidarchitecture.data.repository.remote.SearchRemoteDataRepositoryImpl
import com.ironelder.androidarchitecture.view.baseview.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainPresenter : BasePresenter<MainContract.View>(), MainContract.Presenter {

    private val LOG_TAG = MainPresenter::class.java.toString()

    override fun searchWithAdapter(type: String, query: String?) {
        SearchRemoteDataRepositoryImpl.getDataForSearch(type, query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.showLoading()
            }
            .doAfterTerminate {
                view.hideLoading()
            }
            .subscribe(::onSuccess, ::onError)
            .addDisposable()
    }

    private fun onSuccess(result: TotalModel) {
        if (result.items.isNullOrEmpty()) {
            view.showNoSearchData()
        } else {
            view.onDataChanged(result.items)
        }
    }

    private fun onError(t: Throwable) {
        view.showErrorMessage(t.message)
    }

    override fun setSearchResultToRoom(
        searchResultDao: SearchResultDao?,
        type: String,
        searchWord: String,
        items: ArrayList<ResultItem>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            searchResultDao?.insertSearchResult(
                SearchResult(
                    type,
                    searchWord,
                    Gson().toJson(items)
                )
            )
        }
    }

    override fun getSearchResultToRoom(searchResultDao: SearchResultDao?, type: String) {
        searchResultDao?.getSearchResult(type)?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())?.subscribe({
                view.onLoadFromDatabase(
                    it.search,
                    Gson().fromJson(
                        it.result,
                        Array<ResultItem>::class.java
                    ).toCollection(ArrayList())
                )
            }, { t: Throwable? -> Log.w(LOG_TAG, t?.message ?: "DataBase $type Not Save Data") })
            ?.addDisposable()
    }

}