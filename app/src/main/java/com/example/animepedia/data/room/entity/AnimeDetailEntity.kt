package com.example.animepedia.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.animepedia.data.retrofit.dto.AnimeDetailDto

@Entity(tableName = "anime_detail")
data class AnimeDetailEntity(
    @PrimaryKey
    val id: Int,

    val title: String,
    val titleEnglish: String?,
    val synopsis: String?,
    val imageUrl: String,
    val episodes: Int?,
    val rating: Double?,
    val genres: String,
    val studios: String,
    val trailerUrl: String?
)
