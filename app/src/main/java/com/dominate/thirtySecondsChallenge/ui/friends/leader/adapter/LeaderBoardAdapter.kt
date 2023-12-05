package com.dominate.thirtySecondsChallenge.ui.friends.leader.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.data.model.profile.UserInfoModel
import com.dominate.thirtySecondsChallenge.databinding.RowLeaderBoardBinding
import com.dominate.thirtySecondsChallenge.utils.anim.setFadeAnimation
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import javax.inject.Inject

class LeaderBoardAdapter @Inject constructor(

) :
    BaseAdapter<UserInfoModel, LeaderBoardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowLeaderBoardBinding>(
            inflater,
            R.layout.row_leader_board, parent, false
        )
        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]
        setFadeAnimation(holder.itemView)

        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim_recycle_zoom_bottom)

        holder.itemView.onClick {
            onItemClickListener?.onItemClicked(it, items[position], position)
        }

    }

    class ViewHolder(val binding: RowLeaderBoardBinding) :
        RecyclerView.ViewHolder(binding.root)

}