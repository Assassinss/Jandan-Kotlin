package me.zsj.dan.data.executor

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.target.Target
import pl.droidsonroids.gif.GifDrawable
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author zsj
 */
class DownloadExecutors private constructor() {

    private val TAG = "DownloadExecutors"

    private val networkIO: ExecutorService = Executors.newSingleThreadExecutor()

    private val handler: Handler = Handler(Looper.getMainLooper())

    private lateinit var gifCallback: GifCallback
    private lateinit var downloadCallback: DownloadCallback

    private var task: FutureTarget<File>? = null

    fun registerCallback(gifCallback: GifCallback): DownloadExecutors {
        this.gifCallback = gifCallback
        return this
    }

    fun registerDownloadCallback(downloadCallback: DownloadCallback): DownloadExecutors {
        this.downloadCallback = downloadCallback
        return this
    }

    companion object {
        val INSTANCE: DownloadExecutors = Instance.INSTANCE

        private object Instance {
            val INSTANCE = DownloadExecutors()
        }
    }

    //TODO: find a best way download gif
    fun downloadGif(context: Context, url: String): DownloadExecutors {
        gifCallback.onLoadingStart()
        task = Glide.with(context)
                .load(url)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
        networkIO.execute {
            try {
                val file = task?.get()
                val inputStream = BufferedInputStream(FileInputStream(file))
                val gifDrawable = GifDrawable(inputStream)
                handler.post {
                    gifCallback.onLoadFinished(gifDrawable)
                }
            } catch (e: InterruptedException) {
                Log.d(TAG, "failed to download gif ", e)
                handler.post { gifCallback.onLoadFailed() }
            } catch (e: ExecutionException) {
                Log.d(TAG, "failed to download gif ", e)
                handler.post { gifCallback.onLoadFailed() }
            }
        }
        return this
    }

    fun downloadImage(context: Context, url: String): DownloadExecutors {
        networkIO.execute {
            try {
                val file = Glide.with(context)
                        .load(url)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get()
                handler.post {
                    downloadCallback.onDownloadFile(file)
                }
            } catch (e: InterruptedException) {
                Log.d(TAG, "failed to download gif ", e)
            } catch (e: ExecutionException) {
                Log.d(TAG, "failed to download gif ", e)
            }
        }
        return this
    }

    fun shutdown() {
        Log.d("DownloadExecutors", "task: " + task?.toString())
        if (task != null) {
            Glide.clear(task)
        }
    }

}