package me.zsj.dan.binder

import android.app.Activity
import android.graphics.Bitmap
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.bindView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import me.zsj.dan.R
import me.zsj.dan.data.DataManager
import me.zsj.dan.data.executor.DownloadExecutors
import me.zsj.dan.data.executor.GifCallback
import me.zsj.dan.model.Comment
import me.zsj.dan.utils.StringUtils
import me.zsj.dan.visibility.items.ListItem
import me.zsj.dan.widget.GifRatioScaleImageView
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

/**
 * @author zsj
 */
class GifPictureBinder(private val context: Activity, dm: DataManager) :
        ViewBinder<Comment, GifPictureBinder.GifHolder>(dm) {

    private val TAG = GifPictureBinder::class.java.simpleName

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): GifHolder {
        val root = inflater.inflate(R.layout.item_gif_pic, parent, false)
        return GifHolder(root)
    }

    override fun onBindViewHolder(holder: Holder, item: Comment) {
        holder as GifHolder

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

        Glide.with(context)
                .load(item.pics[0])
                .asBitmap()
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                        holder.gifImage.setOriginalSize(resource!!.width, resource.height)
                        holder.gifImage.setImageBitmap(resource)
                    }
                })

        holder.playGif.setOnClickListener {
            DownloadExecutors.INSTANCE
                    .registerCallback(object : GifCallback {
                        override fun onLoadingStart() {
                            holder.playGif.visibility = View.GONE
                            holder.loadingProgress.visibility = View.VISIBLE
                        }

                        override fun onLoadFinished(gifDrawable: GifDrawable) {
                            holder.playGif.visibility = View.GONE
                            holder.loadingProgress.visibility = View.GONE
                            holder.gifImage.setImageDrawable(gifDrawable)
                        }

                        override fun onLoadFailed() {
                            holder.playGif.visibility = View.VISIBLE
                            holder.loadingProgress.visibility = View.GONE
                        }
                    })
                    .downloadGif(context, item.pics[0])
        }

        holder.gifImage.setOnClickListener {
            startImageActivity(context, arrayListOf(item.pics[0]))
        }

        setClickListener(holder, item)
        holder.card.setOnClickListener {
            val gifDrawable = holder.gifImage.drawable
            if (gifDrawable is GifDrawable) {
                holder.playGif.visibility = View.VISIBLE
                holder.loadingProgress.visibility = View.GONE
                stopGifAnimation(gifDrawable)
            }
            startTucaoActivity(context, item.id)
        }
    }

    override fun onVoteOO(holder: Holder, result: String?) {
        holder as GifHolder
        updateVotePositive(context, holder, result)
    }

    override fun onVoteXX(holder: Holder, result: String?) {
        holder as GifHolder
        updateVoteNegative(context, holder, result)
    }

    inner class GifHolder(itemView: View) : Holder(itemView), ListItem {
        val card: CardView by bindView(R.id.card)
        val gifImage: GifRatioScaleImageView by bindView(R.id.gif_picture)
        val playGif: ImageView by bindView(R.id.play_gif)
        val loadingProgress: ProgressBar by bindView(R.id.loading_progress)
        val textContent: TextView by bindView(R.id.text_content)

        override fun setActive(newActiveView: View?, newActiveViewPosition: Int) {
        }

        //TODO: 取消图片下载请求
        override fun deactivate(currentView: View?, position: Int) {
            //DownloadExecutors.INSTANCE.shutdown()
            val gifImage = currentView?.findViewById(R.id.gif_picture)
            val playGif = currentView?.findViewById(R.id.play_gif)
            val loadingProgress = currentView?.findViewById(R.id.loading_progress)
            if (gifImage != null && playGif != null && loadingProgress != null) {
                gifImage as GifImageView
                playGif as ImageView
                loadingProgress as ProgressBar
                val gifDrawable = gifImage.drawable
                if (gifDrawable is GifDrawable) {
                    playGif.visibility = View.VISIBLE
                    loadingProgress.visibility = View.GONE
                    stopGifAnimation(gifImage.drawable as GifDrawable)
                }
            }
        }
    }

}