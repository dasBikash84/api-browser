package com.dasbikash.api_browser.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.dasbikash.api_browser.R
import com.dasbikash.api_browser.data.models.IndexedJsonObject
import com.dasbikash.api_browser.ui.custom.helpers.JsonObjectListRvAdapter
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.layout_json_object_list_view.view.*

@SuppressLint("SetTextI18n")
class JsonArrayView(
    context: Context?,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val adapter: JsonObjectListRvAdapter = JsonObjectListRvAdapter()

    init {
        LayoutInflater
            .from(context)
            .inflate(
                R.layout.layout_json_object_list_view, this, true
            )
        rvJsonObject.adapter = adapter
    }

    private fun initView(
        keyName: String?,
        jsonObjects: List<JsonObject>
    ) {
        val indexedJsonObjects = mutableListOf<IndexedJsonObject>()
        for (i in jsonObjects.indices) {
            indexedJsonObjects.add(
                IndexedJsonObject(keyName?.let { "$it[$i]" }, jsonObjects[i])
            )
        }
        adapter.submitList(indexedJsonObjects)
    }

    companion object {
        fun getInstance(
            context: Context?,
            keyName: String?,
            jsonArray: JsonArray
        ) = JsonArrayView(context).apply {
            val jsonObjects = mutableListOf<JsonObject>()
            for (i in 0 until jsonArray.size()){
                if (jsonArray.get(i) !is JsonObject) continue
                jsonObjects.add(jsonArray.get(i) as JsonObject)
            }
            initView(keyName, jsonObjects)
        }
    }

}


