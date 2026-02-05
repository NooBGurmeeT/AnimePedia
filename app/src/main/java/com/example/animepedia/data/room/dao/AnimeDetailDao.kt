package com.example.animepedia.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.animepedia.data.room.entity.AnimeDetailEntity

@Dao
interface AnimeDetailDao {

    @Query("SELECT * FROM anime_detail WHERE id = :animeId")
    suspend fun getById(animeId: Int): AnimeDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(detail: AnimeDetailEntity)

    @Query("SELECT id FROM anime_detail")
    suspend fun getAllIds(): List<Int>
}
