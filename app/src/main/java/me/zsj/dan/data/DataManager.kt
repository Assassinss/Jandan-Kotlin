package me.zsj.dan.data

import android.content.Context
import me.zsj.dan.model.Comment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author zsj
 */
class DataManager private constructor(context: Context) : BaseDataManager(context) {

    private val TAG = "DataManager"
    private var loadingCount: AtomicInteger = AtomicInteger(0)
    private var callback: me.zsj.dan.data.Callback? = null
    private var apis: HashMap<Class<out Any>, Any> = HashMap()

    companion object {
        @Volatile private var instance: DataManager? = null

        fun get(context: Context): DataManager = instance ?: build(context).also { instance = it }

        private fun build(context: Context): DataManager = DataManager(context)
    }

    fun setCallback(callback: me.zsj.dan.data.Callback) {
        this.callback = callback
    }

    fun isLoading(): Boolean {
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

    fun <T> loadData(call: Call<T>?): DataManager? {
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

    fun getComments(): ArrayList<Comment> {
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