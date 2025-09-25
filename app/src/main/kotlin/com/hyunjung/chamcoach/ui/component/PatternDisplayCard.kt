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

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chamcoach.core.designsystem.TamaColors.TamaBlue02
import com.chamcoach.core.designsystem.TamaColors.TamaPink02
import com.chamcoach.core.designsystem.TamaColors.TamaPurple02
import com.hyunjung.chamcoach.R
import com.hyunjung.chamcoach.data.Arrow
import com.hyunjung.chamcoach.data.BookmarkColor
import com.hyunjung.chamcoach.data.BookmarkItem
import com.hyunjung.chamcoach.ui.theme.ChamCoachTheme
import com.hyunjung.chamcoach.ui.theme.TamaGray01

@Composable
fun PatternDisplayCard(
  arrows: List<Arrow>,
  currentStepBookmarks: List<BookmarkItem> = emptyList(),
  canAddMoreBookmarks: Boolean = true,
  onSaveBookmark: () -> Unit = {},
  onAddBookmark: () -> Unit = {},
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    BookmarkButtonArea(
      currentStepBookmarks = currentStepBookmarks,
      canAddMoreBookmarks = canAddMoreBookmarks,
      onAddBookmarkClick = onAddBookmark,
    )
    Spacer(modifier = Modifier.height(8.dp))
    ArrowListIcon(arrows)
  }
}

@Composable
private fun BookmarkButtonArea(
  currentStepBookmarks: List<BookmarkItem>,
  canAddMoreBookmarks: Boolean,
  onAddBookmarkClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current

  Box(
    modifier = modifier.fillMaxWidth(),
    contentAlignment = Alignment.Center,
  ) {
    if (currentStepBookmarks.isNotEmpty()) {
      BookmarkedStepDisplay(
        bookmarks = currentStepBookmarks,
        canAddMoreBookmarks = canAddMoreBookmarks,
        onAddBookmarkClick = onAddBookmarkClick,
      )
    } else {
      Box(
        contentAlignment = Alignment.Center,
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          CustomIconButton(
            onClick = {
              if (canAddMoreBookmarks) {
                onAddBookmarkClick()
              } else {
                Toast.makeText(context, "더 이상 북마크를 추가할 수 없습니다.", Toast.LENGTH_SHORT)
                  .show()
              }
            },
            size = 240.dp,
          ) {
            Icon(
              painter = if (canAddMoreBookmarks) {
                painterResource(R.drawable.ic_bookmark)
              } else {
                painterResource(R.drawable.ic_bookmark)
              },
              contentDescription = if (canAddMoreBookmarks) "북마크 추가" else "북마크 저장",
              tint = Color.Unspecified,
              modifier = Modifier.size(240.dp),
            )
          }
          Text(
            text = "북마크 없음",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = TamaGray01,
          )
        }
      }
    }
  }
}

