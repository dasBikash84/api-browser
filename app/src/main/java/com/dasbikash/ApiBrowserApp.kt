package com.dasbikash

import android.app.Application
import com.dasbikash.api_browser.BuildConfig
import com.dasbikash.api_browser.data.BookMarkUtils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ApiBrowserApp : Application(){

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        GlobalScope.launch {
            BookMarkUtils.initBookMarks(this@ApiBrowserApp)
        }

        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

    companion object{
        private lateinit var mInstance:ApiBrowserApp
        fun getAppContext() = mInstance.applicationContext
    }
}