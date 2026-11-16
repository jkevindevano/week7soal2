package com.vp.week7soal2.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.vp.week7soal2.util.Resource
import com.vp.week7soal2.viewmodel.AlbumDetailViewModel

// Layar untuk nampilin detail info album dan daftar track lagunya
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    idAlbum: String,
    onKembali: () -> Unit,
    viewModel: AlbumDetailViewModel = viewModel()
) {
    // Ambil state dari ViewModel
    val statusUiDetail by viewModel.statusUiDetail.collectAsState()
    val apakahDeskripsiDibuka by viewModel.apakahDeskripsiDibuka.collectAsState()

    // Begitu masuk halaman ini, langsung panggil API detail album
    LaunchedEffect(idAlbum) {
        viewModel.muatDetailAlbum(idAlbum)
    }

    Scaffold(
        containerColor = Color(0xFF0E0E0E),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detail Album",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onKembali) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Tombol Kembali",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0E0E0E)
                )
            )
        }
    ) { paddingLayar ->
        when (val kondisiDetail = statusUiDetail) {
            // Tampilan loading berputar warna hijau
            is Resource.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingLayar),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color(0xFF1DB954))
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Memuat detail album & track list...",
                        color = Color(0xFF9E9E9E),
                        fontSize = 13.sp
                    )
                }
            }

            // Tampilan error jika gagal memuat data
            is Resource.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingLayar)
                ) {
                    TampilanErrorUmum(
                        pesanError = kondisiDetail.message,
                        onCobaLagi = { viewModel.muatDetailAlbum(idAlbum) }
                    )
                }
            }

            // Tampilan berhasil: tampilkan cover, tahun, genre, deskripsi, dan list lagu
            is Resource.Success -> {
                val album = kondisiDetail.data.album
                val daftarLagu = kondisiDetail.data.daftarLagu

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingLayar)
                        .padding(horizontal = 20.dp),
                    contentPadding = PaddingValues(bottom = 28.dp)
                ) {
                    // Bagian Cover Album dan Info Singkat
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Cover album besar di tengah
                            AsyncImage(
                                model = album.coverAlbumUrl,
                                contentDescription = album.judulAlbum,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(220.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF1C1C1E))
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Judul album
                            Text(
                                text = album.judulAlbum,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Nama artis pembuat album
                            Text(
                                text = album.namaArtis,
                                color = Color(0xFF1DB954),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Badge info tahun rilis dan genre
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                ChipBadgeInfo(teks = "Tahun ${album.tahunRilis}")
                                ChipBadgeInfo(teks = album.genreAlbum)
                            }
                        }

                        Spacer(modifier = Modifier.height(22.dp))

                        // Bagian Deskripsi Album
                        Text(
                            text = "Deskripsi Album",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = album.deskripsiAlbum,
                            color = Color(0xFFB0B0B5),
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            maxLines = if (apakahDeskripsiDibuka) Int.MAX_VALUE else 3,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (apakahDeskripsiDibuka) "Tutup ringkasan" else "Baca selengkapnya",
                            color = Color(0xFF1DB954),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                viewModel.toggleBukaDeskripsi()
                            }
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        // Header Daftar Lagu + Badge total lagu
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MusicNote,
                                    contentDescription = null,
                                    tint = Color(0xFF1DB954),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Daftar Lagu",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Badge jumlah lagu
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFF122E1E), RoundedCornerShape(20.dp))
                                    .border(1.dp, Color(0xFF1DB954), RoundedCornerShape(20.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${daftarLagu.size} Lagu",
                                    color = Color(0xFF1DB954),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Daftar Lagu (LazyColumn items)
                    items(daftarLagu, key = { it.idLagu }) { lagu ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Nomor lagu (01, 02, dst)
                            Text(
                                text = lagu.nomorUrut,
                                color = Color(0xFF1DB954),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(32.dp)
                            )

                            // Judul dan genre lagu
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = lagu.judulLagu,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = lagu.genreLagu,
                                    color = Color(0xFF8E8E93),
                                    fontSize = 11.sp
                                )
                            }

                            // Durasi lagu dengan ikon jam
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = Color(0xFF8E8E93),
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = lagu.durasiFormatted,
                                    color = Color(0xFFB0B0B5),
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Garis pemisah antar lagu
                        HorizontalDivider(color = Color(0xFF1C1C1E))
                    }
                }
            }
        }
    }
}

// Komponen kecil untuk chip informasi (tahun dan genre)
@Composable
private fun ChipBadgeInfo(teks: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFF1C1C1E), RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF2C2C2E), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = teks,
            color = Color(0xFFD1D1D6),
            fontSize = 11.sp
        )
    }
}
