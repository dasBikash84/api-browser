package com.dasbikash.api_browser.ui.custom

import android.content.Context
import androidx.core.view.isVisible
import com.dasbikash.api_browser.R
import com.dasbikash.api_browser.data.models.response.base.RemoteResponse
import com.dasbikash.api_browser.ui.utils.PrefUtils
import com.dasbikash.api_browser.ui.utils.navigateForwardWithUrl
import com.dasbikash.api_browser.ui.utils.setLinkStyle
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.layout_api_error_handler.*

class ApiErrorHandlerView(
    context: Context,
    errorResponse: RemoteResponse<*>?
) : BottomSheetDialog(context) {
    init {
        setContentView(R.layout.layout_api_error_handler)
        handleResponseError(errorResponse)
    }

    private fun handleResponseError(errorResponse: RemoteResponse<*>?) {
        tvStatusCode.text = context.getString(R.string.status_code_message, errorResponse?.statusCode ?: 404)
        tvErrorMessage.text = context.getString(
            R.string.api_error_message, errorResponse?.message ?: context.getString(
                R.string.generic_error_message
            )
        )
        errorResponse?.documentationUrl?.let { docsUrl ->
            tvDocUrl.text = context.getString(R.string.error_doc, docsUrl)
            tvDocUrl.setLinkStyle()
            tvDocUrl.setOnClickListener {
                tvDocUrl.navigateForwardWithUrl(docsUrl)
            }
        }
        tvDocUrl.isVisible = errorResponse?.documentationUrl != null
        tvAddToken.setLinkStyle()
        tvAddToken.setOnClickListener {
            dismiss()
            TokenAddChangeView(context).show()
        }

        context.getString(R.string.github_personal_token).let {
            if (PrefUtils.getBasePath().contains("github",true)){
                tvGetToken.setLinkStyle()
                tvGetToken.setOnClickListener {
                    tvGetToken.navigateForwardWithUrl(context.getString(R.string.github_personal_token))
                }
            }else{
                tvGetToken.isVisible = false
            }
        }

        vhApiError.isVisible = true
        vhApiError.setOnClickListener { }
    }
}