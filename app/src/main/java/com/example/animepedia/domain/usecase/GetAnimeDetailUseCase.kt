package com.example.animepedia.domain.usecase

import com.example.animepedia.domain.common.Result
import com.example.animepedia.domain.model.AnimeDetailModel
import com.example.animepedia.domain.repository.AnimeRepository
import javax.inject.Inject

class GetAnimeDetailUseCase @Inject constructor(
    private val repository: AnimeRepository
) {

    suspend operator fun invoke(
        animeId: Int,
        isOnline: Boolean
    ): Result<AnimeDetailModel> {
        return repository.getAnimeDetail(animeId, isOnline)
    }
}
