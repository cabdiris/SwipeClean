package com.example.swipeclean.domain.repository

import android.content.IntentSender
import android.net.Uri
import com.example.swipeclean.data.local.TrashImage
import com.example.swipeclean.domain.model.Photo
import com.example.swipeclean.domain.model.trashModel
import kotlinx.coroutines.flow.Flow


sealed class DeleteResult {
    object Success : DeleteResult()
    data class RequiresUserConfirmation(val intentSender: IntentSender) : DeleteResult()
    data class Error(val message: String) : DeleteResult()
}

interface PhotoRepository {
    suspend fun loadAllPhotos(): List<Photo>
    suspend fun loadPhotosByGroup(group: String): List<Photo> // group = month or folder
    suspend fun moveToTrash(photo: Photo): Result<Long> // returns inserted trash id
    suspend fun deletePermanently(photoUris: List<Uri>): DeleteResult
    suspend fun restoreFromArchive(trash: List<TrashImage>): Result<Unit>
    suspend fun restoreFromSwipe(uri: Uri): Result<Unit>
    suspend fun getAllTrash(): List<trashModel>
    suspend fun UserConfirmedDeletion(photoUris: List<Uri>)
}