package me.zsj.dan.binder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.drakeet.multitype.ItemViewBinder
import me.zsj.dan.R

/**
 * @author zsj
 */
class LoadingViewBinder : ItemViewBinder<String, LoadingViewBinder.LoadingHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): LoadingHolder {
        val view = inflater.inflate(R.layout.item_load_more, parent, false)
        return LoadingHolder(view)
    }

    override fun onBindViewHolder(holder: LoadingHolder, item: String) {}

    inner class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}