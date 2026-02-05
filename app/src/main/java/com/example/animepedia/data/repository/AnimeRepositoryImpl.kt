package com.example.animepedia.data.repository

import com.example.animepedia.analytics.logger.AppLogger
import com.example.animepedia.data.cache.AnimeDetailCache
import com.example.animepedia.data.mapper.toAnimeDetailDomain
import com.example.animepedia.data.mapper.toAnimeDetailEntity
import com.example.animepedia.data.mapper.toAnimeDomain
import com.example.animepedia.data.mapper.toAnimeEntity
import com.example.animepedia.data.retrofit.ApiService
import com.example.animepedia.data.room.dao.AnimeDao
import com.example.animepedia.data.room.dao.AnimeDetailDao
import com.example.animepedia.domain.common.Result
import com.example.animepedia.domain.model.AnimeModel
import com.example.animepedia.domain.model.AnimeDetailModel
import com.example.animepedia.domain.repository.AnimeRepository
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val animeDao: AnimeDao,
    private val animeDetailDao: AnimeDetailDao,
    private val appLogger: AppLogger
) : AnimeRepository {

    override suspend fun loadAnimePage(
        page: Int,
        pageSize: Int,
        isOnline: Boolean
    ): Result<Pair<List<AnimeModel>, Boolean>> {
        return try {

            appLogger.logInfo(
                "AnimeRepository",
                "loadAnimePage called page=$page pageSize=$pageSize isOnline=$isOnline"
            )

            // Offline mode so load paged anime list from local database
            if (!isOnline) {
                val offset = (page - 1) * pageSize

                val cached = animeDao
                    .getAnimePage(limit = pageSize, offset = offset)
                    .map { it.toAnimeDomain() }

                appLogger.logInfo(
                    "AnimeRepository",
                    "Returning anime list from local database size=${cached.size}"
                )

                return Result.Success(cached to true)
            }

            // Online mode so fetch anime list from API
            val response = apiService.getTopAnime(page, pageSize)

            // Map API models to entities
            val entities = response.animeList.map { it.toAnimeEntity() }

            // Save fetched anime list into database
            animeDao.insertAll(entities)

            appLogger.logInfo(
                "AnimeRepository",
                "Fetched anime list from API size=${entities.size}"
            )

            // Return domain models with pagination info
            Result.Success(
                entities.map { it.toAnimeDomain() } to
                        response.pagination.hasNextPage
            )

        } catch (e: Exception) {

            appLogger.logError(
                "AnimeRepository",
                "Failed to load anime page page=$page",
                e
            )

            Result.Error("Failed to load anime list", e)
        }
    }

    override suspend fun getAnimeDetail(
        animeId: Int,
        isOnline: Boolean
    ): Result<AnimeDetailModel> {
        return try {

            appLogger.logInfo(
                "AnimeRepository",
                "getAnimeDetail called animeId=$animeId isOnline=$isOnline"
            )

            // Cache says detail exists then fetch from database only
            if (AnimeDetailCache.isDetailCached(animeId)) {
                val cached = animeDetailDao.getById(animeId)
                if (cached != null) {

                    appLogger.logInfo(
                        "AnimeRepository",
                        "Returning anime detail from cache animeId=$animeId"
                    )

                    return Result.Success(cached.toAnimeDetailDomain())
                }
            }

            // Offline and not cached so data cannot be fetched
            if (!isOnline) {

                appLogger.logInfo(
                    "AnimeRepository",
                    "Anime detail not available offline animeId=$animeId"
                )

                return Result.Error("No internet connection and data not available offline")
            }

            // Fetch anime detail from API
            val remote = apiService.getAnimeDetail(animeId).anime
            val entity = remote.toAnimeDetailEntity()

            // Save anime detail to database
            animeDetailDao.insert(entity)

            // Mark anime detail as cached after successful save
            AnimeDetailCache.markDetailCached(animeId)

            appLogger.logInfo(
                "AnimeRepository",
                "Anime detail fetched from API and cached animeId=$animeId"
            )

            // Return domain model without re-fetching from database
            Result.Success(entity.toAnimeDetailDomain())

        } catch (e: Exception) {

            appLogger.logError(
                "AnimeRepository",
                "Failed to load anime detail animeId=$animeId",
                e
            )

            Result.Error("Failed to load anime detail", e)
        }
    }

    override suspend fun initializeAnimeDetailCache() {
        try {

            // Fetch all anime ids whose details are stored locally
            val ids = animeDetailDao.getAllIds()

            // Initialize in memory cache with existing anime detail ids
            AnimeDetailCache.init(ids)

            appLogger.logInfo(
                "AnimeRepository",
                "Anime detail cache initialized size=${ids.size}"
            )

        } catch (e: Exception) {

            appLogger.logError(
                "AnimeRepository",
                "Failed to initialize anime detail cache",
                e
            )
        }
    }
}
