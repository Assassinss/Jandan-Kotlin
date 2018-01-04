package me.zsj.dan.ui.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.zsj.dan.R
import me.zsj.dan.data.DataCallbackAdapter
import me.zsj.dan.model.Comment
import me.zsj.dan.model.Picture
import me.zsj.dan.ui.adapter.PictureAdapter
import me.zsj.dan.ui.adapter.common.OnLoadDataListener
import me.zsj.dan.utils.NoItemAnimator
import me.zsj.dan.utils.getColor
import me.zsj.dan.visibility.calculator.ListItemsVisibilityCalculator
import me.zsj.dan.visibility.calculator.SingleListViewItemActiveCalculator
import me.zsj.dan.visibility.scroll_utils.ItemsPositionGetter
import me.zsj.dan.visibility.scroll_utils.RecyclerViewItemPositionGetter

/**
 * @author zsj
 */
open class PictureFragment : LazyLoadFragment(), OnLoadDataListener {

    private val TAG = "PictureFragment"
    private val GIF_TAG = ".gif"
    private val BORING_CATEGORY = "boring"
    private val MEIZI_CATEGORY = "meizi"

    private var refreshLayout: SwipeRefreshLayout? = null
    private var picsList: RecyclerView? = null

    private lateinit var adapter: PictureAdapter
    private val items: ArrayList<Comment> = ArrayList()
    private var layoutManager: LinearLayoutManager = LinearLayoutManager(activity)

    private lateinit var listItemVisibilityCalculator: ListItemsVisibilityCalculator
    private var itemPositionGetter: ItemsPositionGetter? = null
    private var mScrollState: Int = 0

    private var category: String? = null
    private var page: Int = 1
    private var clear: Boolean = false

    override fun initViews(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_picture, container, false)
        refreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        picsList = view.findViewById(R.id.pic_list)
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
        adapter = PictureAdapter(activity, items, dataManager)
        adapter.setOnLoadDataListener(this)
        adapter.setRecyclerView(picsList!!)
        picsList?.itemAnimator = NoItemAnimator()
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
                if (layoutManager.findLastVisibleItemPosition() >= layoutManager.itemCount - 1
                        && layoutManager.findFirstVisibleItemPosition() != 0) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && !isLoadMore) {
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

    override fun onLoadMoreData() {
        loadMoreData()
    }

    private fun loadMoreData() {
        if (!dataManager.isLoading()) {
            clear = false
            page += 1
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

    private fun onDataLoaded(picture: Picture?) {
        if (clear) {
            items.clear()
        }

        isLoadMore = false

        adapter.onLoadingError(false)
        refreshLayout?.isRefreshing = false
        if (picture?.comments != null) {
            items.addAll(picture.comments)
            dataManager.setComments(items)
            adapter.notifyItemRangeChanged(items.size - picture.comments.size- 1, items.size)
        }
    }

    private fun onLoadDataFailed(error: String?) {
        refreshLayout?.isRefreshing = false
        isLoadMore = false
        if (page > 1) page -= 1
        adapter.onLoadingError(true)
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