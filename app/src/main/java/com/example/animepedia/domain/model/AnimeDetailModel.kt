package com.example.animepedia.domain.model

data class AnimeDetailModel(
    val id: Int,
    val title: String,
    val titleEnglish: String?,
    val synopsis: String?,
    val imageUrl: String,
    val episodes: Int?,
    val rating: Double?,
    val genres: List<String>,
    val studios: List<String>,
    val trailerUrl: String?
)
