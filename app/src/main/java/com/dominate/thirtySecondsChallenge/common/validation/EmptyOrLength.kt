package com.dominate.thirtySecondsChallenge.common.validation

import com.mobsandgeeks.saripaar.annotation.ValidateUsing

@ValidateUsing(EmptyOrLengthRule::class)
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class EmptyOrLength(
    val min: Int,
    val max: Int = Int.MAX_VALUE,
    val trim: Boolean,
    val sequence: Int = -1,
    val messageResId: Int,
    val message: String = "Oops... too pricey"
)