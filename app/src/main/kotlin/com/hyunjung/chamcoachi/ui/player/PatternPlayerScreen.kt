package com.hyunjung.chamcoachi.ui.player

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyunjung.chamcoachi.data.Arrow
import com.hyunjung.chamcoachi.data.BookmarkItem
import com.hyunjung.chamcoachi.data.PatternItem
import com.hyunjung.chamcoachi.data.PatternsRepository
import com.hyunjung.chamcoachi.ui.component.AddBookmarkDialog
import com.hyunjung.chamcoachi.ui.component.ChamCoachiBottomBar
import com.hyunjung.chamcoachi.ui.component.ChamCoachiHeader
import com.hyunjung.chamcoachi.ui.component.PatternDisplayCard
import com.hyunjung.chamcoachi.ui.component.SearchResultItem
import com.hyunjung.chamcoachi.ui.screens.BookmarkManagementScreen
import com.hyunjung.chamcoachi.ui.theme.ChamCoachiGray01
import com.hyunjung.chamcoachi.ui.theme.ChamCoachiTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PatternPlayerScreen(
  viewModel: PatternPlayerViewModel,
  modifier: Modifier = Modifier,
) {
  val state by viewModel.uiState.collectAsState()

  var showAddBookmarkDialog by remember { mutableStateOf(false) }
  var pendingBookmarkStepIndex by remember { mutableStateOf(-1) }

  PatternPlayerScreenContent(
    currentIndex = state.currentIndex,
    totalItems = state.totalItems,
    arrows = state.currentItem?.arrows ?: emptyList(),
    bookmarks = state.bookmarks,
    currentStepBookmarks = state.currentStepBookmarks,
    canAddMoreBookmarks = state.bookmarks.size < state.maxBookmarks,
    bookmarkedIndex = state.bookmarkedIndex,
    isAtBookmark = state.isAtBookmark,
    searchQuery = state.searchQuery,
    searchResults = state.searchResults,
    isSearchMode = state.isSearchMode,
    isBookmarkMode = state.isBookmarkMode,
    onIndexChange = { viewModel.setIndex(it) },
    onSaveBookmark = { viewModel.saveBookmark() },
    onGoToBookmark = { viewModel.goToBookmark() },
    onAddBookmark = { stepIndex ->
      pendingBookmarkStepIndex = stepIndex
      showAddBookmarkDialog = true
    },
    onDeleteBookmark = { bookmarkId -> viewModel.deleteBookmark(bookmarkId) },
    onGoToBookmarkById = { bookmarkId -> viewModel.goToBookmark(bookmarkId) },
    onToggleSearch = { viewModel.toggleSearchMode() },
    onToggleBookmark = { viewModel.toggleBookmarkMode() },
    onSearchQueryChange = { viewModel.updateSearchQuery(it) },
    onGoToSearchResult = { viewModel.goToSearchResult(it) },
    modifier = modifier,
  )

  if (showAddBookmarkDialog && pendingBookmarkStepIndex >= 0) {
    AddBookmarkDialog(
      currentStep = pendingBookmarkStepIndex,
      existingBookmarksCount = state.bookmarks.size,
      onDismiss = {
        showAddBookmarkDialog = false
        pendingBookmarkStepIndex = -1
      },
      onConfirm = { title, color ->
        viewModel.setIndex(pendingBookmarkStepIndex)
        viewModel.addBookmark(title, color)
        showAddBookmarkDialog = false
        pendingBookmarkStepIndex = -1
      },
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PatternPlayerScreenContent(
  currentIndex: Int,
  totalItems: Int,
  arrows: List<Arrow>,
  bookmarks: List<BookmarkItem>,
  currentStepBookmarks: List<BookmarkItem>,
  canAddMoreBookmarks: Boolean,
  bookmarkedIndex: Int,
  isAtBookmark: Boolean,
  searchQuery: String,
  searchResults: List<SearchResult>,
  isSearchMode: Boolean,
  isBookmarkMode: Boolean,
  onIndexChange: (Int) -> Unit,
  onSaveBookmark: () -> Unit,
  onGoToBookmark: () -> Unit,
  onAddBookmark: (Int) -> Unit,
  onDeleteBookmark: (String) -> Unit,
  onGoToBookmarkById: (String) -> Unit,
  onToggleSearch: () -> Unit,
  onToggleBookmark: () -> Unit,
  onSearchQueryChange: (String) -> Unit,
  onGoToSearchResult: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val keyboardController = LocalSoftwareKeyboardController.current
  val configuration = LocalConfiguration.current
  val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

  // 무한 순환을 위해 실제 아이템 수 + 2개만 사용 (양 끝에 가짜 페이지 1개씩)
  // [마지막 아이템] [0] [1] ... [n-1] [첫 번째 아이템]
  val pageCount = if (totalItems > 0) totalItems + 2 else 1
  val startPage = if (totalItems > 0) currentIndex + 1 else 0 // +1은 앞에 가짜 페이지가 있어서

  val pagerState = rememberPagerState(
    initialPage = startPage,
    pageCount = { pageCount },
  )
  val coroutineScope = rememberCoroutineScope()

  // 페이지 인덱스를 실제 아이템 인덱스로 변환
  fun pageToActualIndex(page: Int): Int {
    if (totalItems <= 0) return 0
    return when (page) {
      0 -> totalItems - 1 // 가짜 첫 페이지 = 마지막 아이템
      pageCount - 1 -> 0 // 가짜 마지막 페이지 = 첫 번째 아이템
      else -> page - 1 // 실제 페이지 (1~totalItems) → 아이템 인덱스 (0~totalItems-1)
    }
  }

  // 실제 아이템 인덱스를 페이지로 변환
  fun actualIndexToPage(index: Int): Int {
    return index + 1 // 아이템 인덱스 + 1 = 실제 페이지 위치
  }

  // currentIndex가 외부에서 변경되면 해당 페이지로 이동
  LaunchedEffect(currentIndex) {
    val targetPage = actualIndexToPage(currentIndex)
    if (pagerState.currentPage != targetPage) {
      pagerState.animateScrollToPage(targetPage)
    }
  }

  // 스크롤이 끝났을 때 처리
  LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
    if (!pagerState.isScrollInProgress && totalItems > 0) {
      when (pagerState.currentPage) {
        0 -> {
          // 가짜 첫 페이지에 도달 → 실제 마지막 페이지로 즉시 점프
          pagerState.scrollToPage(totalItems)
        }

        pageCount - 1 -> {
          // 가짜 마지막 페이지에 도달 → 실제 첫 페이지로 즉시 점프
          pagerState.scrollToPage(1)
        }

        else -> {
          // 정상 페이지 - 인덱스 업데이트
          val actualIndex = pageToActualIndex(pagerState.currentPage)
          if (actualIndex != currentIndex) {
            onIndexChange(actualIndex)
          }
        }
      }
    }
  }

  val density = LocalDensity.current
  var headerWidth by remember { mutableStateOf(0.dp) }

  val scrollState = rememberScrollState()

  BoxWithConstraints(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.TopCenter,
  ) {
    val maxWidth = this.maxWidth
    val maxHeight = this.maxHeight
    val headerMaxWidth = minOf(maxWidth, 1280.dp)

    // 가로 모드에서 콘텐츠 영역 높이 계산
    val contentHeight = if (isLandscape) {
      maxHeight * 0.5f // 가로 모드에서는 화면 높이의 50%
    } else {
      null // 세로 모드에서는 weight 사용
    }

    Column(
      modifier = Modifier
        .align(Alignment.TopCenter)
        .then(
          if (isLandscape) {
            Modifier.verticalScroll(scrollState)
          } else {
            Modifier
          },
        ),
    ) {
      ChamCoachiHeader(
        modifier = Modifier
          .align(Alignment.CenterHorizontally)
          .widthIn(min = 360.dp, max = headerMaxWidth)
          .onGloballyPositioned { coordinates ->
            with(density) {
              headerWidth = coordinates.size.width.toDp()
            }
          },
      )

      Column(
        modifier = if (headerWidth > 0.dp) {
          Modifier.width(headerWidth)
        } else {
          Modifier.fillMaxWidth()
        }.then(
          if (!isLandscape) {
            Modifier.weight(1f)
          } else {
            Modifier
          },
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        // Header Section with Search Toggle
        Column(
          modifier = Modifier.padding(horizontal = 16.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
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
                imeAction = ImeAction.Search,
              ),
              keyboardActions = KeyboardActions(
                onSearch = { keyboardController?.hide() },
              ),
              modifier = Modifier.fillMaxWidth(),
              singleLine = true,
            )
            Spacer(modifier = Modifier.height(12.dp))
          }

          // Bookmark UI (when bookmark mode is active)
          if (isBookmarkMode) {
            BookmarkManagementScreen(
              bookmarks = bookmarks,
              canAddMore = canAddMoreBookmarks,
              onAddBookmark = { onAddBookmark(currentIndex) },
              onBookmarkClick = { bookmark: BookmarkItem ->
                onGoToBookmarkById(
                  bookmark.id,
                )
              },
              onDeleteBookmark = { bookmark: BookmarkItem -> onDeleteBookmark(bookmark.id) },
              onToggleBookmark = onToggleBookmark,
            )
          }
        }

        // Main Content Area
        if (isSearchMode) {
          Column(
            modifier = Modifier
              .then(
                if (isLandscape) {
                  Modifier.height(300.dp)
                } else {
                  Modifier.weight(1f)
                },
              )
              .padding(horizontal = 36.dp),
          ) {
            Text(
              modifier = Modifier.padding(horizontal = 8.dp),
              text = if (searchResults.isNotEmpty()) "검색 결과 (${searchResults.size}개)" else "",
              style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight(500),
                color = ChamCoachiGray01,
                textAlign = TextAlign.Center,
              ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (searchResults.isEmpty()) {
              Text(
                text = "일치하는 패턴이 없습니다.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(16.dp),
              )
            } else {
              LazyColumn(
                contentPadding = PaddingValues(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
              ) {
                items(searchResults) { result ->
                  SearchResultItem(
                    result = result,
                    onClick = { onGoToSearchResult(result.index) },
                  )
                }
              }
            }
          }
        } else if (isBookmarkMode) {
          if (isLandscape) {
            Spacer(modifier = Modifier.height(100.dp))
          } else {
            Spacer(modifier = Modifier.weight(1f))
          }
        } else {
          // HorizontalPager for smooth swipe navigation (무한 순환)
          HorizontalPager(
            state = pagerState,
            modifier = Modifier
              .then(
                if (isLandscape) {
                  Modifier.height(420.dp) // 가로 모드에서 고정 높이 (북마크 240dp + 화살표 영역)
                } else {
                  Modifier.weight(1f) // 세로 모드에서는 남은 공간 채우기
                },
              ),
            contentPadding = PaddingValues(horizontal = 0.dp),
          ) { pageIndex ->
            val actualIndex = pageToActualIndex(pageIndex)
            val pageArrows = if (actualIndex in 0 until totalItems) {
              PatternsRepository.tamagotchiArrowSet.items.getOrNull(actualIndex)?.arrows
                ?: emptyList()
            } else {
              emptyList()
            }

            PatternDisplayCard(
              arrows = pageArrows,
              currentStepBookmarks = if (actualIndex == currentIndex) currentStepBookmarks else emptyList(),
              canAddMoreBookmarks = canAddMoreBookmarks,
              onSaveBookmark = onSaveBookmark,
              onAddBookmark = { onAddBookmark(actualIndex) },
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ChamCoachiBottomBar(
          onToggleSearch = onToggleSearch,
          onToggleBookmark = onToggleBookmark,
          onGoToBookmark = onGoToBookmark,
          isSearchMode = isSearchMode,
          isBookmarkMode = isBookmarkMode,
          isAtBookmark = isAtBookmark,
          currentIndex = currentIndex,
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun PatternPlayerScreenPreview() {
  ChamCoachiTheme {
    PatternPlayerScreenContent(
      currentIndex = 2,
      totalItems = 18,
      arrows = listOf(Arrow.RIGHT, Arrow.RIGHT, Arrow.LEFT, Arrow.LEFT, Arrow.RIGHT),
      bookmarkedIndex = 4,
      isAtBookmark = false,
      searchQuery = "",
      searchResults = emptyList(),
      isSearchMode = false,
      isBookmarkMode = false,
      onIndexChange = {},
      onSaveBookmark = {},
      onGoToBookmark = {},
      onToggleSearch = {},
      onToggleBookmark = {},
      onSearchQueryChange = {},
      onGoToSearchResult = {},
      bookmarks = listOf(),
      currentStepBookmarks = listOf(),
      canAddMoreBookmarks = true,
      onAddBookmark = {},
      onDeleteBookmark = {},
      onGoToBookmarkById = {},
      modifier = Modifier,
    )
  }
}

@Preview(showBackground = true, name = "Search Mode")
@Composable
private fun PatternPlayerScreenSearchPreview() {
  ChamCoachiTheme {
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
            listOf(Arrow.RIGHT, Arrow.RIGHT, Arrow.RIGHT, Arrow.LEFT, Arrow.LEFT),
          ),
        ),
        SearchResult(
          2,
          PatternItem(
            3,
            listOf(Arrow.RIGHT, Arrow.RIGHT, Arrow.LEFT, Arrow.LEFT, Arrow.RIGHT),
          ),
        ),
      ),
      isSearchMode = true,
      isBookmarkMode = false,
      onIndexChange = {},
      onSaveBookmark = {},
      onGoToBookmark = {},
      onToggleSearch = {},
      onToggleBookmark = {},
      onSearchQueryChange = {},
      onGoToSearchResult = {},
      bookmarks = listOf(),
      currentStepBookmarks = listOf(),
      canAddMoreBookmarks = true,
      onAddBookmark = {},
      onDeleteBookmark = {},
      onGoToBookmarkById = {},
      modifier = Modifier,
    )
  }
}

@Preview(showBackground = true, name = "Bookmark Mode")
@Composable
private fun PatternPlayerScreenBookmarkPreview() {
  ChamCoachiTheme {
    PatternPlayerScreenContent(
      currentIndex = 2,
      totalItems = 18,
      arrows = listOf(Arrow.RIGHT, Arrow.RIGHT, Arrow.LEFT, Arrow.LEFT, Arrow.RIGHT),
      bookmarkedIndex = 15,
      isAtBookmark = false,
      searchQuery = "",
      searchResults = emptyList(),
      isSearchMode = false,
      isBookmarkMode = true,
      onIndexChange = {},
      onSaveBookmark = {},
      onGoToBookmark = {},
      onToggleSearch = {},
      onToggleBookmark = {},
      onSearchQueryChange = {},
      onGoToSearchResult = {},
      bookmarks = listOf(),
      currentStepBookmarks = listOf(),
      canAddMoreBookmarks = true,
      onAddBookmark = {},
      onDeleteBookmark = {},
      onGoToBookmarkById = {},
      modifier = Modifier,
    )
  }
}
