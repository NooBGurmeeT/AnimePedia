package com.example.animepedia.domain.usecase

import com.example.animepedia.domain.common.Result
import com.example.animepedia.domain.model.AnimeModel
import com.example.animepedia.domain.repository.AnimeRepository
import javax.inject.Inject

class LoadAnimePageUseCase @Inject constructor(
    private val repository: AnimeRepository
) {

    suspend operator fun invoke(
        page: Int,
        pageSize: Int,
        isOnline: Boolean
    ): Result<Pair<List<AnimeModel>, Boolean>> {
        return repository.loadAnimePage(page, pageSize, isOnline)
    }
}
