package com.example.swipeclean.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.swipeclean.data.local.doa.TrashDao

@Database(entities = [TrashImage::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trashDao(): TrashDao
}