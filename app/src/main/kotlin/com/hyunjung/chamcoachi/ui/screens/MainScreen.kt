package com.hyunjung.chamcoachi.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chamcoach.core.designsystem.ChamCoachiTheme
import com.hyunjung.chamcoachi.ui.theme.ChamCoachiBlue02
import com.hyunjung.chamcoachi.ui.theme.ChamCoachiPink02
import com.hyunjung.chamcoachi.ui.theme.ChamCoachiPurple02
import com.hyunjung.chamcoachi.ui.theme.ChamCoachiPurpleText
import com.hyunjung.chamcoachi.ui.theme.ChamCoachiYellow02

@Composable
fun MainScreen(version: String = "v2") {
  var currentPattern by remember { mutableIntStateOf(1) }
  val maxPattern = 2

  Box(
    modifier = Modifier
      .fillMaxSize(),
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      // 헤더
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "Main$version",
          style = MaterialTheme.typography.titleLarge,
          color = MaterialTheme.colorScheme.onBackground,
        )
      }

      // 메인 콘텐츠 카드
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
          // 상태에 따른 이모지와 메시지
          when (version) {
            "bookmark" -> {
              // 북마크 상태 (별 모양)
              Canvas(
                modifier = Modifier.size(80.dp),
              ) {
                val starPath = Path().apply {
                  val centerX = size.width / 2
                  val centerY = size.height / 2
                  val outerRadius = size.width / 3
                  val innerRadius = size.width / 6

                  for (i in 0 until 10) {
                    val angle = (i * 36 - 90) * Math.PI / 180
                    val radius = if (i % 2 == 0) outerRadius else innerRadius
                    val x =
                      centerX + (radius * kotlin.math.cos(angle)).toFloat()
                    val y =
                      centerY + (radius * kotlin.math.sin(angle)).toFloat()

                    if (i == 0) moveTo(x, y) else lineTo(x, y)
                  }
                  close()
                }

                drawPath(
                  path = starPath,
                  color = Color.Gray,
                )
              }

              Text(
                text = "Bookmark",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 16.dp),
              )
            }

            else -> {
              // 완료 상태 (분홍 별)
              Canvas(
                modifier = Modifier.size(80.dp),
              ) {
                val starPath = Path().apply {
                  val centerX = size.width / 2
                  val centerY = size.height / 2
                  val outerRadius = size.width / 3
                  val innerRadius = size.width / 6

                  for (i in 0 until 10) {
                    val angle = (i * 36 - 90) * Math.PI / 180
                    val radius = if (i % 2 == 0) outerRadius else innerRadius
                    val x =
                      centerX + (radius * kotlin.math.cos(angle)).toFloat()
                    val y =
                      centerY + (radius * kotlin.math.sin(angle)).toFloat()

                    if (i == 0) moveTo(x, y) else lineTo(x, y)
                  }
                  close()
                }

                drawPath(
                  path = starPath,
                  color = ChamCoachiPurple02.copy(alpha = 0.8f),
                )
              }

              Text(
                text = "Done!",
                fontSize = 18.sp,
                color = ChamCoachiPurpleText,
                modifier = Modifier.padding(top = 16.dp),
              )
            }
          }

          // 컨트롤 영역
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            // 이전 버튼
            IconButton(
              onClick = {
                if (currentPattern > 1) currentPattern--
              },
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(ChamCoachiPink02),
            ) {
              Icon(
                Icons.Default.KeyboardArrowLeft,
                contentDescription = "Previous",
                tint = Color.White,
              )
            }

            // 패턴 번호들
            Row(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              repeat(maxPattern) { index ->
                val patternNumber = index + 1
                Box(
                  modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                      if (patternNumber == currentPattern) {
                        ChamCoachiPink02
                      } else {
                        MaterialTheme.colorScheme.surface
                      },
                    )
                    .clickable { currentPattern = patternNumber },
                  contentAlignment = Alignment.Center,
                ) {
                  Text(
                    text = patternNumber.toString(),
                    color = if (patternNumber == currentPattern) {
                      Color.White
                    } else {
                      MaterialTheme.colorScheme.onSurface
                    },
                    fontWeight = FontWeight.Bold,
                  )
                }
              }
            }

            // 다음 버튼
            IconButton(
              onClick = {
                if (currentPattern < maxPattern) currentPattern++
              },
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(ChamCoachiPink02),
            ) {
              Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = "Next",
                tint = Color.White,
              )
            }
          }
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
              .background(ChamCoachiYellow02),
          )
          Box(
            modifier = Modifier
              .size(24.dp)
              .clip(CircleShape)
              .background(ChamCoachiBlue02),
          )
        }

        Text(
          text = "3 · 1B",
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
private fun MainScreenPreview() {
  ChamCoachiTheme {
    MainScreen()
  }
}
