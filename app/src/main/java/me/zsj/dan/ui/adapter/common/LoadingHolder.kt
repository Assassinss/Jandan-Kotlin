package me.zsj.dan.ui.adapter.common

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import kotterknife.bindView
import me.zsj.dan.R

/**
 * @author zsj
 */
class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val loadingContainer: FrameLayout by bindView(R.id.loading_container)
    val progressBar: ProgressBar by bindView(R.id.progressBar)
    val errorText: TextView by bindView(R.id.error_text)

    fun showLoading(holder: LoadingHolder, itemCount: Int, error: Boolean) {
        if (itemCount == 1) {
            holder.loadingContainer.visibility = View.GONE
        } else {
            holder.loadingContainer.visibility = View.VISIBLE
            if (error) {
                holder.errorText.visibility = View.VISIBLE
                holder.progressBar.visibility = View.GONE
            } else {
                holder.errorText.visibility = View.GONE
                holder.progressBar.visibility = View.VISIBLE
            }
        }
    }
}