package me.zsj.dan.ui.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.dmoral.toasty.Toasty
import me.zsj.dan.R
import me.zsj.dan.data.DataCallbackAdapter
import me.zsj.dan.model.FreshNew
import me.zsj.dan.model.Post
import me.zsj.dan.ui.adapter.FreshNewsAdapter
import me.zsj.dan.utils.ItemDivider
import me.zsj.dan.utils.getColor
import me.zsj.dan.utils.recyclerview.RecyclerViewExtensions

/**
 * @author zsj
 */
class FreshNewsFragment : LazyLoadFragment(), RecyclerViewExtensions {

    private val TAG = "FreshNewsFragment"

    private var refreshLayout: SwipeRefreshLayout? = null
    private var newsList: RecyclerView? = null

    private lateinit var newsAdapter: FreshNewsAdapter
    private var freshNewsList: ArrayList<Post> = ArrayList()
    private var page: Int = 1
    private var clear: Boolean = false

    override fun initViews(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_fresh_news, container, false)
        refreshLayout = view.findViewById(R.id.swipe_refresh_layout) as SwipeRefreshLayout
        newsList = view.findViewById(R.id.news_list) as RecyclerView
        return view
    }

    override fun initData() {
        refreshLayout?.setColorSchemeColors(getColor(R.color.colorAccent))

        dataManager.registerDataCallback(object : DataCallbackAdapter() {
            override fun onLoadFreshNews(freshNew: FreshNew?) {
                onLoadData(freshNew)
            }

            override fun onLoadFailed(error: String?) {
                onLoadDataFailed(error)
            }
        })

        setupRecyclerView()

        refreshLayout?.setOnRefreshListener {
            page = 1
            clear = true
            dataManager.loadFreshNews(page)
        }

        newsList?.postDelayed({
            refreshLayout?.isRefreshing = true
            dataManager.loadFreshNews(page)
        }, 350)
    }

    private fun setupRecyclerView() {
        newsAdapter = FreshNewsAdapter(activity, freshNewsList)
        newsList?.addItemDecoration(ItemDivider(activity, ItemDivider.VERTICAL_LIST))
        newsList?.adapter = newsAdapter
        newsList?.onLoadMore {
            Log.d(TAG, "loading status: " + dataManager.isLoading())
            if (!dataManager.isLoading()) {
                page += 1
                clear = false
                newsList?.postDelayed({
                    dataManager.loadFreshNews(page)
                }, 500)
            }
        }
    }

    private fun onLoadData(freshNew: FreshNew?) {
        if (clear) {
            freshNewsList.clear()
        }
        refreshLayout?.isRefreshing = false
        if (freshNew?.posts != null) {
            freshNewsList.addAll(freshNew.posts)
        }
        newsAdapter.notifyDataSetChanged()
    }

    private fun onLoadDataFailed(error: String?) {
        refreshLayout?.isRefreshing = false
        if (page > 1) page -= 1
        Toasty.error(activity, error!!).show()
    }

}