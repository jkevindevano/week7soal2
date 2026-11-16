package com.vp.week7soal2.util

// Kelas pembungkus status data (bisa lagi loading, sukses bawa data, atau error bawa pesan)
sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
}
