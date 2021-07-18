package com.dasbikash.api_browser.ui

class LiveDataResource<T> private constructor(val status: Status, val data: T?) {
    enum class Status { SUCCESS, ERROR, LOADING}

    companion object {
        fun <T> success(data:T?): LiveDataResource<T> = LiveDataResource(Status.SUCCESS,data)
        fun <T> error(data:T?): LiveDataResource<T> = LiveDataResource(Status.ERROR,data)
        fun <T> loading(): LiveDataResource<T> = LiveDataResource(Status.LOADING,null)
    }
}