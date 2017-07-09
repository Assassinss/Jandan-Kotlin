package me.zsj.dan.ui.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.dmoral.toasty.Toasty
import me.drakeet.multitype.Items
import me.zsj.dan.R
import me.zsj.dan.binder.GifPictureBinder
import me.zsj.dan.binder.LoadingViewBinder
import me.zsj.dan.binder.MultiPictureBinder
import me.zsj.dan.binder.SinglePictureBinder
import me.zsj.dan.data.DataCallbackAdapter
import me.zsj.dan.model.Comment
import me.zsj.dan.model.Picture
import me.zsj.dan.utils.getColor
import me.zsj.dan.visibility.calculator.ListItemsVisibilityCalculator
import me.zsj.dan.visibility.calculator.SingleListViewItemActiveCalculator
import me.zsj.dan.visibility.scroll_utils.ItemsPositionGetter
import me.zsj.dan.visibility.scroll_utils.RecyclerViewItemPositionGetter

/**
 * @author zsj
 */
open class PictureFragment : LazyLoadFragment() {

    private val TAG = "PictureFragment"
    private val GIF_TAG = ".gif"
    private val BORING_CATEGORY = "boring"
    private val MEIZI_CATEGORY = "meizi"

    private var refreshLayout: SwipeRefreshLayout? = null
    private var picsList: RecyclerView? = null

    private lateinit var adapter: PictureAdapter
    //private val items: ArrayList<Comment> = ArrayList()
    private val items: Items = Items()
    private var layoutManager: LinearLayoutManager = LinearLayoutManager(activity)

    private lateinit var listItemVisibilityCalculator: ListItemsVisibilityCalculator
    private var itemPositionGetter: ItemsPositionGetter? = null
    private var mScrollState: Int = 0

    private var category: String? = null
    private var page: Int = 1
    private var clear: Boolean = false

    override fun initViews(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_picture, container, false)
        refreshLayout = view.findViewById(R.id.swipe_refresh_layout) as SwipeRefreshLayout
        picsList = view.findViewById(R.id.pic_list) as RecyclerView
        return view
    }

    override fun initVariables(bundle: Bundle?) {
        category = bundle?.getString("picture")
        Log.d(TAG, "category: " + category)
    }

    override fun initData() {
        refreshLayout?.setColorSchemeColors(getColor(R.color.colorAccent))

        setupRecyclerView()

        refreshLayout?.setOnRefreshListener {
            clear = true
            page = 1
            if (category.equals(BORING_CATEGORY)) {
                dataManager.loadBoringPics(page)
            } else if (category.equals(MEIZI_CATEGORY)) {
                dataManager.loadOOXXPics(page)
            }
        }

        dataManager.registerDataCallback(object : DataCallbackAdapter() {
            override fun onLoadBoringPics(picture: Picture?) {
                onDataLoaded(picture)
            }

            override fun onLoadFailed(error: String?) {
                onLoadDataFailed(error)
            }
        })

        picsList?.postDelayed({
            refreshLayout?.isRefreshing = true
            if (category.equals(BORING_CATEGORY)) {
                dataManager.loadBoringPics(page)
            } else if (category.equals(MEIZI_CATEGORY)) {
                dataManager.loadOOXXPics(page)
            }
        }, 350)
    }

    private fun setupRecyclerView() {
        adapter = PictureAdapter(items)
        adapter.setRecyclerView(picsList)
        adapter.register(String::class.java, LoadingViewBinder())
        adapter.register(Comment::class.java).to(
                SinglePictureBinder(activity, dataManager),
                GifPictureBinder(activity, dataManager),
                MultiPictureBinder(activity, dataManager)
        ).withClassLinker { comment ->
            if (comment.pics?.size == 1) {
                if (comment.pics[0].endsWith(GIF_TAG)) {
                    GifPictureBinder::class.java
                } else {
                    SinglePictureBinder::class.java
                }
            } else {
                MultiPictureBinder::class.java
            }
        }
        picsList?.layoutManager = this.layoutManager
        picsList?.adapter = adapter

        itemPositionGetter = RecyclerViewItemPositionGetter(layoutManager, picsList)

        listItemVisibilityCalculator = SingleListViewItemActiveCalculator(adapter,
                RecyclerViewItemPositionGetter(layoutManager, picsList))

        picsList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                mScrollState = newState
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !items.isEmpty()) {
                    listItemVisibilityCalculator.onScrollStateIdle()
                }
                if (layoutManager.findFirstVisibleItemPosition() >= layoutManager.itemCount - 2
                        && layoutManager.findFirstVisibleItemPosition() != 0) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        loadMoreData()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (!items.isEmpty()) {
                    listItemVisibilityCalculator.onScrolled(mScrollState)
                }
            }
        })
    }

    private fun loadMoreData() {
        if (!dataManager.isLoading()) {
            clear = false
            page += 1
            showLoadMore()
            isLoadMore = true
            picsList?.postDelayed({
                if (category.equals(BORING_CATEGORY)) {
                    dataManager.loadBoringPics(page)
                } else if (category.equals(MEIZI_CATEGORY)) {
                    dataManager.loadOOXXPics(page)
                }
            }, 1000)
        }
    }

    private var isLoadMore = false

    private fun showLoadMore() {
        if (!isLoadMore && items.size > 0) {
            items.add("LOAD")
            adapter.notifyItemChanged(items.size - 1)
            picsList?.scrollToPosition(items.size - 1)
        }
    }

    private fun hideLoadMore() {
        if (items.size > 1) {
            isLoadMore = false
            items.removeAt(items.size - 1)
            adapter.notifyItemChanged(items.size - 1)
        }
    }

    private fun onDataLoaded(picture: Picture?) {
        if (clear) {
            items.clear()
        }

        hideLoadMore()

        refreshLayout?.isRefreshing = false
        if (picture?.comments != null) {
            items.addAll(picture.comments)
        }
        dataManager.setComments(items)
        adapter.notifyDataSetChanged()
    }

    private fun onLoadDataFailed(error: String?) {
        refreshLayout?.isRefreshing = false
        if (page > 1) page -= 1
        Toasty.error(activity, error!!).show()
    }

    override fun onResume() {
        super.onResume()
        if (!items.isEmpty()) {
            picsList?.post {
                listItemVisibilityCalculator.onScrollStateIdle()
            }
        }
    }
}