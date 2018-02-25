package me.zsj.dan.data

import android.app.Activity
import me.zsj.dan.model.Comment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author zsj
 */
class DataManager(context: Activity) : BaseDataManager(context) {

    private val TAG = "DataManager"
    private var loadingCount: AtomicInteger = AtomicInteger(0)
    private var callback: me.zsj.dan.data.Callback? = null
    private var apis: HashMap<Class<out Any>, Any> = HashMap()


    fun setCallback(callback: me.zsj.dan.data.Callback) {
        this.callback = callback
    }

    fun isLoading() : Boolean {
        return loadingCount.get() > 0
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> createApi(clazz: Class<out T>): T? {
        if (!apis.containsKey(clazz)) {
            val api = retrofit?.create(clazz)
            apis.put(clazz, api as Any)
        }
        return apis[clazz] as T
    }

    fun <T> loadData(call: Call<T>?) : DataManager? {
        dispatchLoadingStart()
        call?.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>?, response: Response<T>?) {
                dispatchLoadingFinish()
                if (response != null && response.isSuccessful) {
                    callback?.onSuccess(response.body())
                }
            }

            override fun onFailure(call: Call<T>?, t: Throwable?) {
                dispatchLoadingFinish()
                callback?.onFailure(t)
            }
        })

        return this
    }

    private var comments: ArrayList<Comment>? = null

    fun getComments() : ArrayList<Comment> {
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