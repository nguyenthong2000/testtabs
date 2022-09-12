package com.example.test_tabs.database

import androidx.room.*
import com.example.test_tabs.models.Tabs

@Dao
interface WebDAO {
    @Insert
    fun insertTab(tab: Tabs)

    @Delete
    fun deleteTab(tab: Tabs)

    @Query("SELECT * FROM tabs")
    fun selectAllTabs(): List<Tabs>

    @Update
    fun updateTab(tab: Tabs)
}