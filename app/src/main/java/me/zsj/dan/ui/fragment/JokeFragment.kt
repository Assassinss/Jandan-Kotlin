package me.zsj.dan.ui.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.zsj.dan.R
import me.zsj.dan.data.DataCallbackAdapter
import me.zsj.dan.model.Comment
import me.zsj.dan.model.Joke
import me.zsj.dan.ui.adapter.JokeAdapter
import me.zsj.dan.ui.adapter.common.OnLoadDataListener
import me.zsj.dan.utils.NoItemAnimator
import me.zsj.dan.utils.getColor
import me.zsj.dan.utils.recyclerview.RecyclerViewExtensions

/**
 * @author zsj
 */
class JokeFragment : LazyLoadFragment(), RecyclerViewExtensions, OnLoadDataListener {

    private var refreshLayout: SwipeRefreshLayout? = null
    private var recyclerView: RecyclerView? = null

    private lateinit var adapter: JokeAdapter

    private var jokeList: ArrayList<Comment> = ArrayList()
    private var page: Int = 1
    private var clear: Boolean = false

    override fun initViews(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_joke, container, false)
        refreshLayout = view.findViewById(R.id.swipe_refresh_layout) as SwipeRefreshLayout
        recyclerView = view.findViewById(R.id.joke_list) as RecyclerView
        return view
    }

    override fun initData() {
        refreshLayout?.setColorSchemeColors(getColor(R.color.colorAccent))

        setupRecyclerView()

        dataManager.registerDataCallback(object : DataCallbackAdapter() {
            override fun onLoadJokes(joke: Joke?) {
                onDataLoaded(joke)
            }

            override fun onLoadFailed(error: String?) {
                onLoadDataFailed(error)
            }
        })

        refreshLayout?.setOnRefreshListener {
            page = 1
            clear = true
            dataManager.loadJokes(page)
        }

        recyclerView?.postDelayed({
            refreshLayout?.isRefreshing = true
            dataManager.loadJokes(page)
        }, 350)
    }

    private fun setupRecyclerView() {
        adapter = JokeAdapter(activity, jokeList, dataManager)
        adapter.setOnLoadDataListener(this)
        recyclerView?.itemAnimator = NoItemAnimator()
        recyclerView?.adapter = adapter

        recyclerView?.onLoadMore {
            onLoadMoreData()
        }
    }

    override fun onLoadMoreData() {
        if (!dataManager.isLoading() && !isLoadMore) {
            page += 1
            clear = false
            isLoadMore = true
            recyclerView?.postDelayed({
                dataManager.loadJokes(page)
            }, 1000)
        }
    }

    private var isLoadMore = false

    private fun onDataLoaded(joke: Joke?) {
        refreshLayout?.isRefreshing = false
        if (clear) {
            jokeList.clear()
        }

        adapter.onLoadingError(false)
        isLoadMore = false

        if (joke?.comments != null) {
            jokeList.addAll(joke.comments)
            dataManager.setComments(jokeList)
            adapter.notifyItemRangeChanged(jokeList.size - joke.comments.size - 1, jokeList.size)
        }
    }

    private fun onLoadDataFailed(error: String?) {
        isLoadMore = false
        refreshLayout?.isRefreshing = false
        if (page > 1) page -= 1
        adapter.onLoadingError(true)
    }
}