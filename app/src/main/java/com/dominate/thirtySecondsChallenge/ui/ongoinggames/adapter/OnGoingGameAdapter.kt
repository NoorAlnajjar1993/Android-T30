package com.dominate.thirtySecondsChallenge.ui.ongoinggames.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.databinding.RowOngoingGameBinding
import com.dominate.thirtySecondsChallenge.ui.ongoinggames.model.OngoingGameModel
import javax.inject.Inject

class OnGoingGameAdapter @Inject constructor(

) :
    BaseAdapter<OngoingGameModel, OnGoingGameAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowOngoingGameBinding>(
            inflater,
            R.layout.row_ongoing_game, parent, false
        )


        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]

        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim_recycle_zoom_bottom)

//        val animation: Animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.zoom_in)
//            holder.binding.ivIcons.startAnimation(animation)

//        holder.itemView.onClick {
////            holder.binding.clSport.startAnimation(animation)
//            onItemClickListener?.onItemClicked(it, items[position], position)
//        }

    }

    class ViewHolder(val binding: RowOngoingGameBinding) :
        RecyclerView.ViewHolder(binding.root)

}