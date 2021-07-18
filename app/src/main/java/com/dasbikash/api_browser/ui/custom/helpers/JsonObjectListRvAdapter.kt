package com.dasbikash.api_browser.ui.custom.helpers

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dasbikash.api_browser.data.models.IndexedJsonObject
import com.dasbikash.api_browser.ui.custom.JsonObjectView

class JsonObjectListRvAdapter() :
    ListAdapter<IndexedJsonObject, JsonObjectListRvAdapter.JsonObjectViewHolder>
        (getDiffUtils()) {

    class JsonObjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(indexedJsonObject: IndexedJsonObject) {
            with(itemView as JsonObjectView) {
                initView(indexedJsonObject.key,indexedJsonObject.jasonObject,true)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JsonObjectViewHolder {
        return JsonObjectViewHolder(JsonObjectView(parent.context))
    }

    override fun onBindViewHolder(holder: JsonObjectViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        fun getDiffUtils() = object : DiffUtil.ItemCallback<IndexedJsonObject>() {
            override fun areItemsTheSame(
                oldItem: IndexedJsonObject,
                newItem: IndexedJsonObject
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: IndexedJsonObject,
                newItem: IndexedJsonObject
            ): Boolean = oldItem == newItem
        }
    }
}