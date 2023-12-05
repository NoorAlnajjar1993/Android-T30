package com.dominate.thirtySecondsChallenge.ui.player.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.data.model.player.ReportReasonResponseModel
import com.dominate.thirtySecondsChallenge.databinding.RowReportBinding
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import javax.inject.Inject

class ReportAdapter @Inject constructor(

) :
    BaseAdapter<ReportReasonResponseModel, ReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowReportBinding>(
            inflater,
            R.layout.row_report, parent, false
        )


        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]

        holder.itemView.onClick {

            for (item in items){
                if (item.isSelected){
                    item.isSelected = false
                }
            }
            items[position].isSelected = true
            notifyDataSetChanged()

            onItemClickListener?.onItemClicked(it, items[position], position)
        }

    }

    class ViewHolder(val binding: RowReportBinding) :
        RecyclerView.ViewHolder(binding.root)

}