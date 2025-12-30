package com.example.swipeclean.data.repository


import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.database.getStringOrNull
import com.example.swipeclean.data.local.AppDatabase
import com.example.swipeclean.data.local.TrashImage
import com.example.swipeclean.data.local.doa.TrashDao
import com.example.swipeclean.data.mapper.toDomain
import com.example.swipeclean.domain.model.Photo
import com.example.swipeclean.domain.model.trashModel
import com.example.swipeclean.domain.repository.DeleteResult
import com.example.swipeclean.domain.repository.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhotoRepositoryImpl(
    private val context: Context,
    private val db: AppDatabase
) : PhotoRepository {

    override suspend fun loadAllPhotos(): List<Photo> = withContext(Dispatchers.IO) {
        val result = mutableListOf<Photo>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val query = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, sortOrder
        )

        query?.use { cursor ->
            val idIdx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameIdx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateIdx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
            val bucketIdx =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idIdx)
                val uri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                val name = cursor.getStringOrNull(nameIdx)
                val dateSeconds = cursor.getLong(dateIdx)
                val dateMillis = dateSeconds * 1000L // ✅ always convert to milliseconds
                val bucket = cursor.getStringOrNull(bucketIdx)

                result.add(Photo(id, uri, name, dateMillis, bucket))
            }
        }
        val trash = db.trashDao().getAll()
        val filter =result.filter { photo ->
            trash.none { it.originalUri == photo.uri.toString() }
        }.toMutableList()
        filter
    }


    override suspend fun loadPhotosByGroup(group: String): List<Photo> =
        withContext(Dispatchers.IO) {
            val allPhotos = loadAllPhotos()
            if (allPhotos.isEmpty()) return@withContext emptyList()

            // Trim and normalize input
            val normalizedGroup = group.trim().lowercase(Locale.getDefault())

            val dateFormatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

            // 🟢 Try date group first
            val dateMatched = allPhotos.filter { photo ->
                val formatted = dateFormatter.format(Date(photo.dateAdded))
                    .lowercase(Locale.getDefault())
                formatted == normalizedGroup
            }

            if (dateMatched.isNotEmpty()) {
                return@withContext dateMatched
            }

            // 🟡 Fallback: match by album/bucket name
            val albumMatched = allPhotos.filter { photo ->
                photo.bucketName?.lowercase(Locale.getDefault()) == normalizedGroup
            }

            return@withContext albumMatched
        }

    override suspend fun moveToTrash(photo: Photo): Result<Long> = withContext(Dispatchers.IO) {
        try {
            val trashId = db.trashDao().insert(
                TrashImage(
                    originalUri = photo.uri.toString(),
                    storedFileName = null,
                    deletedAt = System.currentTimeMillis()
                )
            )
            Result.success(trashId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun UserConfirmedDeletion(photoUris: List<Uri>) {
        db.trashDao().deleteByUris(photoUris.map { it.toString() })
    }

    override suspend fun deletePermanently(photoUris: List<Uri>): DeleteResult = withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val intentSender = MediaStore.createDeleteRequest(context.contentResolver, photoUris).intentSender
                DeleteResult.RequiresUserConfirmation(intentSender)
            } else {
                val allDeleted = photoUris.all { uri ->
                    context.contentResolver.delete(uri, null, null) > 0
                }

                if (allDeleted) {
                    db.trashDao().deleteByUris(photoUris.map { it.toString() })
                    DeleteResult.Success
                } else {
                    DeleteResult.Error("Some deletions failed")
                }
            }
        } catch (e: Exception) {
            DeleteResult.Error(e.localizedMessage ?: "Unknown error")
        }
    }


    override suspend fun restoreFromArchive(trash: List<TrashImage>): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            trash.forEach {
                db.trashDao().deleteById(it.id)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun restoreFromSwipe(uri: Uri): Result<Unit> = withContext(Dispatchers.IO){
        try {
            db.trashDao().deleteByUri(uri.toString())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllTrash(): List<trashModel> = withContext(Dispatchers.IO) {
        db.trashDao()
            .getAll()
    }


}
