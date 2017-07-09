package me.zsj.dan.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.bindView
import com.bumptech.glide.Glide
import me.zsj.dan.R
import me.zsj.dan.data.DataCallbackAdapter
import me.zsj.dan.data.DataManager
import me.zsj.dan.data.DataManagerFactory
import me.zsj.dan.data.executor.DownloadExecutors
import me.zsj.dan.data.executor.GifCallback
import me.zsj.dan.model.NewDetail
import me.zsj.dan.model.Post
import me.zsj.dan.utils.DateUtils
import pl.droidsonroids.gif.GifDrawable

/**
 * @author zsj
 */
class NewDetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        val POST = "post"
    }

    private val cover: ImageView by bindView(R.id.cover)
    private val title: TextView by bindView(R.id.title)
    private val author: TextView by bindView(R.id.author)
    private val dateTime: TextView by bindView(R.id.date_time)
    private val excerpt: TextView by bindView(R.id.excerpt)
    private val webView: WebView by bindView(R.id.webview)
    private val loadingProgress: ProgressBar by bindView(R.id.loading_progress)
    private val errorText: TextView by bindView(R.id.error_text)
    private val actionBack: ImageView by bindView(R.id.action_back)
    private val actionComments: ImageView by bindView(R.id.action_comments)
    private val actionShare: ImageView by bindView(R.id.action_share)

    private lateinit var dataManager: DataManager
    private var post: Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_detail)

        dataManager = DataManagerFactory.getInstance(this)
        dataManager.registerDataCallback(object : DataCallbackAdapter() {
            override fun onLoadNewDetail(newDetail: NewDetail?) {
                loadingProgress.visibility = View.GONE
                onDataLoaded(newDetail)
            }

            override fun onLoadFailed(error: String?) {
                loadingProgress.visibility = View.GONE
                onLoadDataFailed(error)
            }
        })

        post = intent.getParcelableExtra(POST)

        setupUI()

        setupClickListener()

        loadNewDetail()
    }

    private fun setupClickListener() {
        errorText.setOnClickListener(this)
        actionBack.setOnClickListener(this)
        actionComments.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.error_text -> {
                errorText.visibility = View.GONE
                loadNewDetail()
            }
            R.id.action_back -> finish()
            R.id.action_comments -> {
                val intent = Intent(this, CommentActivity::class.java)
                intent.putExtra(CommentActivity.ID, post?.id)
                startActivity(intent)
            }
        }
    }

    private fun setupUI() {
        var picUrl = post?.customFields!!.thumbC[0]
        if (!picUrl.endsWith("gif")) {
            picUrl = picUrl.replace("custom", "medium")
            Glide.with(this)
                    .load(picUrl)
                    .into(cover)
        } else {
            DownloadExecutors.INSTANCE
                    .registerCallback(object : GifCallback {
                        override fun onLoadingStart() {}

                        override fun onLoadFinished(gifDrawable: GifDrawable) {
                            cover.setImageDrawable(gifDrawable)
                        }

                        override fun onLoadFailed() {}
                    })
                    .downloadGif(this, picUrl)
        }

        title.text = post?.title
        author.text = post?.author?.name
        dateTime.text = DateUtils.getRelativeTimeSpanString(post!!.date)
        excerpt.text = post?.excerpt

        actionShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, post?.title + " " + post?.url)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "发送到"))
        }
    }

    private fun loadNewDetail() {
        loadingProgress.visibility = View.VISIBLE
        dataManager.loadNewDetail(post!!.id)
    }

    private fun createHtml(content: String?) : String {
        return  """
        <!DOCTYPE html><html><head>
            <script type="text/javascript">
                window.onload = function() {
                    Array.prototype.map.call(document.getElementsByTagName('img'), function(img) {
                        img.addEventListener("click", function () {
                            image.onImageClick(img.src);
                        });
                        return img.src;
                    });
                };
            </script>
            <style>
                body {padding: 8px; background-color: #ffffff}
                p { font-size:18px; line-height: 24px; padding:5px 0}
                img { display: block; margin: 0 auto; width: 100%; height: auto}
                blockquote { background-color: #f5f5f5; margin: 0; padding: 12px}
                em { font-size: 14px; color:#757575 }
                a { color: #e53935}
                iframe { display: block; margin: 0 auto; width: 100%; height: auto }
            </style></head>
            <body>$content</body>
        </html>"""
    }

    private fun onDataLoaded(newDetail: NewDetail?) {
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.setAppCacheEnabled(true)
        webView.addJavascriptInterface(ImageInterface(), "image")
        webView.loadData(createHtml(newDetail?.post?.content), "text/html; charset=utf-8", "UTF-8")
    }

    private fun onLoadDataFailed(error: String?) {
        errorText.visibility = View.VISIBLE
        errorText.text = getString(R.string.connect_error)
        loadingProgress.visibility = View.GONE
    }

    inner class ImageInterface {

        @JavascriptInterface
        fun onImageClick(picUrl: String) {
            val intent = Intent(this@NewDetailActivity, ImageActivity::class.java)
            intent.putExtra(ImageActivity.INDEX, 0)
            intent.putStringArrayListExtra(ImageActivity.PIC_URLS, arrayListOf(picUrl))
            startActivity(intent)
        }
    }

}