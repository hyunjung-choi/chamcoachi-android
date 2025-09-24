package com.hyunjung.chamcoach.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomIconButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  size: Dp = 48.dp,
  iconSize: Dp = 24.dp,
  enabled: Boolean = true,
  content: @Composable () -> Unit,
) {
  Box(
    modifier = modifier
      .size(size)
      .clickable(
        onClick = onClick,
        enabled = enabled,
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
      ),
    contentAlignment = Alignment.Center,
  ) {
    content()
  }
}
