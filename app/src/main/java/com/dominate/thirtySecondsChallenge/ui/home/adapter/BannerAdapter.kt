package com.dominate.thirtySecondsChallenge.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseBindingRecyclerViewAdapter
import com.dominate.thirtySecondsChallenge.base.adapters.BaseViewHolder
import com.dominate.thirtySecondsChallenge.base.bindingadapters.setImageFromUrl
import com.dominate.thirtySecondsChallenge.data.model.home.AdsResponseModel
import com.dominate.thirtySecondsChallenge.databinding.RowBannerBinding
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick

class BannerAdapter constructor(context: Context) :
    BaseBindingRecyclerViewAdapter<AdsResponseModel>(context) {

    val _context  = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowBannerBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])

        }
    }

    inner class ViewHolder(private val binding: RowBannerBinding) :
        BaseViewHolder<AdsResponseModel>(binding.root) {

        override fun bind(item: AdsResponseModel) {
            binding.item = item

//            Glide.with(_context).load(items[position].redirectUrl)
//                .placeholder( R.drawable.ic_load_image)
//                .error( R.drawable.ic_coin)
//                .into(binding.ivAds)

            binding.root.onClick {
                itemClickListener?.onItemClick(it, adapterPosition, item)
            }
        }
    }

}

