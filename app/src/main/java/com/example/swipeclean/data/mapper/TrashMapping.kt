package com.example.swipeclean.data.mapper

import com.example.swipeclean.data.local.TrashImage
import com.example.swipeclean.domain.model.trashModel

fun TrashImage.toDomain(): trashModel {
    return trashModel(
        id = id,
        originalUri = originalUri,
        storedFileName = storedFileName,
        deletedAt = deletedAt
    )
}