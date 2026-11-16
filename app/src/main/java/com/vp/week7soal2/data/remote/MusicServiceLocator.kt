package com.vp.week7soal2.data.remote

import com.vp.week7soal2.data.repository.MusicRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

// Tempat inisialisasi Retrofit dan Repository (Service Locator sederhana)
object MusicServiceLocator {

    // API key sesuai instruksi soal: 123
    private const val API_KEY = "123"
    private const val BASE_URL = "https://www.theaudiodb.com/api/v1/json/$API_KEY/"

    // Format JSON agar fleksibel dan tidak crash bila ada data kosong / beda field
    private val konverterJson = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    // Client HTTP untuk logging request/response
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    // Bangun instance Retrofit
    private val instanceRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(konverterJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    // Service API Retrofit
    val serviceApi: AudioDbApi by lazy {
        instanceRetrofit.create(AudioDbApi::class.java)
    }

    // Repository yang dipakai ViewModel
    val repositoryMusik: MusicRepository by lazy {
        MusicRepository(serviceApi)
    }
}
