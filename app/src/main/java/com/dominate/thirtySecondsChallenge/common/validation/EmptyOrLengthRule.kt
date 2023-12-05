package com.dominate.thirtySecondsChallenge.common.validation

import com.mobsandgeeks.saripaar.AnnotationRule

class EmptyOrLengthRule(emptyOrLength: EmptyOrLength) : AnnotationRule<EmptyOrLength, String>(
    emptyOrLength
) {

    override fun isValid(text: String?): Boolean {
        requireNotNull(text) { "'text' cannot be null." }
        val ruleMin: Int = mRuleAnnotation.min
        val ruleMax: Int = mRuleAnnotation.max

        // Assert min is <= max
        assertMinMax(ruleMin, ruleMax)

        // Trim?
        val length = if (mRuleAnnotation.trim) text.trim { it <= ' ' }.length else text.length

        // Check for min length
        var minIsValid = true
        if (ruleMin != Int.MIN_VALUE) { // Min is set
            minIsValid = length >= ruleMin
        }

        // Check for max length
        var maxIsValid = true
        if (ruleMax != Int.MAX_VALUE) { // Max is set
            maxIsValid = length <= ruleMax
        }
        return (minIsValid && maxIsValid) || text.isEmpty()
    }

    private fun assertMinMax(min: Int, max: Int) {
        if (min > max) {
            val message = String.format(
                "'min' (%d) should be less than or equal to 'max' (%d).", min, max
            )
            throw IllegalStateException(message)
        }
    }
}
