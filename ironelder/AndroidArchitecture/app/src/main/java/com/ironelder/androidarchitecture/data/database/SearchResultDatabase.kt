package com.ironelder.androidarchitecture.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ironelder.androidarchitecture.data.SearchResult
import com.ironelder.androidarchitecture.data.dao.SearchResultDao

@Database(entities = [(SearchResult::class)], version = 1)
abstract class SearchResultDatabase : RoomDatabase() {

    abstract fun searchResultDao(): SearchResultDao

    companion object {
        @Volatile
        private var INSTANCE: SearchResultDatabase? = null

        fun getInstance(context: Context): SearchResultDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SearchResultDatabase::class.java, "searchData.db"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
    }

}