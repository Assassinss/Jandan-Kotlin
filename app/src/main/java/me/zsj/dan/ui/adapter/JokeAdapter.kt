package me.zsj.dan.ui.adapter

import android.app.Activity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import me.zsj.dan.R
import me.zsj.dan.binder.Holder
import me.zsj.dan.data.DataManager
import me.zsj.dan.model.Comment
import me.zsj.dan.ui.adapter.common.LoadingHolder

/**
 * @author zsj
 */
class JokeAdapter(var context: Activity, var comments: ArrayList<Comment>,
                  var dataManager: DataManager) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemBinder.OnVoteListener {

    private var itemBinder: ItemBinder? = null

    init {
        itemBinder = ItemBinder(dataManager)
        itemBinder?.setOnVoteListener(this)
    }

    override fun onVoteOO(holder: Holder, result: String?) {
        holder as JokeHolder
        itemBinder?.updateVotePositive(context, holder, result)
    }

    override fun onVoteXX(holder: Holder, result: String?) {
        holder as JokeHolder
        itemBinder?.updateVoteNegative(context, holder, result)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        if (viewType == R.layout.item_load_more) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_load_more, parent, false)
            return LoadingHolder(view)
        } else if (viewType == R.layout.item_joke) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_joke, parent, false)
            return JokeHolder(view)
        }
        return null
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (getItemViewType(position) == R.layout.item_load_more) {
            holder as LoadingHolder
            if (itemCount == 1) {
                holder.progressBar.visibility = View.GONE
            } else {
                holder.progressBar.visibility = View.VISIBLE
            }
        } else if (getItemViewType(position) == R.layout.item_joke) {
            holder as JokeHolder
            val comment = comments[position]
            itemBinder?.bindCommonData(context, holder, comment)

            itemBinder?.setClickListener(holder, comment)

            holder.textContent.text = comment.textContent
            holder.card.setOnClickListener { itemBinder?.startTucaoActivity(context, comment.id) }
        }
    }

    override fun getItemCount(): Int {
        var itemCount = 1
        if (comments.size > 0) {
            itemCount += comments.size
        }
        return itemCount
    }

    override fun getItemViewType(position: Int): Int {
        if (position + 1 == itemCount) {
            return R.layout.item_load_more
        } else {
            return R.layout.item_joke
        }
    }

    inner class JokeHolder(itemView: View) : Holder(itemView) {
        val card: CardView by bindView(R.id.card)
        val textContent: TextView by bindView(R.id.text_content)
    }
}