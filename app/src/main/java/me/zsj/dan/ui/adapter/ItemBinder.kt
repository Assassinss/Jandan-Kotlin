package me.zsj.dan.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.PopupMenu
import android.util.Log
import android.view.Gravity
import android.view.View
import me.zsj.dan.R
import me.zsj.dan.binder.Holder
import me.zsj.dan.data.BaseDataManager
import me.zsj.dan.data.DataManager
import me.zsj.dan.model.Comment
import me.zsj.dan.ui.ImageActivity
import me.zsj.dan.ui.TucaoActivity
import me.zsj.dan.utils.DateUtils
import me.zsj.dan.utils.loadColor
import me.zsj.dan.utils.shortToast
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import pl.droidsonroids.gif.GifDrawable
import java.io.IOException

/**
 * @author zsj
 */
open class ItemBinder(var dataManager: DataManager) {

    private val TAG = "ItemBinder"
    private val handler: Handler = Handler(Looper.getMainLooper())
    private lateinit var onVoteListener: OnVoteListener

    fun setOnVoteListener(onVoteListener: OnVoteListener) {
        this.onVoteListener = onVoteListener
    }

    fun setClickListener(holder: Holder, comment: Comment) {
        holder.votePositive.setOnClickListener { voteOO(holder, comment.id) }
        holder.voteNegative.setOnClickListener { voteXX(holder, comment.id) }
        holder.more.setOnClickListener { showActionMenu(holder, comment, it) }
    }

    fun showActionMenu(holder: Holder, comment: Comment, view: View) {
        val actionMenu = PopupMenu(holder.more.context, view, Gravity.END)
        actionMenu.inflate(R.menu.menu_more)
        actionMenu.setOnMenuItemClickListener { item->
            if (item.itemId == R.id.action_share) {
                //TODO: share item
            } else if (item.itemId == R.id.action_copy) {
                //TODO: copy text content
            }
            true
        }
        actionMenu.show()
    }

    fun voteOO(holder: Holder, ID: String) {
        dataManager.getClient().newCall(
                dataManager.buildRequest(BaseDataManager.VOTE_POSITIVE, ID))
                .enqueue(object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        Log.e(TAG, "failed to vote OO", e)
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        if (response!!.isSuccessful) {
                            val body = response.body()?.string()
                            handler.post { onVoteListener.onVoteOO(holder, body) }
                        }
                    }
                })
    }

    fun voteXX(holder: Holder, ID: String) {
        dataManager.getClient().newCall(
                dataManager.buildRequest(BaseDataManager.VOTE_NEGATIVE, ID))
                .enqueue(object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        Log.e(TAG, "faile to vote XX ", e)
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        if (response!!.isSuccessful) {
                            val body = response.body()?.string()
                            handler.post {  onVoteListener.onVoteXX(holder, body)  }
                        }
                    }
                })
    }


    fun bindCommonData(context: Activity, holder: Holder, comment: Comment) {
        val color_gray_600 = context.loadColor(R.color.item_text_color)
        holder.votePositive.setTextColor(color_gray_600)
        holder.voteNegative.setTextColor(color_gray_600)

        if (comment.voted) {
            holder.votePositive.setTextColor(Color.RED)
        } else {
            holder.votePositive.setTextColor(color_gray_600)
        }
        if (comment.negative) {
            holder.voteNegative.setTextColor(Color.BLUE)
        } else {
            holder.voteNegative.setTextColor(color_gray_600)
        }

        holder.author.text = comment.author
        holder.dateTime.text = DateUtils.getRelativeTimeSpanString(comment.commentDate)
        holder.votePositive.text = context.getString(R.string.vote_positive_text, comment.votePositive)
        holder.voteNegative.text = context.getString(R.string.vote_negative_text, comment.voteNegative)
        holder.commentCount.text = context.getString(R.string.tucao_count_text, comment.commentCount)
    }

    fun updateVotePositive(context: Context, holder: Holder, result: String?) {
        val comment = dataManager.getCommnets()[holder.adapterPosition]
        val count = result!!.last()
        if (count == '1') {
            comment.voted = true
            comment.votePositive = (comment.votePositive.toInt() + 1).toString()
            holder.votePositive.text = context.getString(R.string.vote_positive_text, comment.votePositive)
            holder.votePositive.setTextColor(Color.RED)
        } else {
            context.shortToast("You've Voted.")
        }
    }

    fun updateVoteNegative(context: Context, holder: Holder, result: String?) {
        val comment = dataManager.getCommnets()[holder.adapterPosition]
        val count = result!!.last()
        if (count == '1') {
            comment.negative = true
            comment.voteNegative = (comment.voteNegative.toInt() + 1).toString()
            holder.voteNegative.text = context.getString(R.string.vote_negative_text, comment.voteNegative)
            holder.voteNegative.setTextColor(Color.BLUE)
        } else {
            context.shortToast("You've Voted.")
        }
    }

    fun stopGifAnimation(gifDrawable: GifDrawable) {
        if (gifDrawable.isPlaying) {
            gifDrawable.stop()
        }
    }

    fun startImageActivity(context: Context, picUrls: ArrayList<String>) {
        val intent = Intent(context, ImageActivity::class.java)
        intent.putExtra(ImageActivity.INDEX, 0)
        intent.putStringArrayListExtra(ImageActivity.PIC_URLS, picUrls)
        context.startActivity(intent)
    }

    fun startTucaoActivity(context: Context, id: String) {
        val intent = Intent(context, TucaoActivity::class.java)
        intent.putExtra(TucaoActivity.ID, id)
        context.startActivity(intent)
    }

    interface OnVoteListener {
        fun onVoteOO(holder: Holder, result: String?)
        fun onVoteXX(holder: Holder, result: String?)
    }
}