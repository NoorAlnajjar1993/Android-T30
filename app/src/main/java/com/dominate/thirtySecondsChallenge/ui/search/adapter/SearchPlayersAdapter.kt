package com.dominate.thirtySecondsChallenge.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.data.model.friends.PaginationResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.UserInfoModel
import com.dominate.thirtySecondsChallenge.databinding.RowSearchFriendsBinding
import com.dominate.thirtySecondsChallenge.ui.selectfriendlist.model.ChooseFriendsModel
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import javax.inject.Inject

class SearchPlayersAdapter @Inject constructor(

) :
    BaseAdapter<UserInfoModel, SearchPlayersAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowSearchFriendsBinding>(
            inflater,
            R.layout.row_search_friends, parent, false
        )


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim_recycle_zoom_bottom)

        holder.itemView.onClick {
            onItemClickListener?.onItemClicked(it, items[position], position)
        }

    }

    class ViewHolder(val binding: RowSearchFriendsBinding) :
        RecyclerView.ViewHolder(binding.root)

}