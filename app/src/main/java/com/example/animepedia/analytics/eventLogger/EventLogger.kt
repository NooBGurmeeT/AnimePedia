package com.example.animepedia.analytics.eventLogger

interface EventLogger {

    fun logEvent(
        eventName: String,
        params: Map<String, Any?> = emptyMap()
    )
}
