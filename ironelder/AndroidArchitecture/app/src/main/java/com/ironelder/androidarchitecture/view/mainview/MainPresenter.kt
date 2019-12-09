package com.ironelder.androidarchitecture.view.mainview

import com.ironelder.androidarchitecture.data.TotalModel
import com.ironelder.androidarchitecture.data.repository.remote.SearchRemoteDataRepositoryImpl
import com.ironelder.androidarchitecture.view.baseview.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPresenter : BasePresenter<MainContract.View>(), MainContract.Presenter {

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
}