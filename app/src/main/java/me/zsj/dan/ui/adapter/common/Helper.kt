package me.zsj.dan.ui.adapter.common

import me.zsj.dan.model.Comment
import me.zsj.dan.model.TucaoData

class Helper private constructor() {

    companion object {

        @JvmStatic
        fun hasUserId(content: String): Boolean {
            return content.contains(Regex("<a href"))
        }

        @JvmStatic
        fun getComment(content: String, comments: ArrayList<Comment>): Comment? {
            comments.forEach {
                if (content.contains(Regex(it.id.toString()))) {
                    return it
                }
            }
            return null
        }

        @JvmStatic
        fun getTucao(content: String, comments: ArrayList<TucaoData.Tucao>): TucaoData.Tucao? {
            comments.forEach {
                if (content.contains(Regex(it.comment_ID.toString()))) {
                    return it
                }
            }
            return null
        }

        @JvmStatic
        fun removeHref(content: String): String {
            return if (!content.isEmpty()) {
                val startIndex = content.indexOf(":") + 1
                content.substring(startIndex, content.length).replace(" ", "")
            } else {
                content
            }
        }
    }

}
