package com.dasbikash.api_browser.ui.custom

import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import com.dasbikash.api_browser.R
import com.dasbikash.api_browser.ui.utils.PrefUtils
import com.dasbikash.api_browser.ui.utils.showToast
import com.google.android.material.bottomsheet.BottomSheetDialog

class TokenAddChangeView(
    context: Context
) : BottomSheetDialog(context) {
    init {
        setContentView(R.layout.layout_add_change_token)
        PrefUtils.getAuthToken()?.let { token ->
            findViewById<TextView>(R.id.tvOldTokenPrompt)?.isVisible = true
            findViewById<TextView>(R.id.tvUrl)?.apply {
                text = token
                isVisible = true
            }
        }
        val etNewToken = findViewById<EditText>(R.id.etNewToken)!!
        val btnSaveToken = findViewById<Button>(R.id.btnSaveToken)!!

        btnSaveToken.setOnClickListener {
            if (etNewToken.text.isNullOrBlank()){
                context.showToast("Empty token!!")
            }else{
                PrefUtils.saveAuthToken(etNewToken.text!!.trim().toString())
                context.showToast("Token saved!!")
                dismiss()
            }
        }
    }
}