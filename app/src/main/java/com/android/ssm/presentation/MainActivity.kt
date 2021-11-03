package com.android.ssm.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.ssm.R
import com.android.ssm.presentation.brand.BrandListFragment
import com.android.ssm.presentation.issues.IssuesListFragment
import com.android.ssm.presentation.issues.OnIssuesListCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnIssuesListCallback{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigateToUserListPage()
    }


    private fun navigateToUserListPage() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fl_container,
                BrandListFragment.newInstance(),
                BrandListFragment.FRAGMENT_NAME
            ).commit()
    }

    override fun navigateToIssuesListPage(brandId: String,name: String) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fl_container,
                IssuesListFragment.newInstance(brandId,name),
                IssuesListFragment.FRAGMENT_NAME
            )
            .addToBackStack(IssuesListFragment.FRAGMENT_NAME)
            .commit()

    }

}