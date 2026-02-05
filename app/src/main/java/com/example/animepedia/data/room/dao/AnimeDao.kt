package com.example.animepedia.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.animepedia.data.room.entity.AnimeEntity

@Dao
interface AnimeDao {

    @Query("""
        SELECT * FROM anime
        ORDER BY rating DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getAnimePage(
        limit: Int,
        offset: Int
    ): List<AnimeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(anime: List<AnimeEntity>)

    @Query("SELECT COUNT(*) FROM anime")
    suspend fun getCount(): Int
}
