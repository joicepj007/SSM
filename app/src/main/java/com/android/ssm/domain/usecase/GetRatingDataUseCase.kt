package com.android.ssm.domain.usecase

import com.android.ssm.data.database.DatabaseHelper
import com.android.ssm.data.database.Rating
import javax.inject.Inject

/**
 * An interactor that calls the actual implementation of [IssuesViewModel](which is injected by DI)
 * it handles the response that returns data &
 * contains a list of actions, event steps
 */
class GetRatingDataUseCase @Inject constructor(private val databaseHelper: DatabaseHelper) {

    suspend fun insertRatingData(rating: Rating) {
        return databaseHelper.insertAll(rating)
    }

    suspend fun getRatingData() :List<Rating> {
        return databaseHelper.getRatings()
    }
}