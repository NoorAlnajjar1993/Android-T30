package com.dominate.thirtySecondsChallenge.ui.selectfriendlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.data.model.friends.UserFriendsResponseModel
import com.dominate.thirtySecondsChallenge.databinding.RowChooseFriendsBinding
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import javax.inject.Inject

class ChooseFriendsAdapter @Inject constructor(
    var item: ArrayList<UserFriendsResponseModel>,
) :
    BaseAdapter<UserFriendsResponseModel, ChooseFriendsAdapter.ViewHolder>(), Filterable {

    var itemFilterList = ArrayList<UserFriendsResponseModel>()

    init {
        itemFilterList = item
    }

    private var originalItems = ArrayList<UserFriendsResponseModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowChooseFriendsBinding>(
            inflater,
            R.layout.row_choose_friends, parent, false
        )


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = itemFilterList[position]

        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim_recycle_zoom_bottom)

        holder.itemView.onClick {
            // remove this code when add select all friends
            item.forEach {
                it.isCheck = false
            }
            item[position].isCheck = true
            notifyDataSetChanged()
            onItemClickListener?.onItemClicked(it, itemFilterList[position], position)
        }

    }

    override fun getItemCount(): Int {
        return itemFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    itemFilterList = item
                } else {
                    val resultList = ArrayList<UserFriendsResponseModel>()
                    for (row in item) {
                        if (row.firstName.toLowerCase()
                                .contains(
                                    constraint.toString().toLowerCase()
                                )
                        ) {
                            resultList.add(row)
                        }
                    }
                    itemFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = itemFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                itemFilterList = results?.values as ArrayList<UserFriendsResponseModel>
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(val binding: RowChooseFriendsBinding) :
        RecyclerView.ViewHolder(binding.root)

}

