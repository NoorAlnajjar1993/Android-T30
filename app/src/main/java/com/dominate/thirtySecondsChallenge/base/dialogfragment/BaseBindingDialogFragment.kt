package com.dominate.thirtySecondsChallenge.base.dialogfragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


abstract class BaseBindingDialogFragment<V : ViewDataBinding> : BaseDialogFragment() {

    var binding: V? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
            mView = binding?.root
            binding?.lifecycleOwner = this
            onViewVisible(mView!!)
        }

        initToolbar()
        return mView
    }


}