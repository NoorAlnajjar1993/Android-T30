package com.dominate.thirtySecondsChallenge.base.bindingadapters

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Html
import android.text.format.DateUtils
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.common.CommonEnums
import com.dominate.thirtySecondsChallenge.utils.extensions.DateTimeUtil
import com.dominate.thirtySecondsChallenge.utils.extensions.toMillieSecconds
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants
import com.dominate.thirtySecondsChallenge.utils.pref.SharedPreferencesUtil
import java.util.*



@BindingAdapter("tvHtmlText")
fun TextView?.setTextWithHtmlFormatting(htmlText: String) {
    this?.text = Html.fromHtml(htmlText)
}

@BindingAdapter(
    value = ["tvDateText", "tvOutputDateFormat", "tvInputDateFormat"],
    requireAll = true
)
fun TextView?.setFormattedDateText(
    date: String?,
    outputFormat: String,
    inputFormat: String
) {
    date?.let {
        this?.text = DateTimeUtil.changeDateFormat(
            date,
            inputFormat,
            outputFormat,
            true
        )
    }
}

@BindingAdapter("tvHexTextColor")
fun TextView?.setHexTextColor(color: String) {
    this?.setTextColor(Color.parseColor(color))
}

@BindingAdapter("tvResTextColor")
fun TextView?.setResTextColor(@ColorRes color: Int) {
    this?.setTextColor(ContextCompat.getColor(context, color))
}

@BindingAdapter("tvIsScrolling")
fun TextView?.isScrolling(enable: Boolean) {
    if (enable)
        this?.movementMethod = ScrollingMovementMethod()
}

@BindingAdapter("tvRelativeTime")
fun TextView?.setRelativeTime(date: String) {
    this?.text = DateUtils.getRelativeTimeSpanString(
        date.toMillieSecconds(),
        Calendar.getInstance().timeInMillis, DateUtils.MINUTE_IN_MILLIS
    )
}


@BindingAdapter(
    value = [
        "ivDrawableStartImageUrl"
    ],
    requireAll = false
)
fun TextView.setDrawableStartImageUrl(
    imageUrl: String?
) {
    Glide.with(context).load(imageUrl).apply(RequestOptions().fitCenter()).into(
        object : CustomTarget<Drawable>(
            resources.getDimension(com.intuit.sdp.R.dimen._17sdp).toInt(),
            resources.getDimension(com.intuit.sdp.R.dimen._12sdp).toInt()
        ) {
            override fun onLoadCleared(placeholder: Drawable?) {
            }

            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable>?
            ) {

                if (SharedPreferencesUtil.getInstance(context).getStringPreferences(
                        PrefConstants.APP_LANGUAGE_VALUE,
                        CommonEnums.Languages.English.value
                    ) ==  CommonEnums.Languages.English.value) {
                    setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null)
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(null, null, resource,null)
                }
            }
        }
    )
}

@BindingAdapter(
    value = [
        "ivDrawableEndImageUrl"
    ],
    requireAll = false
)
fun TextView.setDrawableEndImageUrl(
    imageUrl: String?
) {
    Glide.with(context).load(imageUrl).apply(RequestOptions().fitCenter()).into(
        object : CustomTarget<Drawable>(
            resources.getDimension(com.intuit.sdp.R.dimen._17sdp).toInt(),
            resources.getDimension(com.intuit.sdp.R.dimen._12sdp).toInt()
        ) {
            override fun onLoadCleared(placeholder: Drawable?) {
            }

            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable>?
            ) {

                if (SharedPreferencesUtil.getInstance(context).getStringPreferences(
                        PrefConstants.APP_LANGUAGE_VALUE,
                        CommonEnums.Languages.English.value
                    ) == CommonEnums.Languages.English.value
                ) {
                    setCompoundDrawablesWithIntrinsicBounds(null, null, resource, null)
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(resource, null,null, null)
                }
            }
        }
    )
}

@BindingAdapter(
    value = [
        "ivSpinnerDrawableStartImageUrl"
    ],
    requireAll = false
)
fun TextView.setSpinnerDrawableStartImageUrl(
    imageUrl: String?
) {
    Glide.with(context).load(imageUrl).apply(RequestOptions().fitCenter()).into(
        object : CustomTarget<Drawable>(
            resources.getDimension(com.intuit.sdp.R.dimen._20sdp).toInt(),
            resources.getDimension(com.intuit.sdp.R.dimen._20sdp).toInt()
        ) {
            override fun onLoadCleared(placeholder: Drawable?) {
            }

            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable>?
            ) {

                if (SharedPreferencesUtil.getInstance(context).getStringPreferences(
                        PrefConstants.APP_LANGUAGE_VALUE,
                        CommonEnums.Languages.English.value
                    ) == CommonEnums.Languages.English.value
                ) {
                    setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null)
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(null, null, resource, null)
                }
            }
        }
    )

    if (imageUrl == null) {

        Glide.with(context).load(R.drawable.ic_default_image_place_holder).apply(RequestOptions().fitCenter())
            .into(
                object : CustomTarget<Drawable>(
                    resources.getDimension(com.intuit.sdp.R.dimen._20sdp).toInt(),
                    resources.getDimension(com.intuit.sdp.R.dimen._20sdp).toInt()
                ) {
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        if (SharedPreferencesUtil.getInstance(context).getStringPreferences(
                                PrefConstants.APP_LANGUAGE_VALUE,
                                CommonEnums.Languages.English.value
                            ) == CommonEnums.Languages.English.value
                        ) {
                            setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null)
                        } else {
                            setCompoundDrawablesWithIntrinsicBounds(null, null, resource, null)
                        }
                    }
                }
            )
    }
}

