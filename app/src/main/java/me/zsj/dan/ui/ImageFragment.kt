package me.zsj.dan.ui

import android.graphics.BitmapFactory
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.ImageViewState
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import me.zsj.dan.R
import me.zsj.dan.glide.ProgressTarget
import me.zsj.dan.ui.fragment.LazyLoadFragment
import me.zsj.dan.utils.ScreenUtils
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import java.io.File

/**
 * @author zsj
 */
class ImageFragment : LazyLoadFragment() {

    private val TAG = "ImageFragment"

    private var largeImage: SubsamplingScaleImageView? = null
    private var gifImage: GifImageView? = null
    private var loadingProgress: ProgressBar? = null

    override fun initViews(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_image, container, false)
        largeImage = view.findViewById(R.id.large_image)
        gifImage = view.findViewById(R.id.gif_image)
        loadingProgress = view.findViewById(R.id.loading_progress)
        return view
    }

    override fun initData() {
        val picUrl = arguments?.getString("picUrl")
        loadingProgress?.visibility = View.VISIBLE
        Glide.with(activity)
                .load(picUrl)
                .downloadOnly(object : ProgressTarget<String, File>(picUrl, null) {
                    override fun onProgress(bytesRead: Long, expectedLength: Long) {
                        val percent = (bytesRead * 100 / expectedLength)
                        loadingProgress?.progress = percent.toInt()
                    }

                    override fun onResourceReady(resource: File?, animation: GlideAnimation<in File>?) {
                        super.onResourceReady(resource, animation)
                        setImage(picUrl!!, resource)
                    }

                    override fun getSize(cb: SizeReadyCallback?) {
                        cb?.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    }

                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                        super.onLoadFailed(e, errorDrawable)
                        loadingProgress?.visibility = View.GONE
                    }
                })
    }

    private fun setImage(picUrl: String, resource: File?) {
        loadingProgress?.visibility = View.GONE
        resource?.let {
            if (picUrl.endsWith(".gif")) {
                val gifDrawable = GifDrawable(it)
                gifImage?.setImageDrawable(gifDrawable)
            } else {
                largeImage?.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM)
                val bitmap = BitmapFactory.decodeFile(it.path)
                val scale = ScreenUtils.getScreenWidth(context).toFloat() / bitmap.width.toFloat()
                largeImage?.minScale = scale
                largeImage?.setImage(ImageSource.cachedBitmap(bitmap),
                        ImageViewState(scale, PointF(0f, 0f),
                                SubsamplingScaleImageView.ORIENTATION_USE_EXIF))
            }
        }
    }
}