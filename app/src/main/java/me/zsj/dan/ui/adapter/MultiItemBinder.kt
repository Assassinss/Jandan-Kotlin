package me.zsj.dan.ui.adapter

import android.app.Activity
import android.view.View
import me.zsj.dan.data.DataManager
import me.zsj.dan.model.Comment
import me.zsj.dan.utils.StringUtils

/**
 * @author zsj
 */
class MultiItemBinder(dataManager: DataManager) : ItemBinder(dataManager) {

    fun bindData(context: Activity, holder: PictureAdapter.MultiHolder, item: Comment) {
        bindCommonData(context, holder, item)

        if (!item.textContent.isEmpty()) {
            if (item.textContent == StringUtils.FILTER_1 || item.textContent == StringUtils.FILTER_2) {
                holder.textContent.visibility = View.GONE
            } else {
                holder.textContent.visibility = View.VISIBLE
                holder.textContent.text = StringUtils.filter(item.textContent)
            }
        } else {
            holder.textContent.visibility = View.GONE
        }

        if (item.pics != null) {
            holder.setPictures(item.pics)
        }

        setClickListener(holder, item)
        holder.card.setOnClickListener { startTucaoActivity(context, item.commentId) }
    }
}