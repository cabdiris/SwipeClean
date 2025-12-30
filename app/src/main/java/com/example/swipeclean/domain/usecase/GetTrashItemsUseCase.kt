package com.example.swipeclean.domain.usecase

import com.example.swipeclean.data.local.TrashImage
import com.example.swipeclean.domain.model.trashModel
import com.example.swipeclean.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow

class GetTrashItemsUseCase(private val repo: PhotoRepository) {
    suspend operator fun invoke(): List<trashModel> = repo.getAllTrash()
}