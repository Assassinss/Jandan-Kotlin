package me.zsj.pretty_girl_kotlin.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 * @author zsj
 */
class NetUtils {

    companion object {

        @JvmStatic
        fun checkNet(context: Context): Boolean {
            val isWifiConnected = isWifiConnected(context)
            val isMobileConnected = isMobileConnected(context)

            if (!isWifiConnected && !isMobileConnected) {
                return false
            }
            return true
        }

        @JvmStatic
        fun isWifiConnected(context: Context): Boolean {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager

            val info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            if (info != null && info.isConnected) {
                return true
            }
            return false
        }

        @JvmStatic
        fun isMobileConnected(context: Context): Boolean {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager

            val info = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            if (info != null && info.isConnected) {
                return true
            }
            return false
        }

    }

}