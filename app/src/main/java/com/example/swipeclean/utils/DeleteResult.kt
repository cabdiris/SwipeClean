package com.example.swipeclean.utils

import android.content.IntentSender

sealed class DeleteResult {
    data class RequiresUserConfirmation(val intentSender: IntentSender) : DeleteResult()
    data object Success : DeleteResult()
    data class Error(val message: String) : DeleteResult()
}