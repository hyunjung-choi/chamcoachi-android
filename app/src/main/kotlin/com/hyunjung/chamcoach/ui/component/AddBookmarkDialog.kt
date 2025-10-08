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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.hyunjung.chamcoach.R
import com.hyunjung.chamcoach.data.BookmarkColor
import com.hyunjung.chamcoach.ui.theme.ChamCoachTheme
import com.hyunjung.chamcoach.ui.theme.TamaBlue02
import com.hyunjung.chamcoach.ui.theme.TamaGray01
import com.hyunjung.chamcoach.ui.theme.TamaPink02
import com.hyunjung.chamcoach.ui.theme.TamaPurple02
import com.hyunjung.chamcoach.ui.theme.TamaPurpleText

@Composable
fun AddBookmarkDialog(
  currentStep: Int,
  existingBookmarksCount: Int,
  onDismiss: () -> Unit,
  onConfirm: (title: String, color: BookmarkColor) -> Unit,
  modifier: Modifier = Modifier,
) {
  var title by remember {
    mutableStateOf("다마고치 ${existingBookmarksCount + 1}")
  }
  var selectedColor by remember { mutableStateOf(BookmarkColor.PINK) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = modifier
        .fillMaxWidth()
        .padding(16.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = Color.White,
      ),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
      AddBookmarkDialogContent(
        currentStep = currentStep,
        existingBookmarksCount = existingBookmarksCount,
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        title = title,
        selectedColor = selectedColor,
        onTitleChange = { title = it },
        onColorChange = { selectedColor = it },
      )
    }
  }
}

@Composable
private fun AddBookmarkDialogContent(
  currentStep: Int,
  existingBookmarksCount: Int,
  onDismiss: () -> Unit,
  onConfirm: (title: String, color: BookmarkColor) -> Unit,
  title: String,
  selectedColor: BookmarkColor,
  onTitleChange: (String) -> Unit,
  onColorChange: (BookmarkColor) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = "북마크 추가",
      fontSize = 16.sp,
      fontWeight = FontWeight.Bold,
      color = TamaPurple02,
    )

    Spacer(modifier = Modifier.height(24.dp))

    OutlinedTextField(
      value = title,
      onValueChange = onTitleChange,
      placeholder = { Text("새로운 북마크 이름") },
      modifier = Modifier.fillMaxWidth(),
      singleLine = true,
      shape = RoundedCornerShape(8.dp),
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
      text = "색상 선택",
      fontSize = 12.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.fillMaxWidth(),
      color = TamaGray01,
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      ColorOption(
        color = TamaPink02,
        isSelected = selectedColor == BookmarkColor.PINK,
        onClick = { onColorChange(BookmarkColor.PINK) },
        label = "분홍",
      )

      ColorOption(
        color = TamaBlue02,
        isSelected = selectedColor == BookmarkColor.BLUE,
        onClick = { onColorChange(BookmarkColor.BLUE) },
        label = "파랑",
      )

      ColorOption(
        color = TamaPurple02,
        isSelected = selectedColor == BookmarkColor.PURPLE,
        onClick = { onColorChange(BookmarkColor.PURPLE) },
        label = "보라",
      )
    }

    Spacer(modifier = Modifier.height(24.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      OutlinedButton(
        onClick = onDismiss,
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(8.dp),
      ) {
        Text(
          text = "취소",
          fontSize = 12.sp,
          color = TamaGray01,
        )
      }

      OutlinedButton(
        onClick = {
          onConfirm(
            title.trim().ifEmpty { "다마고치 ${existingBookmarksCount + 1}" },
            selectedColor,
          )
        },
        modifier = Modifier
          .weight(1f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, TamaPurpleText),
        colors = ButtonDefaults.outlinedButtonColors(
          containerColor = TamaPurple02,
        ),
      ) {
        Text(
          text = "추가",
          fontSize = 12.sp,
          color = Color.White,
        )
      }
    }
  }
}

@Composable
private fun ColorOption(
  color: Color,
  isSelected: Boolean,
  onClick: () -> Unit,
  label: String,
  modifier: Modifier = Modifier,
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier.clickable { onClick() },
  ) {
    Box(
//            modifier = Modifier
//                .size(48.dp)
//                .clip(CircleShape)
//                .background(color)
//                .then(
//                    if (isSelected) {
//                        Modifier.border(3.dp, Color.Black, CircleShape)
//                    } else {
//                        Modifier.border(2.dp, Color.Gray.copy(alpha = 0.3f), CircleShape)
//                    },
//                ),
    ) {
      Icon(
        modifier = Modifier.padding(12.dp),
        painter = when (label) {
          "분홍" -> painterResource(R.drawable.ic_star_pink_unbookmarked)
          "파랑" -> painterResource(R.drawable.ic_star_blue_unbookmarked)
          else -> painterResource(R.drawable.ic_star_purple_unbookmarked)
        },
        contentDescription = null,
        tint = Color.Unspecified,
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun AddBookmarkDialogPreview() {
  ChamCoachTheme {
    AddBookmarkDialogContent(
      currentStep = 4,
      existingBookmarksCount = 1,
      onDismiss = {},
      onConfirm = { _, _ -> },
      title = "다마고치 2",
      selectedColor = BookmarkColor.PINK,
      onTitleChange = {},
      onColorChange = {},
    )
  }
}
