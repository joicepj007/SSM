package com.android.ssm.di

import android.content.Context
import androidx.room.Room
import com.android.ssm.data.SandSMediaApi
import com.android.ssm.data.database.AppDatabase
import com.android.ssm.data.database.DatabaseHelper
import com.android.ssm.data.database.DatabaseHelperImpl
import com.android.ssm.data.database.RatingDao
import com.android.ssm.domain.repository.BrandRepository
import com.android.ssm.data.repository.BrandRepositoryImpl
import com.android.ssm.data.repository.IssuesRepositoryImpl
import com.android.ssm.domain.repository.IssuesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun provideWebService() = SandSMediaApi()

    @Singleton
    @Provides
    fun provideBrandListRepository(
        webService: SandSMediaApi
    ): BrandRepository {
        return BrandRepositoryImpl(webService)
    }

    @Singleton
    @Provides
    fun provideIssuesListRepository(
        webService: SandSMediaApi
    ): IssuesRepository {
        return IssuesRepositoryImpl(webService)
    }

    @Provides
    fun provideRatingDao(appDatabase: AppDatabase): RatingDao {
        return appDatabase.ratingDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "SSM"
        ).build()
    }

    @Singleton
    @Provides
    fun provideDataBaseHelperRepository(
        appDatabase: AppDatabase
    ): DatabaseHelper {
        return DatabaseHelperImpl(appDatabase)
    }

}