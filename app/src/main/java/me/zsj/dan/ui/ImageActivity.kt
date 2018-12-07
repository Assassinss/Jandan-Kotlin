package me.zsj.dan.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotterknife.bindView
import me.zsj.dan.R
import me.zsj.dan.data.DownloadExecutors
import me.zsj.dan.utils.FileUtils
import me.zsj.dan.utils.StringUtils
import me.zsj.dan.utils.shortToast
import java.io.File

/**
 * @author zsj
 */
class ImageActivity : AppCompatActivity(), View.OnClickListener {

    private val REQUEST_WRITE_PERMISSION_CODE = 10007

    companion object {
        val PIC_URLS = "picUrls"
        val INDEX = "index"
    }

    private val viewPager: ViewPager by bindView(R.id.viewPager)
    private val picPage: TextView by bindView(R.id.pic_page)
    private val download: ImageView by bindView(R.id.download)
    private val share: ImageView by bindView(R.id.share)

    private var picUrls: ArrayList<String>? = null
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        download.setOnClickListener(this)
        share.setOnClickListener(this)

        picUrls = intent.getStringArrayListExtra("picUrls")
        index = intent.getIntExtra("index", 0)

        if (picUrls!!.size == 0) {
            picPage.visibility = View.GONE
        }

        picPage.text = getString(R.string.pic_page, (index + 1).toString(),
                picUrls!!.size.toString())

        val adapter = ImagePagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        viewPager.currentItem = index
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                picPage.text = getString(R.string.pic_page,
                        (position + 1).toString(), picUrls!!.size.toString())
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.download -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            REQUEST_WRITE_PERMISSION_CODE)
                } else {
                    downloadImage()
                }
            }
            R.id.share -> {
                shareImage()
            }
        }
    }

    private fun shareImage() {
        val picUrl = picUrls!![viewPager.currentItem]
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, picUrl)
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, "发送到"))
    }

    private fun downloadImage() {
        val picUrl = picUrls!![viewPager.currentItem]
        val file = FileUtils.mkdirsIfNotExists()
        if (file != null) {
            val picFile = File(file, StringUtils.makeFileName(picUrl))
            if (picFile.exists()) {
                notifyScanFile(picFile)
            } else {
                saveImage(picFile, picUrl)
            }
        }
    }

    private fun saveImage(to: File, picUrl: String) {
        DownloadExecutors.get().downloadImage(this, picUrl) {
            val file = FileUtils.copy(it, to)
            if (file != null) {
                notifyScanFile(file)
            }
        }
    }

    private fun notifyScanFile(file: File) {
        MediaScannerConnection.scanFile(applicationContext, arrayOf(file.path), null, null)
        Snackbar.make(viewPager, "图片保存到" + file.path + "目录下", Snackbar.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_WRITE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadImage()
            } else {
                shortToast("保存图片或者GIF需要用到读写权限，应用本身不会收集手机的信息，只是用来保存图片而已")
            }
        }
    }

    inner class ImagePagerAdapter(sm: FragmentManager) : FragmentStatePagerAdapter(sm) {

        override fun getItem(position: Int): Fragment {
            val picUrl = picUrls!![position]
            val bundle = Bundle()
            val fragment = ImageFragment()
            bundle.putString("picUrl", picUrl)
            fragment.arguments = bundle
            return fragment
        }

        override fun getCount(): Int {
            return picUrls!!.size
        }

    }

}