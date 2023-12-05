package com.dominate.thirtySecondsChallenge.ui.menu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.databinding.RowFavoriteBinding
import com.dominate.thirtySecondsChallenge.ui.menu.model.AllGameModel
import javax.inject.Inject

class FavoriteGameAdapter @Inject constructor(

) :
    BaseAdapter<AllGameModel, FavoriteGameAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowFavoriteBinding>(
            inflater,
            R.layout.row_favorite, parent, false
        )


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]

        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim_recycle_zoom_bottom)

//        holder.itemView.onClick {
//            holder.binding.clSport.startAnimation(animation)
//            onItemClickListener?.onItemClicked(it, items[position], position)
//        }

    }

    class ViewHolder(val binding: RowFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root)

}