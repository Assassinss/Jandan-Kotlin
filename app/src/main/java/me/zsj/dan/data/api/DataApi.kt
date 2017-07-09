package me.zsj.dan.data.api

import me.zsj.dan.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author zsj
 */
interface DataApi {

    @GET("/?oxwlxojflwblxbsapi=get_recent_post&include=url,date,tags,author,title,excerpt,comment_count,comment_status,custom_fields&custom_fields=thumb_c,views")
    fun loadFreshNews(@Query("page") page: Int) : Call<FreshNew>

    @GET("/?oxwlxojflwblxbsapi=jandan.get_pic_comments")
    fun loadBoringPics(@Query("page") page: Int) : Call<Picture>

    @GET("/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments")
    fun loadOOXXPics(@Query("page") page: Int) : Call<Picture>

    @GET("/?oxwlxojflwblxbsapi=jandan.get_duan_comments")
    fun loadJokes(@Query("page") page: Int) : Call<Joke>

    @GET("/?oxwlxojflwblxbsapi=get_post&include=id,type,slug,url,title,title_plain,content,excerpt,date")
    fun getNewDetail(@Query("id") id: String) : Call<NewDetail>

    @GET("/?oxwlxojflwblxbsapi=get_post&include=comments,comments_rank")
    fun getComments(@Query("id") id: String) : Call<NewDetail>

    @GET("/tucao/{id}")
    fun loadTucao(@Path("id") commentId: String) : Call<TucaoData>
}