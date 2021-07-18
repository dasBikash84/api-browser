package com.dasbikash.api_browser.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.dasbikash.api_browser.R
import com.dasbikash.api_browser.data.BookMarkUtils

class BookMarkManagerView(
    context: Context?,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    val ivNotBookMarked:ImageView
    val ivBookMarked:ImageView
    private var url:String? = null
    init {
        LayoutInflater
            .from(context)
            .inflate(
                R.layout.layout_book_mark_manager, this, true
            )
        ivBookMarked = rootView?.findViewById<ImageView>(R.id.ivBookMarked)!!
        ivNotBookMarked = rootView?.findViewById<ImageView>(R.id.ivNotBookMarked)!!

        ivBookMarked.setOnClickListener {
            context?.let {
                findViewTreeLifecycleOwner()?.lifecycleScope?.launchWhenResumed {
                    BookMarkUtils.removeBookMark(context, url)
                    refreshView()
                }
            }
        }

        ivNotBookMarked.setOnClickListener {
            url?.let { url ->
                context?.let {
                    BookMarkAddView(context,url,::addBookMark).show()
                }
            }
        }
    }

    fun initView(url:String){
        if (URLUtil.isValidUrl(url)) {
            this.url = url
            refreshView()
        }
    }

    private fun refreshView(){
        url?.let {
            BookMarkUtils.checkIfUrlBookMarked(url).let {
                ivBookMarked.isVisible = it
                ivNotBookMarked.isVisible = !it
            }
        }
    }

    private fun addBookMark(name:String,url:String){
        context?.let {
            findViewTreeLifecycleOwner()?.lifecycleScope?.launchWhenResumed {
                BookMarkUtils.addBookMark(context, url, name)
                refreshView()
            }
        }
    }

}