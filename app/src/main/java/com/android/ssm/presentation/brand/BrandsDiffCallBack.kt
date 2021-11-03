package com.android.ssm.presentation.brand


import androidx.recyclerview.widget.DiffUtil
import com.android.ssm.BrandsListQuery

class BrandsDiffCallBack(
    private val newItems: List<BrandsListQuery.Brand?>,
    private val oldItems: List<BrandsListQuery.Brand?>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return (oldItem?._id == newItem?._id) && (oldItem?.name == newItem?.name)
    }

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem == newItem
    }
}