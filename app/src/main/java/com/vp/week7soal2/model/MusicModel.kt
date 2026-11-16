package com.vp.week7soal2.model

// Model data info artis untuk ditampilkan di layar
data class DataArtis(
    val idArtis: String,
    val namaArtis: String,
    val genreArtis: String,
    val lokasiArtis: String,
    val biografiArtis: String,
    val fotoBannerUrl: String,
    val fotoProfilUrl: String
)

// Model data album artis
data class DataAlbum(
    val idAlbum: String,
    val judulAlbum: String,
    val namaArtis: String,
    val tahunRilis: String,
    val genreAlbum: String,
    val coverAlbumUrl: String,
    val deskripsiAlbum: String
)

// Model data lagu dalam album
data class DataLagu(
    val idLagu: String,
    val nomorUrut: String,
    val judulLagu: String,
    val genreLagu: String,
    val durasiFormatted: String
)

// Model gabungan untuk halaman utama profil artis dan diskografinya
data class DataDashboardArtis(
    val artis: DataArtis,
    val daftarAlbum: List<DataAlbum>
)

// Model gabungan untuk halaman detail album dan daftar lagunya
data class DataDetailDanTrackAlbum(
    val album: DataAlbum,
    val daftarLagu: List<DataLagu>
)
