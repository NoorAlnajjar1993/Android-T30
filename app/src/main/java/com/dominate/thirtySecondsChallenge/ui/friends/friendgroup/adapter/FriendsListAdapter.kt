package com.dominate.thirtySecondsChallenge.ui.friends.friendgroup.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.databinding.RowListFriendsBinding
import com.dominate.thirtySecondsChallenge.ui.friends.friendgroup.model.ListFriendsModel
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import kotlinx.android.synthetic.main.row_list_friends.view.iv_more
import javax.inject.Inject

class FriendsListAdapter @Inject constructor(

) :
    BaseAdapter<UserFriendsResponseModel, FriendsListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowListFriendsBinding>(
            inflater,
            R.layout.row_list_friends, parent, false
        )

        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]

        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim_recycle_zoom_bottom)


        holder.itemView.iv_more.onClick {
            onItemClickListener?.onItemClicked(it, items[position], position)
        }
        holder.itemView.onClick {
            onItemClickListener?.onItemClicked(it, items[position], position)
        }

    }

    class ViewHolder(val binding: RowListFriendsBinding) :
        RecyclerView.ViewHolder(binding.root)

}