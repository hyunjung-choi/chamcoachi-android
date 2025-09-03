package com.hyunjung.chamcoach

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.chamcoach.core.designsystem.ChamCoachTheme
import com.hyunjung.chamcoach.ui.player.PatternPlayerScreen
import com.hyunjung.chamcoach.ui.player.PatternPlayerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(this)[PatternPlayerViewModel::class.java]
        setContent {
            ChamCoachTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PatternPlayerScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}