package com.hyunjung.chamcoachi.ui.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hyunjung.chamcoachi.data.Arrow
import com.hyunjung.chamcoachi.data.BookmarkColor
import com.hyunjung.chamcoachi.data.BookmarkHelper
import com.hyunjung.chamcoachi.data.BookmarkItem
import com.hyunjung.chamcoachi.data.PatternItem
import com.hyunjung.chamcoachi.data.PatternsRepository
import com.hyunjung.chamcoachi.data.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchResult(
  val index: Int,
  val item: PatternItem,
)

data class PlayerUiState(
  val currentIndex: Int = 0,
  val totalItems: Int = 0,
  val currentItem: PatternItem? = null,

  // 다중 북마크 관련
  val bookmarks: List<BookmarkItem> = emptyList(),
  val maxBookmarks: Int = 3,
  // 현재 단계의 북마크들
  val currentStepBookmarks: List<BookmarkItem> = emptyList(),

  // 하위 호환 (기존 UI용)
  val bookmarkedIndex: Int = 0,
  val isAtBookmark: Boolean = false,

  // 검색 관련
  val searchQuery: String = "",
  val searchResults: List<SearchResult> = emptyList(),
  val isSearchMode: Boolean = false,
  val isBookmarkMode: Boolean = false,
)

class PatternPlayerViewModel(application: Application) : AndroidViewModel(application) {

  private val appContext = application.applicationContext
  private val patternSet = PatternsRepository.tamagotchiArrowSet

  private val _uiState = MutableStateFlow(
    PlayerUiState(
      currentIndex = 0,
      totalItems = patternSet.items.size,
      currentItem = patternSet.items.getOrNull(0),
    ),
  )
  val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

  init {
    // 다중 북마크 관찰
    viewModelScope.launch {
      UserPreferences.observeBookmarks(appContext, patternSet.id).collect { bookmarks ->
        _uiState.update { prev ->
          val currentStepBookmarks =
            BookmarkHelper.findBookmarksByStep(bookmarks, prev.currentIndex)

          prev.copy(
            bookmarks = bookmarks,
            currentStepBookmarks = currentStepBookmarks,
            // 하위 호환용
            bookmarkedIndex = bookmarks.firstOrNull()?.stepIndex ?: 0,
            isAtBookmark = currentStepBookmarks.isNotEmpty(),
          )
        }

        // 첫 로드 시 첫 번째 북마크로 이동 (기존 로직 유지)
        if (_uiState.value.currentIndex == 0 && bookmarks.isNotEmpty()) {
          setIndexInternal(bookmarks.first().stepIndex, persist = false)
        }
      }
    }
  }

  // === 기존 함수들 (하위 호환) ===

  fun next() {
    val nextIndex = (_uiState.value.currentIndex + 1).floorMod(_uiState.value.totalItems)
    setIndexInternal(nextIndex, persist = false)
  }

  fun prev() {
    val prevIndex = (_uiState.value.currentIndex - 1).floorMod(_uiState.value.totalItems)
    setIndexInternal(prevIndex, persist = false)
  }

  fun reset() {
    setIndexInternal(0, persist = false)
  }

  fun setIndex(index: Int) {
    val bounded = index.floorMod(_uiState.value.totalItems)
    setIndexInternal(bounded, persist = false)
  }

  fun saveBookmark() {
    // 첫 번째 북마크가 없으면 새로 생성, 있으면 업데이트
    val currentBookmarks = _uiState.value.bookmarks
    val currentIndex = _uiState.value.currentIndex

    viewModelScope.launch {
      if (currentBookmarks.isEmpty()) {
        // 새 북마크 생성
        val newBookmark = BookmarkHelper.createBookmark(currentIndex)
        UserPreferences.addBookmark(appContext, patternSet.id, newBookmark)
      } else {
        // 첫 번째 북마크 업데이트 (기존 동작 유지)
        val updatedBookmark = currentBookmarks.first().copy(stepIndex = currentIndex)
        UserPreferences.updateBookmark(appContext, patternSet.id, updatedBookmark)
      }
    }
  }

  fun goToBookmark() {
    val firstBookmark = _uiState.value.bookmarks.firstOrNull()
    if (firstBookmark != null) {
      setIndexInternal(firstBookmark.stepIndex, persist = false)
    }
  }

  // === 새로운 다중 북마크 함수들 ===

  /**
   * 새 북마크 추가
   */
  fun addBookmark(title: String = "", color: BookmarkColor = BookmarkColor.PINK) {
    val currentIndex = _uiState.value.currentIndex
    val currentBookmarks = _uiState.value.bookmarks

    if (!BookmarkHelper.canAddBookmark(currentBookmarks, _uiState.value.maxBookmarks)) {
      return
    }

    viewModelScope.launch {
      val newBookmark = BookmarkHelper.createBookmark(
        stepIndex = currentIndex,
        title = title.ifEmpty { "다마고치 ${currentBookmarks.size + 1}" },
        color = color,
      )
      UserPreferences.addBookmark(appContext, patternSet.id, newBookmark)
    }
  }

