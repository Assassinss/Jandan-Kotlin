package me.zsj.dan.ui.adapter

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.davemorrissey.labs.subscaleview.ImageSource
import me.zsj.dan.R
import me.zsj.dan.data.DataManager
import me.zsj.dan.model.Comment
import me.zsj.dan.utils.ScreenUtils
import me.zsj.dan.utils.StringUtils

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
                /*.downloadOnly(object : ProgressTarget<String, File>(item.pics[0], null) {
                    override fun onProgress(bytesRead: Long, expectedLength: Long) {
                        Log.e("SingleItem", "on progress....")
                    }

                    override fun onResourceReady(resource: File?, animation: GlideAnimation<in File>?) {
                        val bitmap = BitmapFactory.decodeFile(resource!!.path)
                        Log.e("SingleItem", "bitmap width: " + bitmap.width + "  height:  "+ bitmap.height)
                        if (bitmap.height > 1920) {
                            holder.picture.setBigImage(resource!!, bitmap.width)
                        } else {
                            holder.picture.setImageBitmap(bitmap)
                        }
                    }

                    override fun onLoadStarted(placeholder: Drawable?) {
                        super.onLoadStarted(placeholder)
                        holder.picture.setImageResource(R.drawable.defalut_placeholder_bg)
                    }

                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                        super.onLoadFailed(e, errorDrawable)
                        Log.e("SingleItem", e.toString())
                        holder.picture.setImageResource(R.drawable.defalut_placeholder_bg)
                    }
                })*/
                .asBitmap()
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                       /* resource?.let {
                            if (resource.height > 1920) {
                                holder.browsePicture.visibility = View.VISIBLE
                                holder.picture.setImage(ImageSource.cachedBitmap(it)
                                        .region(Rect(0, 0, it.width, ScreenUtils.dpToPx(120))))
                            } else {
                                holder.browsePicture.visibility = View.GONE
                                holder.picture.setImage(ImageSource.cachedBitmap(it))
                            }
                        }*/
                        resource?.let {
                            if (it.height > 1920) {
                                holder.browsePicture.visibility = View.VISIBLE
                                holder.picture.visibility = View.GONE
                                holder.scaleImageView.visibility = View.VISIBLE
                                holder.scaleImageView.setImage(ImageSource.cachedBitmap(it)
                                        .region(Rect(0, 0, it.width, ScreenUtils.dpToPx(120))))
                            } else {
                                holder.browsePicture.visibility = View.GONE
                                holder.picture.visibility = View.VISIBLE
                                holder.scaleImageView.visibility = View.GONE
                                holder.picture.setOriginalSize(it.width, it.height)
                                holder.picture.setImageBitmap(resource)
                            }
                        }
                    }

                    override fun onLoadStarted(placeholder: Drawable?) {
                        super.onLoadStarted(placeholder)
                        holder.picture.setImageResource(R.drawable.defalut_placeholder_bg)
                    }

                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                        super.onLoadFailed(e, errorDrawable)
                        holder.picture.setImageResource(R.drawable.defalut_placeholder_bg)
                    }
                })

        /*Glide.with(context)
                .load(item.pics[0])
                .into(holder.picture)*/
               /* .downloadOnly(object : ProgressTarget<String, File>(item.pics[0], null) {
                    override fun onProgress(bytesRead: Long, expectedLength: Long) {}

                    override fun onResourceReady(resource: File?, animation: GlideAnimation<in File>?) {
                        super.onResourceReady(resource, animation)
                        val bitmap = BitmapFactory.decodeFile(resource?.path)
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

                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                        super.onLoadFailed(e, errorDrawable)
                        holder.picture.setBackgroundResource(R.drawable.defalut_placeholder_bg)
                    }

                    override fun onLoadStarted(placeholder: Drawable?) {
                        super.onLoadStarted(placeholder)
                        holder.picture.setBackgroundResource(R.drawable.defalut_placeholder_bg)
                    }
                })*/

        holder.picture.setOnClickListener {
            startImageActivity(context, arrayListOf(item.pics[0]))
        }

        holder.scaleImageView.setOnClickListener {
            startImageActivity(context, arrayListOf(item.pics[0]))
        }

        setClickListener(holder, item)
        holder.card.setOnClickListener { startTucaoActivity(context, item.commentId) }
    }
}