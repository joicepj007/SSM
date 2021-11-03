package com.android.ssm.domain.repository

import com.android.ssm.IssuesListQuery
import com.apollographql.apollo.api.Response

/**
 * To make an interaction between [IssuesRepository] & [GetIssuesListUseCase]
 * */
interface IssuesRepository {

    suspend fun queryIssuesList(page: Int, pageSize:Int,brandId: String): Response<IssuesListQuery.Data>
}