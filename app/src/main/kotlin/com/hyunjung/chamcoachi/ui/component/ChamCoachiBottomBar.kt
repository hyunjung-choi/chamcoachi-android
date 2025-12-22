package com.hyunjung.chamcoachi.ui.component

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
import com.chamcoach.core.designsystem.ChamCoachiTheme
import com.hyunjung.chamcoachi.R
import com.hyunjung.chamcoachi.ui.theme.ChamCoachiGray01
import com.hyunjung.chamcoachi.ui.theme.ChamCoachiPurpleText

const val TOTAL_ITEMS = 18

@Composable
fun ChamCoachiBottomBar(
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
          tint = if (isSearchMode) ChamCoachiPurpleText else ChamCoachiGray01,
        )
      }
    }

    Text(
      text = "${currentIndex + 1} | $TOTAL_ITEMS",
      style = TextStyle(
        fontSize = 14.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight(700),
        color = ChamCoachiPurpleText,
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
          tint = if (isBookmarkMode) ChamCoachiPurpleText else ChamCoachiGray01,
        )
      }
    }
  }
}

@Preview
@Composable
private fun ChamCoachiBottomBarPreview() {
  ChamCoachiTheme {
    ChamCoachiBottomBar()
  }
}
