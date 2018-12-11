package me.zsj.dan.binder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import kotterknife.bindView
import me.zsj.dan.R
import me.zsj.dan.model.Comment
import me.zsj.dan.ui.adapter.common.Helper
import me.zsj.dan.utils.DateUtils
import org.jsoup.Jsoup

/**
 * @author zsj
 */
class CommentAdapter : RecyclerView.Adapter<CommentAdapter.Holder>() {

    companion object {
        const val A_TAG = "a"
        const val P_TAG = "P"
        const val FILTER_REGEX = "@: "
    }

    private var comments: ArrayList<Comment>? = null
    private var allComments: ArrayList<Comment>? = null

    fun setData(comments: ArrayList<Comment>, allComments: ArrayList<Comment>) {
        this.comments = comments
        this.allComments = allComments
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val context = holder.itemView.context
        val item = comments!![position]
        holder.voteNegative.text = context?.getString(R.string.vote_negative_text, item.voteNegative)
        holder.votePositive.text = context?.getString(R.string.vote_positive_text, item.votePositive)
        holder.commentName.text = item.name
        holder.commentTime.text = DateUtils.getRelativeTimeSpanString(item.date)
        val doc = Jsoup.parse(item.content)
        doc.select(A_TAG).remove()
        val content = doc.select(P_TAG).text().replace(FILTER_REGEX, "")
        holder.commentContent.text = content
        if (Helper.hasUserId(item.content)) {
            val newComment = Helper.getComment(item.content, allComments!!)
            if (newComment != null) {
                holder.atLayout.visibility = View.VISIBLE
                holder.atName.text = newComment.name
                val otherDoc = Jsoup.parse(newComment.content)
                otherDoc.select(A_TAG).remove()
                holder.atComment.text = otherDoc.select(P_TAG).text().replace(FILTER_REGEX, "")
            } else {
                holder.atLayout.visibility = View.GONE
            }
        } else {
            holder.atLayout.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return comments!!.size
    }

    inner class Holder(itemView: View) : CommentHolder(itemView) {
        val atLayout: LinearLayout by bindView(R.id.at_layout)
        val atName: TextView by bindView(R.id.at_name)
        val atComment: TextView by bindView(R.id.at_comment)
    }
}