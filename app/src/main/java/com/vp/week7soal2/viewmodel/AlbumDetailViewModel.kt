package com.vp.week7soal2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vp.week7soal2.data.remote.MusicServiceLocator
import com.vp.week7soal2.data.repository.MusicRepository
import com.vp.week7soal2.model.DataDetailDanTrackAlbum
import com.vp.week7soal2.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel untuk mengatur state di layar detail album
class AlbumDetailViewModel(
    private val repo: MusicRepository = MusicServiceLocator.repositoryMusik
) : ViewModel() {

    // Status UI detail album: Loading, Success, atau Error
    private val _statusUiDetail = MutableStateFlow<Resource<DataDetailDanTrackAlbum>>(Resource.Loading)
    val statusUiDetail: StateFlow<Resource<DataDetailDanTrackAlbum>> = _statusUiDetail.asStateFlow()

    // Status apakah teks deskripsi album lagi dibuka penuh atau ringkas
    private val _apakahDeskripsiDibuka = MutableStateFlow(false)
    val apakahDeskripsiDibuka: StateFlow<Boolean> = _apakahDeskripsiDibuka.asStateFlow()

    // Fungsi memuat data album dan lagunya berdasarkan ID
    fun muatDetailAlbum(idAlbum: String) {
        viewModelScope.launch {
            _apakahDeskripsiDibuka.value = false
            _statusUiDetail.value = Resource.Loading
            val hasil = repo.ambilDetailDanLaguAlbum(idAlbum)
            _statusUiDetail.value = hasil
        }
    }

    // Fungsi toggle buka/tutup deskripsi
    fun toggleBukaDeskripsi() {
        _apakahDeskripsiDibuka.value = !_apakahDeskripsiDibuka.value
    }
}
