package com.android.ssm.presentation.issues

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.ssm.IssuesListQuery
import com.android.ssm.R
import com.android.ssm.databinding.HolderIssueItemBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.holder_issue_item.view.*
import java.util.ArrayList

internal class IssuesListAdapter(val mRatingListener: OnRatingListAdapterListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var issuesList: List<IssuesListQuery.Issue?> = ArrayList()
    private var dbData: Map<String, Float>?=null
    private val picasso = Picasso.get()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holderAlbumBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context), R.layout.holder_issue_item, parent, false
        )
        return EventViewHolder(holderAlbumBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as EventViewHolder).onBind(getItem(position), position)
    }

    private fun getItem(position: Int): IssuesListQuery.Issue? {
        return issuesList[position]
    }

    override fun getItemCount(): Int {
        return issuesList.size
    }

    fun updateData(users: List<IssuesListQuery.Issue?>?,map :Map<String, Float>?) {
        val diffCallback = users?.let { IssuesDiffCallBack(it, issuesList) }
        val diffResult = diffCallback?.let { DiffUtil.calculateDiff(it) }
        if (users != null) {
            issuesList = users.toMutableList()
        }
        dbData=map
        diffResult?.dispatchUpdatesTo(this)
    }

    inner class EventViewHolder(private val dataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {

        fun onBind(issuesInfo: IssuesListQuery.Issue?, position: Int) {
            val holderUserBinding = dataBinding as HolderIssueItemBinding
            holderUserBinding.issues = issuesInfo
            val url = issuesInfo?.cover?.x
            picasso.load(url).into(itemView.profile_pic)
            val rating =dbData?.get(getItem(position)?._id) ?:0f
            itemView.ratingBar.rating=rating
            Log.d("rating******xyx********", ""+rating)
            itemView.ratingBar.ratingBar.onRatingBarChangeListener =
                RatingBar.OnRatingBarChangeListener { p0, p1, p2 ->
                    if (p2) {
                        if (p1!=0.0f) {
                            getItem(position)?._id?.let { mRatingListener.clickedRatingItem(it, p1) }
                            Log.d("mRatingListener******xyx********", "p1$p1")
                        }

                    }
                }
        }
    }


}
