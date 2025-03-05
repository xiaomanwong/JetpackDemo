package com.example.lib_common

import android.content.Context
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

val numberFormatter by lazy { NumberFormatter(getApplication()) }

class NumberFormatter(context: Context) {
    private val locale: Locale = context.resources.configuration.locales[0]

    fun format(number: Double, full: Boolean = true, digits: Int = 0): String {
        if (full) {
            val numberFormat = NumberFormat.getNumberInstance(locale)
            numberFormat.maximumFractionDigits = digits
            return numberFormat.format(number)
        } else {
            return formatAbbreviated(number, digits)
        }
    }

    private fun formatAbbreviated(number: Double, digits: Int): String {
        // Set units and suffixes based on the language
        val (suffix, unit) = getEastAsianSuffix()
        val magnitude = log10(abs(number)).toInt()
        var scale = if (magnitude < unit || number < 9999) 0 else magnitude / unit

        if (scale >= suffix.size) {
            scale = suffix.size - 1
        }

        val scaledValue: Double = number / 10.0.pow(scale * unit.toDouble())
        val numberFormat = NumberFormat.getNumberInstance(locale)
        numberFormat.maximumFractionDigits = digits
        return numberFormat.format(scaledValue) + suffix[scale]
    }

    private fun getEastAsianSuffix(): Pair<Array<String>, Int> {
        return when (locale) {
            Locale.TAIWAN -> Pair(arrayOf("", "萬", "億"), 4)
            Locale.KOREA -> Pair(arrayOf("", "만", "억"), 4)
            Locale.JAPAN -> Pair(arrayOf("", "万", "亿"), 4)
            Locale("zh", "SG") -> Pair(arrayOf("", "万", "亿"), 4)
            Locale("id", "ID") -> Pair(arrayOf("", "K", "JT", "M"), 3)
            Locale("ms", "MY") -> Pair(arrayOf("", "K", "J", "B"), 3)
            else -> Pair(arrayOf("", "K", "M", "B"), 3)
        }
    }
}