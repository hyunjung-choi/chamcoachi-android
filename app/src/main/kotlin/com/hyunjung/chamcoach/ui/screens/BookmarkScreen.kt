package com.hyunjung.chamcoach.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chamcoach.core.designsystem.TamaColors
import com.hyunjung.chamcoach.ui.theme.TamaBlue02
import com.hyunjung.chamcoach.ui.theme.TamaGray01
import com.hyunjung.chamcoach.ui.theme.TamaOrange02
import com.hyunjung.chamcoach.ui.theme.TamaPurple02

data class BookmarkItem(
    val emoji: String,
    val title: String,
    val number: String,
    val color: Color,
    val hasData: Boolean = true
)

@Composable
fun BookmarkScreen() {
    val bookmarkItems = listOf(
        BookmarkItem("⭐", "1B", "1B", TamaOrange02),
        BookmarkItem("⭐", "3주차", "3주차", TamaBlue02),
        BookmarkItem("⭐", "No data", "No data", TamaPurple02, hasData = false)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        TamaPurple02,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 헤더
            Text(
                text = "Bookmark",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // 북마크 리스트
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    bookmarkItems.forEach { item ->
                        BookmarkItemRow(item = item)
                    }
                }
            }

            // 하단 정보
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(TamaGray01)
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(TamaGray01)
                    )
                }

                Text(
                    text = "1B · 1B",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "18",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun BookmarkItemRow(item: BookmarkItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 별 아이콘
            Canvas(
                modifier = Modifier.size(24.dp)
            ) {
                val starPath = Path().apply {
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val outerRadius = size.width / 3
                    val innerRadius = size.width / 6

                    for (i in 0 until 10) {
                        val angle = (i * 36 - 90) * Math.PI / 180
                        val radius = if (i % 2 == 0) outerRadius else innerRadius
                        val x = centerX + (radius * kotlin.math.cos(angle)).toFloat()
                        val y = centerY + (radius * kotlin.math.sin(angle)).toFloat()

                        if (i == 0) moveTo(x, y) else lineTo(x, y)
                    }
                    close()
                }

                drawPath(
                    path = starPath,
                    color = item.color
                )
            }

            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium,
                color = if (item.hasData)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }

        // 오른쪽 원형 아이콘
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    if (item.hasData)
                        item.color
                    else
                        Color.Gray.copy(alpha = 0.3f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⭐",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}