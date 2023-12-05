package com.dominate.thirtySecondsChallenge.ui.friends.friendgroup.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.databinding.RowItemFriendsBinding
import com.dominate.thirtySecondsChallenge.ui.friends.friendgroup.model.ItemFriendsModel
import com.dominate.thirtySecondsChallenge.utils.anim.zoomIn
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import kotlinx.android.synthetic.main.row_item_friends.view.v_line
import javax.inject.Inject

class ItemFriendsAdapter @Inject constructor(

) :
    BaseAdapter<ItemFriendsModel, ItemFriendsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowItemFriendsBinding>(
            inflater,
            R.layout.row_item_friends, parent, false
        )


        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]

        if (items[position].isSelected){
            holder.itemView.v_line.context.let {
                it.zoomIn(holder.itemView.v_line)
            }
        }

        holder.itemView.onClick {

            for (item in items) {
                item.isSelected = false
            }
            items[position].isSelected = true

            notifyDataSetChanged()

            onItemClickListener?.onItemClicked(it, items[position], position)
        }

    }

    class ViewHolder(val binding: RowItemFriendsBinding) :
        RecyclerView.ViewHolder(binding.root)

}