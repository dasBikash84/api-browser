package com.dasbikash.api_browser.data.remote

import com.dasbikash.api_browser.data.remote.interceptors.AuthenticationInterceptor
import com.dasbikash.api_browser.ui.utils.PrefUtils
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthenticationInterceptor())
            .addInterceptor(getLoggingInterceptor())
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .baseUrl("${PrefUtils.getBasePath()}/")
            .client(okHttpClient)
            .build()
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
}