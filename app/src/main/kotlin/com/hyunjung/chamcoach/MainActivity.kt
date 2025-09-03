/*
 * Copyright 2025 ChamCoach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
            modifier = Modifier.padding(innerPadding),
          )
        }
      }
    }
  }
}
