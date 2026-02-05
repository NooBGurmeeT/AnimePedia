package com.example.animepedia.analytics.eventLogger

object EventConstants {

    object EventName {
        const val SCREEN_OPENED = "screen_opened"
        const val ANIME_OPENED = "anime_opened"
        const val SEARCH_PERFORMED = "search_performed"
        const val LOAD_MORE_CLICKED = "load_more_clicked"
        const val TRAILER_PLAYED = "trailer_played"
        const val ANIME_DETAIL_OPENED = "anime_detail_opened"
        const val ANIME_DETAIL_LOADED = "anime_detail_loaded"
        const val ANIME_DETAIL_FAILED = "anime_detail_failed"
    }

    object Param {
        const val SCREEN_NAME = "screen_name"
        const val ANIME_ID = "anime_id"
        const val ANIME_TITLE = "anime_title"
        const val SEARCH_QUERY = "search_query"
        const val CURRENT_COUNT = "current_count"
        const val IS_ONLINE = "is_online"
        const val ERROR_MESSAGE = "error_message"
    }
}
