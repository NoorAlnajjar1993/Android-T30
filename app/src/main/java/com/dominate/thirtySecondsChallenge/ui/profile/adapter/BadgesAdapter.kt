package com.dominate.thirtySecondsChallenge.ui.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseBindingRecyclerViewAdapterAny
import com.dominate.thirtySecondsChallenge.base.adapters.BaseViewHolder
import com.dominate.thirtySecondsChallenge.data.model.profile.GiftProfileResponseModel
import com.dominate.thirtySecondsChallenge.databinding.RowBadgesBinding
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import kotlinx.android.synthetic.main.fragment_profile.rv_Gifts
import kotlinx.android.synthetic.main.fragment_profile.view.rv_Gifts


class BadgesAdapter constructor(context: Context, typeLayout: Int, flag: Int) :
    BaseBindingRecyclerViewAdapterAny<GiftProfileResponseModel>(context) {

    companion object {
        private const val VIEW_TYPE_LAYOUT_HORIZONTAL = 1 // horizontal
        private const val VIEW_TYPE_LAYOUT_GRID = 2 // grid
    }

    val flag = flag
    val typeLayout = typeLayout
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<GiftProfileResponseModel> {
        return when (viewType) {
            VIEW_TYPE_LAYOUT_HORIZONTAL -> {
                val binding = RowBadgesBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
                ViewHolder(binding)
            }

            VIEW_TYPE_LAYOUT_GRID -> {
                val binding = RowBadgesBinding.inflate(
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

    inner class ViewHolder(private val binding: RowBadgesBinding) :
        BaseViewHolder<GiftProfileResponseModel>(binding.root) {

        override fun bind(item: GiftProfileResponseModel) {
            binding.item = item

            val animation: Animation =
                AnimationUtils.loadAnimation(itemView.context, R.anim.zoom_in)
            binding.ivIcons.startAnimation(animation)
            itemView.onClick {
//            holder.binding.clSport.startAnimation(animation)
                itemClickListener?.onItemClick(it, adapterPosition, item, flag)
            }
        }
    }

    inner class ViewHolderWallpaper(private val binding: RowBadgesBinding) :
        BaseViewHolder<GiftProfileResponseModel>(binding.root) {

        override fun bind(item: GiftProfileResponseModel) {
            binding.item = item

            binding.clMain.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT

            val animation: Animation =
                AnimationUtils.loadAnimation(itemView.context, R.anim.zoom_in)
            binding.ivIcons.startAnimation(animation)

            itemView.onClick {
//            holder.binding.clSport.startAnimation(animation)
                itemClickListener?.onItemClick(it, adapterPosition, item, flag)
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (typeLayout == 1) {
            VIEW_TYPE_LAYOUT_HORIZONTAL
        } else {
            VIEW_TYPE_LAYOUT_GRID
        }
    }

}
