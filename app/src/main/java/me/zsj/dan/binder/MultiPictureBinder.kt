package me.zsj.dan.binder

import android.app.Activity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import me.zsj.dan.R
import me.zsj.dan.data.DataManager
import me.zsj.dan.model.Comment
import me.zsj.dan.utils.StringUtils

/**
 * @author zsj
 */
class MultiPictureBinder(var context: Activity, dm: DataManager) :
        ViewBinder<Comment, MultiPictureBinder.MultiHolder>(dm) {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): MultiHolder {
        val root = inflater.inflate(R.layout.item_multi_pic, parent, false)
        return MultiHolder(root)
    }

    override fun onBindViewHolder(holder: Holder, item: Comment) {
        holder as MultiHolder

        bindCommonData(holder, item)

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
        holder.card.setOnClickListener { startTucaoActivity(context, item.id) }
    }

    override fun onVoteOO(holder: Holder, result: String?) {
        holder as MultiHolder
        updateVotePositive(context, holder, result)
    }

    override fun onVoteXX(holder: Holder, result: String?) {
        holder as MultiHolder
        updateVoteNegative(context, holder, result)
    }

    inner class MultiHolder(itemView: View) : Holder(itemView) {
        val card: CardView by bindView(R.id.card)
        val textContent: TextView by bindView(R.id.text_content)
        val recyclerView: RecyclerView by bindView(R.id.pic_item_list)
        private var pictureAdapter: ItemPictureAdapter? = null

        init {
            pictureAdapter = ItemPictureAdapter()
            recyclerView.adapter = pictureAdapter
        }

        fun setPictures(pics: ArrayList<String>) {
            pictureAdapter!!.setPics(pics)
            pictureAdapter?.notifyDataSetChanged()
        }
    }

}
