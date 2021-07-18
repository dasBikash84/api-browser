package com.dasbikash.api_browser.data.remote.interceptors

import com.dasbikash.api_browser.ui.utils.PrefUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthenticationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val initialRequest = chain.request().updateCredentials()
        return chain.proceed(initialRequest)
    }

    private fun Request.updateCredentials(): Request {
        return newBuilder().apply {
            PrefUtils.getAuthToken()?.let {
                header("Authorization", "Bearer $it")
            }
        }.build()
    }
}