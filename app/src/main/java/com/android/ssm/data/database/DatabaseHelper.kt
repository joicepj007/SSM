package com.android.ssm.data.database

interface DatabaseHelper {
    suspend fun getRatings(): List<Rating>
    suspend fun insertAll(rating: Rating)
}