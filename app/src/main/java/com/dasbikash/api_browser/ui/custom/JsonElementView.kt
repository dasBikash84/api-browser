package com.dasbikash.api_browser.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import com.dasbikash.api_browser.R
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

@SuppressLint("SetTextI18n")
class JsonElementView(
    context: Context?,
    attrs: AttributeSet? = null
) :LinearLayout(context, attrs) {

    private val topContainer:LinearLayoutCompat

    private lateinit var jsonElement:JsonElement

    init {
        LayoutInflater
            .from(context)
            .inflate(R.layout.layout_json_element_view, this, true)

        topContainer = rootView?.findViewById(R.id.topContainer)!!
    }

    fun initView(element: JsonElement){
        this.jsonElement = element
        populateView()
    }

    private fun populateView() {
        when(jsonElement){
            is JsonObject -> {
                JsonObjectView.getInstance(context,null, jsonElement as JsonObject,false)
            }
            is JsonArray -> {
                JsonArrayView.getInstance(context,null,jsonElement as JsonArray)
            }
            is JsonPrimitive -> {
                JsonPrimitiveView.getInstance(context,"",jsonElement as JsonPrimitive)
            }
            else ->{null}
        }?.let { clearAndAddView(it) }
    }

    private fun clearAndAddView(view:View){
        topContainer.removeAllViews()
        topContainer.addView(view)
    }

}