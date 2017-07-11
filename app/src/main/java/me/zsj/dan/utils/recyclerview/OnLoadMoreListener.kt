package me.zsj.dan.utils.recyclerview

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * @author zsj
 */
class OnLoadMoreListener(val callback: Callback) : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val layoutManager = recyclerView!!.layoutManager as LinearLayoutManager
        if (layoutManager.findLastVisibleItemPosition() >= layoutManager.itemCount - 1
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