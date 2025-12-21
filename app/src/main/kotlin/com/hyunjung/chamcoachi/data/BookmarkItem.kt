package com.hyunjung.chamcoachi.data

import java.util.UUID

data class BookmarkItem(
  val id: String = UUID.randomUUID().toString(),
  val stepIndex: Int,
  val title: String = "",
  val createdAt: Long = System.currentTimeMillis(),
  val color: BookmarkColor = BookmarkColor.PINK,
)
