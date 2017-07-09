package me.zsj.dan.data

import me.zsj.dan.model.*

/**
 * @author zsj
 */
interface DataCallback {

    fun onLoadFreshNews(freshNew: FreshNew?)

    fun onLoadBoringPics(picture: Picture?)

    fun onLoadJokes(joke: Joke?)

    fun onLoadNewDetail(newDetail: NewDetail?)

    fun onLoadTucao(tucaoData: TucaoData?)

    fun onLoadFailed(error: String?)
}