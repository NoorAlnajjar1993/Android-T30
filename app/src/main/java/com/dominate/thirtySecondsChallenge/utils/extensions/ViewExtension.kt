package com.dominate.thirtySecondsChallenge.utils.extensions

import android.animation.Animator
import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.ui.sound.AudioPlayer
import com.dominate.thirtySecondsChallenge.utils.anim.slideDownFast
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.System.currentTimeMillis


fun View?.hideKeyboard(activity: Activity?) {
    val imm: InputMethodManager? =
        activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    //Find the currently focused view, so we can grab the correct window token from it.
    imm?.hideSoftInputFromWindow(this?.windowToken, 0)
}

@SuppressLint("ClickableViewAccessibility")
fun setupUI(view: View, activity: Activity?) {

    // Set up touch listener for non-text box views to hide keyboard.
    if (view !is EditText) {
        view.setOnTouchListener { v, event ->
            if (activity != null) {
                hideSoftKeyboard(activity)
            }
            false
        }
    }

    //If a layout container, iterate over children and seed recursion.
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val innerView = view.getChildAt(i)
            setupUI(innerView, activity)
        }
    }
}

fun hideSoftKeyboard(activity: Activity) {
    val inputMethodManager = activity.getSystemService(
        Activity.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    if (inputMethodManager.isAcceptingText) {
        inputMethodManager.hideSoftInputFromWindow(
            activity.currentFocus?.windowToken,
            0
        )
    }
}

fun View?.showPopupMenu(
    @MenuRes menu: Int,
    clickListener: PopupMenu.OnMenuItemClickListener? = null
) {
    this?.let {
        PopupMenu(this.context, it).apply {
            // MainActivity implements OnMenuItemClickListener
            setOnMenuItemClickListener(clickListener)
            inflate(menu)
            show()
        }
    }
}

fun View.showSnackbar(@StringRes messageRes: Int, length: Int = Snackbar.LENGTH_LONG) {
    val snackBar = Snackbar.make(this, resources.getString(messageRes), length)
    snackBar.show()

}

@SuppressLint("RestrictedApi")
fun View.showSnackbar(string: String,snackbar: Int) {
    val snackBar = Snackbar.make(this, "", Snackbar.LENGTH_LONG)

    // Get the Snackbar layout
    val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
    val customTypeface = ResourcesCompat.getFont(context, R.font.ar_bold_font)

    // Create a TextView to hold the message
    val textView = TextView(context)
    textView.text = string
    textView.setTextColor(Color.WHITE)
    textView.gravity = Gravity.CENTER

    // Add the TextView to the Snackbar layout
    snackBarLayout.addView(textView, 0)

    customTypeface?.let { textView.typeface = Typeface.create(it, Typeface.NORMAL) }
    snackBarLayout.background = ContextCompat.getDrawable(context, snackbar)

    // snackBar.view.setBackgroundColor(context.getColor(R.color.green))
    // Set gravity to top
    val params = snackBarLayout.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.TOP
    params.topMargin = resources.getDimensionPixelSize(R.dimen.status_bar_size)
    snackBarLayout.layoutParams = params

    // animation
    context.slideDownFast(snackBarLayout)

    // Set callback for handling custom animation before hiding
    snackBar.addCallback(object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            // Apply custom animation before hiding
            val customAnimator = AnimatorInflater.loadAnimator(context, R.animator.animator_slide_up) as Animator
            customAnimator.setTarget(snackBarLayout)
            customAnimator.start()

        }
    })
    snackBarLayout.setOnClickListener {
        val customAnimator = AnimatorInflater.loadAnimator(context, R.animator.animator_slide_up) as Animator
        customAnimator.setTarget(snackBarLayout)
        customAnimator.start()
        snackBar.dismiss()
    }
    snackBar.show()
}


