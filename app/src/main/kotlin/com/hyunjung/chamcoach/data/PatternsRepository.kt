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
