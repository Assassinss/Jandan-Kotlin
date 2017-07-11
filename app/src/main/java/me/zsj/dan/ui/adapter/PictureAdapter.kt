package me.zsj.dan.ui.adapter

import android.app.Activity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.bindView
import me.zsj.dan.R
import me.zsj.dan.binder.Holder
import me.zsj.dan.data.DataManager
import me.zsj.dan.model.Comment
import me.zsj.dan.ui.adapter.common.LoadingHolder
import me.zsj.dan.visibility.items.ListItem
import me.zsj.dan.visibility.scroll_utils.ItemsProvider
import me.zsj.dan.widget.GifRatioScaleImageView
import me.zsj.dan.widget.RatioScaleImageView
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

/**
 * @author zsj
 */
class PictureAdapter(var context: Activity, var comments: ArrayList<Comment>,
                     var dataManager: DataManager) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemsProvider, ItemBinder.OnVoteListener {

    private val GIF_TAG = ".gif"
    private var recyclerView: RecyclerView? = null
    private var singleItemBinder: SingleItemBinder? = null
    private var gifItemBinder: GifItemBinder? = null
    private var multiItemBinder: MultiItemBinder? = null

    init {
        singleItemBinder = SingleItemBinder(dataManager)
        singleItemBinder?.setOnVoteListener(this)
        gifItemBinder = GifItemBinder(dataManager)
        gifItemBinder?.setOnVoteListener(this)
        multiItemBinder = MultiItemBinder(dataManager)
        multiItemBinder?.setOnVoteListener(this)
    }

    override fun onVoteOO(holder: Holder, result: String?) {
        if (holder is SingleHolder) {
            singleItemBinder?.updateVotePositive(context, holder, result)
        } else if (holder is GifHolder) {
            gifItemBinder?.updateVotePositive(context, holder, result)
        } else if (holder is MultiHolder) {
            multiItemBinder?.updateVotePositive(context, holder, result)
        }
    }

    override fun onVoteXX(holder: Holder, result: String?) {
        if (holder is SingleHolder) {
            singleItemBinder?.updateVoteNegative(context, holder, result)
        } else if (holder is GifHolder) {
            gifItemBinder?.updateVoteNegative(context, holder, result)
        } else if (holder is MultiHolder) {
            multiItemBinder?.updateVoteNegative(context, holder, result)
        }
    }

    override fun getListItem(position: Int): ListItem? {
        val holder = recyclerView?.findViewHolderForAdapterPosition(position)
        if (holder is ListItem) {
            return holder
        }
        return null
    }

    override fun listItemSize(): Int {
        return itemCount
    }

    fun setRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        val inflater = LayoutInflater.from(context)
        if (viewType == R.layout.item_load_more) {
            val view = inflater.inflate(R.layout.item_load_more, parent, false)
            return LoadingHolder(view)
        } else if (viewType == R.layout.item_single_pic) {
            val view = inflater.inflate(R.layout.item_single_pic, parent, false)
            return SingleHolder(view)
        } else if (viewType == R.layout.item_gif_pic) {
            val view = inflater.inflate(R.layout.item_gif_pic, parent, false)
            return GifHolder(view)
        } else if (viewType == R.layout.item_multi_pic) {
            val view = inflater.inflate(R.layout.item_multi_pic, parent, false)
            return MultiHolder(view)
        }
        return null
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (getItemViewType(position) == R.layout.item_load_more) {
            holder as LoadingHolder
            if (itemCount == 1) {
                holder.progressBar.visibility = View.GONE
            } else {
                holder.progressBar.visibility = View.VISIBLE
            }
        } else if (getItemViewType(position) == R.layout.item_single_pic) {
            val comment = comments[position]
            holder as SingleHolder
            singleItemBinder?.bindData(context, holder, comment)
        } else if (getItemViewType(position) == R.layout.item_gif_pic) {
            val comment = comments[position]
            holder as GifHolder
            gifItemBinder?.bindData(context, holder, comment)
        } else {
            val comment = comments[position]
            holder as MultiHolder
            multiItemBinder?.bindData(context, holder, comment)
        }
    }

    override fun getItemCount(): Int {
        var itemCount = 1
        if (comments.size > 0) {
            itemCount += comments.size
        }
        return itemCount
    }

    override fun getItemViewType(position: Int): Int {
        if (position + 1 == itemCount) {
            return R.layout.item_load_more
        } else {
            val comment = comments[position]
            if (comment.pics?.size == 1) {
                if (comment.pics[0].endsWith(GIF_TAG)) {
                    return R.layout.item_gif_pic
                } else {
                    return R.layout.item_single_pic
                }
            } else {
                return R.layout.item_multi_pic
            }
        }
    }

    inner class SingleHolder(itemView: View) : Holder(itemView) {
        val card: CardView by bindView(R.id.card)
        val textContent: TextView by bindView(R.id.text_content)
        val picture: RatioScaleImageView by bindView(R.id.picture)
        val browsePicture: TextView by bindView(R.id.browse_big_picture)
    }

    inner class GifHolder(itemView: View) : Holder(itemView), ListItem {
        val card: CardView by bindView(R.id.card)
        val gifImage: GifRatioScaleImageView by bindView(R.id.gif_picture)
        val playGif: ImageView by bindView(R.id.play_gif)
        val loadingProgress: ProgressBar by bindView(R.id.loading_progress)
        val textContent: TextView by bindView(R.id.text_content)

        override fun setActive(newActiveView: View?, newActiveViewPosition: Int) {
        }

        //TODO: 取消图片下载请求
        override fun deactivate(currentView: View?, position: Int) {
            val gifImage = currentView?.findViewById(R.id.gif_picture)
            val playGif = currentView?.findViewById(R.id.play_gif)
            val loadingProgress = currentView?.findViewById(R.id.loading_progress)
            if (gifImage != null && playGif != null && loadingProgress != null) {
                gifImage as GifImageView
                playGif as ImageView
                loadingProgress as ProgressBar
                val gifDrawable = gifImage.drawable
                if (gifDrawable is GifDrawable) {
                    playGif.visibility = View.VISIBLE
                    loadingProgress.visibility = View.GONE
                    gifItemBinder?.stopGifAnimation(gifImage.drawable as GifDrawable)
                    notifyItemChanged(position)
                }
            }
        }
    }

    inner class MultiHolder(itemView: View) : Holder(itemView) {
        val card: CardView by bindView(R.id.card)
        val textContent: TextView by bindView(R.id.text_content)
        val recyclerView: RecyclerView by bindView(R.id.pic_item_list)
        private var pictureAdapter: ItemPictureAdapter? = null

        init {
            pictureAdapter = ItemPictureAdapter()
            recyclerView.adapter = pictureAdapter
        }

        fun setPictures(pics: ArrayList<String>) {
            pictureAdapter!!.setPics(pics)
            pictureAdapter?.notifyDataSetChanged()
        }
    }
}