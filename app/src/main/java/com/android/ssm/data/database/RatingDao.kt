package com.android.ssm.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RatingDao {
    @Query("SELECT * FROM rating")
    fun getAll(): List<Rating>

    @Insert
    fun insertAll(vararg rating: Rating)

    @Delete
    fun delete(rating: Rating)
}