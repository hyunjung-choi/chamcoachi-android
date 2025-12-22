package com.hyunjung.chamcoachi.data

object PatternsRepository {

  private fun parseArrowsFromDigits(digits: String): List<Arrow> {
    return digits.map { ch ->
      when (ch) {
        '1' -> Arrow.LEFT
        '2' -> Arrow.RIGHT
        else -> throw IllegalArgumentException("Unsupported arrow digit: $ch. Only '1' for LEFT and '2' for RIGHT are supported.")
      }
    }
  }

  private val tamagotchiArrowDigits: List<String> = listOf(
    // 1 = LEFT, 2 = RIGHT
    "22211",
    "12121",
    "22112",
    "12122",
    "21112",
    "12122",
    "11212",
    "12221",
    "11212",
    "12211",
    "21212",
    "22111",
    "21212",
    "21121",
    "21222",
    "11121",
    "21221",
    "12121",
  )

  val tamagotchiArrowSet: PatternSet by lazy {
    val items = tamagotchiArrowDigits.mapIndexed { index, digits ->
      PatternItem(order = index + 1, arrows = parseArrowsFromDigits(digits))
    }
    PatternSet(
      id = "tamagotchi_arrow",
      title = "참참참 패턴",
      description = "다마고치 파라다이스 참참참(arrow) 18단계 패턴. 끝나면 처음으로 순환.",
      items = items,
    )
  }
}
