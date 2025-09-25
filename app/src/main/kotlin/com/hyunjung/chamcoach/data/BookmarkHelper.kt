/*
 * Copyright 2025 ChamCoach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyunjung.chamcoach.data

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
