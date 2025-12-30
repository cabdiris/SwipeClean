package com.example.swipeclean.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipeclean.data.local.TrashImage
import com.example.swipeclean.domain.model.trashModel
import com.example.swipeclean.domain.repository.DeleteResult
import com.example.swipeclean.domain.usecase.DeletePhotoPermanentlyUseCase
import com.example.swipeclean.domain.usecase.GetTrashItemsUseCase
import com.example.swipeclean.domain.usecase.RestorePhotoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArchiveViewModel(
    private val getTrashItems: GetTrashItemsUseCase,
    private val restore: RestorePhotoUseCase,
    private val deletePermanently: DeletePhotoPermanentlyUseCase
) : ViewModel() {
    private val _trash = MutableStateFlow<List<trashModel>>(emptyList())
    val trash: StateFlow<List<trashModel>> = _trash

    private val _trashSelected = MutableStateFlow<List<trashModel>>(emptyList())
    val trashSelected: StateFlow<List<trashModel>> = _trashSelected
    fun loadTrash() {
        viewModelScope.launch {
            _trash.value = getTrashItems()
        }
    }

    fun toggleSelection(item: trashModel) {
        _trashSelected.value = if (_trashSelected.value.contains(item)) {
            _trashSelected.value - item
        } else {
            _trashSelected.value + item
        }
    }

    fun clearSelection() {
        _trashSelected.value = emptyList()
    }

    fun restoreItem(trashImage: List<TrashImage>) {
        viewModelScope.launch {
            val res = restore(trashImage)
            if (res.isSuccess) loadTrash()
            clearSelection()
        }
    }

//    fun deletePermanentlyFromTrash(trashImage: List<TrashImage>, onResult: (DeleteResult) -> Unit) {
//        viewModelScope.launch {
//            val uris = trashImage.map { Uri.parse(it.originalUri) }
//            val result = deletePermanently(uris)
//            onResult(result)
//        }
//    }

    fun deleteSelected(
        onRequiresConfirmation: (DeleteResult.RequiresUserConfirmation) -> Unit,
        onCompleted: () -> Unit,
        onError: (String) -> Unit,
        selected: List<trashModel>
    ) {
        val images = selected.map { TrashImage(it.id, it.originalUri, null, it.deletedAt) }
        viewModelScope.launch {
            val uris = images.map { Uri.parse(it.originalUri) }
            when (val result = deletePermanently(uris)) {
                is DeleteResult.RequiresUserConfirmation -> onRequiresConfirmation(result)
                is DeleteResult.Success -> {
                    onCompleted()
                    loadTrash()
                    clearSelection()
                }
                is DeleteResult.Error -> onError(result.message)
            }
        }
    }

    /** Call this after Android 11+ user confirms deletion */
    fun confirmDeletionForAndroid11Plus(selected: List<trashModel>) {
        val uris = selected.map { Uri.parse(it.originalUri) }
        viewModelScope.launch {
            deletePermanently.onUserConfirmedDeletion(uris)
            loadTrash()
            clearSelection()
        }
    }
}
