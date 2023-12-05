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
import com.dominate.thirtySecondsChallenge.databinding.RowFriendsRequestBinding
import com.dominate.thirtySecondsChallenge.ui.friends.friendgroup.model.ListFriendsModel
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import javax.inject.Inject

class FriendsRequestAdapter @Inject constructor(

) :
    BaseAdapter<UserFriendsResponseModel, FriendsRequestAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowFriendsRequestBinding>(
            inflater,
            R.layout.row_friends_request, parent, false
        )


        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]

        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim_recycle_zoom_bottom)


        holder.binding.ivMoreRequest.onClick {
            onItemClickListener?.onItemClicked(it, items[position], position)
        }

        holder.binding.cvAccept.onClick {
            onItemClickListener?.onItemClicked(it, items[position], position)
        }

        holder.binding.cvReject.onClick {
            onItemClickListener?.onItemClicked(it, items[position], position)
        }

        holder.itemView.onClick {
            onItemClickListener?.onItemClicked(holder.itemView, items[position], position)
        }


    }

    class ViewHolder(val binding: RowFriendsRequestBinding) :
        RecyclerView.ViewHolder(binding.root)

}