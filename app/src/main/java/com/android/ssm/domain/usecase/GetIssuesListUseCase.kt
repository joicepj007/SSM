package com.android.ssm.domain.usecase

import com.android.ssm.BrandsListQuery
import com.android.ssm.IssuesListQuery
import com.android.ssm.domain.repository.BrandRepository
import com.android.ssm.domain.repository.IssuesRepository
import com.apollographql.apollo.api.Response
import javax.inject.Inject

/**
 * An interactor that calls the actual implementation of [IssuesViewModel](which is injected by DI)
 * it handles the response that returns data &
 * contains a list of actions, event steps
 */
class GetIssuesListUseCase @Inject constructor(private val repository: IssuesRepository) {

    suspend fun getIssuesListData(page: Int, pageSize:Int,brandId: String): Response<IssuesListQuery.Data> {
        return repository.queryIssuesList(page,pageSize,brandId)
    }
}