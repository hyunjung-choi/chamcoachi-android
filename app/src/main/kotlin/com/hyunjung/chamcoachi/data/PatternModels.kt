package com.hyunjung.chamcoachi.data

enum class Arrow {
  LEFT,
  RIGHT,
}

data class PatternItem(
  val order: Int,
  val arrows: List<Arrow>,
)

data class PatternSet(
  val id: String,
  val title: String,
  val description: String,
  val items: List<PatternItem>,
)
