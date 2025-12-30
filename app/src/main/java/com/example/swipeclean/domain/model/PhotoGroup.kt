package com.example.swipeclean.domain.model

data class PhotoGroup(
    val title: String, // e.g. "October 2025" or "Screenshots"
    val photos: List<Photo>,
    val count: Int = photos.size
)