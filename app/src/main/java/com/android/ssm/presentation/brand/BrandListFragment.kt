package com.android.ssm.presentation.brand

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
import com.android.ssm.BrandsListQuery
import com.android.ssm.R
import com.android.ssm.databinding.FragmentBrandListBinding
import com.android.ssm.presentation.issues.OnIssuesListCallback
import com.android.ssm.util.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class BrandListFragment : Fragment(),OnBrandListAdapterListener {

    private lateinit var binding: FragmentBrandListBinding
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private var mCallback: OnIssuesListCallback? = null
    private var adapter: BrandsListAdapter? = null
    private var toolbar: Toolbar? = null
    private val viewModel by viewModels<BrandViewModel>()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnIssuesListCallback) {
            mCallback = context
        } else throw ClassCastException(context.toString() + "must implement OnIssuesListCallback!")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = BrandsListAdapter(this)
        viewModel.queryCharactersList("1","10")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_brand_list, container, false)
        binding.brandViewModel = viewModel
        binding.brandRecyclerView.adapter = adapter

        toolbar = binding.root.findViewById(R.id.toolbar) as Toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar?.title = getString(R.string.str_users)

        //** Set the Layout Manager of the RecyclerView
        setRVLayoutManager()

        setupObservers()

        return binding.root
    }


    private fun setRVLayoutManager() {
        mLayoutManager = LinearLayoutManager(requireContext())
        binding.brandRecyclerView.layoutManager = mLayoutManager
        binding.brandRecyclerView.setHasFixedSize(true)
        binding.brandRecyclerView.itemAnimator = null
    }

    private fun setupObservers() {
        viewModel.brandsList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ViewState.Loading -> {
                    viewModel.isLoad.observe(viewLifecycleOwner, { load ->
                        load?.let { visibility ->
                            binding.progressBar.visibility =
                                if (visibility) View.GONE else View.VISIBLE
                            binding.brandRecyclerView.visibility =
                                View.VISIBLE
                            binding.noNetwork.visibility = View.GONE
                            binding.textinputError.visibility = View.GONE
                            binding.retry.visibility = View.GONE
                        }
                    })
                }
                is ViewState.Success -> {
                    if (response.value?.data?.brands?.brands?.size != 0) {
                        val results = response.value?.data?.brands?.brands
                        initRecyclerView(response.value?.data?.brands?.brands)
                    }
                }
                is ViewState.Error -> {
                    if (response.message.equals(getString(R.string.str_no_internet))) {
                        viewModel.isLoad.observe(viewLifecycleOwner, { load ->
                            load?.let { visibility ->
                                binding.progressBar.visibility =
                                    if (visibility) View.GONE else View.VISIBLE
                                binding.retry.visibility = View.VISIBLE
                                binding.brandRecyclerView.visibility =
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
                                binding.brandRecyclerView.visibility =
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
                        viewModel.queryCharactersList("1", "10")
                    }
                }
            }
        })
    }


    override fun onDetach() {
        super.onDetach()
        adapter = null
        mCallback = null
    }

    private fun initRecyclerView(brandList: List<BrandsListQuery.Brand?>?) {
        adapter?.updateData(brandList)
    }

    override fun showUsersList(brandId: String,name: String) {
        mCallback?.navigateToIssuesListPage(brandId,name)
    }

    companion object {

        val FRAGMENT_NAME: String = BrandListFragment::class.java.name

        @JvmStatic
        fun newInstance() =
            BrandListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }


}