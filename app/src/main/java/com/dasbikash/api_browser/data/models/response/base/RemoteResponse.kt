package com.dasbikash.api_browser.data.models.response.base

import androidx.annotation.Keep

@Keep
data class RemoteResponse<T>(
    val payload:T?,
    val message:String?,
    val documentationUrl:String?,
    val statusCode:Int,
    val success:Boolean = false,
)