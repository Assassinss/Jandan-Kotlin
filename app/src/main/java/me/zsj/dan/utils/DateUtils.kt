package me.zsj.dan.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author zsj
 */
class DateUtils {

    companion object {

        @JvmStatic
        fun getRelativeTimeSpanString(date: Date) : String {
            val currentTime = Calendar.getInstance().time
            val diff = date.time - currentTime.time
            val days = diff / (1000 * 60 * 60 * 24)
            if (Math.abs(days) >= 1) {
                val sdf = SimpleDateFormat("MM-dd", Locale.CHINESE)
                return sdf.format(date)
            } else {
                return android.text.format.DateUtils.getRelativeTimeSpanString(date.time,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString().toLowerCase()
            }
        }
    }
}