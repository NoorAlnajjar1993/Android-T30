package com.dominate.thirtySecondsChallenge.ui.whatdoyouknow.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.BaseAdapter
import com.dominate.thirtySecondsChallenge.data.signalR.isready.AnswerModel
import com.dominate.thirtySecondsChallenge.databinding.RowQuestionsBinding
import com.dominate.thirtySecondsChallenge.ui.whatdoyouknow.model.QuestionModel
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import javax.inject.Inject

class QuestionAdapter @Inject constructor(

) :
    BaseAdapter<AnswerModel, QuestionAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<RowQuestionsBinding>(
            inflater,
            R.layout.row_questions, parent, false
        )


        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]

//        val animation: Animation =
//            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.zoom_in)
//        holder.binding.clMain.startAnimation(animation)

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

    class ViewHolder(val binding: RowQuestionsBinding) :
        RecyclerView.ViewHolder(binding.root)

}