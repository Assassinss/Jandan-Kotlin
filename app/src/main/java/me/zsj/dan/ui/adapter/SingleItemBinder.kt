package me.zsj.dan.ui.adapter

import android.app.Activity
import android.graphics.BitmapFactory
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import me.zsj.dan.data.DataManager
import me.zsj.dan.glide.ProgressTarget
import me.zsj.dan.model.Comment
import me.zsj.dan.utils.ScreenUtils
import me.zsj.dan.utils.StringUtils
import java.io.File

/**
 * @author zsj
 */
class SingleItemBinder(dataManager: DataManager) : ItemBinder(dataManager) {

    fun bindData(context: Activity, holder: PictureAdapter.SingleHolder, item: Comment) {
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
                .downloadOnly(object : ProgressTarget<String, File>(item.pics[0], null) {
                    override fun onProgress(bytesRead: Long, expectedLength: Long) {}

                    override fun onResourceReady(resource: File?, animation: GlideAnimation<in File>?) {
                        super.onResourceReady(resource, animation)
                        val bitmap = BitmapFactory.decodeFile(resource!!.path)
                        val width = bitmap.width
                        val height = bitmap.height
                        if (height >= ScreenUtils.getScreenHeight(context)) {
                            holder.browsePicture.visibility = View.VISIBLE
                            holder.picture.setBigImage(resource, width)
                        } else {
                            holder.browsePicture.visibility = View.GONE
                            holder.picture.setImageView(bitmap, width, height)
                        }
                    }

                    override fun getSize(cb: SizeReadyCallback?) {
                        cb?.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    }
                })

        holder.picture.setOnClickListener {
            startImageActivity(context, arrayListOf(item.pics[0]))
        }

        setClickListener(holder, item)
        holder.card.setOnClickListener { startTucaoActivity(context, item.id) }
    }
}