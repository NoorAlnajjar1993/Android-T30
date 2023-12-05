package com.dominate.thirtySecondsChallenge.ui.shoppingcart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseBindingRecyclerViewAdapterAny
import com.dominate.thirtySecondsChallenge.base.adapters.BaseViewHolder
import com.dominate.thirtySecondsChallenge.data.model.store.AccessoriesResponseModel
import com.dominate.thirtySecondsChallenge.databinding.RowPostersBinding
import com.dominate.thirtySecondsChallenge.databinding.RowWallpapersBinding
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick


class AccessoriesAdapter constructor(context: Context, flag: Int) :
    BaseBindingRecyclerViewAdapterAny<AccessoriesResponseModel>(context) {

    companion object {
        private const val VIEW_TYPE_LAYOUT_ONE = 11111
        private const val VIEW_TYPE_LAYOUT_TWO = 22222
    }

    val flag = flag
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AccessoriesResponseModel> {
        return when (viewType) {
            VIEW_TYPE_LAYOUT_ONE -> {
                val binding = RowPostersBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
                ViewHolder(binding)
            }
            VIEW_TYPE_LAYOUT_TWO -> {
                val binding = RowWallpapersBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
                ViewHolderWallpaper(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bind(items[position])
            is ViewHolderWallpaper -> holder.bind(items[position])
            else -> throw IllegalArgumentException("Invalid view holder type")
        }
    }

    inner class ViewHolder(private val binding: RowPostersBinding) :
        BaseViewHolder<AccessoriesResponseModel>(binding.root) {

        override fun bind(item: AccessoriesResponseModel) {
            binding.item = item

            val animation: Animation =
                AnimationUtils.loadAnimation(binding.clMain.context, R.anim.zoom_in)
            binding.clMain.startAnimation(animation)

            binding.root.onClick {
                itemClickListener?.onItemClick(it, adapterPosition, item, flag)
            }

        }
    }

    inner class ViewHolderWallpaper(private val binding: RowWallpapersBinding) :
        BaseViewHolder<AccessoriesResponseModel>(binding.root) {

        override fun bind(item: AccessoriesResponseModel) {
            binding.item = item

            val animation: Animation =
                AnimationUtils.loadAnimation(binding.clMain.context, R.anim.zoom_in)
            binding.clMain.startAnimation(animation)

            binding.root.onClick {
                itemClickListener?.onItemClick(it, adapterPosition, item, flag)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (flag == 11111) {
            VIEW_TYPE_LAYOUT_ONE
        } else {
            VIEW_TYPE_LAYOUT_TWO
        }
    }

}

