package com.hyunjung.chamcoachi.review

import java.util.concurrent.TimeUnit

data class ReviewHistory(
    val launchCount: Int = 0,
    val lastRequestedAt: Long = 0L,
    val requestCount: Int = 0,
)

/**
 * 인앱 리뷰 팝업을 띄울지 판단하는 조건
 *
 * - 앱을 [MIN_LAUNCH_COUNT]번 이상 실행
 * - 마지막 요청 후 [MIN_INTERVAL_MILLIS]가 지났을 것 (첫 요청은 바로 가능)
 * - 누적 요청이 [MAX_REQUEST_COUNT]번 미만
 */
object ReviewRequestPolicy {
    const val MIN_LAUNCH_COUNT = 3
    const val MAX_REQUEST_COUNT = 3
    val MIN_INTERVAL_MILLIS: Long = TimeUnit.DAYS.toMillis(30)

    fun shouldRequest(history: ReviewHistory, now: Long): Boolean {
        if (history.launchCount < MIN_LAUNCH_COUNT) return false
        if (history.requestCount >= MAX_REQUEST_COUNT) return false
        if (history.requestCount == 0) return true
        return now - history.lastRequestedAt >= MIN_INTERVAL_MILLIS
    }
}