@SuppressLint("RestrictedApi")
fun Fragment.showSnackbarFragment(string: String, snackbar: Int) {
    val activityRootView = requireActivity().window.findViewById<View>(android.R.id.content)
    val snackBar = Snackbar.make(activityRootView, "", Snackbar.LENGTH_LONG)

    // Get the Snackbar layout
    val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
    val customTypeface = ResourcesCompat.getFont(this.requireContext(), R.font.ar_bold_font)

    // Create a TextView to hold the message
    val textView = TextView(context)
    textView.text = string
    textView.setTextColor(Color.WHITE)
    textView.gravity = Gravity.CENTER

    // Add the TextView to the Snackbar layout
    snackBarLayout.addView(textView, 0)

    customTypeface?.let { textView.typeface = Typeface.create(it, Typeface.NORMAL) }
    snackBarLayout.background = ContextCompat.getDrawable(this.requireContext(), snackbar)

    // snackBar.view.setBackgroundColor(context.getColor(R.color.green))
    // Set gravity to top
    val params = snackBarLayout.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.TOP
    params.topMargin = resources.getDimensionPixelSize(R.dimen.status_bar_size)
    snackBarLayout.layoutParams = params

    // animation
    context.slideDownFast(snackBarLayout)

    // Set callback for handling custom animation before hiding
    snackBar.addCallback(object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            // Apply custom animation before hiding
            val customAnimator = AnimatorInflater.loadAnimator(context, R.animator.animator_slide_up) as Animator
            customAnimator.setTarget(snackBarLayout)
            customAnimator.start()

        }
    })
    snackBarLayout.setOnClickListener {
        val customAnimator = AnimatorInflater.loadAnimator(context, R.animator.animator_slide_up) as Animator
        customAnimator.setTarget(snackBarLayout)
        customAnimator.start()
        snackBar.dismiss()
    }

    snackBarLayout.elevation = 6f

    snackBar.show()
}

@SuppressLint("RestrictedApi")
fun Activity.showSnackbarActivity(string: String, snackbar: Int) {
    val rootView: View = findViewById(android.R.id.content) // Get the root view of the activity
    val snackBar = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG)

    // Get the Snackbar layout
    val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
    val customTypeface = ResourcesCompat.getFont(this, R.font.ar_bold_font)

    // Create a TextView to hold the message
    val textView = TextView(this)
    textView.text = string
    textView.setTextColor(Color.WHITE)
    textView.gravity = Gravity.CENTER

    // Add the TextView to the Snackbar layout
    snackBarLayout.addView(textView, 0)

    customTypeface?.let { textView.typeface = Typeface.create(it, Typeface.NORMAL) }
    snackBarLayout.background = ContextCompat.getDrawable(this, snackbar)

    // snackBar.view.setBackgroundColor(context.getColor(R.color.green))
    // Set gravity to top
    val params = snackBarLayout.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.TOP
    params.topMargin = resources.getDimensionPixelSize(R.dimen.status_bar_size)
    snackBarLayout.layoutParams = params

    // animation
    this.slideDownFast(snackBarLayout)

    // Set callback for handling custom animation before hiding
    snackBar.addCallback(object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            // Apply custom animation before hiding
            val customAnimator = AnimatorInflater.loadAnimator(rootView.context, R.animator.animator_slide_up) as Animator
            customAnimator.setTarget(snackBarLayout)
            customAnimator.start()

        }
    })
    snackBarLayout.setOnClickListener {
        val customAnimator = AnimatorInflater.loadAnimator(this, R.animator.animator_slide_up) as Animator
        customAnimator.setTarget(snackBarLayout)
        customAnimator.start()
        snackBar.dismiss()
    }
    snackBar.show()
}


