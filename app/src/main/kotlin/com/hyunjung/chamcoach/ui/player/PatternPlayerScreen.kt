package com.hyunjung.chamcoach.ui.player

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyunjung.chamcoach.R
import com.hyunjung.chamcoach.data.Arrow
import com.hyunjung.chamcoach.data.PatternItem
import com.hyunjung.chamcoach.ui.component.ChamCoachBottomBar
import com.hyunjung.chamcoach.ui.component.ChamCoachHeader
import com.hyunjung.chamcoach.ui.component.PatternDisplayCard
import com.hyunjung.chamcoach.ui.component.SearchResultItem
import com.hyunjung.chamcoach.ui.theme.ChamCoachTheme
import com.hyunjung.chamcoach.ui.theme.TamaGray01

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PatternPlayerScreen(
    viewModel: PatternPlayerViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    PatternPlayerScreenContent(
        currentIndex = state.currentIndex,
        totalItems = state.totalItems,
        arrows = state.currentItem?.arrows ?: emptyList(),
        bookmarkedIndex = state.bookmarkedIndex,
        isAtBookmark = state.isAtBookmark,
        searchQuery = state.searchQuery,
        searchResults = state.searchResults,
        isSearchMode = state.isSearchMode,
        onIndexChange = { viewModel.setIndex(it) },
        onSaveBookmark = { viewModel.saveBookmark() },
        onGoToBookmark = { viewModel.goToBookmark() },
        onToggleSearch = { viewModel.toggleSearchMode() },
        onSearchQueryChange = { viewModel.updateSearchQuery(it) },
        onGoToSearchResult = { viewModel.goToSearchResult(it) },
        modifier = modifier
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PatternPlayerScreenContent(
    currentIndex: Int,
    totalItems: Int,
    arrows: List<Arrow>,
    bookmarkedIndex: Int,
    isAtBookmark: Boolean,
    searchQuery: String,
    searchResults: List<SearchResult>,
    isSearchMode: Boolean,
    onIndexChange: (Int) -> Unit,
    onSaveBookmark: () -> Unit,
    onGoToBookmark: () -> Unit,
    onToggleSearch: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onGoToSearchResult: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val virtualPageCount = if (totalItems > 0) totalItems * 1000 else 1000
    val startPage = if (totalItems > 0) (virtualPageCount / 2) + currentIndex else 0

    val pagerState = rememberPagerState(
        initialPage = startPage,
        pageCount = { virtualPageCount }
    )
    val coroutineScope = rememberCoroutineScope()

    fun virtualPageToIndex(virtualPage: Int): Int {
        return if (totalItems > 0) virtualPage % totalItems else 0
    }

    LaunchedEffect(currentIndex) {
        val currentVirtualPage = pagerState.currentPage
        val currentActualIndex = virtualPageToIndex(currentVirtualPage)

        if (currentActualIndex != currentIndex) {
            val targetVirtualPage = currentVirtualPage + (currentIndex - currentActualIndex)
            pagerState.animateScrollToPage(targetVirtualPage)
        }
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            val actualIndex = virtualPageToIndex(pagerState.currentPage)
            if (actualIndex != currentIndex) {
                onIndexChange(actualIndex)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ChamCoachHeader()



        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Section with Search Toggle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Search Bar (when search mode is active)
                if (isSearchMode) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text("패턴 검색 (예: 221, 121...)") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "검색")
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchQueryChange("") }) {
                                    Icon(Icons.Default.Close, contentDescription = "지우기")
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = { keyboardController?.hide() }
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Progress Card (only show when not in search mode)
//                if (!isSearchMode) {
//                    // Simple bookmark indicator
//                    Surface(
//                        shape = RoundedCornerShape(16.dp),
//                        color = if (isAtBookmark)
//                            MaterialTheme.colorScheme.primaryContainer
//                        else
//                            MaterialTheme.colorScheme.secondaryContainer,
//                        modifier = Modifier.padding(horizontal = 4.dp)
//                    ) {
//                    }
//                }
            }

            // Main Content Area
            if (isSearchMode) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        text = "검색 결과 (${searchResults.size}개)",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight(500),
                            color = TamaGray01,
                            textAlign = TextAlign.Center,
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (searchResults.isEmpty()) {
                        Text(
                            text = "일치하는 패턴이 없습니다",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(searchResults) { result ->
                                SearchResultItem(
                                    result = result,
                                    onClick = { onGoToSearchResult(result.index) }
                                )
                            }
                        }
                    }
                }

            } else {
                // HorizontalPager for smooth swipe navigation
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 0.dp)
                ) { virtualPageIndex ->
                    val actualIndex = virtualPageToIndex(virtualPageIndex)
                    val pageArrows = if (actualIndex < totalItems) {
                        com.hyunjung.chamcoach.data.PatternsRepository.tamagotchiArrowSet.items.getOrNull(
                            actualIndex
                        )?.arrows ?: emptyList()
                    } else {
                        emptyList()
                    }

                    PatternDisplayCard(
                        onSaveBookmark = onSaveBookmark,
                        isAtBookmark = isAtBookmark,
//                        stepNumber = actualIndex + 1,
                        arrows = pageArrows
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ChamCoachBottomBar(
                onToggleSearch = onToggleSearch,
                onGoToBookmark = onGoToBookmark,
                isSearchMode = isSearchMode,
                isAtBookmark = isAtBookmark,
                currentIndex = currentIndex
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PatternPlayerScreenPreview() {
    ChamCoachTheme {
        PatternPlayerScreenContent(
            currentIndex = 2,
            totalItems = 18,
            arrows = listOf(Arrow.RIGHT, Arrow.RIGHT, Arrow.LEFT, Arrow.LEFT, Arrow.RIGHT),
            bookmarkedIndex = 4,
            isAtBookmark = false,
            searchQuery = "",
            searchResults = emptyList(),
            isSearchMode = false,
            onIndexChange = {},
            onSaveBookmark = {},
            onGoToBookmark = {},
            onToggleSearch = {},
            onSearchQueryChange = {},
            onGoToSearchResult = {}
        )
    }
}

@Preview(showBackground = true, name = "Search Mode")
@Composable
private fun PatternPlayerScreenSearchPreview() {
    ChamCoachTheme {
        PatternPlayerScreenContent(
            currentIndex = 2,
            totalItems = 18,
            arrows = listOf(Arrow.RIGHT, Arrow.RIGHT, Arrow.LEFT, Arrow.LEFT, Arrow.RIGHT),
            bookmarkedIndex = 4,
            isAtBookmark = false,
            searchQuery = "221",
            searchResults = listOf(
                SearchResult(
                    0,
                    PatternItem(
                        1,
                        listOf(Arrow.RIGHT, Arrow.RIGHT, Arrow.RIGHT, Arrow.LEFT, Arrow.LEFT)
                    )
                ),
                SearchResult(
                    2,
                    PatternItem(
                        3,
                        listOf(Arrow.RIGHT, Arrow.RIGHT, Arrow.LEFT, Arrow.LEFT, Arrow.RIGHT)
                    )
                )
            ),
            isSearchMode = true,
            onIndexChange = {},
            onSaveBookmark = {},
            onGoToBookmark = {},
            onToggleSearch = {},
            onSearchQueryChange = {},
            onGoToSearchResult = {}
        )
    }
}

