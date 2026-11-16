package com.vp.week7soal2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vp.week7soal2.navigation.MusicNavHost
import com.vp.week7soal2.ui.theme.Week7soal2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Week7soal2Theme {
                MusicNavHost()
            }
        }
    }
}

