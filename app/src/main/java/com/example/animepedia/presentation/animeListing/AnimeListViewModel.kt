package com.example.animepedia.presentation.animeListing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animepedia.analytics.eventLogger.EventConstants
import com.example.animepedia.analytics.eventLogger.EventLogger
import com.example.animepedia.domain.common.Result
import com.example.animepedia.domain.model.AnimeModel
import com.example.animepedia.domain.usecase.LoadAnimePageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeListViewModel @Inject constructor(
    private val loadAnimePageUseCase: LoadAnimePageUseCase,
    private val eventLogger: EventLogger
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _animeList = MutableStateFlow<List<AnimeModel>>(emptyList())
    val animeList: StateFlow<List<AnimeModel>> = _animeList

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    private var currentPage = 1
    private var hasNextPage = true

    private val _searchQuery = MutableStateFlow("")

    fun loadNextPage(isOnline: Boolean) {
        if (_loading.value || !hasNextPage) return

        eventLogger.logEvent(
            EventConstants.EventName.LOAD_MORE_CLICKED,
            mapOf(
                EventConstants.Param.CURRENT_COUNT to _animeList.value.size
            )
        )

        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true

            when (
                val result = loadAnimePageUseCase(
                    page = currentPage,
                    pageSize = 25,
                    isOnline = isOnline
                )
            ) {
                is Result.Success -> {
                    val (items, hasMore) = result.data
                    _animeList.value += items

                    hasNextPage = if(isOnline.not() && items.isEmpty()){
                        false
                    } else {
                        hasMore
                    }

                    currentPage++
                }

                is Result.Error -> {
                    _toastMessage.emit(result.message)
                }
            }

            _loading.value = false
        }
    }

    val filteredAnimeList: StateFlow<List<AnimeModel>> =
        combine(animeList, _searchQuery) { list, query ->
            if (query.isBlank()) list
            else list.filter {
                it.title.contains(query, ignoreCase = true)
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onSearch(query: String) {
        _searchQuery.value = query

        eventLogger.logEvent(
            EventConstants.EventName.SEARCH_PERFORMED,
            mapOf(
                EventConstants.Param.SEARCH_QUERY to query
            )
        )
    }
}
