package com.example.swipeclean.domain.usecase

import android.net.Uri
import com.example.swipeclean.domain.repository.DeleteResult
import com.example.swipeclean.domain.repository.PhotoRepository

class DeletePhotoPermanentlyUseCase(private val repo: PhotoRepository) {
    suspend operator fun invoke(photoUris: List<Uri>): DeleteResult = repo.deletePermanently(photoUris)
    suspend fun onUserConfirmedDeletion(photoUris: List<Uri>) = repo.UserConfirmedDeletion(photoUris)
}