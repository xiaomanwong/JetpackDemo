package com.example.lib_common

import android.content.Context
import java.text.DecimalFormatSymbols
import java.text.Normalizer
import java.util.Locale

class PunctuationFormatter(context: Context) {
    private val locale: Locale = context.resources.configuration.locales[0]


    fun normalizePunctuation(input: String): String {
        val currentLocale = locale
        val normalizedString = Normalizer.normalize(input, Normalizer.Form.NFD)
        val stringBuilder = StringBuilder()

        for (c in normalizedString) {
            if (Character.isLetterOrDigit(c) || Character.isWhitespace(c)) {
                stringBuilder.append(c)
            } else {
                val normalizedChar = Normalizer.normalize(c.toString(), Normalizer.Form.NFC)
                val punctuation = normalizedChar.trim()
                val localizedPunctuation =
                    DecimalFormatSymbols.getInstance(currentLocale).exponentSeparator

                if (punctuation.isNotEmpty()) {
                    stringBuilder.append(localizedPunctuation)
                }
            }
        }

        return stringBuilder.toString()
    }
}
