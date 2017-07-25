package me.zsj.dan.ui.adapter

import android.app.Activity
import android.graphics.Bitmap
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import me.zsj.dan.data.DataManager
import me.zsj.dan.data.executor.DownloadExecutors
import me.zsj.dan.data.executor.GifCallback
import me.zsj.dan.model.Comment
import me.zsj.dan.utils.StringUtils
import pl.droidsonroids.gif.GifDrawable

/**
 * @author zsj
 */
class GifItemBinder(dataManager: DataManager) : ItemBinder(dataManager) {

    fun bindData(context: Activity, holder: PictureAdapter.GifHolder, item: Comment) {
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

        Glide.with(context)
                .load(item.pics[0])
                .asBitmap()
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                        holder.gifImage.setImageView(resource, resource!!.width, resource.height)
                    }
                })

        holder.playGif.setOnClickListener {
            DownloadExecutors.getExecutors()
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
            stopGifAnimation(holder)
            startTucaoActivity(context, item.id)
        }

        stopGifAnimation(holder)
    }

    fun stopGifAnimation(holder: PictureAdapter.GifHolder) {
        val gifDrawable = holder.gifImage.drawable
        if (gifDrawable is GifDrawable) {
            holder.playGif.visibility = View.VISIBLE
            holder.loadingProgress.visibility = View.GONE
            stopGifAnimation(gifDrawable)
        }
    }
}