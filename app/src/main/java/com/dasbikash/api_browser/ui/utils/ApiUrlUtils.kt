package com.dasbikash.api_browser.ui.utils

import android.content.Context
import com.dasbikash.api_browser.ui.custom.ParamInputView

object ApiUrlUtils {

    private val PATH_PARAM_PLACE_HOLDER_REGEX = Regex("\\{[a-zA-Z0-9/_]+\\}")
    private val REQ_QUERY_MATCHER_REGEX = Regex("[^}&?]+=\\{")
    private val OPTIONAL_QUERY_MATCHER_REGEX = Regex("\\{[&\\?][^=]+\\}")

    fun getPathString(url:String?): String? = url?.split("?")?.let { it[0] }

    fun getPathParamPlaceholders(url: String?):List<String> =
        mutableListOf<String>().apply {
            getPathString(url)?.let { path ->
                var match = PATH_PARAM_PLACE_HOLDER_REGEX.find(path)
                while (match != null) {
                    add(path.substring(match.range))
                    match = match.next()
                }
            }
        }

    fun getPathParamsInputViews(
        url: String?,context:Context
    ):List<ParamInputView> =
        getPathParamPlaceholders(url).map { matchedString ->
            val paramName = matchedString
                .replace("{","")
                .replace("}","")
                .replace("/","")
            ParamInputView(paramName,context,true,matchedString.contains('/'))
        }

    private fun getRequiredQueryParams(url: String?):List<String> =
        mutableListOf<String>().apply {
            url?.let { queryString ->
                var reqMatch = REQ_QUERY_MATCHER_REGEX.find(queryString)
                while (reqMatch != null) {
                    val match = queryString.substring(reqMatch.range)
                    add(match.substring(0, match.indexOfFirst { it == '=' }))
                    reqMatch = reqMatch.next()
                }
            }
        }
    private fun getOptionalQueryParams(url: String?):List<String> =
        mutableListOf<String>().apply {
            url?.let { queryString ->
                var reqMatch = OPTIONAL_QUERY_MATCHER_REGEX.find(queryString)
                if (reqMatch != null) {
                    queryString.substring(reqMatch.range).let {
                        println("queryString: $it")
                        addAll(
                            it.substring(2, it.length - 1).split(",")
                                .apply { println("queryString: $this") })
                    }
                }
            }
        }

    fun getQueryParamQueryParamInputViews(url: String?,context: Context):List<ParamInputView> =
        mutableListOf<ParamInputView>().apply {
            getRequiredQueryParams(url).asSequence().forEach {
                add(ParamInputView(it,context, isPathParam = false, isOptional = false))
            }
            getOptionalQueryParams(url).asSequence().forEach {
                add(ParamInputView(it,context, isPathParam = false, isOptional = true))
            }
        }

}