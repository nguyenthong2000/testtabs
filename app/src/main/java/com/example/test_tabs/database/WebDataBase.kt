package com.example.test_tabs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.test_tabs.models.Tabs

@Database(entities = [Tabs::class], version = 1)
abstract class WebDataBase: RoomDatabase() {
    abstract fun webDAO():WebDAO
    companion object{
        fun getInstance(context: Context): WebDataBase{
            return Room.databaseBuilder(context,WebDataBase::class.java,"web.db").allowMainThreadQueries().build()
        }
    }
}