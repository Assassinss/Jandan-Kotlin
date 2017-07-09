package me.zsj.dan.data

import android.app.Activity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.zsj.dan.data.api.DataApi
import me.zsj.pretty_girl_kotlin.utils.NetUtils
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author zsj
 */
open class BaseDataManager(context: Activity) {

    private val TAG = "BaseDataManager"
    private val HOST = "http://i.jandan.net"

    companion object {
        val VOTE_POSITIVE_URL = "http://i.jandan.net/index.php?acv_ajax=true&option=1"
        val VOTE_NEGATIVE_URL = "http://i.jandan.net/index.php?acv_ajax=true&option=0"
        val VOTE_POSITIVE = true
        val VOTE_NEGATIVE = false
    }

    private val cacheSize: Long = 10 * 1024 * 1024 // 10Mb
    private var client: OkHttpClient? = null
    private var dataApi: DataApi? = null
    private lateinit var gson: Gson

    //TODO；缓存优化
    private val CACHE_CONTROL_INTERCEPTOR: Interceptor = Interceptor { chain ->
        var request = chain.request()
        if (!NetUtils.checkNet(context)) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
        }
        var originalResponse = chain.proceed(request)
        //504, no http cache
        if (originalResponse.code() == 504) {
            originalResponse = chain.proceed(
                    request.newBuilder()
                            .cacheControl(CacheControl.FORCE_NETWORK)
                            .build())
        }
        originalResponse.newBuilder()
                .header("Cache-Control", "public, If-Modified-Since, max-age=86400, max-stale=2419200")
                .removeHeader("Pragma")
                .build()
    }

    init {
        if (client == null) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
            client = OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .cache(Cache(context.cacheDir, cacheSize))
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(CACHE_CONTROL_INTERCEPTOR)
                    .build()
            gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .serializeNulls().create()
        }
    }

    fun getDataApi() : DataApi {
        if (dataApi == null) return createDataApi()
        return dataApi!!
    }

    fun createDataApi() : DataApi {
        val retrofit = Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        dataApi = retrofit.create(DataApi::class.java) as DataApi
        return dataApi!!
    }

    fun buildRequest(votePositive: Boolean, id: String) : Request {
        val requestBody = if (votePositive) {
            MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("ID", id)
                    .build()
        } else {
            FormBody.Builder()
                    .add("ID", id)
                    .build()
        }
        val url = if (votePositive) VOTE_POSITIVE_URL else VOTE_NEGATIVE_URL
        val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
        return request
    }

    fun getClient() : OkHttpClient {
        return client!!
    }
}