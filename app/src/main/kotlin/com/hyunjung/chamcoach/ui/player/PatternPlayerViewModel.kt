package com.hyunjung.chamcoach.ui.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hyunjung.chamcoach.data.Arrow
import com.hyunjung.chamcoach.data.PatternItem
import com.hyunjung.chamcoach.data.PatternsRepository
import com.hyunjung.chamcoach.data.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchResult(
    val index: Int,
    val item: PatternItem
)

data class PlayerUiState(
    val currentIndex: Int = 0,
    val totalItems: Int = 0,
    val currentItem: PatternItem? = null,
    val bookmarkedIndex: Int = 0,
    val isAtBookmark: Boolean = true,
    val searchQuery: String = "",
    val searchResults: List<SearchResult> = emptyList(),
    val isSearchMode: Boolean = false
)

class PatternPlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    private val patternSet = PatternsRepository.tamagotchiArrowSet

    private val _uiState = MutableStateFlow(
        PlayerUiState(
            currentIndex = 0,
            totalItems = patternSet.items.size,
            currentItem = patternSet.items.getOrNull(0),
            bookmarkedIndex = 0,
            isAtBookmark = true
        )
    )
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    init {
        // Observe saved bookmark and update state
        viewModelScope.launch {
            UserPreferences.observeBookmarkIndex(appContext, patternSet.id).collect { savedIndex ->
                _uiState.update { prev ->
                    prev.copy(
                        bookmarkedIndex = savedIndex,
                        isAtBookmark = prev.currentIndex == savedIndex
                    )
                }
                // Only set current index on first load
                if (_uiState.value.currentIndex == 0 && savedIndex != 0) {
                    setIndexInternal(savedIndex, persist = false)
                }
            }
        }
    }

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
        val currentIndex = _uiState.value.currentIndex
        viewModelScope.launch {
            UserPreferences.saveBookmarkIndex(appContext, patternSet.id, currentIndex)
        }
    }

    fun goToBookmark() {
        setIndexInternal(_uiState.value.bookmarkedIndex, persist = false)
    }

    fun toggleSearchMode() {
        _uiState.update { prev ->
            prev.copy(
                isSearchMode = !prev.isSearchMode,
                searchQuery = if (prev.isSearchMode) "" else prev.searchQuery,
                searchResults = if (prev.isSearchMode) emptyList() else prev.searchResults
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
        // Close search mode after selecting a result
        _uiState.update { prev ->
            prev.copy(
                isSearchMode = false,
                searchQuery = "",
                searchResults = emptyList()
            )
        }
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) {
            _uiState.update { prev ->
                prev.copy(searchResults = emptyList())
            }
            return
        }

        // Parse search query (1=LEFT, 2=RIGHT)
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

        // Find matching patterns
        val results = patternSet.items.mapIndexedNotNull { index, item ->
            if (item.arrows.containsSubsequence(searchArrows)) {
                SearchResult(index, item)
            } else null
        }

        _uiState.update { prev ->
            prev.copy(searchResults = results)
        }
    }

    private fun setIndexInternal(index: Int, persist: Boolean) {
        val bounded =
            if (_uiState.value.totalItems == 0) 0 else index.floorMod(_uiState.value.totalItems)
        _uiState.update { prev ->
            prev.copy(
                currentIndex = bounded,
                currentItem = patternSet.items.getOrNull(bounded),
                isAtBookmark = bounded == prev.bookmarkedIndex
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
