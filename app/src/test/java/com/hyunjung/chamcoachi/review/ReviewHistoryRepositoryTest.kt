package com.hyunjung.chamcoachi.review

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.hyunjung.chamcoachi.review.ReviewRequestPolicy.MIN_INTERVAL_MILLIS
import com.hyunjung.chamcoachi.review.ReviewRequestPolicy.MIN_LAUNCH_COUNT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class ReviewHistoryRepositoryTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var dataStoreScope: CoroutineScope
    private lateinit var repository: ReviewHistoryRepository

    private val now = 1_700_000_000_000L

    @Before
    fun setUp() {
        dataStoreScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        val dataStore = PreferenceDataStoreFactory.create(scope = dataStoreScope) {
            File(tempFolder.root, "test.preferences_pb")
        }
        repository = ReviewHistoryRepository(dataStore)
    }

    @After
    fun tearDown() {
        dataStoreScope.cancel()
    }

    @Test
    fun `실행 횟수를 채우기 전에는 요청하지 않는다`() = runTest {
        repeat(MIN_LAUNCH_COUNT - 1) { repository.incrementLaunchCount() }

        assertFalse(repository.tryConsumeReviewOpportunity(now))
    }

    @Test
    fun `한 번 요청하면 기간이 지날 때까지 다시 요청하지 않는다`() = runTest {
        repeat(MIN_LAUNCH_COUNT) { repository.incrementLaunchCount() }

        assertTrue(repository.tryConsumeReviewOpportunity(now))
        assertFalse(repository.tryConsumeReviewOpportunity(now + 1))
        assertTrue(repository.tryConsumeReviewOpportunity(now + MIN_INTERVAL_MILLIS))
    }
}
