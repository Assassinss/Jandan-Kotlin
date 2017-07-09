package me.zsj.dan.data.executor

import pl.droidsonroids.gif.GifDrawable


/**
 * @author zsj
 */
interface GifCallback {
    fun onLoadingStart()
    fun onLoadFinished(gifDrawable: GifDrawable)
    fun onLoadFailed()
}