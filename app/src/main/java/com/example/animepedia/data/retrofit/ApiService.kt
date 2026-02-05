package com.example.animepedia.data.retrofit

import com.example.animepedia.data.retrofit.dto.AnimeDetailResponseDto
import com.example.animepedia.data.retrofit.dto.TopAnimeResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    /**
     * Top Anime list (paginated)
     *
     * Example:
     * /v4/top/anime?page=1&limit=25
     */
    @GET("v4/top/anime")
    suspend fun getTopAnime(
        @Query("page") page: Int,
        @Query("limit") limit: Int = DEFAULT_PAGE_SIZE
    ): TopAnimeResponseDto

    /**
     * Anime detail by MAL id
     *
     * Example:
     * /v4/anime/1
     */
    @GET("v4/anime/{animeId}")
    suspend fun getAnimeDetail(
        @Path("animeId") animeId: Int
    ): AnimeDetailResponseDto

    companion object {
        const val DEFAULT_PAGE_SIZE = 25
    }
}
