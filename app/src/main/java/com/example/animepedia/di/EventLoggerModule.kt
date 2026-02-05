package com.example.animepedia.di

import com.example.animepedia.analytics.eventLogger.AndroidEventLogger
import com.example.animepedia.analytics.eventLogger.EventLogger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EventLoggerModule {

    @Binds
    @Singleton
    abstract fun bindEventLogger(
        impl: AndroidEventLogger
    ): EventLogger
}
