package com.example.animepedia.di

import android.content.Context
import androidx.room.Room
import com.example.animepedia.data.room.dao.AnimeDao
import com.example.animepedia.data.room.dao.AnimeDetailDao
import com.example.animepedia.data.room.database.AnimeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "animepedia_db"

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AnimeDatabase {
        return Room.databaseBuilder(
            context,
            AnimeDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideAnimeDao(
        database: AnimeDatabase
    ): AnimeDao {
        return database.animeDao()
    }

    @Provides
    fun provideAnimeDetailDao(
        database: AnimeDatabase
    ): AnimeDetailDao {
        return database.animeDetailDao()
    }
}
