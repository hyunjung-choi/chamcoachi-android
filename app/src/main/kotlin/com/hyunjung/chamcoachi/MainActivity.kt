package com.hyunjung.chamcoachi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.chamcoach.core.designsystem.ChamCoachiTheme
import com.hyunjung.chamcoachi.review.InAppReviewLauncher
import com.hyunjung.chamcoachi.review.ReviewHistoryRepository
import com.hyunjung.chamcoachi.ui.player.PatternPlayerScreen
import com.hyunjung.chamcoachi.ui.player.PatternPlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var reviewHistoryRepository: ReviewHistoryRepository

    @Inject
    lateinit var inAppReviewLauncher: InAppReviewLauncher

    private val viewModel: PatternPlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (savedInstanceState == null) {
            lifecycleScope.launch { reviewHistoryRepository.incrementLaunchCount() }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reviewRequests.collect {
                    inAppReviewLauncher.launch(this@MainActivity)
                }
            }
        }

        setContent {
            ChamCoachiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PatternPlayerScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}
