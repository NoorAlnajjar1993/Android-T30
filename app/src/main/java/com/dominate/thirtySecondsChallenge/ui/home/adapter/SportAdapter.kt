package com.dominate.thirtySecondsChallenge.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.databinding.RowSportBinding
import com.dominate.thirtySecondsChallenge.ui.home.model.SportModel
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import javax.inject.Inject

class SportAdapter @Inject constructor(

) :
    BaseAdapter<SportModel, SportAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowSportBinding>(
            inflater,
            R.layout.row_sport, parent, false
        )


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]

        val animation: Animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.shaking_view)
        val animationShine: Animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.shine_anim)
        holder.binding.shine.startAnimation(animationShine)
        animationShine.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(p0: Animation?) {
                holder.binding.shine.visibility = View.GONE
            }

            override fun onAnimationStart(p0: Animation?) {}

            override fun onAnimationRepeat(p0: Animation?) {}

        })

        holder.itemView.onClick {
            holder.binding.clSport.startAnimation(animation)
            onItemClickListener?.onItemClicked(it, items[position], position)
        }

    }

    class ViewHolder(val binding: RowSportBinding) :
        RecyclerView.ViewHolder(binding.root)


}