package com.dasbikash.api_browser.data.models

import androidx.annotation.Keep

@Keep
data class ParamInput(
    val paramName:String,
    val isPathParam:Boolean,
    val isOptional:Boolean,
    val paramValue:Any?
)