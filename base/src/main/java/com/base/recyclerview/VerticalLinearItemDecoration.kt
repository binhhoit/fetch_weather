package com.base.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by vophamtuananh on 1/14/18.
 */
class VerticalLinearItemDecoration(private val space: Int = 0,
                                   private val includeBottom: Boolean = true,
                                   private val includeTop: Boolean = false) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition

        if(includeBottom || itemPosition < parent.adapter!!.itemCount - 1)
            outRect.bottom = space

        if (includeTop && itemPosition == 0) {
            outRect.top = space
        }
    }
}