package com.dominate.thirtySecondsChallenge.base.adapters

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseBindingRecyclerViewAdapterAny<MODEL>(
    protected val context: Context?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<MODEL> = ArrayList()
    var itemClickListener: OnItemClickListener? = null

    fun submitItems(newItems: List<MODEL>) {
        if (items == newItems) {
            return
        }
        items = newItems
        notifyDataSetChanged()
    }

    fun clear() {
        items = ArrayList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int, item: Any, flag:Int)
        fun onItemLongClick(view: View?, position: Int, item: Any, flag:Int) {}
    }

}