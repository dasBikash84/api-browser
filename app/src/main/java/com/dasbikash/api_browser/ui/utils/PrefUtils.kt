package com.dasbikash.api_browser.ui.utils

import com.dasbikash.ApiBrowserApp
import com.dasbikash.api_browser.BuildConfig
import com.dasbikash.shared_preference_ext.SharedPreferenceUtils

object PrefUtils {

    private const val gitHubApiBasePath = "https://api.github.com"

    private val preferenceUtils by lazy { SharedPreferenceUtils.getDefaultInstance() }
    private val authTokenKey = "com.dasbikash.api_browser.ui.utils.PrefUtils.authTokenKey"
    private val basePathKey = "com.dasbikash.api_browser.ui.utils.PrefUtils.basePathKey"

    fun getAuthToken(): String =
        preferenceUtils.getData(ApiBrowserApp.getAppContext(), authTokenKey, String::class.java) ?:
            BuildConfig.authToken

    fun saveAuthToken(token: String) {
        if (token.isNotBlank()) {
            preferenceUtils.saveData(ApiBrowserApp.getAppContext(), token, authTokenKey)
        }
    }

    fun getBasePath(): String =
        preferenceUtils.getData(ApiBrowserApp.getAppContext(), basePathKey, String::class.java)
            ?: gitHubApiBasePath

    fun saveBasePath(path: String) {
        if (path.isNotBlank()) {
            preferenceUtils.saveData(ApiBrowserApp.getAppContext(), path, basePathKey)
        }
    }
}