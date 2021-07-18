package com.dasbikash.api_browser.data.models.response.repo_search

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RepoSearchResponse(

	@field:SerializedName("total_count")
	val totalCount: Int? = null,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean? = null,

	@field:SerializedName("items")
	val items: List<RepoDetail?>? = null
)