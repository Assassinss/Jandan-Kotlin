package me.zsj.dan.utils.recyclerview

import android.support.v7.widget.RecyclerView

/**
 * @author zsj
 */
interface RecyclerViewExtensions {

    fun RecyclerView.onLoadMore(action: () -> Unit) {
        this.addOnScrollListener(OnLoadMoreListener(object : OnLoadMoreListener.Callback {
            override fun onLoadMore() {
                action.invoke()
            }
        }))
    }

}