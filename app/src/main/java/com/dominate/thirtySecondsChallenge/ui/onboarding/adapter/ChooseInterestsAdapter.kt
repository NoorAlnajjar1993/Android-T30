package com.dominate.thirtySecondsChallenge.ui.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.data.model.profile.ActiveInterestsResponseModel
import com.dominate.thirtySecondsChallenge.databinding.RowChooseInterestsBinding
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import javax.inject.Inject

class ChooseInterestsAdapter @Inject constructor() :
    BaseAdapter<ActiveInterestsResponseModel, ChooseInterestsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowChooseInterestsBinding>(
            inflater,
            R.layout.row_choose_interests, parent, false
        )


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]

        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim_recycle_zoom_bottom)

        holder.itemView.onClick {
            items[position].isCheck = items[position].isCheck != true
            notifyItemChanged(position)
            onItemClickListener?.onItemClicked(it, items[position], position)
        }

    }

    class ViewHolder(val binding: RowChooseInterestsBinding) :
        RecyclerView.ViewHolder(binding.root)

}
