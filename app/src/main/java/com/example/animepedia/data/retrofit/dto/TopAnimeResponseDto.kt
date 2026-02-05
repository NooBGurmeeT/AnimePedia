package com.example.animepedia.data.retrofit.dto

import com.google.gson.annotations.SerializedName

data class TopAnimeResponseDto(
    @SerializedName("data")
    val animeList: List<AnimeDto>,

    @SerializedName("pagination")
    val pagination: PaginationDto
)

data class AnimeDto(
    @SerializedName("mal_id")
    val id: Int,

    val title: String,

    val episodes: Int?,

    @SerializedName("score")
    val rating: Double?,

    val images: ImagesDto
)

data class ImagesDto(
    val jpg: JpgImageDto
)

data class JpgImageDto(
    @SerializedName("image_url")
    val imageUrl: String
)

data class PaginationDto(
    @SerializedName("current_page")
    val currentPage: Int,

    @SerializedName("has_next_page")
    val hasNextPage: Boolean
)
