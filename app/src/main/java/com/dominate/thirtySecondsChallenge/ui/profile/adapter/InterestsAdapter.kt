package com.dominate.thirtySecondsChallenge.ui.profile.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.data.model.profile.InterestsResponseModel
import com.dominate.thirtySecondsChallenge.databinding.RowInterestsBinding
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import javax.inject.Inject

class InterestsAdapter @Inject constructor(

) :
    BaseAdapter<InterestsResponseModel, InterestsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowInterestsBinding>(
            inflater,
            R.layout.row_interests, parent, false
        )


        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]

        val animation: Animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.zoom_in)
            holder.binding.ivIcons.startAnimation(animation)

        holder.itemView.onClick {
//            holder.binding.clSport.startAnimation(animation)
            onItemClickListener?.onItemClicked(it, items[position], position)
        }

    }

    class ViewHolder(val binding: RowInterestsBinding) :
        RecyclerView.ViewHolder(binding.root)

}