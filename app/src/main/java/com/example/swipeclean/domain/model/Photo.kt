package com.example.swipeclean.domain.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class Photo(
    val id: Long,
    val uri: Uri,
    val displayName: String?,
    val dateAdded: Long,
    val bucketName: String? // folder/album
)