package com.example.animepedia.data.mapper

import com.example.animepedia.data.retrofit.dto.AnimeDetailDto
import com.example.animepedia.data.retrofit.dto.AnimeDto
import com.example.animepedia.data.room.entity.AnimeDetailEntity
import com.example.animepedia.data.room.entity.AnimeEntity
import com.example.animepedia.domain.model.AnimeDetailModel
import com.example.animepedia.domain.model.AnimeModel

fun AnimeDto.toAnimeEntity(): AnimeEntity {
    return AnimeEntity(
        id = id,
        title = title,
        imageUrl = images.jpg.imageUrl,
        episodes = episodes,
        rating = rating
    )
}

fun AnimeDetailDto.toAnimeDetailEntity(): AnimeDetailEntity {
    return AnimeDetailEntity(
        id = id,
        title = title,
        titleEnglish = titleEnglish,
        synopsis = synopsis,
        imageUrl = images.jpg.imageUrl,
        episodes = episodes,
        rating = rating,
        genres = genres.joinToString { it.name },
        studios = studios.joinToString { it.name },
        trailerUrl = trailer?.embedUrl
    )
}

fun AnimeEntity.toAnimeDomain(): AnimeModel {
    return AnimeModel(
        id = id,
        title = title,
        imageUrl = imageUrl,
        episodes = episodes,
        rating = rating
    )
}

fun AnimeDetailEntity.toAnimeDetailDomain(): AnimeDetailModel {
    return AnimeDetailModel(
        id = id,
        title = title,
        titleEnglish = titleEnglish,
        synopsis = synopsis,
        imageUrl = imageUrl,
        episodes = episodes,
        rating = rating,
        genres = genres.split(",").map { it.trim() },
        studios = studios.split(",").map { it.trim() },
        trailerUrl = trailerUrl
    )
}