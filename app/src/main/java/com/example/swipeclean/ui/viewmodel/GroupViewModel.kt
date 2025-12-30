package com.example.swipeclean.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipeclean.domain.model.PhotoGroup
import com.example.swipeclean.domain.usecase.GetPhotoAlbumsUseCase
import com.example.swipeclean.domain.usecase.GetPhotoGroupsUseCase
import com.example.swipeclean.utils.AdsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GroupViewModel(
    private val getPhotoGroups: GetPhotoGroupsUseCase,
    private val getAlbums: GetPhotoAlbumsUseCase,
    private val adsManager: AdsManager
) : ViewModel() {
    private val _groups = MutableStateFlow<List<PhotoGroup>>(emptyList())
    val groups: StateFlow<List<PhotoGroup>> = _groups

    private val _albums = MutableStateFlow<List<PhotoGroup>>(emptyList())
    val albums: StateFlow<List<PhotoGroup>> = _albums

    private val _permissionsGranted = MutableStateFlow(false)
    val permissionsGranted: StateFlow<Boolean> = _permissionsGranted

    fun setPermissionsGranted(granted: Boolean) {
        _permissionsGranted.value = granted
        if (granted) {
            loadGroups()
            loadAlbums()
        }
    }


    fun loadGroups() {
        viewModelScope.launch {
            _groups.value = getPhotoGroups()
        }
    }
    fun loadAlbums() {
        viewModelScope.launch {
            _albums.value = getAlbums()
        }
    }
    fun bannerAd() = adsManager.getBanner()
}