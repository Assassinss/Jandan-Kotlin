package me.zsj.dan.data

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

    private var task: FutureTarget<File>? = null

    companion object {
        private var INSTANCE: DownloadExecutors? = null

        fun get() = INSTANCE ?: getInstance().also { INSTANCE = it }

        private fun getInstance() : DownloadExecutors {
            return DownloadExecutors()
        }
    }

    //TODO: find a best way download gif
    fun downloadGif(context: Context, url: String,
                    loadingStart: () -> Unit,
                    loadingFinish: (gifDrawable: GifDrawable) -> Unit,
                    loadingFailed: () -> Unit): DownloadExecutors {
        loadingStart.invoke()
        task = Glide.with(context)
                .load(url)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
        networkIO.execute {
            try {
                val file = task?.get()
                val inputStream = BufferedInputStream(FileInputStream(file))
                val gifDrawable = GifDrawable(inputStream)
                handler.post {
                    loadingFinish.invoke(gifDrawable)
                }
            } catch (e: InterruptedException) {
                Log.d(TAG, "failed to download gif ", e)
                handler.post { loadingFailed.invoke() }
            } catch (e: ExecutionException) {
                Log.d(TAG, "failed to download gif ", e)
                handler.post { loadingFailed.invoke() }
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
                    //downloadCallback.onDownloadFile(file)
                }
            } catch (e: InterruptedException) {
                Log.d(TAG, "failed to download gif ", e)
            } catch (e: ExecutionException) {
                Log.d(TAG, "failed to download gif ", e)
            }
        }
    }

}