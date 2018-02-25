package me.zsj.dan.data

/**
 * @author by zsj
 */
interface Callback {

    fun onSuccess(data: Any?)

    fun onFailure(t: Throwable?)
}