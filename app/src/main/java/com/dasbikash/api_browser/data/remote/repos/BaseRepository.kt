package com.dasbikash.api_browser.data.remote.repos

import com.dasbikash.api_browser.data.models.response.base.ErrorResponse
import com.dasbikash.api_browser.data.models.response.base.RemoteResponse
import com.dasbikash.api_browser.data.remote.ApiClient
import com.google.gson.Gson
import retrofit2.Response

abstract class BaseRepository<T>(type:Class<T>) {
    private val gson by lazy { Gson() }
    protected val apiClient: T by lazy { ApiClient.retrofit.create(type) }

    protected suspend fun <T> fetchRemoteData(
        apiCall:suspend ()-> Response<T>
    ): RemoteResponse<T> {
        return try {
            getRemoteResponse(apiCall.invoke())
        } catch (ex: Throwable) {
            ex.printStackTrace()
            RemoteResponse(null,"Unknown error!!",null, UNKNOWN_ERROR_CODE)
        }
    }

    private fun <T> getRemoteResponse(response: Response<T>): RemoteResponse<T> {
        return if (response.isSuccessful){
            RemoteResponse(
                response.body(),
                null,null,
                response.code(),response.isSuccessful
            )
        }else{
            try {
                gson.fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
            }catch (ex:Throwable){
                ex.printStackTrace()
                null
            }.let {
                RemoteResponse(
                    null,
                    it?.message,it?.documentationUrl,
                    response.code(),response.isSuccessful
                )
            }
        }
    }

    companion object{
        const val UNKNOWN_ERROR_CODE = 499
    }
}
