package com.example.swipeclean.data.local.doa

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.swipeclean.data.local.TrashImage
import com.example.swipeclean.domain.model.trashModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TrashDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trash: TrashImage): Long

    @Query("SELECT * FROM trash_table ORDER BY deletedAt DESC")
    suspend fun getAll(): List<trashModel>

    @Query("DELETE FROM trash_table WHERE deletedAt < :expiry")
    suspend fun deleteExpired(expiry: Long)

    @Query("DELETE FROM trash_table WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM trash_table WHERE deletedAt < :expiry")
    suspend fun getExpiredPhotos(expiry: Long): List<TrashImage>

    @Query("DELETE FROM trash_table WHERE originalUri = :uri")
    suspend fun deleteByUri(uri: String)

    @Query("DELETE FROM trash_table WHERE originalUri IN (:uris)")
    suspend fun deleteByUris(uris: List<String>)


}

