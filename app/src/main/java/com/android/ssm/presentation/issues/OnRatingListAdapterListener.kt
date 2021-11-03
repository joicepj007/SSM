package com.android.ssm.presentation.issues

/**
 * To make an interaction between [BrandsListAdapter] & [UsersListFragment]
 * */
interface OnRatingListAdapterListener {

    fun clickedRatingItem(id:String,rating: Float)
}