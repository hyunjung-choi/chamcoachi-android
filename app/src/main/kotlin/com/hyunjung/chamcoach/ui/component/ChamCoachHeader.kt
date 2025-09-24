package com.hyunjung.chamcoach.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.hyunjung.chamcoach.R

@Composable
fun ChamCoachHeader(modifier: Modifier = Modifier) {
  Image(
    modifier = modifier.fillMaxWidth(),
    painter = painterResource(R.drawable.img_cham_coach_title),
    contentDescription = null,
    contentScale = ContentScale.Crop,
  )
}
