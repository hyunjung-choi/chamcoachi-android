package com.hyunjung.chamcoachi.ui.player

import com.hyunjung.chamcoachi.data.BookmarkItem
import com.hyunjung.chamcoachi.data.PatternItem

data class PlayerUiState(
    val currentIndex: Int = 0,
    val totalItems: Int = 0,
    val currentItem: PatternItem? = null,

    // 다중 북마크
    val bookmarks: List<BookmarkItem> = emptyList(),
    val maxBookmarks: Int = 3,
    // 현재 단계의 북마크들
    val currentStepBookmarks: List<BookmarkItem> = emptyList(),

    // 기존 UI용
    val bookmarkedIndex: Int = 0,
    val isAtBookmark: Boolean = false,

    // 검색
    val searchQuery: String = "",
    val searchResults: List<SearchResult> = emptyList(),
    val isSearchMode: Boolean = false,
    val isBookmarkMode: Boolean = false,
)
