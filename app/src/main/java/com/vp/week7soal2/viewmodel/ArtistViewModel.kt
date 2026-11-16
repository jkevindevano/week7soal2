package com.vp.week7soal2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vp.week7soal2.data.remote.MusicServiceLocator
import com.vp.week7soal2.data.repository.MusicRepository
import com.vp.week7soal2.model.DataDashboardArtis
import com.vp.week7soal2.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel untuk mengatur state dan logika di halaman profil artis
class ArtistViewModel(
    private val repo: MusicRepository = MusicServiceLocator.repositoryMusik
) : ViewModel() {

    // Teks yang lagi diketik di kotak pencarian (default awal Coldplay sesuai gambar)
    private val _teksPencarian = MutableStateFlow("Coldplay")
    val teksPencarian: StateFlow<String> = _teksPencarian.asStateFlow()

    // Status UI: Loading, Success bawa DataDashboardArtis, atau Error
    private val _statusUi = MutableStateFlow<Resource<DataDashboardArtis>>(Resource.Loading)
    val statusUi: StateFlow<Resource<DataDashboardArtis>> = _statusUi.asStateFlow()

    // Status apakah biografi artis lagi dibuka penuh atau diringkas
    private val _apakahBioDibuka = MutableStateFlow(false)
    val apakahBioDibuka: StateFlow<Boolean> = _apakahBioDibuka.asStateFlow()

    init {
        // Otomatis cari Coldplay pas aplikasi pertama kali dibuka
        cariDataArtis("Coldplay")
    }

    // Fungsi dipanggil pas user ngetik di search bar
    fun onUbahTeksPencarian(teksBaru: String) {
        _teksPencarian.value = teksBaru
    }

    // Fungsi buka atau tutup teks biografi artis
    fun toggleBukaBio() {
        _apakahBioDibuka.value = !_apakahBioDibuka.value
    }

    // Fungsi cari artis pakai Coroutine secara asynchronous
    fun cariDataArtis(namaYangDicari: String = _teksPencarian.value) {
        if (namaYangDicari.isBlank()) return

        viewModelScope.launch {
            // Reset status bio jadi tertutup setiap kali cari baru
            _apakahBioDibuka.value = false
            _statusUi.value = Resource.Loading

            // Panggil repository untuk ambil data
            val hasil = repo.ambilDataArtisDanAlbum(namaYangDicari.trim())
            _statusUi.value = hasil
        }
    }
}
