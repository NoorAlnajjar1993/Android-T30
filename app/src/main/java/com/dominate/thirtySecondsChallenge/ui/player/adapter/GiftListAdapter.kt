package com.dominate.thirtySecondsChallenge.ui.player.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.data.model.profile.GiftsResponseModel
import com.dominate.thirtySecondsChallenge.databinding.RowGiftBinding
import com.dominate.thirtySecondsChallenge.ui.player.model.GiftListModel
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import javax.inject.Inject

class GiftListAdapter @Inject constructor(

) :
    BaseAdapter<GiftsResponseModel, GiftListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowGiftBinding>(
            inflater,
            R.layout.row_gift, parent, false
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

    class ViewHolder(val binding: RowGiftBinding) :
        RecyclerView.ViewHolder(binding.root)

}