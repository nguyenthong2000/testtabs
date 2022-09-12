package com.example.test_tabs.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tabs (
    @PrimaryKey val id: Long,
    var imagePath: String,
    var url: String,
    var visibility: Boolean
): java.io.Serializable