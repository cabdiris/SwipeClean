package com.example.swipeclean.ui.viewmodel

import android.app.Activity
import com.example.swipeclean.domain.model.Photo
import com.example.swipeclean.domain.usecase.GetPhotosByGroupUseCase
import com.example.swipeclean.domain.usecase.MovePhotoToTrashUseCase
import android.content.IntentSender
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipeclean.domain.usecase.RestorePhotoByUriUseCase
import com.example.swipeclean.utils.AdsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

class PhotoViewModel(
    private val getPhotosByGroup: GetPhotosByGroupUseCase,
    private val moveToTrash: MovePhotoToTrashUseCase,
    private val restorePhoto: RestorePhotoByUriUseCase,
    private val adsManager: AdsManager
) : ViewModel() {

    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> = _photos

    private val _deletedPhotos = MutableStateFlow<List<Photo>>(emptyList())
    val deletedPhotos: StateFlow<List<Photo>> = _deletedPhotos

    private val _swipephoto = MutableStateFlow<List<Pair<Photo, SwipeAction>>>(emptyList())
    val swipephotos: StateFlow<List<Pair<Photo, SwipeAction>>> = _swipephoto

    enum class SwipeAction {
        KEEP, DELETE
    }

    private val _deleteConfirmation = MutableStateFlow<IntentSender?>(null)
    val deleteConfirmation: StateFlow<IntentSender?> = _deleteConfirmation

    fun loadPhotos(activity: Activity,group: String) {
        adsManager.showInterstitial(activity) {
            viewModelScope.launch {
                _photos.value = getPhotosByGroup(group)
            }
        }
    }

    init {
        adsManager.initialize()
        adsManager.loadInterstitial("ca-app-pub-3940256099942544/1033173712") // test
    }
//    fun swipePhoto(photo: Photo, action: SwipeAction) {
//        viewModelScope.launch {
//            _swipephoto.add(photo to action)
//
//            when (action) {
//                SwipeAction.KEEP -> {
//                    _photos.value = _photos.value.filterNot { it.id == photo.id }
//                    Log.d("PhotoViewModel", "KEEP photo: ${photo.displayName}")
//                }
//
//                SwipeAction.DELETE -> {
//                    moveToTrash(photo)
//                    _photos.value = _photos.value.filterNot { it.id == photo.id }
//                    Log.d("PhotoViewModel", "DELETE photo: ${photo.displayName}")
//                }
//            }
//        }
//    }

    fun hasDeletePhoto(): Boolean {
        return _swipephoto.value.any { it.second == SwipeAction.DELETE }
    }


    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun undoLastAction() {
        val currentList = _swipephoto.value
        if (currentList.isEmpty()) return

        val (photo, action) = currentList.last()

        // Remove last action from the list reactively
        _swipephoto.value = currentList.dropLast(1)

        when (action) {
            SwipeAction.KEEP -> {

            }
            SwipeAction.DELETE -> {
                // Restore photo if it was deleted
                viewModelScope.launch {
                    restorePhoto(photo.uri)
                }
            }
        }
    }

    fun swipeLeftDelete(photo: Photo, currentIndex: Int) {
        viewModelScope.launch {
            try {
                val result = moveToTrash(photo)
                if (result.isSuccess) {
                    _swipephoto.value = _swipephoto.value + (photo to SwipeAction.DELETE)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun swipeRightKeep(photo: Photo, currentIndex: Int) {
        viewModelScope.launch {
            // just skip: remove from list but not delete
            _swipephoto.value = _swipephoto.value + (photo to SwipeAction.KEEP)
        }
    }

//    fun undoLastDelete(photo: Photo) {
//        viewModelScope.launch {
//            try {
//                restorePhoto(photo.uri)
//                _deletedPhotos.value = _deletedPhotos.value.filterNot { it.id == photo.id }
//                _photos.value = listOf(photo) + _photos.value
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

    // Called by Activity after the IntentSender-based deletion flow completes (resultCode OK)
    fun onUserConfirmedDeletion() {
        _deleteConfirmation.value = null
    }

    // For UI to clear confirmation intent if canceled
    fun clearDeleteConfirmation() {
        _deleteConfirmation.value = null
    }
}
