package com.example.animepedia.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.animepedia.data.retrofit.dto.AnimeDto

@Entity(tableName = "anime")
data class AnimeEntity(

    @PrimaryKey
    val id: Int,

    val title: String,
    val imageUrl: String,
    val episodes: Int?,
    val rating: Double?
)
