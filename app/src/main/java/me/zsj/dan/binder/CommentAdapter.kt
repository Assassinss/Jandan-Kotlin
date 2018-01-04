package me.zsj.dan.binder

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotterknife.bindView
import me.zsj.dan.R
import me.zsj.dan.model.Comment
import me.zsj.dan.utils.DateUtils
import me.zsj.dan.utils.StringUtils

/**
 * @author zsj
 */
class CommentAdapter : RecyclerView.Adapter<CommentAdapter.Holder>() {

    private var comments: ArrayList<Comment>? = null

    fun setData(comments: ArrayList<Comment>) {
        this.comments = comments
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_comment, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        val context = holder?.commentContent?.context
        val item = comments!![position]
        holder?.voteNegative?.text = context?.getString(R.string.vote_negative_text, item.voteNegative)
        holder?.votePositive?.text = context?.getString(R.string.vote_positive_text, item.votePositive)
        holder?.commentName?.text = item.name
        holder?.commentTime?.text = DateUtils.getRelativeTimeSpanString(item.date)
        holder?.commentContent?.text = Html.fromHtml(StringUtils.filterHtml(item.content))
    }

    override fun getItemCount(): Int {
        return comments!!.size
    }

    inner class Holder(itemView: View) : CommentHolder(itemView) {
        val commentContainer: ConstraintLayout by bindView(R.id.item_comment_container)
    }
}