package com.example.swipeclean.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trash_table")
data class TrashImage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val originalUri: String,
    val storedFileName: String?, // filename inside app internal trash dir (optional)
    val deletedAt: Long // epoch millis
)