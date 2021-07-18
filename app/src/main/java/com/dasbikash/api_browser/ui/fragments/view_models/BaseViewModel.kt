package com.dasbikash.api_browser.ui.fragments.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dasbikash.api_browser.data.models.response.base.RemoteResponse
import com.dasbikash.api_browser.data.remote.repos.WebRepository
import com.dasbikash.api_browser.ui.LiveDataResource
import com.google.gson.JsonElement

abstract class BaseViewModel():ViewModel(){
    private val webRepository by lazy { WebRepository() }

    fun getDataByUrl(url:String): LiveData<LiveDataResource<RemoteResponse<JsonElement>>> =
        liveData<LiveDataResource<RemoteResponse<JsonElement>>> {
            emit(LiveDataResource.loading())
            webRepository.getDataByUrl(url).let {
                if (it.success) emit(LiveDataResource.success(it))
                else emit(LiveDataResource.error(it))
            }
        }
}