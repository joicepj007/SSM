package com.android.ssm.data.repository

import com.android.ssm.IssuesListQuery
import com.android.ssm.data.SandSMediaApi
import com.android.ssm.domain.repository.IssuesRepository
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await

/**
 * This repository is responsible for
 * fetching data[IssuesListQuery()] from server
 * */
class IssuesRepositoryImpl(
    private val webService: SandSMediaApi
) : IssuesRepository {
    override suspend fun queryIssuesList(page: Int, pageSize:Int,brandId: String): Response<IssuesListQuery.Data> {
        return webService.getApolloClient().query(IssuesListQuery(page,pageSize,brandId)).await()
    }
}