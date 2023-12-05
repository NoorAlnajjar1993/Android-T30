package com.dominate.thirtySecondsChallenge.ui.whatdoyouknow.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.databinding.RowStrickBinding
import com.dominate.thirtySecondsChallenge.ui.whatdoyouknow.model.StrikeModel
import javax.inject.Inject

class StrikeAdapter @Inject constructor(
) :
    BaseAdapter<StrikeModel, StrikeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowStrickBinding>(
            inflater,
            R.layout.row_strick, parent, false
        )


        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]


    }

    class ViewHolder(val binding: RowStrickBinding) :
        RecyclerView.ViewHolder(binding.root)

}