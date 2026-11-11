package com.hyunjung.chamcoachi.review

import android.app.Activity
import android.util.Log
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManager
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class InAppReviewLauncher @Inject constructor(
    private val reviewManager: ReviewManager,
) {

    suspend fun launch(activity: Activity) {
        try {
            val reviewInfo = reviewManager.requestReview()
            reviewManager.launchReview(activity, reviewInfo)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.w(TAG, "In-app review flow failed", e)
        }
    }

    private companion object {
        const val TAG = "InAppReview"
    }
}
