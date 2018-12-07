package me.zsj.dan.ui.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.zsj.dan.R
import me.zsj.dan.data.Callback
import me.zsj.dan.data.ICall
import me.zsj.dan.data.api.DataApi
import me.zsj.dan.model.Comment
import me.zsj.dan.model.Joke
import me.zsj.dan.ui.adapter.JokeAdapter
import me.zsj.dan.ui.adapter.common.OnLoadDataListener
import me.zsj.dan.utils.getColor
import me.zsj.dan.utils.recyclerview.RecyclerViewExtensions
import retrofit2.Call

/**
 * @author zsj
 */
class JokeFragment : LazyLoadFragment(), ICall<Joke>, Callback,
        RecyclerViewExtensions, OnLoadDataListener {

    private var refreshLayout: SwipeRefreshLayout? = null
    private var recyclerView: RecyclerView? = null

    private lateinit var adapter: JokeAdapter

    private var jokeList: ArrayList<Comment> = ArrayList()
    private var page: Int = 1
    private var clear: Boolean = false

    override fun initViews(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_joke, container, false)
        refreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        recyclerView = view.findViewById(R.id.joke_list)
        return view
    }

    override fun createCall(arg: Any?): Call<Joke> {
        return dataManager.createApi(DataApi::class.java)!!.loadJokes(arg as Int)
    }

    override fun initData() {
        refreshLayout?.setColorSchemeColors(getColor(R.color.colorAccent))

        setupRecyclerView()

        dataManager.setCallback(this)

        refreshLayout?.setOnRefreshListener {
            page = 1
            clear = true
            dataManager.loadData(createCall(page))
        }

        refreshLayout?.isRefreshing = true
        recyclerView?.postDelayed({
            dataManager.loadData(createCall(page))
        }, 350)
    }

    private fun setupRecyclerView() {
        adapter = JokeAdapter(activity!!, jokeList, dataManager)
        adapter.setOnLoadDataListener(this)
        recyclerView?.itemAnimator?.changeDuration = 0
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
                dataManager.loadData(createCall(page))
            }, 1000)
        }
    }

    private var isLoadMore = false

    override fun onSuccess(data: Any?) {
        val joke = data as Joke
        refreshLayout?.isRefreshing = false
        if (clear) {
            jokeList.clear()
        }

        adapter.onLoadingError(false)
        isLoadMore = false

        jokeList.addAll(joke.comments)
        dataManager.setComments(jokeList)
        adapter.notifyItemRangeChanged(jokeList.size - joke.comments.size - 1, jokeList.size)
    }

    override fun onFailure(t: Throwable?) {
        isLoadMore = false
        refreshLayout?.isRefreshing = false
        if (page > 1) page -= 1
        adapter.onLoadingError(true)
    }
}