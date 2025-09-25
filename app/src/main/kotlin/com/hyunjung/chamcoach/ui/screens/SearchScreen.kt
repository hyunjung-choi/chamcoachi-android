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
package com.hyunjung.chamcoach.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chamcoach.core.designsystem.ChamCoachTheme
import com.hyunjung.chamcoach.ui.theme.TamaGray01
import com.hyunjung.chamcoach.ui.theme.TamaPurple02

@Composable
fun SearchScreen() {
  var searchText by remember { mutableStateOf("") }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            TamaPurple02,
            MaterialTheme.colorScheme.background,
          ),
        ),
      ),
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      // 헤더
      Text(
        text = "Search",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(vertical = 16.dp),
      )

      // 검색 입력 필드
      OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        label = { Text("검색하기") },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = "Search")
        },
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 16.dp),
        shape = RoundedCornerShape(12.dp),
      )

      // 검색 결과 영역
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center,
        ) {
          Text(
            text = "🔍",
            fontSize = 64.sp,
            modifier = Modifier.padding(bottom = 16.dp),
          )

          Text(
            text = if (searchText.isEmpty()) "검색어를 입력하세요" else "\"$searchText\" 검색 중...",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
          )
        }
      }

      // 하단 정보
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(4.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Box(
            modifier = Modifier
              .size(24.dp)
              .clip(CircleShape)
              .background(TamaGray01),
          )
          Box(
            modifier = Modifier
              .size(24.dp)
              .clip(CircleShape)
              .background(TamaGray01),
          )
        }

        Text(
          text = "12 · 1B",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onBackground,
        )

        Text(
          text = "18",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground,
        )
      }
    }
  }
}

@Preview
@Composable
private fun SearchScreenPreview() {
  ChamCoachTheme {
    SearchScreen()
  }
}
