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
package com.hyunjung.chamcoach.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chamcoach.core.designsystem.ChamCoachTheme
import com.hyunjung.chamcoach.R
import com.hyunjung.chamcoach.ui.theme.TamaGray01
import com.hyunjung.chamcoach.ui.theme.TamaPurpleText

const val TOTAL_ITEMS = 18

@Composable
fun ChamCoachBottomBar(
  onToggleSearch: () -> Unit = {},
  onToggleBookmark: () -> Unit = {},
  onGoToBookmark: () -> Unit = {},
  isSearchMode: Boolean = false,
  isBookmarkMode: Boolean = false,
  isAtBookmark: Boolean = false,
  currentIndex: Int = 0,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 4.dp),
    horizontalArrangement = Arrangement.spacedBy(70.dp, Alignment.CenterHorizontally),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Column(
      modifier = Modifier
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
        ) { onToggleSearch() }
        .padding(vertical = 14.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Box(

        contentAlignment = Alignment.Center,
      ) {
        Icon(
          modifier = Modifier.size(56.dp),
          painter = painterResource(R.drawable.ic_search_with_text),
          contentDescription = null,
          tint = if (isSearchMode) TamaPurpleText else TamaGray01,
        )
      }
    }

    Text(
      text = "${currentIndex + 1} | $TOTAL_ITEMS",
      style = TextStyle(
        fontSize = 14.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight(700),
        color = TamaPurpleText,
        textAlign = TextAlign.Center,
      ),
    )

    Column(
      modifier = Modifier
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
        ) { onToggleBookmark() }
        .padding(vertical = 14.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Box(

        contentAlignment = Alignment.Center,
      ) {
        Icon(
          modifier = Modifier.size(56.dp),
          painter = painterResource(R.drawable.ic_bookmark_with_text),
          contentDescription = null,
          tint = if (isBookmarkMode) TamaPurpleText else TamaGray01,
        )
      }
    }
  }
}

@Preview
@Composable
private fun ChamCoachBottomBarPreview() {
  ChamCoachTheme {
    ChamCoachBottomBar()
  }
}