@Composable
private fun BookmarkedStepDisplay(
  bookmarks: List<BookmarkItem>,
  canAddMoreBookmarks: Boolean,
  onAddBookmarkClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current

  Box(
    contentAlignment = Alignment.Center,
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      // 북마크가 1개인 경우: 해당 색상 아이콘
      // 북마크가 여러개인 경우: 첫 번째 북마크 색상 아이콘 또는 조합 표시
      when {
        bookmarks.isNotEmpty() && bookmarks.size <= 3 -> {
          // 단일 북마크: 색상별 아이콘
          CustomIconButton(
            onClick = {
              if (canAddMoreBookmarks) {
                onAddBookmarkClick()
              } else {
                Toast.makeText(context, "더 이상 북마크를 추가할 수 없습니다.", Toast.LENGTH_SHORT)
                  .show()
              }
            },
            size = 240.dp,
          ) {
            Icon(
              // BookmarkedStepDisplay와 같은 아이콘 크기
              painter = painterResource(bookmarks.first().color.toBookmarkIcon()),
              contentDescription = if (canAddMoreBookmarks) "북마크 추가" else "북마크 저장",
              tint = Color.Unspecified,
              modifier = Modifier.size(240.dp),
            )
          }
        }

        else -> {
          // 북마크 없음 (이 경우는 발생하지 않겠지만)
          Icon(
            // BookmarkedStepDisplay와 같은 아이콘 크기
            painter = painterResource(R.drawable.ic_bookmark),
            contentDescription = "북마크됨",
            tint = Color.Unspecified,
            modifier = Modifier.size(240.dp),
          )
        }
      }

      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        if (bookmarks.isNotEmpty()) {
          val displayBookmarks = bookmarks.take(1)
          displayBookmarks.forEach { bookmark ->
            if (bookmarks.size == 1) {
              Text(
                text = bookmark.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = TamaGray01,
              )
            } else if (bookmarks.size > 1) {
              Text(
                text = "${bookmark.title} 외 ${bookmarks.size - 1}개",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = TamaGray01,
              )
            } else {
              Text(
                text = "북마크 없음",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = TamaGray01,
              )
            }
          }
        }

        Row(
          horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
          bookmarks.forEach { bookmark ->
            Box(
              modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(bookmark.color.toComposeColor()),
            )
          }
        }
      }
    }
  }
}

@Composable
private fun ArrowListIcon(arrows: List<Arrow>) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceEvenly,
    modifier = Modifier.fillMaxWidth(),
  ) {
    arrows.forEachIndexed { index, arrow ->
      Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Box(
          modifier = Modifier,
          contentAlignment = Alignment.Center,
        ) {
          when (arrow) {
            Arrow.LEFT -> {
              Icon(
                painter = painterResource(R.drawable.ic_arrow_left),
                contentDescription = "Left arrow",
                tint = Color.Unspecified,
                modifier = Modifier.size(48.dp),
              )
            }

            Arrow.RIGHT -> {
              Icon(
                painter = painterResource(R.drawable.ic_arrow_right),
                contentDescription = "Right arrow",
                tint = Color.Unspecified,
                modifier = Modifier.size(48.dp),
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = when (arrow) {
            Arrow.LEFT -> "1"
            Arrow.RIGHT -> "2"
          },
          fontSize = 32.sp,
          fontWeight = FontWeight.SemiBold,
          color = TamaGray01,
        )
      }
    }
  }
}

private fun BookmarkColor.toBookmarkIcon(): Int {
  return when (this) {
    BookmarkColor.PINK -> R.drawable.img_check_pink
    BookmarkColor.BLUE -> R.drawable.img_check_blue
    BookmarkColor.PURPLE -> R.drawable.img_check_purple
  }
}

@Composable
private fun BookmarkColor.toComposeColor(): Color {
  return when (this) {
    BookmarkColor.PINK -> TamaPink02
    BookmarkColor.BLUE -> TamaBlue02
    BookmarkColor.PURPLE -> TamaPurple02
  }
}

@Preview(showBackground = true)
@Composable
private fun PatternDisplayCardPreview() {
  ChamCoachTheme {
    PatternDisplayCard(
      currentStepBookmarks = listOf(
        BookmarkItem("0", 0, "집 다마고치"),
        BookmarkItem("1", 1, "회사 다마고치"),
      ),
      arrows = listOf(
        // 1
        Arrow.LEFT,
        // 2
        Arrow.RIGHT,
        // 2
        Arrow.RIGHT,
        // 1
        Arrow.LEFT,
        // 2
        Arrow.RIGHT,
      ),
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun PatternDisplayEmptyCardPreview() {
  ChamCoachTheme {
    PatternDisplayCard(
      arrows = listOf(
        // 1
        Arrow.LEFT,
        // 2
        Arrow.RIGHT,
        // 2
        Arrow.RIGHT,
        // 1
        Arrow.LEFT,
        // 2
        Arrow.RIGHT,
      ),
    )
  }
}
