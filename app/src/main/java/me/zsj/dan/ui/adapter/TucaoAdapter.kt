package me.zsj.dan.ui.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import kotterknife.bindView
import me.zsj.dan.R
import me.zsj.dan.binder.CommentAdapter
import me.zsj.dan.binder.CommentHolder
import me.zsj.dan.model.TucaoData
import me.zsj.dan.ui.adapter.common.Helper
import me.zsj.dan.utils.DateUtils
import org.jsoup.Jsoup

/**
 * @author zsj
 */
class TucaoAdapter(private var context: Activity, private var comments: ArrayList<TucaoData.Tucao>) :
        RecyclerView.Adapter<TucaoAdapter.TucaoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TucaoHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
        return TucaoHolder(view)
    }

    override fun onBindViewHolder(holder: TucaoHolder, position: Int) {
        val tucao = comments[position]
        holder.commentName.text = tucao.comment_author
        holder.voteNegative.text = context.getString(R.string.vote_negative_text, tucao.vote_negative)
        holder.votePositive.text = context.getString(R.string.vote_positive_text, tucao.vote_positive)
        holder.commentTime.text = DateUtils.getRelativeTimeSpanString(tucao.comment_date)
        val doc = Jsoup.parse(tucao.comment_content)
        doc.select(CommentAdapter.A_TAG).remove()
        holder.commentContent.text = doc.text().replace(CommentAdapter.FILTER_REGEX, "")
        if (Helper.hasUserId(tucao.comment_content)) {
            val newTucao = Helper.getTucao(tucao.comment_content, comments)
            holder.atLayout.visibility = View.VISIBLE
            holder.atName.text = newTucao?.comment_author
            val newDoc = Jsoup.parse(newTucao?.comment_content)
            newDoc.select(CommentAdapter.A_TAG).remove()
            holder.atComment.text = newDoc.text().replace(CommentAdapter.FILTER_REGEX, "")
        } else {
            holder.atLayout.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    inner class TucaoHolder(itemView: View) : CommentHolder(itemView) {
        val atLayout: LinearLayout by bindView(R.id.at_layout)
        val atName: TextView by bindView(R.id.at_name)
        val atComment: TextView by bindView(R.id.at_comment)
    }
}