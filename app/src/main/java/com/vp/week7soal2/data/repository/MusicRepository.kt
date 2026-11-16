package com.vp.week7soal2.data.repository

import com.vp.week7soal2.data.remote.AudioDbApi
import com.vp.week7soal2.model.DataDashboardArtis
import com.vp.week7soal2.model.DataDetailDanTrackAlbum
import com.vp.week7soal2.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import java.io.IOException

// Repository untuk ngambil data musik dari API dan ngolah ke format model yang siap pakai
class MusicRepository(private val apiService: AudioDbApi) {

    // Ambil profil artis sekaligus daftar albumnya secara asynchronous pakai Coroutine
    suspend fun ambilDataArtisDanAlbum(namaArtis: String): Resource<DataDashboardArtis> {
        return try {
            coroutineScope {
                // Panggil API artis dan API album barengan
                val tugasCariArtis = async { apiService.cariArtis(namaArtis) }
                val tugasCariAlbum = async { apiService.cariAlbumArtis(namaArtis) }

                val hasilArtis = tugasCariArtis.await()
                val hasilAlbum = tugasCariAlbum.await()

                // Ambil artis pertama
                val artisDitemukan = hasilArtis.artists?.firstOrNull()
                    ?: return@coroutineScope Resource.Error("Artis \"$namaArtis\" tidak ditemukan. Coba cari artis lain.")

                // Ambil daftar album dan urutkan dari tahun terbaru
                val listAlbumBersih = hasilAlbum.album
                    ?.map { it.keDataAlbum() }
                    ?.sortedByDescending { it.tahunRilis }
                    ?: emptyList()

                Resource.Success(
                    DataDashboardArtis(
                        artis = artisDitemukan.keDataArtis(),
                        daftarAlbum = listAlbumBersih
                    )
                )
            }
        } catch (e: HttpException) {
            Resource.Error("Gagal mengambil data dari server (Kode HTTP: ${e.code()})")
        } catch (e: IOException) {
            Resource.Error("Gagal terhubung ke internet. Periksa koneksi data atau Wi-Fi kamu.")
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak diketahui.")
        }
    }

    // Ambil detail album dan daftar lagunya sekaligus
    suspend fun ambilDetailDanLaguAlbum(idAlbum: String): Resource<DataDetailDanTrackAlbum> {
        return try {
            coroutineScope {
                val tugasDetailAlbum = async { apiService.ambilDetailAlbum(idAlbum) }
                val tugasDaftarLagu = async { apiService.ambilDaftarLaguAlbum(idAlbum) }

                val hasilDetail = tugasDetailAlbum.await()
                val hasilLagu = tugasDaftarLagu.await()

                val albumDitemukan = hasilDetail.album?.firstOrNull()
                    ?: return@coroutineScope Resource.Error("Detail album tidak ditemukan.")

                val listLaguBersih = hasilLagu.track
                    ?.mapIndexed { indeks, trackDto -> trackDto.keDataLagu(indeks) }
                    ?: emptyList()

                Resource.Success(
                    DataDetailDanTrackAlbum(
                        album = albumDitemukan.keDataAlbum(),
                        daftarLagu = listLaguBersih
                    )
                )
            }
        } catch (e: HttpException) {
            Resource.Error("Gagal memuat detail album (Kode HTTP: ${e.code()})")
        } catch (e: IOException) {
            Resource.Error("Gagal terhubung ke internet. Periksa koneksi kamu.")
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan saat memuat album.")
        }
    }
}
