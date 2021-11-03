package com.android.ssm.presentation.brand

/**
 * To make an interaction between [BrandsListAdapter] & [BrandListFragment]
 * */
interface OnBrandListAdapterListener {

    fun showUsersList(brandId: String,name: String)
}