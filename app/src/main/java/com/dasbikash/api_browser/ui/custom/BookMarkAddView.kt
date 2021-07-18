package com.dasbikash.api_browser.ui.custom

import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.dasbikash.api_browser.R
import com.dasbikash.api_browser.ui.utils.showToast
import com.google.android.material.bottomsheet.BottomSheetDialog

class BookMarkAddView(
    context: Context,
    url:String,
    addBookmark:(String,String)->Unit
) : BottomSheetDialog(context) {
    init {
        setContentView(R.layout.layout_add_bookmark)
        findViewById<TextView>(R.id.tvUrl)?.text = url
        val etDisplayName = findViewById<EditText>(R.id.etDisplayName)!!
        val btnSaveBookmark = findViewById<Button>(R.id.btnSaveBookmark)!!

        btnSaveBookmark.setOnClickListener {
            if (etDisplayName.text.isNullOrBlank()){
                context.showToast("Display name required!!")
            }else{
                addBookmark(etDisplayName.text.trim().toString(),url)
                dismiss()
            }
        }
    }
}