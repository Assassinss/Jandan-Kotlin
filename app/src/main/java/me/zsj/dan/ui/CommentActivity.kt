package me.zsj.dan.ui

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import android.widget.Toast
import es.dmoral.toasty.Toasty
import kotterknife.bindView
import me.drakeet.multitype.Items
import me.drakeet.multitype.MultiTypeAdapter
import me.zsj.dan.R
import me.zsj.dan.binder.CommentBinder
import me.zsj.dan.binder.CommentCategoryBinder
import me.zsj.dan.data.Callback
import me.zsj.dan.data.DataManager
import me.zsj.dan.data.ICall
import me.zsj.dan.data.api.DataApi
import me.zsj.dan.model.NewDetail
import me.zsj.dan.model.PostComment
import retrofit2.Call

/**
 * @author zsj
 */
class CommentActivity : AppCompatActivity(), ICall<NewDetail>, Callback {

    companion object {
        val ID = "id"
    }

    private val recyclerView: RecyclerView by bindView(R.id.comment_list)
    private val refreshLayout: SwipeRefreshLayout by bindView(R.id.swipe_refresh_layout)
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val tipsText: TextView by bindView(R.id.tips_text)

    private lateinit var adapter: MultiTypeAdapter
    private lateinit var dataManager: DataManager
    private var items: Items = Items()


    override fun createCall(arg: Any?): Call<NewDetail> {
        return dataManager.createApi(DataApi::class.java)!!.getComments(arg as String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getStringExtra(ID)

        dataManager = DataManager.get(this)

        dataManager.loadData(createCall(id))

        dataManager.setCallback(this)

        refreshLayout.setOnRefreshListener {
            items.clear()
            dataManager.loadData(createCall(id))
        }

        adapter = MultiTypeAdapter(items)
        adapter.register(String::class.java, CommentCategoryBinder())
        adapter.register(PostComment::class.java, CommentBinder())

        recyclerView.adapter = adapter

        refreshLayout.isRefreshing = true
    }

    override fun onSuccess(data: Any?) {
        val newDetail = data as NewDetail
        refreshLayout.isRefreshing = false
        if (newDetail.post.comments?.size != 0) {
            items.add(getString(R.string.category_comment_rank))
            items.add(PostComment(PostComment.COMMENTS_RANK, newDetail.post))
            items.add(getString(R.string.category_comment_normal))
            items.add(PostComment(PostComment.COMMENTS, newDetail.post))
            adapter.notifyDataSetChanged()
        }

        if (newDetail.post.comments?.size == 0) {
            tipsText.visibility = View.VISIBLE
        } else {
            tipsText.visibility = View.GONE
        }
    }

    override fun onFailure(t: Throwable?) {
        refreshLayout.isRefreshing = false
        Toasty.error(this, t?.message.toString(), Toast.LENGTH_SHORT).show()
    }
}