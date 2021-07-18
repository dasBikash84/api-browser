package com.dasbikash.api_browser.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.dasbikash.api_browser.R
import com.dasbikash.api_browser.data.models.ParamInput
import com.dasbikash.api_browser.ui.input_filters.DecimalInputFilter
import com.dasbikash.api_browser.ui.input_filters.IntegerInputFilter
import kotlinx.android.synthetic.main.layout_path_param_input_view.view.*

@SuppressLint("SetTextI18n")
class ParamInputView(
    private val paramName:String,
    context: Context?,
    private val isPathParam:Boolean = true,
    private val isOptional:Boolean = true,
    private val valueType:Class<*> = String::class.java,
    attrs: AttributeSet? = null
) :LinearLayout(context, attrs) {

    init {
        LayoutInflater
            .from(context)
            .inflate(
                R.layout.layout_path_param_input_view, this, true
            )

        if (paramName.isNotBlank()) {
            tvPathParamName.text = "$paramName :"
            etParam.hint = paramName//if(isOptional) "Optional" else ""

            if (valueType == Int::class.java) {
                IntegerInputFilter()
            } else if (
                valueType == Float::class.java ||
                valueType == Double::class.java
            ) {
                DecimalInputFilter()
            }
        }else{rootView.isVisible = false}
    }

    fun getInput(): ParamInput =
        if (!etParam.text.isNullOrBlank()){
            when(valueType){
                Int::class.java -> etParam.text!!.trim().toString().toInt()
                Float::class.java -> etParam.text!!.trim().toString().toFloat()
                Double::class.java -> etParam.text!!.trim().toString().toDouble()
                else -> etParam.text!!.trim().toString()
            }
        }else {
            null
        }.let {
            ParamInput(paramName,isPathParam,isOptional,it)
        }
}