package com.vp.week7soal2.data.repository

import com.vp.week7soal2.data.dto.AlbumDto
import com.vp.week7soal2.data.dto.ArtistDto
import com.vp.week7soal2.data.dto.TrackDto
import com.vp.week7soal2.model.DataAlbum
import com.vp.week7soal2.model.DataArtis
import com.vp.week7soal2.model.DataLagu
import java.util.Locale
import java.util.UUID

// Mengubah ArtistDto dari API jadi DataArtis yang siap dipakai di UI
fun ArtistDto.keDataArtis(): DataArtis {
    return DataArtis(
        idArtis = idArtist ?: UUID.randomUUID().toString(),
        namaArtis = strArtist ?: "Artis Tidak Diketahui",
        genreArtis = strGenre?.takeIf { it.isNotBlank() } ?: "Music",
        lokasiArtis = strCountry?.takeIf { it.isNotBlank() } ?: "-",
        biografiArtis = strBiography?.takeIf { it.isNotBlank() }
            ?: strBiographyEN?.takeIf { it.isNotBlank() }
            ?: "Belum ada informasi biografi untuk artis ini.",
        fotoBannerUrl = strArtistFanart?.takeIf { it.isNotBlank() }
            ?: strArtistBanner?.takeIf { it.isNotBlank() }
            ?: strArtistThumb ?: "",
        fotoProfilUrl = strArtistThumb?.takeIf { it.isNotBlank() }
            ?: strArtistFanart ?: ""
    )
}

// Mengubah AlbumDto dari API jadi DataAlbum
fun AlbumDto.keDataAlbum(): DataAlbum {
    return DataAlbum(
        idAlbum = idAlbum ?: UUID.randomUUID().toString(),
        judulAlbum = strAlbum ?: "Tanpa Judul",
        namaArtis = strArtist ?: "Artis Tidak Diketahui",
        tahunRilis = intYearReleased?.takeIf { it.isNotBlank() } ?: "-",
        genreAlbum = strGenre?.takeIf { it.isNotBlank() } ?: "Pop-Rock",
        coverAlbumUrl = strAlbumThumb ?: "",
        deskripsiAlbum = strDescription?.takeIf { it.isNotBlank() }
            ?: strDescriptionEN?.takeIf { it.isNotBlank() }
            ?: "Deskripsi untuk album ini belum tersedia."
    )
}

// Mengubah TrackDto dari API jadi DataLagu dengan format durasi dan nomor
fun TrackDto.keDataLagu(indeks: Int): DataLagu {
    // Nomor urut lagu (misal 1 jadi 01)
    val angkaTrack = intTrackNumber?.toIntOrNull() ?: (indeks + 1)
    val teksNomor = String.format(Locale.US, "%02d", angkaTrack)

    // Format durasi milidetik jadi menit:detik
    val durasiMili = intDuration?.toLongOrNull() ?: 0L
    val totalDetik = durasiMili / 1000
    val menit = totalDetik / 60
    val sisaDetik = totalDetik % 60
    val teksDurasi = if (durasiMili > 0) {
        String.format(Locale.US, "%02d:%02d", menit, sisaDetik)
    } else {
        "--:--"
    }

    return DataLagu(
        idLagu = idTrack ?: UUID.randomUUID().toString(),
        nomorUrut = teksNomor,
        judulLagu = strTrack ?: "Tanpa Judul Lagu",
        genreLagu = strGenre?.takeIf { it.isNotBlank() } ?: "Pop-Rock",
        durasiFormatted = teksDurasi
    )
}
