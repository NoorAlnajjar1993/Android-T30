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
import com.dominate.thirtySecondsChallenge.databinding.RowBadgesBinding
import com.dominate.thirtySecondsChallenge.databinding.RowEditProfileBinding
import com.dominate.thirtySecondsChallenge.ui.profile.model.BadgesModel
import com.dominate.thirtySecondsChallenge.ui.profile.model.EditProfileModel
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import javax.inject.Inject

class EditProfileAdapter @Inject constructor(

) :
    BaseAdapter<EditProfileModel, EditProfileAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowEditProfileBinding>(
            inflater,
            R.layout.row_edit_profile, parent, false
        )


        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]

        val animation: Animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.zoom_in)
            holder.binding.ivIcons.startAnimation(animation)

        holder.itemView.onClick {
            onItemClickListener?.onItemClicked(it, items[position], position)
            notifyItemChanged(position)
        }

    }

    class ViewHolder(val binding: RowEditProfileBinding) :
        RecyclerView.ViewHolder(binding.root)

}