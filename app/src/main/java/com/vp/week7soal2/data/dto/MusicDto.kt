package com.vp.week7soal2.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// DTO untuk response pencarian artis
@Serializable
data class ArtistResponseDto(
    @SerialName("artists")
    val artists: List<ArtistDto>? = null
)

// Data mentah dari API artis
@Serializable
data class ArtistDto(
    @SerialName("idArtist")
    val idArtist: String? = null,
    @SerialName("strArtist")
    val strArtist: String? = null,
    @SerialName("strGenre")
    val strGenre: String? = null,
    @SerialName("strCountry")
    val strCountry: String? = null,
    @SerialName("strBiography")
    val strBiography: String? = null,
    @SerialName("strBiographyEN")
    val strBiographyEN: String? = null,
    @SerialName("strArtistThumb")
    val strArtistThumb: String? = null,
    @SerialName("strArtistBanner")
    val strArtistBanner: String? = null,
    @SerialName("strArtistFanart")
    val strArtistFanart: String? = null
)

// DTO untuk response album
@Serializable
data class AlbumResponseDto(
    @SerialName("album")
    val album: List<AlbumDto>? = null
)

// Data mentah album dari API
@Serializable
data class AlbumDto(
    @SerialName("idAlbum")
    val idAlbum: String? = null,
    @SerialName("strAlbum")
    val strAlbum: String? = null,
    @SerialName("strArtist")
    val strArtist: String? = null,
    @SerialName("intYearReleased")
    val intYearReleased: String? = null,
    @SerialName("strGenre")
    val strGenre: String? = null,
    @SerialName("strAlbumThumb")
    val strAlbumThumb: String? = null,
    @SerialName("strDescription")
    val strDescription: String? = null,
    @SerialName("strDescriptionEN")
    val strDescriptionEN: String? = null
)

// DTO untuk response daftar lagu
@Serializable
data class TrackResponseDto(
    @SerialName("track")
    val track: List<TrackDto>? = null
)

// Data mentah lagu dari API
@Serializable
data class TrackDto(
    @SerialName("idTrack")
    val idTrack: String? = null,
    @SerialName("strTrack")
    val strTrack: String? = null,
    @SerialName("strGenre")
    val strGenre: String? = null,
    @SerialName("intDuration")
    val intDuration: String? = null,
    @SerialName("intTrackNumber")
    val intTrackNumber: String? = null
)
