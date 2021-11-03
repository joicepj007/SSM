package com.android.ssm.presentation.issues


import androidx.recyclerview.widget.DiffUtil
import com.android.ssm.BrandsListQuery
import com.android.ssm.IssuesListQuery

class IssuesDiffCallBack(
    private val newItems: List<IssuesListQuery.Issue?>,
    private val oldItems: List<IssuesListQuery.Issue?>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return (oldItem?._id == newItem?._id) && (oldItem?.designation == newItem?.designation)
    }

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem == newItem
    }
}