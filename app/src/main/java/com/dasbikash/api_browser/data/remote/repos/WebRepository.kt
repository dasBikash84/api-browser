package com.dasbikash.api_browser.data.remote.repos

import com.dasbikash.api_browser.data.models.response.base.RemoteResponse
import com.dasbikash.api_browser.data.remote.services.WebApiService
import com.google.gson.JsonElement

class WebRepository: BaseRepository<WebApiService>(WebApiService::class.java) {

    suspend fun getDataByUrl(url:String): RemoteResponse<JsonElement> =
        fetchRemoteData { apiClient.getDataByUrl(url) }
}
