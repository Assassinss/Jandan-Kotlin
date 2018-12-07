package me.zsj.dan.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.zsj.pretty_girl_kotlin.utils.NetUtils
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author zsj
 */
open class BaseDataManager(context: Context) {

    private val TAG = "BaseDataManager"
    private val HOST = "http://i.jandan.net"

    companion object {
        const val VOTE_POSITIVE_URL = "http://i.jandan.net/index.php?acv_ajax=true&option=1"
        const val VOTE_NEGATIVE_URL = "http://i.jandan.net/index.php?acv_ajax=true&option=0"
        const val VOTE_POSITIVE = true
        const val VOTE_NEGATIVE = false
        private const val CACHE_SIZE: Long = 10 * 1024 * 1024 //10Mb
    }

    private var client: OkHttpClient? = null
    private lateinit var gson: Gson
    var retrofit: Retrofit? = null

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
        if (NetUtils.checkNet(context)) {
            originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=604800")
                    .removeHeader("Pragma")
                    .build()
        } else {
            originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                    .removeHeader("Pragma")
                    .build()
        }
    }

    init {
        if (client == null) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            client = OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .cache(Cache(context.cacheDir, CACHE_SIZE))
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(CACHE_CONTROL_INTERCEPTOR)
                    .build()
            gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .serializeNulls().create()

            retrofit = Retrofit.Builder()
                    .baseUrl(HOST)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build()
        }
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
        return Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
    }

    fun getClient() : OkHttpClient {
        return client!!
    }
}