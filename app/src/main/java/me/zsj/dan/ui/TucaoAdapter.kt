package me.zsj.dan.ui

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.zsj.dan.R
import me.zsj.dan.binder.comment.CommentHolder
import me.zsj.dan.model.TucaoData
import me.zsj.dan.utils.DateUtils

/**
 * @author zsj
 */
class TucaoAdapter(private var context: Activity, private var comments: ArrayList<TucaoData.Tucao>) :
        RecyclerView.Adapter<TucaoAdapter.TucaoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TucaoHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
        return TucaoHolder(view)
    }

    override fun onBindViewHolder(holder: TucaoHolder?, position: Int) {
        val tucao = comments[position]
        holder?.commentName?.text = tucao.comment_author
        holder?.voteNegative?.text = context.getString(R.string.vote_negative_text, tucao.vote_negative)
        holder?.votePositive?.text = context.getString(R.string.vote_positive_text, tucao.vote_positive)
        holder?.commentTime?.text = DateUtils.getRelativeTimeSpanString(tucao.comment_date)
        holder?.commentContent?.text = Html.fromHtml(tucao.comment_content)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    inner class TucaoHolder(itemView: View) : CommentHolder(itemView)
}