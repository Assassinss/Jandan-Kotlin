package me.zsj.dan

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import me.zsj.dan.utils.PreferenceManager

/**
 * @author zsj
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setTheme()
    }

    private fun setTheme() {
        val day = PreferenceManager.getBoolean(this, PreferenceManager.DAY, true)
        if (day) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}