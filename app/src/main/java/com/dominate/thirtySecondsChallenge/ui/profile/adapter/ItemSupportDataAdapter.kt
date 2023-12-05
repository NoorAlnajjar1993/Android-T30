package com.dominate.thirtySecondsChallenge.ui.profile.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.transition.AutoTransition
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseBindingRecyclerViewAdapterAny
import com.dominate.thirtySecondsChallenge.base.adapters.BaseViewHolder
import com.dominate.thirtySecondsChallenge.data.model.profile.GetAllFaqsResponseModel
import com.dominate.thirtySecondsChallenge.databinding.RowItemSupportDataBinding
import com.dominate.thirtySecondsChallenge.ui.profile.model.ItemSupportData
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick


class ItemSupportDataAdapter constructor(context: Context, flag: Int) :
    BaseBindingRecyclerViewAdapterAny<GetAllFaqsResponseModel>(context) {

    val flag = flag

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemSupportDataBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }

    }

    inner class ViewHolder(private val binding: RowItemSupportDataBinding) :
        BaseViewHolder<GetAllFaqsResponseModel>(binding.root) {

        override fun bind(item: GetAllFaqsResponseModel) {
            binding.item = item
            val autoTransition = AutoTransition()
            autoTransition.duration = 350


            binding.hiddenView.visibility = if (item.isSelected) View.VISIBLE else View.GONE
            binding.root.onClick {

//                if (binding.hiddenView.visibility == View.VISIBLE) {
//                    TransitionManager.beginDelayedTransition(binding.baseCardview, autoTransition)
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        binding.hiddenView.visibility = View.GONE
//                    }, 350)
//                    binding.ivArrow.setImageResource(R.drawable.ic_spinner_arrow)
//                } else {
//                    TransitionManager.beginDelayedTransition(binding.baseCardview, AutoTransition())
//                    binding.hiddenView.visibility = View.VISIBLE
//                    binding.ivArrow.setImageResource(R.drawable.ic_arrow_up)
//                }
//                item.isSelected = !item.isSelected
//                notifyItemChanged(adapterPosition)
                item.isSelected = !item.isSelected
                notifyItemChanged(adapterPosition)

                itemClickListener?.onItemClick(it, adapterPosition, item, flag)
            }

        }


    }

}