package me.zsj.dan.data

import me.zsj.dan.model.*

/**
 * @author zsj
 */
abstract class DataCallbackAdapter : DataCallback {

    override fun onLoadFreshNews(freshNew: FreshNew?) {

    }

    override fun onLoadBoringPics(picture: Picture?) {

    }

    override fun onLoadJokes(joke: Joke?) {

    }

    override fun onLoadNewDetail(newDetail: NewDetail?) {

    }

    override fun onLoadTucao(tucaoData: TucaoData?) {

    }

    override fun onLoadFailed(error: String?) {

    }
}