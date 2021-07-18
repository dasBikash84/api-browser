package com.dasbikash.api_browser.data.remote.services

import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface WebApiService{
    @GET
    suspend fun getDataByUrl(@Url url:String): Response<JsonElement>
}
