package me.zsj.dan.ui.adapter

import android.app.Activity
import android.view.View
import com.bumptech.glide.Glide
import me.zsj.dan.R
import me.zsj.dan.data.DataManager
import me.zsj.dan.data.Downloader
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

        //val width = ScreenUtils.getScreenWidth(context) - ScreenUtils.dpToPx(64)

        Glide.with(context)
                .load(item.pics[0])
                .asBitmap()
                .placeholder(R.drawable.defalut_placeholder_bg)
                .error(R.drawable.defalut_placeholder_bg)
                //.override(width, width)
                .into(holder.gifImage)
                /*.into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                        holder.gifImage.setImageView(resource, resource!!.width, resource.height)
                    }
                })*/

        holder.playGif.setOnClickListener {
            Downloader.get()
                    .downloadGif(context, item.pics[0], { percent ->
                        holder.loadingProgress.progress = percent
                    }, { //start download
                        holder.playGif.visibility = View.GONE
                        holder.loadingProgress.visibility = View.VISIBLE
                    }, { // download finish
                        holder.playGif.visibility = View.GONE
                        holder.loadingProgress.visibility = View.GONE
                        holder.gifImage.setImageDrawable(it)
                    }, { //download failed
                        holder.playGif.visibility = View.VISIBLE
                        holder.loadingProgress.visibility = View.GONE
                    })
        }

        holder.gifImage.setOnClickListener {
            startImageActivity(context, arrayListOf(item.pics[0]))
        }

        setClickListener(holder, item)
        holder.card.setOnClickListener {
            stopGifAnimation(holder)
            startTucaoActivity(context, item.commentId)
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