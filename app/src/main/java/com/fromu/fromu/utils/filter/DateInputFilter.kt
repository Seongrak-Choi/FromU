package com.fromu.fromu.utils.filter

import android.text.InputFilter
import android.text.Spanned


class DateInputFilter : InputFilter {
    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence {
        val builder = StringBuilder(dest)
        builder.replace(dstart, dend, source.subSequence(start, end).toString())
        val date = builder.toString()

        // Check if the date is valid and within range
        if (!isValidDate(date)) {
            return dest.subSequence(dstart, dend)
        }

        // Format the date with slashes
        val formattedDate = formatDate(date)

        // Check if the formatted date length is greater than the maximum length
        return if (formattedDate.length > MAX_LENGTH) {
            dest.subSequence(dstart, dend)
        } else formattedDate
    }

    private fun isValidDate(date: String): Boolean {
        // Implement your date validation logic here
        // Return true if the date is valid, false otherwise
        return true
    }

    private fun formatDate(date: String): String {
        // Format the date with slashes
        var formattedDate = ""
        if (date.isNotEmpty()) {
            formattedDate = date.substring(0, Math.min(4, date.length))
            if (date.length > 4) {
                formattedDate += DATE_SEPARATOR + date.substring(4, Math.min(6, date.length))
                if (date.length > 6) {
                    formattedDate += DATE_SEPARATOR + date.substring(6, Math.min(8, date.length))
                }
            }
        }
        return formattedDate
    }

    companion object {
        private const val MAX_LENGTH = 10
        private const val DATE_SEPARATOR = "/"
    }
}