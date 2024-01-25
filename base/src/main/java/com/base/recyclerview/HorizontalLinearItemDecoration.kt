package com.base.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by vophamtuananh on 1/14/18.
 */
class HorizontalLinearItemDecoration(
    private val mLeftSpace: Int = 0,
    private val mTopSpace: Int = 0,
    private val mRightSpace: Int = 0,
    private val mBottomSpace: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val lp = view.layoutParams as RecyclerView.LayoutParams
        val itemPosition = lp.viewAdapterPosition

//        outRect.bottom = mBottomSpace
//        outRect.left = mLeftSpace
//        outRect.top = mTopSpace
//        outRect.right = mRightSpace

        if (itemPosition != parent.adapter!!.itemCount - 1) {
            lp.setMargins(mLeftSpace, mTopSpace, mRightSpace, mBottomSpace)
        } else {
            lp.setMargins(mLeftSpace, mTopSpace, 0, mBottomSpace)
        }
        view.layoutParams = lp
    }
}