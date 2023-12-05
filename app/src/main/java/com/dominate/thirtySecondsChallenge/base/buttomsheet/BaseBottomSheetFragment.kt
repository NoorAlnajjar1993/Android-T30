package com.dominate.thirtySecondsChallenge.base.buttomsheet

import android.os.Bundle
import android.view.*
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.refreshLocal
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException


abstract class BaseBottomSheetFragment : BottomSheetDialogFragment(), OnLocaleChangedListener {

    protected abstract val layoutId: Int

    var mView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.refreshLocal()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mView == null) {
            mView = inflater.inflate(layoutId, container, false)
            onViewVisible(mView!!)
        }
        initToolbar()
        observee()
        return mView
    }

    protected open fun onViewVisible(view: View) {
    }

    protected open fun initToolbar() {
    }

    protected open fun observee() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        toolbar?.setNavigationOnClickListener {
//            activity?.onBackPressed()
//        }
    }

    fun handleError(throwable: Throwable?) {
        when (throwable) {
            is IOException -> {
                HandleRequestFailedUtil.showDialogMessage(
                    listOf(getString(R.string.error_no_internet)),
                    requireContext(),
                    childFragmentManager
                )
            }
            is SocketTimeoutException -> {
                HandleRequestFailedUtil.showDialogMessage(
                    listOf(getString(R.string.error_server)),
                    requireContext(),
                    childFragmentManager
                )
            }
            is HttpException -> {

            }
            else -> {
                HandleRequestFailedUtil.showDialogMessage(
                    listOf(getString(R.string.error_msg)),
                    requireContext(),
                    childFragmentManager
                )
            }
        }
    }

    override fun onAfterLocaleChanged() {
    }

    override fun onBeforeLocaleChanged() {

    }

}