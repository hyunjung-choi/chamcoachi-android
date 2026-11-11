package com.hyunjung.chamcoachi.review

import com.hyunjung.chamcoachi.review.ReviewRequestPolicy.MAX_REQUEST_COUNT
import com.hyunjung.chamcoachi.review.ReviewRequestPolicy.MIN_INTERVAL_MILLIS
import com.hyunjung.chamcoachi.review.ReviewRequestPolicy.MIN_LAUNCH_COUNT
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReviewRequestPolicyTest {

    private val now = 1_700_000_000_000L

    @Test
    fun `실행 횟수가 부족하면 요청하지 않는다`() {
        val history = ReviewHistory(launchCount = MIN_LAUNCH_COUNT - 1)
        assertFalse(ReviewRequestPolicy.shouldRequest(history, now))
    }

    @Test
    fun `실행 횟수를 채웠고 요청 이력이 없으면 요청한다`() {
        val history = ReviewHistory(launchCount = MIN_LAUNCH_COUNT)
        assertTrue(ReviewRequestPolicy.shouldRequest(history, now))
    }

    @Test
    fun `마지막 요청 후 기간이 지나지 않았으면 요청하지 않는다`() {
        val history = ReviewHistory(
            launchCount = MIN_LAUNCH_COUNT,
            lastRequestedAt = now - MIN_INTERVAL_MILLIS + 1,
            requestCount = 1,
        )
        assertFalse(ReviewRequestPolicy.shouldRequest(history, now))
    }

    @Test
    fun `마지막 요청 후 기간이 지났으면 다시 요청한다`() {
        val history = ReviewHistory(
            launchCount = MIN_LAUNCH_COUNT,
            lastRequestedAt = now - MIN_INTERVAL_MILLIS,
            requestCount = 1,
        )
        assertTrue(ReviewRequestPolicy.shouldRequest(history, now))
    }

    @Test
    fun `최대 요청 횟수에 도달하면 더 요청하지 않는다`() {
        val history = ReviewHistory(
            launchCount = 100,
            lastRequestedAt = 0L,
            requestCount = MAX_REQUEST_COUNT,
        )
        assertFalse(ReviewRequestPolicy.shouldRequest(history, now))
    }
}
