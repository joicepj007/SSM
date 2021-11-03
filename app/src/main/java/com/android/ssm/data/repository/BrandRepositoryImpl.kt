package com.android.ssm.data.repository
import com.android.ssm.BrandsListQuery
import com.android.ssm.IssuesListQuery
import com.android.ssm.data.SandSMediaApi
import com.android.ssm.domain.repository.BrandRepository
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await

/**
 * This repository is responsible for
 * fetching data[BrandsListQuery()] from server
 * */
class BrandRepositoryImpl(
    private val webService: SandSMediaApi
) : BrandRepository {

    override suspend fun queryBrandsList(page: String, pageSize:String): Response<BrandsListQuery.Data> {
        return webService.getApolloClient().query(BrandsListQuery()).await()
    }
}