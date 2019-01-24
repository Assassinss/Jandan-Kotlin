package me.zsj.dan.data

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import me.zsj.dan.glide.ProgressTarget
import pl.droidsonroids.gif.GifDrawable
import java.io.File
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author zsj
 */
class Downloader private constructor() {

    private val TAG = "Downloader"

    private val networkIO: ExecutorService = Executors.newSingleThreadExecutor()

    private val handler: Handler = Handler(Looper.getMainLooper())

    private var requests = HashMap<String, ProgressTarget<String, File>>()

    companion object {
        private var INSTANCE: Downloader? = null

        fun get() = INSTANCE ?: getInstance().also { INSTANCE = it }

        private fun getInstance() : Downloader {
            return Downloader()
        }
    }

    fun downloadGif(context: Context, url: String,
                    progress: (percent: Int) -> Unit,
                    loadingStart: () -> Unit,
                    loadingFinish: (gifDrawable: GifDrawable) -> Unit,
                    loadingFailed: () -> Unit): Downloader {
        loadingStart.invoke()
        val oldTarget = requests[url]
        oldTarget?.request?.begin()
        val target = Glide.with(context)
                .load(url)
                .downloadOnly(object : ProgressTarget<String, File>(url, null) {
                    override fun onProgress(bytesRead: Long, expectedLength: Long) {
                        val percent = (bytesRead * 100 / expectedLength)
                        progress.invoke(percent.toInt())
                    }

                    override fun onResourceReady(resource: File?, animation: GlideAnimation<in File>?) {
                        super.onResourceReady(resource, animation)
                        resource?.let {
                            val gifDrawable = GifDrawable(it)
                            loadingFinish.invoke(gifDrawable)
                        }
                        requests.remove(url)
                    }

                    override fun getSize(cb: SizeReadyCallback?) {
                        cb?.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    }

                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                        super.onLoadFailed(e, errorDrawable)
                        loadingFailed.invoke()
                    }
                })
        requests[url] = target
        return this
    }

    fun downloadImage(context: Context,
                      url: String,
                      action: (file: File) -> Unit) {
        networkIO.execute {
            try {
                val file = Glide.with(context)
                        .load(url)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get()
                handler.post {
                    action.invoke(file)
                }
            } catch (e: InterruptedException) {
                Log.d(TAG, "failed to download gif ", e)
            } catch (e: ExecutionException) {
                Log.d(TAG, "failed to download gif ", e)
            }
        }
    }

    fun stopDownload(url: String) {
        val target = requests[url]
        target?.request?.pause()
    }

}