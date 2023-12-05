package com.dominate.thirtySecondsChallenge.ui.shoppingcart.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.data.model.store.DiamondsResponseModel
import com.dominate.thirtySecondsChallenge.databinding.RowCurrenciesBinding
import com.dominate.thirtySecondsChallenge.databinding.RowDiamondCartBinding
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.model.CurrenciesModel
import com.dominate.thirtySecondsChallenge.ui.shoppingcart.model.DiamondsCartModel
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import javax.inject.Inject

class DiamondsCartAdapter @Inject constructor(

) :
    BaseAdapter<DiamondsResponseModel, DiamondsCartAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowDiamondCartBinding>(
            inflater,
            R.layout.row_diamond_cart, parent, false
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

    class ViewHolder(val binding: RowDiamondCartBinding) :
        RecyclerView.ViewHolder(binding.root)

}