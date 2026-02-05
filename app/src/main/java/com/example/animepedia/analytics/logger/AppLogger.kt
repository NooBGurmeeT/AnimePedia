package com.example.animepedia.analytics.logger

interface AppLogger {
    fun logInfo(tag: String, message: String)
    fun logError(tag: String, message: String, throwable: Throwable? = null)
}