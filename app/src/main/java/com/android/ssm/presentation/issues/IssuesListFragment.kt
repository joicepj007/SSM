package com.android.ssm.presentation.issues

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.ssm.IssuesListQuery
import com.android.ssm.R
import com.android.ssm.data.database.Rating
import com.android.ssm.databinding.FragmentIssuesListBinding
import com.android.ssm.util.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class IssuesListFragment : Fragment(), OnRatingListAdapterListener {

    private lateinit var binding: FragmentIssuesListBinding
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private var adapter: IssuesListAdapter? = null
    private var toolbar: Toolbar? = null
    private val viewModel by viewModels<IssuesViewModel>()
    private var brandId: String? = null
    private var issueName: String? = null
    private var ratingsData:List<Rating>?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        brandId = arguments?.getString(KEY_BRAND_ID)
        issueName = arguments?.getString(KEY_BRAND_TITLE)
        brandId?.let { viewModel.queryCharactersList(1,10,it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_issues_list, container, false)
        binding.issuesViewModel = viewModel

        toolbar = binding.root.findViewById(R.id.toolbar) as Toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar?.title = issueName

        //** Set the Layout Manager of the RecyclerView
        setRVLayoutManager()

        setupObservers()

        return binding.root
    }


    private fun setRVLayoutManager() {
        mLayoutManager = LinearLayoutManager(requireContext())
        binding.issuesRecyclerView.layoutManager = mLayoutManager
        binding.issuesRecyclerView.setHasFixedSize(true)
        binding.issuesRecyclerView.itemAnimator = null
    }

    private fun setupObservers() {
        viewModel.issuesList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ViewState.Loading -> {
                    viewModel.isLoad.observe(viewLifecycleOwner, { load ->
                        load?.let { visibility ->
                            binding.progressBar.visibility =
                                if (visibility) View.GONE else View.VISIBLE
                            binding.issuesRecyclerView.visibility =
                                View.VISIBLE
                            binding.noNetwork.visibility = View.GONE
                            binding.textinputError.visibility = View.GONE
                            binding.retry.visibility = View.GONE
                        }
                    })
                }
                is ViewState.Success -> {
                    if (response.value?.data?.issues?.issues?.size != 0) {
                        val results = response.value?.data?.issues?.issues
                        initRecyclerView(results)
                    }
                }
                is ViewState.Error -> {
                    if (response.message.equals(getString(R.string.str_no_internet))) {
                        viewModel.isLoad.observe(viewLifecycleOwner, { load ->
                            load?.let { visibility ->
                                binding.progressBar.visibility =
                                    if (visibility) View.GONE else View.VISIBLE
                                binding.retry.visibility = View.VISIBLE
                                binding.issuesRecyclerView.visibility =
                                    View.GONE
                                binding.noNetwork.visibility = View.VISIBLE
                                binding.textinputError.visibility = View.GONE
                            }
                        })
                    } else {
                        viewModel.isLoad.observe(viewLifecycleOwner, { load ->
                            load?.let { visibility ->
                                binding.progressBar.visibility =
                                    if (visibility) View.GONE else View.VISIBLE
                                binding.retry.visibility = View.VISIBLE
                                binding.textinputError.visibility =
                                    View.VISIBLE
                                binding.issuesRecyclerView.visibility =
                                    View.GONE
                                binding.noNetwork.visibility = View.GONE
                                binding.textinputError.text =
                                    response.message.toString()
                            }
                        })
                    }
                }
            }
        }

        viewModel.retryClickedLiveData.observe(viewLifecycleOwner, {
            it?.let { clicked ->
                if (clicked) {
                    if (binding.retry.isVisible) {
                        brandId?.let { id -> viewModel.queryCharactersList(1,10,id) }
                    }
                }
            }
        })
        viewModel.getDatabaseRatingLiveData.observe(viewLifecycleOwner, { rating->
            ratingsData=rating

        })
    }



    private fun initRecyclerView(brandList: List<IssuesListQuery.Issue?>?) {
        adapter = IssuesListAdapter(this)
        binding.issuesRecyclerView.adapter = adapter
        val dbRatingsData = ratingsData?.map { it.id to it.rating }?.toMap()
        adapter?.updateData(brandList,dbRatingsData)
    }

    companion object {
        const val KEY_BRAND_ID = "KEY_BRAND_ID"
        const val KEY_BRAND_TITLE = "KEY_BRAND_TITLE"
        val FRAGMENT_NAME: String = IssuesListFragment::class.java.name

        @JvmStatic
        fun newInstance(brandId: String,name: String) =
            IssuesListFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_BRAND_ID, brandId)
                    putString(KEY_BRAND_TITLE, name)
                }
            }
    }

    override fun clickedRatingItem(id:String,rating: Float) {
          val ratings=Rating(id = id,rating = rating)
        viewModel.insertRatingdata(ratings)
    }
}