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
import me.zsj.dan.R
import me.zsj.dan.data.Callback
import me.zsj.dan.data.DataManager
import me.zsj.dan.data.ICall
import me.zsj.dan.data.api.DataApi
import me.zsj.dan.model.TucaoData
import me.zsj.dan.ui.adapter.TucaoAdapter
import retrofit2.Call

/**
 * @author zsj
 */
class TucaoActivity : AppCompatActivity(), ICall<TucaoData>, Callback {

    companion object {
        val ID = "id"
    }

    private val recyclerView: RecyclerView by bindView(R.id.comment_list)
    private val refreshLayout: SwipeRefreshLayout by bindView(R.id.swipe_refresh_layout)
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val tipsText: TextView by bindView(R.id.tips_text)

    private lateinit var adapter: TucaoAdapter

    private var comments: ArrayList<TucaoData.Tucao> = ArrayList()

    private lateinit var dataManager: DataManager


    override fun createCall(arg: Any?): Call<TucaoData> {
        return dataManager.createApi(DataApi::class.java)!!.loadTucao(arg as String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tucao)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getStringExtra(ID)

        dataManager = DataManager.get(this)

        refreshLayout.isRefreshing = true
        dataManager.loadData(createCall(id))

        dataManager.setCallback(this)

        refreshLayout.setOnRefreshListener {
            comments.clear()
            dataManager.loadData(createCall(id))
        }

        adapter = TucaoAdapter(this, comments)
        recyclerView.adapter = adapter
    }

    override fun onSuccess(data: Any?) {
        data as TucaoData
        refreshLayout.isRefreshing = false
        if (data.comments != null) {
            if (data.comments.size == 0) {
                tipsText.visibility = View.VISIBLE
            } else {
                tipsText.visibility = View.GONE
            }
            comments.addAll(data.comments)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onFailure(t: Throwable?) {
        refreshLayout.isRefreshing = false
        Toasty.error(this@TucaoActivity, t?.message.toString(), Toast.LENGTH_SHORT).show()
    }
}