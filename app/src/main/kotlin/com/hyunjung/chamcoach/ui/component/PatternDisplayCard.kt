package com.hyunjung.chamcoach.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyunjung.chamcoach.R
import com.hyunjung.chamcoach.data.Arrow
import com.hyunjung.chamcoach.ui.theme.ChamCoachTheme
import com.hyunjung.chamcoach.ui.theme.TamaGray01

@Composable
fun PatternDisplayCard(
    onSaveBookmark: () -> Unit = {},
    isAtBookmark: Boolean = false,
    arrows: List<Arrow>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomIconButton(
            onClick = { onSaveBookmark() },
            size = 240.dp
        ) {
            Icon(
                painter = if (isAtBookmark) painterResource(R.drawable.ic_done) else painterResource(
                    R.drawable.ic_bookmark
                ),
                contentDescription = if (isAtBookmark) "Remove bookmark" else "Save bookmark",
                tint = Color.Unspecified
            )
        }

        ArrowListIcon(arrows)
    }
}

@Composable
private fun ArrowListIcon(arrows: List<Arrow>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    )
    {
        arrows.forEachIndexed { index, arrow ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center
                ) {
                    when (arrow) {
                        Arrow.LEFT -> {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_left),
                                contentDescription = "Left arrow",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Arrow.RIGHT -> {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_right),
                                contentDescription = "Right arrow",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(48.dp)
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
                    color = TamaGray01
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
                Arrow.LEFT,   // 1
                Arrow.RIGHT,  // 2
                Arrow.RIGHT,  // 2
                Arrow.LEFT,   // 1
                Arrow.RIGHT   // 2
            )
        )
    }
}