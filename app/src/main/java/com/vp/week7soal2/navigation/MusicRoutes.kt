package com.vp.week7soal2.navigation

// Daftar rute untuk navigasi antar halaman
sealed class MusicRoutes(val route: String) {
    object ArtistHome : MusicRoutes("halaman_artis")
    object AlbumDetail : MusicRoutes("halaman_detail_album/{idAlbum}") {
        fun buatRute(idAlbum: String): String = "halaman_detail_album/$idAlbum"
    }
}
