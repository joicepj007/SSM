package com.android.ssm.domain.repository

import com.android.ssm.BrandsListQuery
import com.apollographql.apollo.api.Response

/**
 * To make an interaction between [BrandRepository] & [GetBrandListUseCase]
 * */

interface BrandRepository {

    suspend fun queryBrandsList(page: String, pageSize:String): Response<BrandsListQuery.Data>

}