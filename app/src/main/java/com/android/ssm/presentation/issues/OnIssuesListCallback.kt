package com.android.ssm.presentation.issues

/**
 * To make an interaction between [MainActivity] & its children
 * */
interface OnIssuesListCallback {

    fun navigateToIssuesListPage(brandId: String,name:String)

}