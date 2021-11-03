package com.android.ssm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(entities = [Rating::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ratingDao(): RatingDao
}