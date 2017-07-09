package me.zsj.dan.binder

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import me.zsj.dan.R
import me.zsj.dan.data.DataManager
import me.zsj.dan.glide.ProgressTarget
import me.zsj.dan.model.Comment
import me.zsj.dan.utils.ScreenUtils
import me.zsj.dan.utils.StringUtils
import me.zsj.dan.widget.RatioScaleImageView
import java.io.File

/**
 * @author zsj
 */
class SinglePictureBinder(private val context: Activity, dm: DataManager) :
        ViewBinder<Comment, SinglePictureBinder.SingleHolder>(dm) {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): SingleHolder {
        val root = inflater.inflate(R.layout.item_single_pic, parent, false)
        return SingleHolder(root)
    }

    override fun onBindViewHolder(holder: Holder, item: Comment) {
        holder as SingleHolder

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
                .downloadOnly(object : ProgressTarget<String, File>(item.pics[0], null) {
                    override fun onProgress(bytesRead: Long, expectedLength: Long) {}

                    override fun onResourceReady(resource: File?, animation: GlideAnimation<in File>?) {
                        super.onResourceReady(resource, animation)
                        val options = BitmapFactory.Options()
                        options.inJustDecodeBounds = true
                        options.inPreferredConfig = Bitmap.Config.RGB_565
                        BitmapFactory.decodeFile(resource!!.path, options)
                        val width = options.outWidth
                        val height = options.outHeight
                        if (height >= ScreenUtils.getScreenHeight(context)) {
                            holder.browsePicture.visibility = View.VISIBLE
                            holder.picture.setBigImage(resource, options)
                        } else {
                            holder.browsePicture.visibility = View.GONE
                            holder.picture.setOriginalSize(width, height)
                            holder.picture.setImage(BitmapFactory.decodeFile(resource.path))
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

    override fun onVoteOO(holder: Holder, result: String?) {
        holder as SingleHolder
        updateVotePositive(context, holder, result)
    }

    override fun onVoteXX(holder: Holder, result: String?) {
        holder as SingleHolder
        updateVoteNegative(context, holder, result)
    }

    open inner class SingleHolder(itemView: View) : Holder(itemView) {
        val card: CardView by bindView(R.id.card)
        val textContent: TextView by bindView(R.id.text_content)
        val picture: RatioScaleImageView by bindView(R.id.picture)
        val browsePicture: TextView by bindView(R.id.browse_big_picture)
    }

}