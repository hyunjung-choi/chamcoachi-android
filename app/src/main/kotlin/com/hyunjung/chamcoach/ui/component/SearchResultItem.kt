package com.hyunjung.chamcoach.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyunjung.chamcoach.R
import com.hyunjung.chamcoach.data.Arrow
import com.hyunjung.chamcoach.ui.player.SearchResult
import com.hyunjung.chamcoach.ui.theme.TamaGray01
import com.hyunjung.chamcoach.ui.theme.TamaGray02
import com.hyunjung.chamcoach.ui.theme.TamaPurple02

@Composable
fun SearchResultItem(
  result: SearchResult,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(1.dp)
      .background(TamaGray02),
  )
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(12.dp)
      .clickable { onClick() },
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = "${result.index + 1}단계",
        style = TextStyle(
          fontSize = 16.sp,
          fontWeight = FontWeight(800),
          color = TamaPurple02,
        ),
      )
      Spacer(modifier = Modifier.width(4.dp))
      result.item.arrows.forEach { arrow ->
        Text(
          text = arrowSymbol(arrow),
          style = TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight(600),
            color = TamaGray01,
          ),
        )
      }
    }

    Icon(
      painter = painterResource(R.drawable.ic_arrow),
      contentDescription = null,
      tint = Color.Unspecified,
    )
  }
}

private fun arrowSymbol(arrow: Arrow): String = when (arrow) {
  Arrow.LEFT -> "1"
  Arrow.RIGHT -> "2"
}
