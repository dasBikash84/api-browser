package com.dasbikash.api_browser.data.models.response.base

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ErrorResponse(
	@field:SerializedName("message")
	val message: String?,

	@field:SerializedName("documentation_url")
	val documentationUrl: String?
)
