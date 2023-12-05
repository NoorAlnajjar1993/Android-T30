package com.dominate.thirtySecondsChallenge.ui.profile.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.data.model.profile.BackgroundImagesProfileResponseModel
import com.dominate.thirtySecondsChallenge.databinding.RowEditBackgroundProfileBinding
import com.dominate.thirtySecondsChallenge.ui.profile.model.EditBackgroundModel
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import javax.inject.Inject

class EditBackgroundProfileAdapter @Inject constructor(

) :
    BaseAdapter<BackgroundImagesProfileResponseModel, EditBackgroundProfileAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowEditBackgroundProfileBinding>(
            inflater,
            R.layout.row_edit_background_profile, parent, false
        )


        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]

        holder.itemView.onClick {

            items.forEachIndexed { index, value ->
                if (value.isSelected) {
                    value.isSelected = false
                    notifyItemChanged(index)
                }
            }
            items[position].isSelected = true
            notifyItemChanged(position)

            onItemClickListener?.onItemClicked(it, items[position], position)
        }

    }

    class ViewHolder(val binding: RowEditBackgroundProfileBinding) :
        RecyclerView.ViewHolder(binding.root)

}