package com.example.swipeclean.domain.usecase

import com.example.swipeclean.domain.model.Photo
import com.example.swipeclean.domain.repository.PhotoRepository

class MovePhotoToTrashUseCase(private val repo: PhotoRepository) {
    suspend operator fun invoke(photo: Photo) = repo.moveToTrash(photo)
}