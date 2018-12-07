package me.zsj.dan.binder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotterknife.bindView
import me.zsj.dan.R

/**
 * @author zsj
 */
open class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val author: TextView by bindView(R.id.author)
    val dateTime: TextView by bindView(R.id.date_time)
    val votePositive: TextView by bindView(R.id.vote_positive)
    val voteNegative: TextView by bindView(R.id.vote_negative)
    val commentCount: TextView by bindView(R.id.comment_count)
    val more: ImageView by bindView(R.id.more)
}