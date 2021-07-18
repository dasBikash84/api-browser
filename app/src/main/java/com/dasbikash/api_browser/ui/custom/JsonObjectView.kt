package com.dasbikash.api_browser.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import com.dasbikash.api_browser.R
import com.dasbikash.api_browser.ui.utils.navigateToBrowserFragment
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import kotlinx.android.synthetic.main.layout_json_object_view.view.*

@SuppressLint("SetTextI18n")
class JsonObjectView(
    context: Context?,
    attrs: AttributeSet? = null
) :LinearLayout(context, attrs) {
    private var insideList:Boolean= false

    companion object{
        private const val MAX_CHILD_VIEW_COUNT = 10
        fun getInstance(context: Context?,
                        keyName:String?,
                        jsonObject: JsonObject,
                        insideList:Boolean
        ) = JsonObjectView(context).apply {
            initView(keyName,jsonObject,insideList)
        }
    }

    private val keyTextView:TextView
    private val btnViewDetails:Button
    private val topContainer:LinearLayoutCompat
    private val vhNotTopLevelControls:ViewGroup

    private var keyName:String? = null
    private lateinit var jsonObject:JsonObject

    init {
        LayoutInflater
            .from(context)
            .inflate(
                R.layout.layout_json_object_view, this, true
            )

        topContainer = rootView?.findViewById(R.id.topContainer)!!
        vhNotTopLevelControls = topContainer.findViewById(R.id.vhNotTopLevelControls)!!
        keyTextView = topContainer.findViewById(R.id.tvkey)!!
        btnViewDetails = topContainer.findViewById(R.id.btnViewDetails)!!

        vhNotTopLevelControls.isVisible = false
        keyTextView.isVisible = false
        btnViewDetails.visibility = View.INVISIBLE


    }

    private fun showViewDetailsButton(jsonObject: JsonObject) {
        btnViewDetails.visibility = View.VISIBLE
        vhNotTopLevelControls.isVisible = true
        btnViewDetails.setOnClickListener {
            topContainer.navigateToBrowserFragment(null,jsonObject.toString())
        }
    }

    fun initView(keyName:String?,
                 jsonObject: JsonObject,
                insideList:Boolean
    ){
        this.keyName = keyName
        this.jsonObject = jsonObject
        this.insideList = insideList
        populateView(insideList)
    }

    private fun isOnTopLevel() = keyName == null

    private fun populateView(insideList:Boolean) {

        if (!keyName.isNullOrBlank()) {
            keyTextView.text = "${keyName!!} :"
            keyTextView.isVisible = true
            vhNotTopLevelControls.isVisible = true
            topContainer.setBackgroundColor(Color.parseColor("#50000000"))
            (resources.displayMetrics.widthPixels * 0.95).toInt()
        }else{
            resources.displayMetrics.widthPixels
        }.let {

        }

        topContainer.layoutParams = topContainer.layoutParams.apply {
            width = (resources.displayMetrics.widthPixels * (if (insideList) 0.90 else 1.0)).toInt()
        }

        jsonObject.keySet().asSequence().forEach { currentKey ->

            if (!isOnTopLevel() &&
                    topContainer.childCount == MAX_CHILD_VIEW_COUNT+1){
                showViewDetailsButton(jsonObject)
                return@forEach
            }

            val element = jsonObject.get(currentKey)
            if (element.isJsonPrimitive){
                JsonPrimitiveView.getInstance(context,currentKey,element as JsonPrimitive)

            }else{
                if (!isOnTopLevel()) {
                    showViewDetailsButton(jsonObject)
                    null
                }else{
                    when(element){
                        is JsonObject -> getInstance(context,currentKey, element,insideList)
                        is JsonArray -> JsonArrayView.getInstance(context,currentKey,element)
                        is JsonPrimitive -> JsonPrimitiveView.getInstance(context,currentKey,element)
                        else -> null
                    }
                }
            }?.let {
                topContainer.addView(it)
            }
            if (!isOnTopLevel() &&
                topContainer.childCount == MAX_CHILD_VIEW_COUNT+1) return@forEach
        }
    }

}