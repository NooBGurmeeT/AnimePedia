package com.example.animepedia.domain.repository

import com.example.animepedia.domain.common.Result
import com.example.animepedia.domain.model.AnimeModel
import com.example.animepedia.domain.model.AnimeDetailModel

interface AnimeRepository {

    suspend fun loadAnimePage(
        page: Int,
        pageSize: Int,
        isOnline: Boolean
    ): Result<Pair<List<AnimeModel>, Boolean>>

    suspend fun getAnimeDetail(
        animeId: Int,
        isOnline: Boolean
    ): Result<AnimeDetailModel>

    suspend fun initializeAnimeDetailCache()
}
