package com.hyunjung.chamcoachi.data

object BookmarkHelper {

  /**
   * 새 북마크 생성
   */
  fun createBookmark(
    stepIndex: Int,
    title: String = "",
    color: BookmarkColor = BookmarkColor.PINK,
  ): BookmarkItem {
    val finalTitle = title.ifEmpty { "다마고치 ${(stepIndex + 1)}단계" }
    return BookmarkItem(
      stepIndex = stepIndex,
      title = finalTitle,
      color = color,
    )
  }

  /**
   * 북마크 목록에서 특정 단계 찾기
   */
  fun findBookmarksByStep(bookmarks: List<BookmarkItem>, stepIndex: Int): List<BookmarkItem> {
    return bookmarks.filter { it.stepIndex == stepIndex }
  }

  /**
   * 북마크 개수 제한 체크
   */
  fun canAddBookmark(bookmarks: List<BookmarkItem>, maxCount: Int = 3): Boolean {
    return bookmarks.size < maxCount
  }
}
