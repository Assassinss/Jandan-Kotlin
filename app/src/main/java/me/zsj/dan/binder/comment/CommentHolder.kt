package me.zsj.dan.binder.comment

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.bindView
import me.zsj.dan.R

/**
 * @author zsj
 */
open class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val commentName: TextView by bindView(R.id.comment_name)
    val voteNegative: TextView by bindView(R.id.vote_negative)
    val votePositive: TextView by bindView(R.id.vote_positive)
    val commentTime: TextView by bindView(R.id.comment_time)
    val commentContent: TextView by bindView(R.id.comment_content)
}