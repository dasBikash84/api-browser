package com.dasbikash.api_browser.ui.custom

import android.content.Context
import android.webkit.URLUtil
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import com.dasbikash.api_browser.R
import com.dasbikash.api_browser.ui.utils.PrefUtils
import com.dasbikash.api_browser.ui.utils.showToast
import com.google.android.material.bottomsheet.BottomSheetDialog

class BasePathChangeView(
    context: Context
) : BottomSheetDialog(context) {
    init {
        setContentView(R.layout.layout_change_base_path)
        PrefUtils.getBasePath().let { path ->
            findViewById<TextView>(R.id.tvOldPathPrompt)?.isVisible = true
            findViewById<TextView>(R.id.tvOldPath)?.apply {
                text = path
                isVisible = true
            }
        }
        val etNewPath = findViewById<EditText>(R.id.etNewPath)!!
        val btnSavePath = findViewById<Button>(R.id.btnSavePath)!!

        btnSavePath.setOnClickListener {
            if (etNewPath.text.isNullOrBlank() ||
                    !URLUtil.isNetworkUrl(etNewPath.text?.toString())){
                context.showToast("Invalid path!!")
            }else{
                PrefUtils.saveBasePath(etNewPath.text!!.trim().toString())
                context.showToast("Path saved!!")
                dismiss()
            }
        }
    }
}