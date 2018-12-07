package me.zsj.dan.model


/**
 * @author zsj
 */
data class Picture(val status: String,
                   val count: Int,
                   val comments: ArrayList<Comment>)