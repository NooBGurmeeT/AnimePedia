package com.example.animepedia.presentation.animeDetail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import com.example.animepedia.analytics.logger.AppLogger
import com.example.animepedia.presentation.util.NetworkUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AnimeDetailActivity : ComponentActivity() {

    private val viewModel: AnimeDetailViewModel by viewModels()

    @Inject
    lateinit var appLogger: AppLogger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appLogger.logInfo("AnimeDetailActivity", "onCreate called")

        val isInternetAvailable = NetworkUtil.isInternetAvailable(this)
        val animeId = intent.getIntExtra(EXTRA_ANIME_ID, -1)
        val animeTitle = intent.getStringExtra(EXTRA_ANIME_TITLE) ?: "Anime Detail"

        if (animeId == -1) {
            appLogger.logError(
                "AnimeDetailActivity",
                "Invalid animeId received"
            )
            finish()
            return
        }

        observeToast()
        viewModel.loadAnimeDetail(animeId, isInternetAvailable)

        setContent {
            AnimeDetailUI().LayoutUI(
                animeTitle = animeTitle,
                anime = viewModel.animeDetail.collectAsState().value,
                loading = viewModel.loading.collectAsState().value,
                onBack = {
                    appLogger.logInfo("AnimeDetailActivity", "Back pressed")
                    finish()
                },
                isOnline = isInternetAvailable,
            )
        }
    }

    private fun observeToast() {
        lifecycleScope.launch {
            viewModel.toast.collectLatest {
                Toast.makeText(
                    this@AnimeDetailActivity,
                    it,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        const val EXTRA_ANIME_ID = "anime_id"
        const val EXTRA_ANIME_TITLE = "anime_title"
    }
}
