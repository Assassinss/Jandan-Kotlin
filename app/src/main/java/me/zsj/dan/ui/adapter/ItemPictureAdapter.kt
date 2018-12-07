package me.zsj.dan.ui.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import kotterknife.bindView
import me.zsj.dan.R
import me.zsj.dan.ui.ImageActivity
import me.zsj.dan.widget.RatioImageView

/**
 * @author zsj
 */
class ItemPictureAdapter : RecyclerView.Adapter<ItemPictureAdapter.ItemPictureHolder>() {

    private val GIF_TAG = ".gif"

    private var pics: ArrayList<String> = ArrayList()

    fun setPics(pics: ArrayList<String>) {
        this.pics = pics
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPictureHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_picture, parent, false)
        return ItemPictureHolder(view)
    }

    override fun onBindViewHolder(holder: ItemPictureHolder, position: Int) {
        val context = holder.picture.context
        val picUrl = pics[position]
        holder.picture.setOriginalSize(1, 1)
        if (picUrl.endsWith(GIF_TAG)) {
            holder.gifTag.visibility = View.VISIBLE
        } else {
            holder.gifTag.visibility = View.GONE
        }

        Glide.with(context)
                .load(picUrl)
                .asBitmap()
                .into(holder.picture)

        holder.picture.setOnClickListener {
            val intent = Intent(context, ImageActivity::class.java)
            intent.putExtra(ImageActivity.INDEX, position)
            intent.putStringArrayListExtra(ImageActivity.PIC_URLS, pics)
            context!!.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return pics.size
    }

    inner class ItemPictureHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gifTag: ImageView by bindView(R.id.gif_tag)
        val picture: RatioImageView by bindView(R.id.picture)
    }
}