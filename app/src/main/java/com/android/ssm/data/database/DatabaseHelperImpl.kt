package com.android.ssm.data.database

import com.android.ssm.data.database.AppDatabase
import com.android.ssm.data.database.DatabaseHelper
import com.android.ssm.data.database.Rating

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : DatabaseHelper {

    override suspend fun getRatings(): List<Rating> = appDatabase.ratingDao().getAll()

    override suspend fun insertAll(rating: Rating) = appDatabase.ratingDao().insertAll(rating)

}