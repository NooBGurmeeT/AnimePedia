package com.example.animepedia.domain.model

data class AnimeModel(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val episodes: Int?,
    val rating: Double?
)
