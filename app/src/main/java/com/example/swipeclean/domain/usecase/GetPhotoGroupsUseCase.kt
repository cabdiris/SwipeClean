package com.example.swipeclean.domain.usecase


import com.example.swipeclean.domain.model.PhotoGroup
import com.example.swipeclean.domain.repository.PhotoRepository
import java.text.SimpleDateFormat
import java.util.*

class GetPhotoGroupsUseCase(private val repo: PhotoRepository) {
    suspend operator fun invoke(): List<PhotoGroup> {
        val photos = repo.loadAllPhotos()
        // group by month-year
        val formatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        return photos.groupBy { formatter.format(Date(it.dateAdded)) }
            .map { (title, list) -> PhotoGroup(title = title, photos = list) }
            .sortedByDescending { it.photos.firstOrNull()?.dateAdded ?: 0L }
    }

}