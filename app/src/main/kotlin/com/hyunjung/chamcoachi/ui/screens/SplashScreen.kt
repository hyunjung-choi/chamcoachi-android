package com.hyunjung.chamcoachi.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.chamcoach.core.designsystem.ChamCoachiTheme
import com.hyunjung.chamcoachi.R
import com.hyunjung.chamcoachi.ui.component.ChamCoachiHeader

@Composable
fun SplashScreen() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.White),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    ChamCoachiHeader(modifier = Modifier.fillMaxWidth())

    Image(
      painter = painterResource(R.drawable.img_splash),
      contentDescription = null,
      alignment = Alignment.Center,
    )
  }
}

@Preview
@Composable
private fun SplashScreenPreview() {
  ChamCoachiTheme {
    SplashScreen()
  }
}
