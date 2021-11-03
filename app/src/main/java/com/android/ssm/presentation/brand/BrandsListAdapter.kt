package com.android.ssm.presentation.brand

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.ssm.BrandsListQuery
import com.android.ssm.R
import com.android.ssm.databinding.HolderItemBinding
import java.util.ArrayList

internal class BrandsListAdapter(val mListener: OnBrandListAdapterListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var brandList: List<BrandsListQuery.Brand?> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holderAlbumBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context), R.layout.holder_item, parent, false
        )
        return EventViewHolder(holderAlbumBinding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as EventViewHolder).onBind(getItem(position), position)
    }

    private fun getItem(position: Int): BrandsListQuery.Brand? {
        return brandList[position]
    }

    override fun getItemCount(): Int {
        return brandList.size
    }

    fun updateData(users: List<BrandsListQuery.Brand?>?) {
        val diffCallback = users?.let { BrandsDiffCallBack(it, brandList) }
        val diffResult = diffCallback?.let { DiffUtil.calculateDiff(it) }
        if (users != null) {
            brandList = users.toMutableList()
        }
        diffResult?.dispatchUpdatesTo(this)
    }

    inner class EventViewHolder(private val dataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun onBind(brandInfo: BrandsListQuery.Brand?, position: Int) {
            val holderUserBinding = dataBinding as HolderItemBinding
            holderUserBinding.brands = brandInfo
            itemView.setOnClickListener {
                brandInfo?._id?.let { id -> brandInfo.name?.let { name ->
                    mListener.showUsersList(id,
                        name
                    )
                } }
            }
        }
    }
}
