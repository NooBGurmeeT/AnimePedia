package com.example.animepedia

import android.app.Application
import com.example.animepedia.analytics.logger.AppLogger
import com.example.animepedia.domain.repository.AnimeRepository
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class AnimePediaApplication : Application(){
    @Inject
    lateinit var animeRepository: AnimeRepository

    @Inject
    lateinit var logger: AppLogger

    override fun onCreate() {
        super.onCreate()
        logger.logInfo(
            tag = "AnimePediaApplication",
            message = "Application started"
        )

        CoroutineScope(Dispatchers.IO).launch {
            animeRepository.initializeAnimeDetailCache()
        }
    }
}