@SuppressLint("RestrictedApi")
fun View.showSnackbarError(string: String, snackbar: Int) {
    val snackBar = Snackbar.make(this, "", Snackbar.LENGTH_LONG)

    // Get the Snackbar layout
    val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
    val customTypeface = ResourcesCompat.getFont(context, R.font.ar_bold_font)

    // Create a TextView to hold the message
    val textView = TextView(context)
    textView.text = string
    textView.setTextColor(Color.WHITE)
    textView.gravity = Gravity.CENTER

    // Add the TextView to the Snackbar layout
    snackBarLayout.addView(textView, 0)

    customTypeface?.let { textView.typeface = Typeface.create(it, Typeface.NORMAL) }
    snackBarLayout.background = ContextCompat.getDrawable(context, snackbar)

    // snackBar.view.setBackgroundColor(context.getColor(R.color.green))
    // Set gravity to top
    val params = snackBarLayout.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.TOP
    params.topMargin = resources.getDimensionPixelSize(R.dimen.status_bar_size)
    snackBarLayout.layoutParams = params

    // animation
    context.slideDownFast(snackBarLayout)

    // Set callback for handling custom animation before hiding
    snackBar.addCallback(object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            // Apply custom animation before hiding
            val customAnimator = AnimatorInflater.loadAnimator(context, R.animator.animator_slide_up) as Animator
            customAnimator.setTarget(snackBarLayout)
            customAnimator.start()

        }
    })
    snackBarLayout.setOnClickListener {
        val customAnimator = AnimatorInflater.loadAnimator(context, R.animator.animator_slide_up) as Animator
        customAnimator.setTarget(snackBarLayout)
        customAnimator.start()
        snackBar.dismiss()
    }
    snackBar.show()
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.setupClearButtonWithAction() {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            val clearIcon = if (editable?.isNotEmpty() == true) R.drawable.ic_clear else 0
            setCompoundDrawablesWithIntrinsicBounds(0, 0, clearIcon, 0)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    })

    setOnTouchListener(View.OnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                this.setText("")
                return@OnTouchListener true
            }
        }
        return@OnTouchListener false
    })
}

fun View.checkIfAllEditTextsFilled(): Boolean {
    val layout = this as ViewGroup
    var enable = false
    for (i in 0 until layout.childCount) {
        val child = layout.getChildAt(i)
        if (child is EditText) {
            if (child.text.toString().isNotEmpty()) {
                enable = true
            }
        } else if (child is ViewGroup) {
            child.checkIfAllEditTextsFilled()
        }
    }
    return enable
}

fun View.addEditTextsWatcher(textWatcher: TextWatcher) {
    val layout = this as ViewGroup
    for (i in 0 until layout.childCount) {
        val child = layout.getChildAt(i)
        if (child is EditText) {
            child.addTextChangedListener(textWatcher)
        } else if (child is ViewGroup) {
            child.addEditTextsWatcher(textWatcher)
        }

    }
}


fun View.disableView() {
    isEnabled = false
}

fun View.enableView() {
    isEnabled = true
}


fun View.disableViews() {
    val layout = this as ViewGroup
    for (i in 0 until layout.childCount) {
        val child = layout.getChildAt(i)
        child.isEnabled = false
        if (child is ViewGroup) {
            child.disableViews()
        }
    }

}

fun View.enableViews() {
    val layout = this as ViewGroup
    for (i in 0 until layout.childCount) {
        val child = layout.getChildAt(i)
        child.isEnabled = true
        if (child is ViewGroup) {
            child.enableViews()
        }
    }
}


//fun ImageView.loadImage(url: String?) {
//    val fullUrl = when {
//        url?.startsWith("/storage") == true -> {
//            url
//        }
//        url?.startsWith("http") == false -> {
//            IMAGES_BASE_URL + url
//        }
//        else -> {
//            url
//        }
//    } as String
//
//    Glide.with(this).load(fullUrl)
//        .into(this)
//}

inline fun EditText.onTextChanged(crossinline callback: (text: CharSequence?) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            callback(s)
        }
    })
}

fun View.onClick(click: (View) -> Unit) {
    var lastClickTime = 0L
    setOnClickListener {
        AudioPlayer(context, R.raw.click_t30).startPlayback()
        if (currentTimeMillis() > lastClickTime + 650) click(this)
        lastClickTime = currentTimeMillis()
    }
}