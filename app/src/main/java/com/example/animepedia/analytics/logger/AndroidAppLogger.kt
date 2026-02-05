package com.example.animepedia.analytics.logger

import android.util.Log
import com.example.animepedia.analytics.logger.AppLogger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidAppLogger @Inject constructor() : AppLogger {

    override fun logInfo(tag: String, message: String) {
        Log.i(tag, message)
    }

    override fun logError(tag: String, message: String, throwable: Throwable?) {
        if (throwable != null) {
            Log.e(tag, message, throwable)
        } else {
            Log.e(tag, message)
        }
    }
}