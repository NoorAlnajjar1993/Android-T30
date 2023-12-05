package com.dominate.thirtySecondsChallenge.ui.shoppingcart.adapter

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.data.model.store.CoinsResponseModel
import com.dominate.thirtySecondsChallenge.databinding.RowCurrenciesBinding
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.model.CurrenciesModel
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import javax.inject.Inject

class CurrenciesAdapter @Inject constructor(

) :
    BaseAdapter<CoinsResponseModel, CurrenciesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowCurrenciesBinding>(
            inflater,
            R.layout.row_currencies, parent, false
        )


        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]

        val animation: Animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.zoom_in)
        holder.binding.clMain.startAnimation(animation)

        holder.itemView.onClick {
            onItemClickListener?.onItemClicked(it, items[position], position)
        }

    }

    class ViewHolder(val binding: RowCurrenciesBinding) :
        RecyclerView.ViewHolder(binding.root)

}