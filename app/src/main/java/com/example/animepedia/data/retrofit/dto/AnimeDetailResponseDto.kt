package com.example.animepedia.data.retrofit.dto

import com.google.gson.annotations.SerializedName

data class AnimeDetailResponseDto(
    @SerializedName("data")
    val anime: AnimeDetailDto
)
data class AnimeDetailDto(
    @SerializedName("mal_id")
    val id: Int,

    val title: String,

    @SerializedName("title_english")
    val titleEnglish: String?,

    val synopsis: String?,

    val episodes: Int?,

    @SerializedName("score")
    val rating: Double?,

    val images: ImagesDto,

    val genres: List<NameDto>,
    val studios: List<NameDto>,

    val trailer: TrailerDto?
)
data class NameDto(
    val name: String
)

data class TrailerDto(
    @SerializedName("embed_url")
    val embedUrl: String?
)
