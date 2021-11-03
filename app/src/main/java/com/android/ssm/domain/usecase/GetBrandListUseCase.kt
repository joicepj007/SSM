package com.android.ssm.domain.usecase


import com.android.ssm.BrandsListQuery
import com.android.ssm.domain.repository.BrandRepository
import com.apollographql.apollo.api.Response
import javax.inject.Inject

/**
 * An interactor that calls the actual implementation of [BrandViewModel](which is injected by DI)
 * it handles the response that returns data &
 * contains a list of actions, event steps
 */
class GetBrandListUseCase @Inject constructor(private val repository: BrandRepository) {

    suspend fun getBrandListData(page: String, pageSize:String): Response<BrandsListQuery.Data> {
        return repository.queryBrandsList(page,pageSize)
    }
}