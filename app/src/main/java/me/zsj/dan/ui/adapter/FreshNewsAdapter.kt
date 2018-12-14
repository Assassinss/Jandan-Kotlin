package me.zsj.dan.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotterknife.bindView
import me.zsj.dan.R
import me.zsj.dan.model.Post
import me.zsj.dan.ui.NewDetailActivity
import me.zsj.dan.ui.adapter.common.LoadingHolder
import me.zsj.dan.ui.adapter.common.OnErrorListener
import me.zsj.dan.ui.adapter.common.OnLoadDataListener
import me.zsj.dan.utils.DateUtils
import java.util.*

/**
 * @author zsj
 */
class FreshNewsAdapter(var context: Activity, var freshNewsList: ArrayList<Post>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(), OnErrorListener {

    private var error: Boolean = false
    private var onLoadDataListener: OnLoadDataListener? = null

    fun setOnLoadDataListener(onLoadDataListener: OnLoadDataListener) {
        this.onLoadDataListener = onLoadDataListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == R.layout.item_load_more) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_load_more, parent, false)
            return LoadingHolder(view)
        } else if (viewType == R.layout.item_news) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false)
            return NewsHolder(view)
        }
        throw IllegalArgumentException()
    }

    override fun onLoadingError(error: Boolean) {
        this.error = error
        notifyItemChanged(freshNewsList.size)
    }

    @SuppressLint("StringFormatMatches")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == R.layout.item_load_more) {
            holder as LoadingHolder
            holder.showLoading(holder, itemCount, error)
            holder.loadingContainer.setOnClickListener {
                holder.progressBar.visibility = View.VISIBLE
                holder.errorText.visibility = View.GONE
                onLoadDataListener?.onLoadMoreData()
            }
        } else if (getItemViewType(position) == R.layout.item_news) {
            holder as NewsHolder
            val freshNew = freshNewsList[position]
            holder.title.text = freshNew.title
            holder.author.text = freshNew.author.name
            holder.dateTime.text = DateUtils.getRelativeTimeSpanString(freshNew.date)
            if (freshNew.commentCount == 0) {
                holder.commentCount.visibility = View.GONE
            } else {
                holder.commentCount.visibility = View.VISIBLE
                holder.commentCount.text = context.getString(R.string.comment_count_text, freshNew.commentCount)
            }

            Glide.with(context)
                    .load(freshNew.customFields.thumbC[0])
                    .asBitmap()
                    .placeholder(R.drawable.defalut_placeholder_bg)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.cover)

            holder.itemContainer.setOnClickListener {
                val intent = Intent(context, NewDetailActivity::class.java)
                intent.putExtra(NewDetailActivity.POST, freshNew)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        var itemCount = 1
        if (freshNewsList.size > 0) {
            itemCount += freshNewsList.size
        }
        return itemCount
    }

    override fun getItemViewType(position: Int): Int {
        return if (position + 1 == itemCount) {
            R.layout.item_load_more
        } else {
            R.layout.item_news
        }
    }

    inner class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemContainer: ConstraintLayout by bindView(R.id.item_container)
        val title: TextView by bindView(R.id.title)
        val author: TextView by bindView(R.id.author)
        val dateTime: TextView by bindView(R.id.date_time)
        val commentCount: TextView by bindView(R.id.comment_count)
        val cover: ImageView by bindView(R.id.cover)
    }

}