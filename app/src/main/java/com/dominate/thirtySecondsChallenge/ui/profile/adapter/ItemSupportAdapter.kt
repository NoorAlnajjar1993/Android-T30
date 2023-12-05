package com.dominate.thirtySecondsChallenge.ui.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.base.adapters.BaseBindingRecyclerViewAdapterAny
import com.dominate.thirtySecondsChallenge.base.adapters.BaseViewHolder
import com.dominate.thirtySecondsChallenge.data.model.profile.FaqCategoryResponseModel
import com.dominate.thirtySecondsChallenge.databinding.RowItemSupportBinding
import com.dominate.thirtySecondsChallenge.utils.anim.zoomIn
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick

class ItemSupportAdapter constructor(context: Context, flag: Int) :
    BaseBindingRecyclerViewAdapterAny<FaqCategoryResponseModel>(context) {

    val flag = flag

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemSupportBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowItemSupportBinding) :
        BaseViewHolder<FaqCategoryResponseModel>(binding.root) {

        override fun bind(item: FaqCategoryResponseModel) {
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