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
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyunjung.chamcoach.R
import com.hyunjung.chamcoach.data.BookmarkColor
import com.hyunjung.chamcoach.data.BookmarkItem
import com.hyunjung.chamcoach.ui.theme.ChamCoachTheme
import com.hyunjung.chamcoach.ui.theme.TamaBlue02
import com.hyunjung.chamcoach.ui.theme.TamaGray01
import com.hyunjung.chamcoach.ui.theme.TamaPink02
import com.hyunjung.chamcoach.ui.theme.TamaPurple02

@Composable
fun BookmarkManagementScreen(
  modifier: Modifier = Modifier,
  bookmarks: List<BookmarkItem>,
  maxBookmarks: Int = 3,
  canAddMore: Boolean,
  onBookmarkClick: (BookmarkItem) -> Unit,
  onDeleteBookmark: (BookmarkItem) -> Unit,
  onAddBookmark: () -> Unit,
  onToggleBookmark: () -> Unit,
) {
  Column(
    modifier = modifier.padding(16.dp),
  ) {
    Image(
      painter = painterResource(R.drawable.img_bookmark_title),
      contentDescription = null,
      modifier = Modifier
        .width(140.dp)
        .padding(bottom = 16.dp),
    )

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(1.dp),
    )

    // 북마크 슬롯들 (최대 3개)
    repeat(maxBookmarks) { index ->
      val bookmark = bookmarks.getOrNull(index)

      if (bookmark != null) {
        ExistingBookmarkItem(
          bookmark = bookmark,
          onClick = {
            onBookmarkClick(bookmark)
            onToggleBookmark() // Close bookmark mode
          },
          onDelete = { onDeleteBookmark(bookmark) },
        )
      } else {
        EmptyBookmarkSlot(
          slotNumber = index + 1,
          canAdd = canAddMore,
          onClick = if (canAddMore) onAddBookmark else null,
        )
      }

      if (index < maxBookmarks - 1) {
        Spacer(modifier = Modifier.height(8.dp))
      }
    }
  }
}

@Composable
private fun ExistingBookmarkItem(
  bookmark: BookmarkItem,
  onClick: () -> Unit,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
      .clickable { onClick() },
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      // 북마크 아이콘 (색상별로 다른 아이콘)
      Icon(
        painter = painterResource(bookmark.color.toIconResource()),
        contentDescription = null,
        tint = Color.Unspecified,
      )

      Spacer(modifier = Modifier.width(16.dp))

      // 북마크 정보
      Column(
        modifier = Modifier.weight(1f),
      ) {
        Text(
          text = bookmark.title,
          style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = bookmark.color.toTextColor(),
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )

        Text(
          text = "${bookmark.stepIndex + 1}단계",
          style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = bookmark.color.toTextColor(),
          ),
        )
      }

      // 삭제 버튼 (작게)
      IconButton(
        onClick = onDelete,
        modifier = Modifier.width(24.dp),
      ) {
        Icon(
          imageVector = Icons.Default.Delete,
          contentDescription = "삭제",
          tint = TamaGray01,
          modifier = Modifier.width(24.dp),
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      // 화살표 (색상별)
      Icon(
        modifier = Modifier.width(24.dp),
        painter = painterResource(bookmark.color.toArrowResource()),
        contentDescription = null,
        tint = Color.Unspecified,
      )
    }
  }
}

@Composable
private fun EmptyBookmarkSlot(
  slotNumber: Int,
  canAdd: Boolean,
  onClick: (() -> Unit)?,
  modifier: Modifier = Modifier,
) {
  val textColor = when (slotNumber) {
    1 -> TamaPink02
    2 -> TamaBlue02
    3 -> TamaPurple02
    else -> TamaGray01
  }

  Surface(
    shape = RoundedCornerShape(12.dp),
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
      .then(
        if (onClick != null) {
          Modifier.clickable { onClick() }
        } else {
          Modifier
        },
      ),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      if (canAdd) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "북마크 추가",
          tint = textColor,
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
          text = "북마크 추가",
          style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
          ),
        )
      } else {
        Text(
          text = "No data",
          style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
          ),
        )
      }
    }
  }
}

@Composable
private fun BookmarkColor.toTextColor(): Color {
  return when (this) {
    BookmarkColor.PINK -> TamaPink02
    BookmarkColor.BLUE -> TamaBlue02
    BookmarkColor.PURPLE -> TamaPurple02
  }
}

private fun BookmarkColor.toIconResource(): Int {
  return when (this) {
    BookmarkColor.PINK -> R.drawable.ic_star_pink
    BookmarkColor.BLUE -> R.drawable.ic_star_blue
    BookmarkColor.PURPLE -> R.drawable.ic_star_purple
  }
}

private fun BookmarkColor.toArrowResource(): Int {
  return when (this) {
    BookmarkColor.PINK -> R.drawable.ic_arrow_pink_right
    BookmarkColor.BLUE -> R.drawable.ic_arrow_blue
    BookmarkColor.PURPLE -> R.drawable.ic_arrow
  }
}

@Preview
@Composable
private fun ExistingDesignBookmarkScreenPreview() {
  ChamCoachTheme {
    BookmarkManagementScreen(
      bookmarks = listOf(
        BookmarkItem(
          stepIndex = 14,
          title = "집 다마고치",
          color = BookmarkColor.PINK,
        ),
      ),
      maxBookmarks = 3,
      canAddMore = true,
      onBookmarkClick = {},
      onDeleteBookmark = {},
      onAddBookmark = {},
      onToggleBookmark = {},
    )
  }
}

@Preview
@Composable
private fun ExistingDesignBookmarkScreenFullPreview() {
  ChamCoachTheme {
    BookmarkManagementScreen(
      bookmarks = listOf(
        BookmarkItem(
          stepIndex = 14,
          title = "집 다마고치",
          color = BookmarkColor.PINK,
        ),
        BookmarkItem(
          stepIndex = 2,
          title = "회사 다마고치",
          color = BookmarkColor.BLUE,
        ),
        BookmarkItem(
          stepIndex = 8,
          title = "예비 다마고치",
          color = BookmarkColor.PURPLE,
        ),
      ),
      maxBookmarks = 3,
      canAddMore = false,
      onBookmarkClick = {},
      onDeleteBookmark = {},
      onAddBookmark = {},
      onToggleBookmark = {},
    )
  }
}

private fun main() {
}
