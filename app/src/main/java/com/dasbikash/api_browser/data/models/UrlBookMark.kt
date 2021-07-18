package com.dasbikash.api_browser.data.models

import androidx.annotation.Keep
import java.io.Serializable
import java.util.*

@Keep
data class UrlBookMark(
    val url: String,
    val displayName: String,
    val time: Date = Date()
) : Serializable {
    fun updateUrl(url: String) = copy(url = url, time = Date())
    fun updateDisplayName(displayName: String) = copy(displayName = displayName, time = Date())
}