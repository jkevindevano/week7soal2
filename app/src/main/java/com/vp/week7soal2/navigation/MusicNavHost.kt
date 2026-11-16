package com.vp.week7soal2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vp.week7soal2.ui.screen.AlbumDetailScreen
import com.vp.week7soal2.ui.screen.ArtistScreen

// Pengatur navigasi antar layar dalam aplikasi
@Composable
fun MusicNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = MusicRoutes.ArtistHome.route
    ) {
        // 1. Layar utama: profil artis dan koleksi album
        composable(MusicRoutes.ArtistHome.route) {
            ArtistScreen(
                onKlikAlbum = { idAlbumDipilih ->
                    navController.navigate(MusicRoutes.AlbumDetail.buatRute(idAlbumDipilih))
                }
            )
        }

        // 2. Layar detail: detail album dan track list
        composable(
            route = MusicRoutes.AlbumDetail.route,
            arguments = listOf(navArgument("idAlbum") { type = NavType.StringType })
        ) { backStackEntry ->
            val idAlbum = backStackEntry.arguments?.getString("idAlbum") ?: ""
            AlbumDetailScreen(
                idAlbum = idAlbum,
                onKembali = { navController.popBackStack() }
            )
        }
    }
}
