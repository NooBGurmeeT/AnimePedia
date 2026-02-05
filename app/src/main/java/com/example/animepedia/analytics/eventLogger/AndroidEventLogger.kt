package com.example.animepedia.analytics.eventLogger

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidEventLogger @Inject constructor() : EventLogger {

    override fun logEvent(
        eventName: String,
        params: Map<String, Any?>
    ) {
        val formattedParams = if (params.isEmpty()) {
            "{}"
        } else {
            params.entries.joinToString(
                prefix = "{ ",
                postfix = " }"
            ) { "${it.key}=${it.value}" }
        }

        Log.d(
            "UserEvent",
            "event=$eventName params=$formattedParams"
        )
    }
}
