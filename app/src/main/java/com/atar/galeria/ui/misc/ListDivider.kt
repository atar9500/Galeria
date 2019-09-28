package com.atar.galeria.ui.misc

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Class that handles equal spacing between items within the RecyclerView
 */
class ListDivider(gridSpacingPx: Int, gridSize: Int = 1, horizontal: Boolean = false) :
    RecyclerView.ItemDecoration() {

    /**
     * Data
     */
    private var mSizeGridSpacingPx: Int = gridSpacingPx
    private var mGridSize: Int = gridSize
    private var mHorizontal: Boolean = horizontal
    private var mNeedLeftSpacing = false

    /**
     * RecyclerView.ItemDecoration Functions
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val frameWidth =
            ((parent.width - mSizeGridSpacingPx.toFloat() * (mGridSize - 1)) / mGridSize).toInt()
        val padding = parent.width / mGridSize - frameWidth
        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        outRect.top = mSizeGridSpacingPx
        if (itemPosition % mGridSize == 0) {
            outRect.left = mSizeGridSpacingPx
            outRect.right = padding
            mNeedLeftSpacing = true
        } else if ((itemPosition + 1) % mGridSize == 0) {
            mNeedLeftSpacing = false
            outRect.right = mSizeGridSpacingPx
            outRect.left = padding
        } else if (mNeedLeftSpacing) {
            mNeedLeftSpacing = false
            outRect.left = mSizeGridSpacingPx - padding
            if ((itemPosition + 2) % mGridSize == 0) {
                outRect.right = mSizeGridSpacingPx - padding
            } else {
                outRect.right = mSizeGridSpacingPx / 2
            }
        } else if ((itemPosition + 2) % mGridSize == 0) {
            mNeedLeftSpacing = false
            outRect.left = mSizeGridSpacingPx / 2
            outRect.right = mSizeGridSpacingPx - padding
        } else {
            mNeedLeftSpacing = false
            outRect.left = mSizeGridSpacingPx / 2
            outRect.right = mSizeGridSpacingPx / 2
        }
        if (itemPosition >= state.itemCount - mGridSize || mHorizontal) {
            outRect.bottom = mSizeGridSpacingPx
        } else {
            outRect.bottom = 0
        }
    }
}