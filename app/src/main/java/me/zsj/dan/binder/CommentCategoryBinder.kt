package me.zsj.dan.binder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import me.drakeet.multitype.ItemViewBinder
import me.zsj.dan.R

/**
 * @author zsj
 */
class CommentCategoryBinder : ItemViewBinder<String, CommentCategoryBinder.CategoryHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): CategoryHolder {
        val view = inflater.inflate(R.layout.item_comment_category, parent, false)
        return CategoryHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryHolder, item: String) {
        holder.commentCategory.text = item
    }

    inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentCategory: TextView by bindView(R.id.comment_category)
    }
}