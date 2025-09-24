package com.hyunjung.chamcoach.ui.component

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyunjung.chamcoach.R
import com.hyunjung.chamcoach.data.Arrow
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
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // 북마크 버튼 영역
        BookmarkButtonArea(
            currentStepBookmarks = currentStepBookmarks,
            canAddMoreBookmarks = canAddMoreBookmarks,
            onSaveBookmark = onSaveBookmark,
            onAddBookmark = onAddBookmark
        )

        Spacer(modifier = Modifier.height(16.dp))

        ArrowListIcon(arrows)
    }
}

@Composable
private fun BookmarkButtonArea(
    currentStepBookmarks: List<BookmarkItem>,
    canAddMoreBookmarks: Boolean,
    onSaveBookmark: () -> Unit,
    onAddBookmark: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(240.dp),
        contentAlignment = Alignment.Center
    ) {
        if (currentStepBookmarks.isNotEmpty()) {
            // 이미 북마크된 단계 - 색상 표시
            BookmarkedStepDisplay(bookmarks = currentStepBookmarks)
        } else {
            // 북마크되지 않은 단계 - 추가 버튼
            CustomIconButton(
                onClick = {
                    if (canAddMoreBookmarks) {
                        onAddBookmark()
                    } else {
                        onSaveBookmark() // 하위 호환
                    }
                },
                size = 240.dp,
            ) {
                Icon(
                    painter = if (canAddMoreBookmarks) {
                        painterResource(R.drawable.ic_bookmark)
                    } else {
                        painterResource(R.drawable.ic_bookmark) // 동일한 아이콘 사용
                    },
                    contentDescription = if (canAddMoreBookmarks) "북마크 추가" else "북마크 저장",
                    tint = Color.Unspecified,
                )
            }
        }
    }
}

@Composable
private fun BookmarkedStepDisplay(
    bookmarks: List<BookmarkItem>,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // 완료 아이콘
        Icon(
            painter = painterResource(R.drawable.ic_done),
            contentDescription = "북마크됨",
            tint = Color.Unspecified,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 북마크 개수 및 색상 표시
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${bookmarks.size}개 북마크",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TamaGray01
            )

            // 색상 인디케이터들
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                bookmarks.forEach { bookmark ->
                    Box(
                        modifier = Modifier
                          .size(12.dp)
                          .clip(CircleShape)
                          .background(Color.Unspecified)
                    )
                }
            }
        }

        // 북마크 제목들 (최대 2개까지만 표시)
        if (bookmarks.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            val displayBookmarks = bookmarks.take(2)
            displayBookmarks.forEach { bookmark ->
                Text(
                    text = bookmark.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = TamaGray01
                )
            }

            if (bookmarks.size > 2) {
                Text(
                    text = "외 ${bookmarks.size - 2}개",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = TamaGray01
                )
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

@Preview(showBackground = true)
@Composable
private fun PatternDisplayCardPreview() {
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
