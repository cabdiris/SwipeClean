package com.example.swipeclean.domain.usecase

import com.example.swipeclean.domain.model.Photo
import com.example.swipeclean.domain.repository.PhotoRepository

class GetPhotosByGroupUseCase(private val repo: PhotoRepository) {
    suspend operator fun invoke(group: String): List<Photo> = repo.loadPhotosByGroup(group)
}