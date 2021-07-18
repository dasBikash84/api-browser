package com.dasbikash.api_browser.ui.custom

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import com.dasbikash.api_browser.R
import com.dasbikash.api_browser.ui.utils.ApiUrlUtils
import com.dasbikash.api_browser.ui.utils.showToast
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.net.URLEncoder

class UrlParamInputView(
    context: Context,
    url: String,
    sendRequestWithParams: (String) -> Unit
) : LinearLayout(context) {
    private val paramInputBlock: ViewGroup
    private val vhParamInput: LinearLayoutCompat
    private val btnSendWithParam: Button

    init {
        LayoutInflater
            .from(context)
            .inflate(
                R.layout.layout_url_param_input_view, this, true
            )
//        setContentView(R.layout.layout_url_param_input_view)
        paramInputBlock = rootView?.findViewById(R.id.paramInputBlock)!!
        vhParamInput = rootView?.findViewById(R.id.vhParamInput)!!
        btnSendWithParam = rootView?.findViewById(R.id.btnSendWithParam)!!

        getRequiredParamInputView(url).let { paramInputViews ->
            vhParamInput.removeAllViews()
            if (paramInputViews.isNotEmpty()) {
                paramInputViews.asSequence()
                    .forEach { vhParamInput.addView(it) }
            }else{
                paramInputBlock.isVisible = false
            }
        }

        btnSendWithParam.setOnClickListener {
            getFormattedUrl(url)?.let {
                sendRequestWithParams.invoke(it)
                paramInputBlock.isVisible = false
            }
        }
//        setCancelable(false)
    }

    private fun getRequiredParamInputView(url: String): List<ParamInputView> {
        val views = mutableListOf<ParamInputView>()
        views.addAll(
            ApiUrlUtils
                .getPathParamsInputViews(
                    url, context
                )
        )
        views.addAll(
            ApiUrlUtils
                .getQueryParamQueryParamInputViews(
                    url, context
                )
        )
        return views
    }

    private fun getFormattedUrl(url: String): String? {
        if (vhParamInput.children.map {
                (it as ParamInputView).getInput()
            }.count { !it.isOptional && it.paramValue == null } > 0) {
            context.showToast("Please add all required params.")
            return null
        }

        var pathString = ApiUrlUtils.getPathString(url)!!

        vhParamInput.children.map {
            (it as ParamInputView).getInput()
        }.filter { it.isPathParam }.forEach { paramInput ->
            pathString = pathString.replace(
                ApiUrlUtils
                    .getPathParamPlaceholders(url)
                    .first { it.contains(paramInput.paramName) },
                paramInput.paramValue?.toString()
                    ?.let { "${if (paramInput.isOptional) "/" else ""}$it" } ?: ""
            )
        }

        vhParamInput.children.map {
            (it as ParamInputView).getInput()
        }.filter { !it.isPathParam && it.paramValue != null }.toList().let {
            if (it.isNotEmpty()) pathString = "$pathString?"
            for (i in it.indices) {
                pathString = "$pathString${it[i].paramName}=${
                    URLEncoder.encode(it[i].paramValue.toString())
                }"
                if (i != it.size - 1) pathString = "$pathString&"
            }
        }
        return pathString
    }
}