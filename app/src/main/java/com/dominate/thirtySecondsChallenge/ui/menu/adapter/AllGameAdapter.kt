package com.dominate.thirtySecondsChallenge.ui.menu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.data.model.menu.CategoryGetMainResponseModel
import com.dominate.thirtySecondsChallenge.databinding.RowAllGameBinding
import com.dominate.thirtySecondsChallenge.ui.home.model.SportModel
import com.dominate.thirtySecondsChallenge.ui.menu.model.AllGameModel
import javax.inject.Inject

class AllGameAdapter @Inject constructor(

) :
    BaseAdapter<CategoryGetMainResponseModel, AllGameAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowAllGameBinding>(
            inflater,
            R.layout.row_all_game, parent, false
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

    class ViewHolder(val binding: RowAllGameBinding) :
        RecyclerView.ViewHolder(binding.root)

}