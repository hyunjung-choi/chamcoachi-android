package com.hyunjung.chamcoachi.review

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewHistoryRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    suspend fun incrementLaunchCount() {
        try {
            dataStore.edit { prefs ->
                prefs[LAUNCH_COUNT] = (prefs[LAUNCH_COUNT] ?: 0) + 1
            }
        } catch (e: IOException) {
            Log.w(TAG, "Failed to update launch count", e)
        }
    }

    suspend fun tryConsumeReviewOpportunity(now: Long = System.currentTimeMillis()): Boolean {
        var granted = false
        try {
            dataStore.edit { prefs ->
                val history = prefs.toReviewHistory()
                granted = ReviewRequestPolicy.shouldRequest(history, now)
                if (granted) {
                    prefs[LAST_REQUESTED_AT] = now
                    prefs[REQUEST_COUNT] = history.requestCount + 1
                }
            }
        } catch (e: IOException) {
            Log.w(TAG, "Failed to update review history", e)
            return false
        }
        return granted
    }

    private fun Preferences.toReviewHistory() = ReviewHistory(
        launchCount = this[LAUNCH_COUNT] ?: 0,
        lastRequestedAt = this[LAST_REQUESTED_AT] ?: 0L,
        requestCount = this[REQUEST_COUNT] ?: 0,
    )

    private companion object {
        const val TAG = "ReviewHistory"
        val LAUNCH_COUNT = intPreferencesKey("review_launch_count")
        val LAST_REQUESTED_AT = longPreferencesKey("review_last_requested_at")
        val REQUEST_COUNT = intPreferencesKey("review_request_count")
    }
}
