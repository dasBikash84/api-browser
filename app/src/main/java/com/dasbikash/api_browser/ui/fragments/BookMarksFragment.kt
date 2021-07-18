package com.dasbikash.api_browser.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import com.dasbikash.api_browser.R
import com.dasbikash.api_browser.data.BookMarkUtils
import com.dasbikash.api_browser.ui.custom.helpers.UrlBookMarkSwipeCallBack
import com.dasbikash.api_browser.ui.custom.helpers.UrlBookmarksRvAdapter
import kotlinx.android.synthetic.main.fragment_bookmarks.*

class BookMarksFragment : BaseFragment() {

    private val adapter by lazy { UrlBookmarksRvAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookmarks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvBookMarks.adapter = adapter
        ItemTouchHelper(
            UrlBookMarkSwipeCallBack(
                {
                    lifecycleScope.launchWhenResumed {
                        BookMarkUtils.removeBookMark(requireContext(),it)
                        refreshBookMarksView()
                    }
                },
                ItemTouchHelper.RIGHT
            )
        ).attachToRecyclerView(rvBookMarks)
        lifecycleScope.launchWhenResumed {
            refreshBookMarksView()
        }
    }

    private suspend fun refreshBookMarksView() {
        BookMarkUtils.getAllBookMarks(requireContext()).let {
            log(it.toString())
            adapter.submitList(it)
        }
    }
}