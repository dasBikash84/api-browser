package com.dasbikash.api_browser.ui.custom.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dasbikash.api_browser.R
import com.dasbikash.api_browser.data.models.UrlBookMark
import com.dasbikash.api_browser.ui.utils.navigateForwardWithUrl
import com.dasbikash.api_browser.ui.utils.setLinkStyle
import kotlinx.android.synthetic.main.layout_url_bookmark_view.view.*

class UrlBookmarksRvAdapter() :
    ListAdapter<UrlBookMark, UrlBookmarksRvAdapter.UrlBookMarkViewHolder>
        (getDiffUtils()) {

    class UrlBookMarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var mUrlBookMark:UrlBookMark

        fun bind(urlBookMark: UrlBookMark) {
            mUrlBookMark = urlBookMark
            itemView.tvDisplayName.text = urlBookMark.displayName
            with(itemView.tvUrl) {
                text = urlBookMark.url
                setLinkStyle()
            }
            itemView.setOnClickListener {
                itemView.navigateForwardWithUrl(urlBookMark.url)
            }
        }

        fun getEntry() = mUrlBookMark
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UrlBookMarkViewHolder {
        return UrlBookMarkViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_url_bookmark_view,parent,false)
        )
    }

    override fun onBindViewHolder(holder: UrlBookMarkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        fun getDiffUtils() = object : DiffUtil.ItemCallback<UrlBookMark>() {
            override fun areItemsTheSame(
                oldItem: UrlBookMark,
                newItem: UrlBookMark
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: UrlBookMark,
                newItem: UrlBookMark
            ): Boolean = oldItem == newItem
        }
    }
}