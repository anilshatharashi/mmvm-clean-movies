package com.example.mvvmclean.anilshatharashi.presentation.mapper

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DateFormatterImpl @Inject constructor(private val dateFormat: DateFormat) : DateFormatter {

    override fun format(givenString: String?): String = givenString?.let {
        val date = dateFormat.parse(it)
        return date?.run {
            SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(this)
        } ?: "Invalid Date"
    } ?: "Invalid Date"
}