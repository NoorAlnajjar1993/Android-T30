package com.dominate.thirtySecondsChallenge.base.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView


abstract class BaseViewHolder<MODEL>(itemView: View?) :
    RecyclerView.ViewHolder(itemView!!) {
    abstract fun bind(item: MODEL)
}