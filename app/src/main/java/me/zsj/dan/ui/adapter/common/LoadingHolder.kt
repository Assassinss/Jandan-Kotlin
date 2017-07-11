package me.zsj.dan.ui.adapter.common

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import butterknife.bindView
import me.zsj.dan.R

/**
 * @author zsj
 */
class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val progressBar: ProgressBar by bindView(R.id.progressBar)
}