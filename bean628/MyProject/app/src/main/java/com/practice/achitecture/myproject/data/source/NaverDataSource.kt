package com.practice.achitecture.myproject.data.source

import com.practice.achitecture.myproject.enums.SearchType
import com.practice.achitecture.myproject.model.SearchedItem

interface NaverDataSource {

    interface GettingResultOfSearchingCallback {
        fun onSuccess(items: List<SearchedItem>)
        fun onFailure(throwable: Throwable)
    }

    interface LoadHistoryOfSearchCallback {
        fun onLoadSuccess(items: List<SearchedItem>)
        fun onEmptyData()
    }

    fun searchWordByNaver(
        searchType: SearchType,
        word: String,
        callback: GettingResultOfSearchingCallback
    )

    fun loadHistoryOfSearch(
        searchType: SearchType,
        callback: LoadHistoryOfSearchCallback
    )

    fun getLastSearchType(): SearchType

    fun getCache(searchType: SearchType): String

    fun setCache(searchType: SearchType, word: String, list: List<SearchedItem>)

    fun saveSearchedListInRoom(searchType: SearchType, word: String, list: List<SearchedItem>)

}