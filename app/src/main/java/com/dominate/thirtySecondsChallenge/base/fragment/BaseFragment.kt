package com.dominate.thirtySecondsChallenge.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener
import com.bumptech.glide.load.HttpException
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.utils.HandleRequestFailedUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.refreshLocal
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.io.IOException
import java.net.SocketTimeoutException


abstract class BaseFragment : Fragment() , OnLocaleChangedListener {

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
        toolbar?.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
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

//    fun showNotAvailableYetDialog() {
//        iOSDialogBuilder(context)
//            .setTitle(getString(R.string.alert_dialog_title))
//            .setSubtitle(getString(R.string.not_available_yet))
//            .setBoldPositiveLabel(true)
//            .setCancelable(true)
//            .setPositiveListener(
//                context?.getString(R.string.ok)
//            ) { dialog ->
//                val number = "06-5504545"
//                val intent = Intent(Intent.ACTION_DIAL)
//                intent.data = Uri.parse("tel:$number ")
//                startActivity(intent)
//                dialog.dismiss()
//            }
//            .build().show()
//    }
    override fun onAfterLocaleChanged() {
    }

    override fun onBeforeLocaleChanged() {

    }

}