  /**
   * 북마크 삭제
   */
  fun deleteBookmark(bookmarkId: String) {
    viewModelScope.launch {
      UserPreferences.deleteBookmark(appContext, patternSet.id, bookmarkId)
    }
  }

  /**
   * 북마크 업데이트
   */
  fun updateBookmark(bookmarkId: String, title: String? = null, color: BookmarkColor? = null) {
    val bookmark = _uiState.value.bookmarks.find { it.id == bookmarkId } ?: return

    val updatedBookmark = bookmark.copy(
      title = title ?: bookmark.title,
      color = color ?: bookmark.color,
    )

    viewModelScope.launch {
      UserPreferences.updateBookmark(appContext, patternSet.id, updatedBookmark)
    }
  }

  /**
   * 특정 북마크로 이동
   */
  fun goToBookmark(bookmarkId: String) {
    val bookmark = _uiState.value.bookmarks.find { it.id == bookmarkId }
    if (bookmark != null) {
      setIndexInternal(bookmark.stepIndex, persist = false)
    }
  }

  /**
   * 현재 단계가 북마크되어 있는지 확인
   */
  fun isCurrentStepBookmarked(): Boolean {
    return _uiState.value.currentStepBookmarks.isNotEmpty()
  }

  /**
   * 북마크 추가 가능 여부 확인
   */
  fun canAddMoreBookmarks(): Boolean {
    return BookmarkHelper.canAddBookmark(_uiState.value.bookmarks, _uiState.value.maxBookmarks)
  }

  fun toggleSearchMode() {
    _uiState.update { prev ->
      val newSearchMode = !prev.isSearchMode
      prev.copy(
        isSearchMode = newSearchMode,
        isBookmarkMode = false,
        searchQuery = if (newSearchMode) prev.searchQuery else "",
        searchResults = if (newSearchMode) prev.searchResults else emptyList(),
      )
    }
  }

  fun toggleBookmarkMode() {
    _uiState.update { prev ->
      val newBookmarkMode = !prev.isBookmarkMode
      prev.copy(
        isBookmarkMode = newBookmarkMode,
        isSearchMode = false,
        // 검색 상태도 초기화
        searchQuery = if (!newBookmarkMode) "" else prev.searchQuery,
        searchResults = if (!newBookmarkMode) emptyList() else prev.searchResults,
      )
    }
  }

  fun updateSearchQuery(query: String) {
    _uiState.update { prev ->
      prev.copy(searchQuery = query)
    }
    performSearch(query)
  }

  fun goToSearchResult(index: Int) {
    setIndexInternal(index, persist = false)
    _uiState.update { prev ->
      prev.copy(
        isSearchMode = false,
        searchQuery = "",
        searchResults = emptyList(),
      )
    }
  }

  // === 내부 헬퍼 함수들 ===

  private fun performSearch(query: String) {
    if (query.isBlank()) {
      _uiState.update { prev ->
        prev.copy(searchResults = emptyList())
      }
      return
    }

    val searchArrows = try {
      query.filter { it.isDigit() }.map { ch ->
        when (ch) {
          '1' -> Arrow.LEFT
          '2' -> Arrow.RIGHT
          else -> throw IllegalArgumentException("Invalid arrow digit: $ch")
        }
      }
    } catch (e: Exception) {
      _uiState.update { prev ->
        prev.copy(searchResults = emptyList())
      }
      return
    }

    if (searchArrows.isEmpty()) {
      _uiState.update { prev ->
        prev.copy(searchResults = emptyList())
      }
      return
    }

    val results = patternSet.items.mapIndexedNotNull { index, item ->
      if (item.arrows.containsSubsequence(searchArrows)) {
        SearchResult(index, item)
      } else {
        null
      }
    }

    _uiState.update { prev ->
      prev.copy(searchResults = results)
    }
  }

  private fun setIndexInternal(index: Int, persist: Boolean) {
    val bounded =
      if (_uiState.value.totalItems == 0) 0 else index.floorMod(_uiState.value.totalItems)

    _uiState.update { prev ->
      val currentStepBookmarks = BookmarkHelper.findBookmarksByStep(prev.bookmarks, bounded)

      prev.copy(
        currentIndex = bounded,
        currentItem = patternSet.items.getOrNull(bounded),
        currentStepBookmarks = currentStepBookmarks,
        isAtBookmark = currentStepBookmarks.isNotEmpty(),
      )
    }

    if (persist) {
      viewModelScope.launch {
        UserPreferences.saveBookmarkIndex(appContext, patternSet.id, bounded)
      }
    }
  }
}

private fun Int.floorMod(m: Int): Int {
  if (m == 0) return 0
  val r = this % m
  return if (r >= 0) r else r + m
}

private fun <T> List<T>.containsSubsequence(sublist: List<T>): Boolean {
  if (sublist.isEmpty()) return true
  if (sublist.size > this.size) return false

  for (i in 0..this.size - sublist.size) {
    var found = true
    for (j in sublist.indices) {
      if (this[i + j] != sublist[j]) {
        found = false
        break
      }
    }
    if (found) return true
  }
  return false
}
