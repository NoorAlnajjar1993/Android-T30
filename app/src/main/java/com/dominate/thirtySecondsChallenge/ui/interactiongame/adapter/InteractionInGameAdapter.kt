package com.dominate.thirtySecondsChallenge.ui.interactiongame.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.data.model.gift.StickersResponseModel
import com.dominate.thirtySecondsChallenge.databinding.RowInteractionGameBinding
import com.dominate.thirtySecondsChallenge.ui.interactiongame.model.InteractionInGameModel
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import javax.inject.Inject

class InteractionInGameAdapter @Inject constructor(

) :
    BaseAdapter<StickersResponseModel, InteractionInGameAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowInteractionGameBinding>(
            inflater,
            R.layout.row_interaction_game, parent, false
        )


        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]

        holder.itemView.onClick {
            var count = 0
            items.forEach {
                if (it.isSelected){
                    it.isSelected = false
                    notifyItemChanged(count)
                }
                count++
            }
            items[position].isSelected = items[position].isSelected != true
            notifyItemChanged(position)

            onItemClickListener?.onItemClicked(it, items[position], position)
        }

    }

    class ViewHolder(val binding: RowInteractionGameBinding) :
        RecyclerView.ViewHolder(binding.root)

}