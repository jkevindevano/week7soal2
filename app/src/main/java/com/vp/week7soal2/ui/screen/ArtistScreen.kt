package com.vp.week7soal2.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.vp.week7soal2.ui.widgets.ItemGridAlbum
import com.vp.week7soal2.util.Resource
import com.vp.week7soal2.viewmodel.ArtistViewModel

// Halaman utama untuk nampilin data artis, bio, dan daftar albumnya
@Composable
fun ArtistScreen(
    viewModel: ArtistViewModel = viewModel(),
    onKlikAlbum: (String) -> Unit
) {
    // Ambil state dari ViewModel
    val teksPencarian by viewModel.teksPencarian.collectAsState()
    val statusUi by viewModel.statusUi.collectAsState()
    val apakahBioDibuka by viewModel.apakahBioDibuka.collectAsState()

    // Buat nutup keyboard pas tombol search ditekan
    val pengaturFokus = LocalFocusManager.current

    Scaffold(
        containerColor = Color(0xFF0E0E0E) // Warna background gelap Spotify
    ) { paddingLayar ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingLayar)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // 1. Header paling atas: Logo hijau + Tulisan "Music Artist"
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1DB954)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = "Logo Musik",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Music Artist",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Search bar untuk ngetik nama artis
            OutlinedTextField(
                value = teksPencarian,
                onValueChange = { inputanBaru ->
                    viewModel.onUbahTeksPencarian(inputanBaru)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Cari nama artis...", color = Color(0xFF8E8E93))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Cari",
                        tint = Color(0xFF1DB954)
                    )
                },
                trailingIcon = {
                    // Tombol silang untuk hapus teks pencarian
                    if (teksPencarian.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onUbahTeksPencarian("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Hapus teks",
                                tint = Color(0xFF8E8E93)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        pengaturFokus.clearFocus()
                        viewModel.cariDataArtis()
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1C1C1E),
                    unfocusedContainerColor = Color(0xFF1C1C1E),
                    focusedBorderColor = Color(0xFF2C2C2E),
                    unfocusedBorderColor = Color(0xFF2C2C2E)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Tampilan konten sesuai status dari ViewModel
            when (val kondisiSekarang = statusUi) {
                // Kalau lagi loading
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF1DB954))
                    }
                }

                // Kalau terjadi error / data tidak ditemukan
                is Resource.Error -> {
                    TampilanErrorUmum(
                        pesanError = kondisiSekarang.message,
                        onCobaLagi = { viewModel.cariDataArtis() }
                    )
                }

                // Kalau berhasil dapat data
                is Resource.Success -> {
                    val dataArtis = kondisiSekarang.data.artis
                    val daftarAlbum = kondisiSekarang.data.daftarAlbum

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 24.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Banner Artis (mengambil 2 kolom penuh)
                        item(span = { GridItemSpan(2) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(190.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF1C1C1E))
                            ) {
                                // Foto banner besar
                                AsyncImage(
                                    model = dataArtis.fotoBannerUrl,
                                    contentDescription = dataArtis.namaArtis,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                // Efek gradasi gelap di bagian bawah gambar
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    Color.Black.copy(alpha = 0.85f)
                                                )
                                            )
                                        )
                                )

                                // Info foto profil bulat, nama artis, dan genrenya
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = dataArtis.fotoProfilUrl,
                                        contentDescription = "Foto Profil",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .border(1.5.dp, Color.White, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = dataArtis.namaArtis,
                                            color = Color.White,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${dataArtis.genreArtis} • ${dataArtis.lokasiArtis}",
                                            color = Color(0xFF1DB954),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }

                        // Bagian Tentang Artis / Biografi (2 kolom penuh)
                        item(span = { GridItemSpan(2) }) {
                            Column {
                                Text(
                                    text = "Tentang Artis",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = dataArtis.biografiArtis,
                                    color = Color(0xFFB0B0B5),
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp,
                                    maxLines = if (apakahBioDibuka) Int.MAX_VALUE else 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (apakahBioDibuka) "Tutup ringkasan" else "Baca selengkapnya",
                                    color = Color(0xFF1DB954),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.clickable {
                                        viewModel.toggleBukaBio()
                                    }
                                )
                            }
                        }

                        // Header Diskografi Album + Badge jumlah album (2 kolom penuh)
                        item(span = { GridItemSpan(2) }) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Diskografi Album",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Koleksi rilisan resmi",
                                        color = Color(0xFF8E8E93),
                                        fontSize = 11.sp
                                    )
                                }

                                // Badge kotak hijau berisi jumlah album
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFF122E1E), RoundedCornerShape(20.dp))
                                        .border(1.dp, Color(0xFF1DB954), RoundedCornerShape(20.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${daftarAlbum.size} Album",
                                        color = Color(0xFF1DB954),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Daftar album dalam bentuk grid 2 kolom
                        items(daftarAlbum, key = { it.idAlbum }) { album ->
                            ItemGridAlbum(
                                album = album,
                                padaSaatDiklik = { onKlikAlbum(album.idAlbum) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// Komponen pesan error yang konsisten di seluruh aplikasi
@Composable
fun TampilanErrorUmum(
    pesanError: String,
    onCobaLagi: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = "Peringatan Error",
            tint = Color(0xFFE53935),
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Terjadi Kendala",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = pesanError,
            color = Color(0xFF9E9E9E),
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Tombol hijau coba lagi
        Button(
            onClick = onCobaLagi,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954))
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Coba Lagi",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
