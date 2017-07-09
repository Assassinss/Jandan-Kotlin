package me.zsj.dan.binder

import android.app.Activity
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import me.zsj.dan.R
import me.zsj.dan.data.DataManager
import me.zsj.dan.model.Comment

/**
 * @author zsj
 */
class JokeViewBinder(private val context: Activity, dm: DataManager) :
        ViewBinder<Comment, JokeViewBinder.JokeHolder>(dm) {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): Holder {
        val view = inflater.inflate(R.layout.item_joke, parent, false)
        return JokeHolder(view)
    }

    override fun onBindViewHolder(holder: Holder, comment: Comment) {
        holder as JokeHolder

        bindCommonData(holder, comment)

        setClickListener(holder, comment)

        holder.textContent.text = comment.textContent
        holder.card.setOnClickListener { startTucaoActivity(context, comment.id) }
    }

    override fun onVoteOO(holder: Holder, result: String?) {
        holder as JokeHolder
        updateVotePositive(context, holder, result)
    }

    override fun onVoteXX(holder: Holder, result: String?) {
        holder as JokeHolder
        updateVoteNegative(context, holder, result)
    }

    inner class JokeHolder(itemView: View) : Holder(itemView) {
        val card: CardView by bindView(R.id.card)
        val textContent: TextView by bindView(R.id.text_content)
    }
}