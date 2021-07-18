package com.dasbikash.api_browser.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.webkit.URLUtil
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.dasbikash.api_browser.R
import com.dasbikash.api_browser.ui.utils.isInternalUrl
import com.dasbikash.api_browser.ui.utils.loadNetWorkImage
import com.dasbikash.api_browser.ui.utils.navigateForwardWithUrl
import com.dasbikash.api_browser.ui.utils.setLinkStyle
import com.google.gson.JsonPrimitive
import kotlinx.android.synthetic.main.layout_json_primitive_view.view.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SetTextI18n")
class JsonPrimitiveView(
    context: Context?,
    attrs: AttributeSet? = null
) :LinearLayout(context, attrs) {
    companion object{
        private val remoteDateStringRegex = Regex("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z")
        private const val remoteDateStringFormat = "yyyy-mm-dd'T'HH:mm:ss'Z'"
        private const val displayDateStringFormat = "EEE, d MMM yyyy HH:mm:ss Z"
        private val simpleDateFormat1 = SimpleDateFormat(remoteDateStringFormat, Locale.getDefault())
        private val simpleDateFormat2 = SimpleDateFormat(displayDateStringFormat, Locale.getDefault())

        private fun getFormatEdDateString(dataString: String):String =
            try{
                if (dataString.matches(remoteDateStringRegex)) {
                    simpleDateFormat1.parse(dataString)?.let { simpleDateFormat2.format(it) }
                } else dataString
            }catch (ex:Throwable) {null} ?: dataString

        fun getInstance(context: Context?,
                        keyName:String,
                        jsonPrimitive: JsonPrimitive
        ) = JsonPrimitiveView(context).apply {
            initView(keyName,jsonPrimitive)
        }
    }
    private lateinit var dataString:String
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_json_primitive_view, this, true)
    }

    fun initView(
        keyName:String,
        jsonPrimitive: JsonPrimitive
    ){
        dataString = jsonPrimitive.asString.trim()
        if (dataString.isNotBlank()) {

            tvkey.text = "$keyName :"

            tvText.text = getFormatEdDateString(dataString)
            ivImage.isVisible = false

            if (jsonPrimitive.isString &&
                URLUtil.isNetworkUrl(dataString)
            ) {
                tvText.setLinkStyle()
                tvText.setOnClickListener {
                    navigateForwardWithUrl(dataString)
                }

//                if (!isInternalUrl(dataString)) {
//                    ivImage.loadNetWorkImage(
//                        dataString, onSuccess = {
//                            tvText.isVisible = false
//                            ivImage.isVisible = true
//                        }
//                    )
//                }
            }
        }else rootView.isVisible = false
    }
}