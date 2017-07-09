package me.zsj.dan.utils.recyclerview

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * @author zsj
 */
class OnLoadMoreListener(val callback: Callback, val visibleItemCount: Int) : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val layoutManager = recyclerView!!.layoutManager as LinearLayoutManager
        if (layoutManager.findFirstCompletelyVisibleItemPosition() >= layoutManager.itemCount - visibleItemCount
                && layoutManager.findFirstVisibleItemPosition() != 0) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE ) {
                callback.onLoadMore()
            }
        }
    }

    interface Callback {
        fun onLoadMore()
    }
}