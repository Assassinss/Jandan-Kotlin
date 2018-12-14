package me.zsj.dan.ui.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotterknife.bindView
import me.zsj.dan.R
import me.zsj.dan.data.Callback
import me.zsj.dan.data.ICall
import me.zsj.dan.data.api.DataApi
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
import retrofit2.Call

/**
 * @author zsj
 */
open class PictureFragment : LazyLoadFragment(), ICall<Picture>, Callback, OnLoadDataListener {

    private val TAG = "PictureFragment"
    private val BORING_CATEGORY = "boring"
    private val MEIZI_CATEGORY = "meizi"

    private val refreshLayout: SwipeRefreshLayout by bindView(R.id.swipe_refresh_layout)
    private val picsList: RecyclerView by bindView(R.id.pic_list)

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
        return inflater!!.inflate(R.layout.fragment_picture, container, false)
    }

    override fun createCall(arg: Any?): Call<Picture> {
        return if (category == BORING_CATEGORY) {
            dataManager.createApi(DataApi::class.java)!!.loadBoringPics(arg as Int)
        } else {
            dataManager.createApi(DataApi::class.java)!!.loadOOXXPics(arg as Int)
        }
    }

    override fun initVariables(bundle: Bundle?) {
        category = bundle?.getString("picture")
        Log.d(TAG, "category: " + category)
    }

    override fun initData() {
        refreshLayout.setColorSchemeColors(getColor(R.color.colorAccent))

        setupRecyclerView()

        refreshLayout.setOnRefreshListener {
            clear = true
            page = 1
            dataManager.loadData(createCall(page))
        }

        dataManager.setCallback(this)

        refreshLayout.isRefreshing = true
        picsList.postDelayed({
            dataManager.loadData(createCall(page))
        }, 350)
    }

    private fun setupRecyclerView() {
        adapter = PictureAdapter(activity!!, items, dataManager)
        adapter.setOnLoadDataListener(this)
        adapter.setRecyclerView(picsList)
        picsList.itemAnimator = NoItemAnimator()
        picsList.itemAnimator?.changeDuration = 0
        picsList.layoutManager = this.layoutManager
        picsList.adapter = adapter

        itemPositionGetter = RecyclerViewItemPositionGetter(layoutManager, picsList)

        listItemVisibilityCalculator = SingleListViewItemActiveCalculator(adapter,
                RecyclerViewItemPositionGetter(layoutManager, picsList))

        picsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
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

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
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
            picsList.postDelayed({
                dataManager.loadData(createCall(page))
            }, 1000)
        }
    }

    private var isLoadMore = false

    override fun onSuccess(data: Any?) {
        val picture = data as Picture
        refreshLayout.isRefreshing = false
        if (clear) {
            items.clear()
        }

        isLoadMore = false

        adapter.onLoadingError(false)
        refreshLayout.isRefreshing = false

        items.addAll(picture.comments)
        dataManager.setComments(items)
        val range = items.size - picture.comments.size
        adapter.notifyItemRangeChanged(items.size - picture.comments.size, range)
    }

    override fun onFailure(t: Throwable?) {
        refreshLayout.isRefreshing = false
        isLoadMore = false
        if (page > 1) page -= 1
        adapter.onLoadingError(true)
    }

    override fun onResume() {
        super.onResume()
        if (!items.isEmpty()) {
            picsList.post {
                listItemVisibilityCalculator.onScrollStateIdle()
            }
        }
    }
}