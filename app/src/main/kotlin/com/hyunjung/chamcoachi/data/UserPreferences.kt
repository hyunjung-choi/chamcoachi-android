package com.hyunjung.chamcoachi.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val DATASTORE_NAME = "user_prefs"

val Context.userDataStore by preferencesDataStore(name = DATASTORE_NAME)

/**
 * 직렬화 가능한 북마크 데이터
 */
@Serializable
data class SerializableBookmark(
  val id: String,
  val stepIndex: Int,
  val title: String,
  val createdAt: Long,
  val colorName: String,
)

object UserPreferences {

  private val json = Json { ignoreUnknownKeys = true }

  // 기존 단일 북마크 키 (마이그레이션용)
  private fun legacyBookmarkKeyFor(setId: String) = intPreferencesKey("bookmark_index_$setId")

  // 새로운 다중 북마크 키
  private fun bookmarksKeyFor(setId: String) = stringPreferencesKey("bookmarks_$setId")

  /**
   * 북마크 목록 관찰
   */
  fun observeBookmarks(context: Context, setId: String): Flow<List<BookmarkItem>> {
    return context.userDataStore.data.map { prefs: Preferences ->
      val bookmarksJson = prefs[bookmarksKeyFor(setId)]

      if (bookmarksJson != null) {
        // 새로운 다중 북마크 데이터가 있는 경우
        try {
          val serializableBookmarks = json.decodeFromString<List<SerializableBookmark>>(bookmarksJson)
          serializableBookmarks.map { it.toBookmarkItem() }
        } catch (e: Exception) {
          // JSON 파싱 실패 시 빈 리스트 반환
          emptyList()
        }
      } else {
        // 레거시 단일 북마크 마이그레이션
        val legacyIndex = prefs[legacyBookmarkKeyFor(setId)]
        if (legacyIndex != null && legacyIndex >= 0) {
          listOf(
            BookmarkItem(
              stepIndex = legacyIndex,
              title = "기존 북마크",
              color = BookmarkColor.PINK,
            ),
          )
        } else {
          emptyList()
        }
      }
    }
  }

  /**
   * 기존 단일 북마크 인덱스 관찰 (하위 호환용)
   */
  fun observeBookmarkIndex(context: Context, setId: String): Flow<Int> {
    return observeBookmarks(context, setId).map { bookmarks ->
      bookmarks.firstOrNull()?.stepIndex ?: 0
    }
  }

  /**
   * 북마크 추가
   */
  suspend fun addBookmark(context: Context, setId: String, bookmark: BookmarkItem) {
    context.userDataStore.edit { prefs ->
      val currentBookmarks = getCurrentBookmarks(prefs, setId).toMutableList()

      // 최대 3개 제한
      if (currentBookmarks.size >= 3) {
        return@edit
      }

      currentBookmarks.add(bookmark)
      val json = json.encodeToString(currentBookmarks.map { it.toSerializable() })
      prefs[bookmarksKeyFor(setId)] = json

      // 레거시 키 제거 (마이그레이션 완료)
      prefs.remove(legacyBookmarkKeyFor(setId))
    }
  }

  /**
   * 북마크 삭제
   */
  suspend fun deleteBookmark(context: Context, setId: String, bookmarkId: String) {
    context.userDataStore.edit { prefs ->
      val currentBookmarks = getCurrentBookmarks(prefs, setId)
      val updatedBookmarks = currentBookmarks.filter { it.id != bookmarkId }

      if (updatedBookmarks.isEmpty()) {
        prefs.remove(bookmarksKeyFor(setId))
      } else {
        val json = json.encodeToString(updatedBookmarks.map { it.toSerializable() })
        prefs[bookmarksKeyFor(setId)] = json
      }
    }
  }

  /**
   * 북마크 업데이트
   */
  suspend fun updateBookmark(context: Context, setId: String, updatedBookmark: BookmarkItem) {
    context.userDataStore.edit { prefs ->
      val currentBookmarks = getCurrentBookmarks(prefs, setId).toMutableList()
      val index = currentBookmarks.indexOfFirst { it.id == updatedBookmark.id }

      if (index >= 0) {
        currentBookmarks[index] = updatedBookmark
        val json = json.encodeToString(currentBookmarks.map { it.toSerializable() })
        prefs[bookmarksKeyFor(setId)] = json
      }
    }
  }

  /**
   * 기존 단일 북마크 저장 (하위 호환용)
   */
  suspend fun saveBookmarkIndex(context: Context, setId: String, index: Int) {
    // 기존 북마크들을 가져와서 첫 번째 북마크 업데이트 또는 새로 생성
    val bookmarks = context.userDataStore.data.map { prefs ->
      getCurrentBookmarks(prefs, setId)
    }

    context.userDataStore.edit { prefs ->
      val currentBookmarks = getCurrentBookmarks(prefs, setId).toMutableList()

      if (currentBookmarks.isEmpty()) {
        // 새 북마크 생성
        val newBookmark = BookmarkHelper.createBookmark(index)
        currentBookmarks.add(newBookmark)
      } else {
        // 첫 번째 북마크 업데이트
        currentBookmarks[0] = currentBookmarks[0].copy(stepIndex = index)
      }

      val json = json.encodeToString(currentBookmarks.map { it.toSerializable() })
      prefs[bookmarksKeyFor(setId)] = json

      // 레거시 키도 업데이트 (하위 호환)
      prefs[legacyBookmarkKeyFor(setId)] = index
    }
  }

  /**
   * 현재 북마크 목록 가져오기 (내부 헬퍼)
   */
  private fun getCurrentBookmarks(prefs: Preferences, setId: String): List<BookmarkItem> {
    val bookmarksJson = prefs[bookmarksKeyFor(setId)]

    return if (bookmarksJson != null) {
      try {
        val serializableBookmarks = json.decodeFromString<List<SerializableBookmark>>(bookmarksJson)
        serializableBookmarks.map { it.toBookmarkItem() }
      } catch (e: Exception) {
        emptyList()
      }
    } else {
      // 레거시 마이그레이션
      val legacyIndex = prefs[legacyBookmarkKeyFor(setId)]
      if (legacyIndex != null && legacyIndex >= 0) {
        listOf(
          BookmarkItem(
            stepIndex = legacyIndex,
            title = "기존 북마크",
            color = BookmarkColor.PINK,
          ),
        )
      } else {
        emptyList()
      }
    }
  }
}

/**
 * 확장 함수들
 */
private fun BookmarkItem.toSerializable(): SerializableBookmark {
  return SerializableBookmark(
    id = id,
    stepIndex = stepIndex,
    title = title,
    createdAt = createdAt,
    colorName = color.name,
  )
}

private fun SerializableBookmark.toBookmarkItem(): BookmarkItem {
  return BookmarkItem(
    id = id,
    stepIndex = stepIndex,
    title = title,
    createdAt = createdAt,
    color = try {
      BookmarkColor.valueOf(colorName)
    } catch (e: IllegalArgumentException) {
      BookmarkColor.PINK
    },
  )
}
