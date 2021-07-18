package com.dasbikash.api_browser.ui.custom.helpers

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dasbikash.api_browser.data.models.UrlBookMark

class UrlBookMarkSwipeCallBack(val swipeAction: (UrlBookMark)->Unit, swipeDirs:Int=0) :
    ItemTouchHelper.SimpleCallback(0, swipeDirs) {

    override fun onMove(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        swipeAction((viewHolder as UrlBookmarksRvAdapter.UrlBookMarkViewHolder).getEntry())
    }
}