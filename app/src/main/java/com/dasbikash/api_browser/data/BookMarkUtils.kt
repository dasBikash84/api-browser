package com.dasbikash.api_browser.data

import android.content.Context
import android.webkit.URLUtil
import com.dasbikash.api_browser.data.models.UrlBookMark
import com.dasbikash.api_browser.data.models.UrlBookMarks
import com.dasbikash.api_browser.ui.utils.showToast
import com.dasbikash.shared_preference_ext.SharedPreferenceUtils
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import java.io.InputStreamReader

object BookMarkUtils {
    private val gson by lazy { Gson() }

    private const val initAsset = "init_book_marks.json"
    private val bookMarksKey = "com.dasbikash.api_browser.data.BookMarkUtils.bookMarksKey"
    private val preferenceUtils by lazy { SharedPreferenceUtils.getDefaultInstance() }

    private val currentBookmarks = mutableListOf<UrlBookMark>()

    fun checkIfUrlBookMarked(url: String?):Boolean =
        currentBookmarks.count { it.url.equals(url?.trim(),false) } !=0

    suspend fun initBookMarks(context: Context) {
        runBlocking {
            try{
                if (!preferenceUtils.checkIfExists(context, bookMarksKey)) {
                    val fileReader = InputStreamReader(context.assets.open(initAsset))
                    gson.let {
                        it.fromJson<UrlBookMarks>(
                            fileReader, UrlBookMarks::class.java
                        )
                    }?.let {
                        saveAllBookMarks(context, it)
                    }
                }
            }catch (ex:Throwable){ex.printStackTrace()}
        }
    }

    suspend fun getAllBookMarks(context: Context): List<UrlBookMark> =
        (preferenceUtils.getDataSuspended(
            context,
            bookMarksKey,
            String::class.java
        )?.let {
            gson.fromJson(it, UrlBookMarks::class.java).bookMarkList.sortedByDescending { it.time }
        } ?: emptyList()).apply {
            currentBookmarks.clear()
            currentBookmarks.addAll(this)
        }

    suspend fun addBookMark(context: Context, url: String, displayName: String): Boolean {
        val allBookMarks = mutableListOf<UrlBookMark>()
        when {
            url.isBlank().or(displayName.isBlank()) ->  "Blank url/displayName!!"
            !URLUtil.isNetworkUrl(url) -> "Invalid Url"
            getAllBookMarks(context)
                .apply { allBookMarks.addAll(this) }
                .count {
                    it.url.trim()
                        .equals(url, true)
                } > 0 ->  "url already bookmarked!!"
            else -> null
        }?.let {
            context.showToast(it)
            return false
        }
        if (allBookMarks.isEmpty()) {
            getAllBookMarks(context)
                .apply { allBookMarks.addAll(this) }
        }
        allBookMarks.add(
            UrlBookMark(url.trim(), displayName.trim())
        )
        saveAllBookMarks(context, allBookMarks)
        context.showToast("Bookmark saved.")
        return true
    }

    private suspend fun saveAllBookMarks(
        context: Context,
        allBookMarks: List<UrlBookMark>
    ) {
        saveAllBookMarks(context, UrlBookMarks(allBookMarks))
    }

    private suspend fun saveAllBookMarks(
        context: Context,
        urlBookMarks: UrlBookMarks
    ) {
        currentBookmarks.clear()
        currentBookmarks.addAll(urlBookMarks.bookMarkList)
        preferenceUtils.saveDataSuspended(context, gson.toJson(urlBookMarks), bookMarksKey)
    }

    suspend fun removeBookMark(context: Context, bookMark: UrlBookMark) {
        removeBookMark(context,bookMark.url)
    }

    suspend fun removeBookMark(context: Context, url:String?) {
        getAllBookMarks(context).filter { it.url != url?.trim() }.let {
            saveAllBookMarks(context, it)
            context.showToast("Bookmark removed!!")
        }
    }
}