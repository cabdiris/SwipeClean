package com.example.swipeclean.domain.model

data class trashModel(
    val id: Long,
    val originalUri: String,
    val storedFileName: String?,
    val deletedAt: Long
)