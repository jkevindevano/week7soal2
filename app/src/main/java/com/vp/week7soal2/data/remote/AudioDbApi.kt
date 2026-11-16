package com.vp.week7soal2.data.remote

import com.vp.week7soal2.data.dto.AlbumResponseDto
import com.vp.week7soal2.data.dto.ArtistResponseDto
import com.vp.week7soal2.data.dto.TrackResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

// Interface Retrofit untuk manggil endpoint TheAudioDB
interface AudioDbApi {

    // 1. Endpoint cari info artis berdasarkan nama
    @GET("search.php")
    suspend fun cariArtis(
        @Query("s") namaArtis: String
    ): ArtistResponseDto

    // 2. Endpoint cari semua album artis berdasarkan nama
    @GET("searchalbum.php")
    suspend fun cariAlbumArtis(
        @Query("s") namaArtis: String
    ): AlbumResponseDto

    // 3. Endpoint ambil detail lengkap album berdasarkan id album
    @GET("album.php")
    suspend fun ambilDetailAlbum(
        @Query("m") idAlbum: String
    ): AlbumResponseDto

    // 4. Endpoint ambil daftar lagu di dalam album berdasarkan id album
    @GET("track.php")
    suspend fun ambilDaftarLaguAlbum(
        @Query("m") idAlbum: String
    ): TrackResponseDto
}
