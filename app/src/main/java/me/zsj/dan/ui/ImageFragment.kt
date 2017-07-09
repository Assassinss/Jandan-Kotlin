package me.zsj.dan.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.shizhefei.view.largeimage.LargeImageView
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory
import me.zsj.dan.R
import me.zsj.dan.data.executor.DownloadExecutors
import me.zsj.dan.data.executor.GifCallback
import me.zsj.dan.glide.ProgressTarget
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import java.io.File
import java.lang.Exception

/**
 * @author zsj
 */
class ImageFragment : Fragment() {

    private var largeImage: LargeImageView?= null
    private var gifImage: GifImageView?= null
    private var loadingProgress: ProgressBar? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_image, container, false)
        largeImage = view.findViewById(R.id.large_image) as LargeImageView
        gifImage = view.findViewById(R.id.gif_image) as GifImageView
        loadingProgress = view.findViewById(R.id.loading_progress) as ProgressBar
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val picUrl = arguments.getString("picUrl")

        if (picUrl.endsWith(".gif")) {
            //TODO:退出 Activity 取消下载任务
            DownloadExecutors.INSTANCE
                    .registerCallback(object : GifCallback {
                        override fun onLoadingStart() {
                            loadingProgress?.visibility = View.VISIBLE
                        }

                        override fun onLoadFinished(gifDrawable: GifDrawable) {
                            loadingProgress?.visibility = View.GONE
                            gifImage?.setImageDrawable(gifDrawable)
                        }

                        override fun onLoadFailed() {
                            loadingProgress?.visibility = View.GONE
                        }
                    })
                    .downloadGif(activity, picUrl)
        } else {
            loadingProgress?.visibility = View.VISIBLE
            Glide.with(activity)
                    .load(picUrl)
                    .downloadOnly(object : ProgressTarget<String, File>(picUrl, null) {
                        override fun onProgress(bytesRead: Long, expectedLength: Long) {}

                        override fun onResourceReady(resource: File?, animation: GlideAnimation<in File>?) {
                            super.onResourceReady(resource, animation)
                            loadingProgress?.visibility = View.GONE
                            largeImage?.setImage(FileBitmapDecoderFactory(resource))
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
    }

    override fun onDestroy() {
        Glide.clear(largeImage)
        super.onDestroy()
    }
}