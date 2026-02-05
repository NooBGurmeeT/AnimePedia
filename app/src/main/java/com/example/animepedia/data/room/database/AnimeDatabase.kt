package com.example.animepedia.data.room.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.animepedia.data.room.dao.AnimeDao
import com.example.animepedia.data.room.dao.AnimeDetailDao
import com.example.animepedia.data.room.entity.AnimeDetailEntity
import com.example.animepedia.data.room.entity.AnimeEntity

@Database(
    entities = [
        AnimeEntity::class,
        AnimeDetailEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AnimeDatabase : RoomDatabase() {

    abstract fun animeDao(): AnimeDao

    abstract fun animeDetailDao(): AnimeDetailDao
}
