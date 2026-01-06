package com.hyunjung.chamcoachi.ui.component

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hyunjung.chamcoachi.R

@Composable
fun ChamCoachiHeader(modifier: Modifier = Modifier) {
  val configuration = LocalConfiguration.current
  val isLandscape = configuration.orientation == ORIENTATION_LANDSCAPE

  val contentScale = if (isLandscape) ContentScale.Fit else ContentScale.Crop

  val headerModifier = if (isLandscape) {
    modifier.heightIn(max = 200.dp)
  } else {
    modifier.fillMaxWidth()
  }

  Image(
    modifier = headerModifier,
    painter = painterResource(R.drawable.img_cham_coach_title),
    contentDescription = null,
    contentScale = contentScale,
  )
}
