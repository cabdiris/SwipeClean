package com.example.swipeclean.domain.usecase

import android.net.Uri
import com.example.swipeclean.domain.repository.PhotoRepository

class RestorePhotoByUriUseCase (private val repo: PhotoRepository) {
    suspend operator fun invoke(uri: Uri) = repo.restoreFromSwipe(uri)
}