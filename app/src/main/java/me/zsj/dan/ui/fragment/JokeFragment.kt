package me.zsj.dan.ui.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.dmoral.toasty.Toasty
import me.drakeet.multitype.Items
import me.drakeet.multitype.MultiTypeAdapter
import me.zsj.dan.R
import me.zsj.dan.binder.JokeViewBinder
import me.zsj.dan.binder.LoadingViewBinder
import me.zsj.dan.data.DataCallbackAdapter
import me.zsj.dan.model.Comment
import me.zsj.dan.model.Joke
import me.zsj.dan.utils.getColor
import me.zsj.dan.utils.recyclerview.RecyclerViewExtensions

/**
 * @author zsj
 */
class JokeFragment : LazyLoadFragment(), RecyclerViewExtensions {

    private var refreshLayout: SwipeRefreshLayout? = null
    private var recyclerView: RecyclerView? = null

    private lateinit var adapter: MultiTypeAdapter

    //private var jokeList: ArrayList<Comment> = ArrayList()
    private var items: Items = Items()
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
        adapter = MultiTypeAdapter(items)
        adapter.register(String::class.java, LoadingViewBinder())
        adapter.register(Comment::class.java, JokeViewBinder(activity, dataManager))

        recyclerView?.adapter = adapter

        recyclerView?.onLoadMore(4) {
            if (!dataManager.isLoading()) {
                page += 1
                clear = false
                showLoadMore()
                isLoadMore = true
                recyclerView?.postDelayed({
                    dataManager.loadJokes(page)
                }, 1000)
            }
        }
    }

    private var isLoadMore = false

    private fun showLoadMore() {
        if (!isLoadMore && items.size > 0) {
            items.add("LOAD")
            adapter.notifyItemChanged(items.size - 1)
            recyclerView?.scrollToPosition(items.size - 1)
        }
    }

    private fun hideLoadMore() {
        if (items.size > 1) {
            isLoadMore = false
            items.removeAt(items.size - 1)
            adapter.notifyItemChanged(items.size - 1)
        }
    }

    private fun onDataLoaded(joke: Joke?) {
        refreshLayout?.isRefreshing = false
        if (clear) {
            items.clear()
        }

        hideLoadMore()

        if (joke?.comments != null) {
            items.addAll(joke.comments)
        }
        dataManager.setComments(items)
        adapter.notifyDataSetChanged()
    }

    private fun onLoadDataFailed(error: String?) {
        refreshLayout?.isRefreshing = false
        if (page > 1) page -= 1
        Toasty.error(activity, error!!).show()
    }
}