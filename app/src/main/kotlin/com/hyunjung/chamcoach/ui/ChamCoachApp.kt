package com.hyunjung.chamcoach.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hyunjung.chamcoach.ui.screens.BookmarkScreen
import com.hyunjung.chamcoach.ui.screens.MainScreen
import com.hyunjung.chamcoach.ui.screens.SearchScreen
import com.hyunjung.chamcoach.ui.screens.SplashScreen
import kotlinx.coroutines.launch

@Composable
fun ChamCoachApp(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { 5 })
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> SplashScreen()
                1 -> MainScreen(version = "v2")
                2 -> MainScreen(version = "bookmark")
                3 -> SearchScreen()
                4 -> BookmarkScreen()
            }
        }

        NavigationBar {
            NavigationBarItem(
                selected = pagerState.currentPage == 1,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                icon = { Icon(Icons.Default.Home, contentDescription = "Main") },
                label = { Text("메인") }
            )

            NavigationBarItem(
                selected = pagerState.currentPage == 3,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(3)
                    }
                },
                icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                label = { Text("검색") }
            )

            NavigationBarItem(
                selected = pagerState.currentPage == 4,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(4)
                    }
                },
                icon = { Icon(Icons.Default.Star, contentDescription = "Bookmark") },
                label = { Text("북마크") }
            )
        }
    }
}