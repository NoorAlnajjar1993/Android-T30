package com.dominate.thirtySecondsChallenge.ui.auctionround.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.databinding.RowCountAnswerBinding
import com.dominate.thirtySecondsChallenge.ui.auctionround.model.CountAnswerModel
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import javax.inject.Inject

class CountAnswerAdapter @Inject constructor(

) :
    BaseAdapter<CountAnswerModel, CountAnswerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowCountAnswerBinding>(
            inflater,
            R.layout.row_count_answer, parent, false
        )


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]

        holder.itemView.onClick {

            items.forEachIndexed { index, countAnswerModel ->
                if (it.isSelected){
                    items[index].isSelected = false
                    notifyItemChanged(index)
                }
            }
            items[position].isSelected = true
            notifyItemChanged(position)

            onItemClickListener?.onItemClicked(it, items[position], position)
        }

    }

    class ViewHolder(val binding: RowCountAnswerBinding) :
        RecyclerView.ViewHolder(binding.root)

}