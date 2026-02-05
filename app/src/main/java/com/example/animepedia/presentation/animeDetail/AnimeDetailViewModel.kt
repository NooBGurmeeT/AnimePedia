package com.example.animepedia.presentation.animeDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animepedia.analytics.eventLogger.EventConstants
import com.example.animepedia.analytics.eventLogger.EventLogger
import com.example.animepedia.domain.common.Result
import com.example.animepedia.domain.model.AnimeDetailModel
import com.example.animepedia.domain.usecase.GetAnimeDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    private val getAnimeDetailUseCase: GetAnimeDetailUseCase,
    private val eventLogger: EventLogger
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _animeDetail = MutableStateFlow<AnimeDetailModel?>(null)
    val animeDetail: StateFlow<AnimeDetailModel?> = _animeDetail

    private val _toast = MutableSharedFlow<String>(replay = 0)
    val toast: SharedFlow<String> = _toast

    fun loadAnimeDetail(animeId: Int, isOnline: Boolean) {

        eventLogger.logEvent(
            EventConstants.EventName.ANIME_DETAIL_OPENED,
            mapOf(
                EventConstants.Param.ANIME_ID to animeId,
                EventConstants.Param.IS_ONLINE to isOnline
            )
        )

        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true

            when (val result = getAnimeDetailUseCase(animeId, isOnline)) {
                is Result.Success -> {
                    _animeDetail.value = result.data
                }

                is Result.Error -> {
                    _toast.emit(result.message)
                }
            }

            _loading.value = false
        }
    }
}
