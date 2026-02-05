package com.example.animepedia.di

import com.example.animepedia.analytics.logger.AndroidAppLogger
import com.example.animepedia.analytics.logger.AppLogger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppLoggerModule {

    @Binds
    @Singleton
    abstract fun bindAppLogger(
        impl: AndroidAppLogger
    ): AppLogger
}
