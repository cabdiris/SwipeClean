package com.example.swipeclean.domain.usecase

import com.example.swipeclean.data.local.TrashImage
import com.example.swipeclean.domain.repository.PhotoRepository

class RestorePhotoUseCase(private val repo: PhotoRepository) {
    suspend operator fun invoke(trash: List<TrashImage>) = repo.restoreFromArchive(trash)
}