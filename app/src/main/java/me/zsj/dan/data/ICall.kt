package me.zsj.dan.data

import retrofit2.Call


/**
 * @author by zsj
 */

interface ICall<T> {

    fun createCall(arg: Any?): Call<T>
}