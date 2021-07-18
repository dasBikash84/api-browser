package com.dasbikash.api_browser.ui.input_filters

import android.text.InputFilter
import android.text.Spanned

abstract class NumberInputFilter: InputFilter {

    abstract val acceptDecimal: Boolean

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        val strBuilder = StringBuilder()

        source?.forEach { char ->
            if (char.isDigit()) strBuilder.append(char)
            else if ((acceptDecimal && char == '.')) strBuilder.append(char)
        }
        return strBuilder
    }
}

class IntegerInputFilter(override val acceptDecimal: Boolean = false) :NumberInputFilter()
class DecimalInputFilter(override val acceptDecimal: Boolean = true) :NumberInputFilter()