package com.dasbikash.api_browser.data.models

import androidx.annotation.Keep
import com.google.gson.JsonObject

@Keep
data class IndexedJsonObject(
    val key: String?,
    val jasonObject: JsonObject
)