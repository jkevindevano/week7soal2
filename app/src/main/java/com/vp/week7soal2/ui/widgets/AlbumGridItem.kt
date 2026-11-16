package com.vp.week7soal2.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.vp.week7soal2.model.DataAlbum

// Komponen kartu kecil untuk nampilin satu album di dalam grid
@Composable
fun ItemGridAlbum(
    album: DataAlbum,
    padaSaatDiklik: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { padaSaatDiklik() }
    ) {
        // Gambar cover album bentuk kotak dengan sudut melengkung
        AsyncImage(
            model = album.coverAlbumUrl,
            contentDescription = album.judulAlbum,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1E1E1E))
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Judul Album
        Text(
            text = album.judulAlbum,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Baris untuk info tahun rilis dan genre
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tahun rilis warna hijau Spotify
            Text(
                text = album.tahunRilis,
                color = Color(0xFF1DB954),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Genre musik warna abu-abu
            Text(
                text = album.genreAlbum,
                color = Color(0xFF9E9E9E),
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 6.dp)
            )
        }
    }
}
