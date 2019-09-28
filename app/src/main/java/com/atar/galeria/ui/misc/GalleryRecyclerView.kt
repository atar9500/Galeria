package com.atar.galeria.ui.misc

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * A RecyclerView with an easier empty view implementation
 */
class GalleryRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    /**
     * Data
     */
    private var mEmptyView: View? = null
    private val mEmptyObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            if (adapter?.itemCount == 0) {
                mEmptyView?.visibility = View.VISIBLE
                this@GalleryRecyclerView.visibility = View.GONE
            } else {
                mEmptyView?.visibility = View.GONE
                this@GalleryRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(mEmptyObserver)
        mEmptyObserver.onChanged()
    }

    fun setEmptyView(emptyView: View) {
        mEmptyView = emptyView
    }

}