package com.example.animepedia.presentation.animeListing

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import com.example.animepedia.analytics.logger.AppLogger
import com.example.animepedia.presentation.animeDetail.AnimeDetailActivity
import com.example.animepedia.presentation.util.NetworkUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AnimeListingActivity : ComponentActivity() {

    private val viewModel: AnimeListViewModel by viewModels()

    @Inject
    lateinit var appLogger: AppLogger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appLogger.logInfo("AnimeListingActivity", "onCreate called")

        val isInternetAvailable = NetworkUtil.isInternetAvailable(this)
        observeToast()

        setContent {
            AnimeListingUI().LayoutUI(
                animeList = viewModel.filteredAnimeList.collectAsState().value,
                totalCount = viewModel.animeList.collectAsState().value.size,
                loading = viewModel.loading.collectAsState().value,
                onLoadNextPage = {
                    viewModel.loadNextPage(isOnline = isInternetAvailable)
                },
                onSearch = { query ->
                    viewModel.onSearch(query)
                },
                onAnimeClick = { id, title ->
                    openAnimeDetail(id, title)
                }
            )
        }

        viewModel.loadNextPage(isOnline = isInternetAvailable)
    }

    private fun openAnimeDetail(animeId: Int, animeTitle: String) {
        try {
            val intent = Intent(this, AnimeDetailActivity::class.java).apply {
                putExtra(AnimeDetailActivity.EXTRA_ANIME_ID, animeId)
                putExtra(AnimeDetailActivity.EXTRA_ANIME_TITLE, animeTitle)
            }
            startActivity(intent)
        } catch (e: Exception) {
            appLogger.logError("AnimeListingActivity", "Navigation failed", e)
        }
    }

    private fun observeToast() {
        lifecycleScope.launch {
            viewModel.toastMessage.collectLatest { message ->
                Toast.makeText(
                    this@AnimeListingActivity,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
