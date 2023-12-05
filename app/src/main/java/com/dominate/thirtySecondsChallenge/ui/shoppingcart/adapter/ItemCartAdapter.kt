package com.dominate.thirtySecondsChallenge.ui.shoppingcart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.base.adapters.BaseBindingRecyclerViewAdapterAny
import com.dominate.thirtySecondsChallenge.base.adapters.BaseViewHolder
import com.dominate.thirtySecondsChallenge.data.model.store.MainCategoryResponseModel
import com.dominate.thirtySecondsChallenge.databinding.RowItemCartBinding
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.model.ItemCart
import com.dominate.thirtySecondsChallenge.utils.anim.zoomIn
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick

class ItemCartAdapter constructor(context: Context, flag: Int) :
    BaseBindingRecyclerViewAdapterAny<MainCategoryResponseModel>(context) {

    val flag = flag

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemCartBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }

    }

    inner class ViewHolder(private val binding: RowItemCartBinding) :
        BaseViewHolder<MainCategoryResponseModel>(binding.root) {

        override fun bind(item: MainCategoryResponseModel) {
            binding.item = item

            if (items[adapterPosition].isSelected){
                binding.vLine.context.let {
                    it.zoomIn(binding.vLine)
                }
            }

            binding.root.onClick {
                items.forEach {
                    it.isSelected = false
                }
                item.isSelected = true
                notifyDataSetChanged()

                itemClickListener?.onItemClick(it, adapterPosition, item, flag)
            }

        }
    }

}