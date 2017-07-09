package me.zsj.dan.binder.comment

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import me.drakeet.multitype.ItemViewBinder
import me.zsj.dan.R
import me.zsj.dan.model.Comment
import me.zsj.dan.model.PostComment

/**
 * @author zsj
 */
class CommentBinder : ItemViewBinder<PostComment, CommentBinder.CommentHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): CommentHolder {
        val view = inflater.inflate(R.layout.layout_comment_list, parent, false)
        return CommentHolder(view)
    }

    override fun onBindViewHolder(holder: CommentHolder, item: PostComment) {
        if (item.category == PostComment.COMMENTS) {
            holder.setComments(item.post.comments)
        } else if (item.category == PostComment.COMMENTS_RANK) {
            holder.setComments(item.post.commentsRank)
        }
    }

    inner class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView by bindView(R.id.comment_list)

        private val adapter: CommentAdapter = CommentAdapter()
        init {
            recyclerView.adapter = adapter
        }

        fun setComments(comments: ArrayList<Comment>) {
            adapter.setData(comments)
            adapter.notifyDataSetChanged()
        }
    }
}