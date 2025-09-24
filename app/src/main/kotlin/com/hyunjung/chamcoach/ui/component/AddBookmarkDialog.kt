package com.hyunjung.chamcoach.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.hyunjung.chamcoach.data.BookmarkColor
import com.hyunjung.chamcoach.ui.theme.ChamCoachTheme
import com.hyunjung.chamcoach.ui.theme.TamaBlue02
import com.hyunjung.chamcoach.ui.theme.TamaPink02
import com.hyunjung.chamcoach.ui.theme.TamaPurple02

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
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 제목
                Text(
                    text = "북마크 추가",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TamaPurple02
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 현재 단계 표시
                Text(
                    text = "${currentStep + 1}단계를 북마크에 추가합니다",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 제목 입력
                Text(
                    text = "북마크 이름",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("예: 집 다마고치, 회사 다마고치") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 색상 선택
                Text(
                    text = "색상 선택",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ColorOption(
                        color = TamaPink02,
                        isSelected = selectedColor == BookmarkColor.PINK,
                        onClick = { selectedColor = BookmarkColor.PINK },
                        label = "분홍"
                    )

                    ColorOption(
                        color = TamaBlue02,
                        isSelected = selectedColor == BookmarkColor.BLUE,
                        onClick = { selectedColor = BookmarkColor.BLUE },
                        label = "파랑"
                    )

                    ColorOption(
                        color = TamaPurple02,
                        isSelected = selectedColor == BookmarkColor.PURPLE,
                        onClick = { selectedColor = BookmarkColor.PURPLE },
                        label = "보라"
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 버튼들
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("취소")
                    }

                    Button(
                        onClick = {
                            onConfirm(title.trim().ifEmpty { "다마고치 ${existingBookmarksCount + 1}" }, selectedColor)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TamaPurple02
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "추가",
                            color = Color.White
                        )
                    }
                }
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
        modifier = modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color)
                .then(
                    if (isSelected) {
                        Modifier.border(3.dp, Color.Black, CircleShape)
                    } else {
                        Modifier.border(2.dp, Color.Gray.copy(alpha = 0.3f), CircleShape)
                    }
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.Black else Color.Gray
        )
    }
}

@Preview
@Composable
private fun AddBookmarkDialogPreview() {
    ChamCoachTheme {
        AddBookmarkDialog(
            currentStep = 4,
            existingBookmarksCount = 1,
            onDismiss = {},
            onConfirm = { _, _ -> }
        )
    }
}