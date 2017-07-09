package me.zsj.dan.ui

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import android.widget.Toast
import butterknife.bindView
import es.dmoral.toasty.Toasty
import me.zsj.dan.R
import me.zsj.dan.data.DataCallbackAdapter
import me.zsj.dan.data.DataManager
import me.zsj.dan.data.DataManagerFactory
import me.zsj.dan.model.TucaoData

/**
 * @author zsj
 */
class TucaoActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tucao)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getStringExtra(ID)

        dataManager = DataManagerFactory.getInstance(this)

        dataManager.registerDataCallback(object : DataCallbackAdapter() {
            override fun onLoadTucao(tucaoData: TucaoData?) {
                refreshLayout.isRefreshing = false
                if (tucaoData?.comments != null) {
                    if (tucaoData.comments.size == 0) {
                        tipsText.visibility = View.VISIBLE
                    } else {
                        tipsText.visibility = View.GONE
                    }
                    comments.addAll(tucaoData.comments)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onLoadFailed(error: String?) {
                refreshLayout.isRefreshing = false
                Toasty.error(this@TucaoActivity, error!!, Toast.LENGTH_SHORT).show()
            }
        })

        refreshLayout.setOnRefreshListener {
            comments.clear()
            dataManager.loadTucao(id)
        }

        adapter = TucaoAdapter(this, comments)
        recyclerView.adapter = adapter

        refreshLayout.isRefreshing = true
        dataManager.loadTucao(id)
    }

}