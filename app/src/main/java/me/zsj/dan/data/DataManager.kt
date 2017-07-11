package me.zsj.dan.data

import android.app.Activity
import android.util.Log
import me.drakeet.multitype.Items
import me.zsj.dan.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author zsj
 */
class DataManager(context: Activity) : BaseDataManager(context) {

    private val TAG = "DataManager"
    private lateinit var dataCallback: DataCallback
    private var loadingCount: AtomicInteger = AtomicInteger(0)

    fun registerDataCallback(dataCallback: DataCallback) {
        this.dataCallback = dataCallback
    }

    fun isLoading() : Boolean {
        return loadingCount.get() > 0
    }

    fun loadFreshNews(page: Int) {
        val call = getDataApi().loadFreshNews(page)
        dispatchLoadingStart()
        call.enqueue(object : Callback<FreshNew> {
            override fun onFailure(call: Call<FreshNew>?, t: Throwable?) {
                dispatchLoadingFinish()
                dataCallback.onLoadFailed(t!!.message)
                Log.e(TAG, "failed to load news: " + t.message, t)
            }

            override fun onResponse(call: Call<FreshNew>?, response: Response<FreshNew>?) {
                dispatchLoadingFinish()
                if (response!!.isSuccessful) {
                    Log.d(TAG, "on loaded fresh news success.")
                    dataCallback.onLoadFreshNews(response.body())
                }
            }
        })
    }

    fun loadBoringPics(page: Int) {
        val call = getDataApi().loadBoringPics(page)
        dispatchLoadingStart()
        call.enqueue(object : Callback<Picture> {
            override fun onResponse(call: Call<Picture>?, response: Response<Picture>?) {
                dispatchLoadingFinish()
                if (response!!.isSuccessful) {
                    Log.d(TAG, "loading boring pics success.")
                    dataCallback.onLoadBoringPics(response.body())
                }
            }

            override fun onFailure(call: Call<Picture>?, t: Throwable?) {
                dispatchLoadingFinish()
                dataCallback.onLoadFailed(t!!.message)
                Log.e(TAG, "failed to load boring pics.", t)
            }

        })
    }

    fun loadOOXXPics(page: Int) {
        val call = getDataApi().loadOOXXPics(page)
        dispatchLoadingStart()
        call.enqueue(object : Callback<Picture> {
            override fun onResponse(call: Call<Picture>?, response: Response<Picture>?) {
                dispatchLoadingFinish()
                if (response!!.isSuccessful) {
                    Log.d(TAG, "loading ooxx pics success.")
                    dataCallback.onLoadBoringPics(response.body())
                }
            }

            override fun onFailure(call: Call<Picture>?, t: Throwable?) {
                dispatchLoadingFinish()
                dataCallback.onLoadFailed(t!!.message)
                Log.e(TAG, "failed to load ooxx pics.", t)
            }

        })
    }

    fun loadJokes(page: Int) {
        val call = getDataApi().loadJokes(page)
        dispatchLoadingStart()
        call.enqueue(object : Callback<Joke> {
            override fun onFailure(call: Call<Joke>?, t: Throwable?) {
                dispatchLoadingFinish()
                dataCallback.onLoadFailed(t!!.message)
                Log.e(TAG, "failed to load joke.", t)
            }

            override fun onResponse(call: Call<Joke>?, response: Response<Joke>?) {
                dispatchLoadingFinish()
                if (response!!.isSuccessful) {
                    Log.d(TAG, "loading joke success.")
                    dataCallback.onLoadJokes(response.body())
                }
            }
        })
    }

    fun loadNewDetail(id: String) {
        val call = getDataApi().getNewDetail(id)
        call.enqueue(object : Callback<NewDetail> {
            override fun onResponse(call: Call<NewDetail>?, response: Response<NewDetail>?) {
                if (response!!.isSuccessful) {
                    Log.d(TAG, "loading new detail success.")
                    dataCallback.onLoadNewDetail(response.body())
                }
            }

            override fun onFailure(call: Call<NewDetail>?, t: Throwable?) {
                dataCallback.onLoadFailed(t!!.message)
                Log.e(TAG, "failed to load new detail.", t)
            }
        })
    }

    fun loadComments(id: String) {
        val call = getDataApi().getComments(id)
        call.enqueue(object : Callback<NewDetail> {
            override fun onResponse(call: Call<NewDetail>?, response: Response<NewDetail>?) {
                if (response!!.isSuccessful) {
                    Log.d(TAG, "loading comments success.")
                    dataCallback.onLoadNewDetail(response.body())
                }
            }

            override fun onFailure(call: Call<NewDetail>?, t: Throwable?) {
                dataCallback.onLoadFailed(t!!.message)
                Log.e(TAG, "failed to load comments.", t)
            }
        })
    }

    fun loadTucao(id: String) {
        val call = getDataApi().loadTucao(id)
        call.enqueue(object : Callback<TucaoData> {
            override fun onResponse(call: Call<TucaoData>?, response: Response<TucaoData>?) {
                if (response!!.isSuccessful) {
                    Log.d(TAG, "loading comments success.")
                    dataCallback.onLoadTucao(response.body())
                }
            }

            override fun onFailure(call: Call<TucaoData>?, t: Throwable?) {
                dataCallback.onLoadFailed(t!!.message)
                Log.e(TAG, "failed to load comments.", t)
            }
        })
    }

    private var comments: ArrayList<Comment>? = null

    fun getCommnets() : ArrayList<Comment> {
        return comments!!
    }

    fun setComments(comments: ArrayList<Comment>) {
        this.comments = comments
    }

    fun dispatchLoadingStart() {
        loadingCount.getAndIncrement()
    }

    fun dispatchLoadingFinish() {
        loadingCount.decrementAndGet()
    }

}