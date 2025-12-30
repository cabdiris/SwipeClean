package com.example.swipeclean.domain.usecase



import com.example.swipeclean.domain.model.PhotoGroup
import com.example.swipeclean.domain.repository.PhotoRepository
import java.text.SimpleDateFormat
import java.util.*

class GetPhotoAlbumsUseCase(private val repo: PhotoRepository) {
    suspend operator fun invoke(): List<PhotoGroup> {
        val photos = repo.loadAllPhotos()

        return photos
            .groupBy { it.bucketName ?: "Unknown" }
            .map { (album, items) -> PhotoGroup(title = album, photos = items) }
            .sortedByDescending { it.photos.size }

    }

}