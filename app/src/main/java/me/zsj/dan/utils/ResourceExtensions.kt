package me.zsj.dan.utils

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.IntegerRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.widget.Toast

/**
 * @author zsj
 */

inline fun Fragment.getColor(@ColorRes colorId: Int) = ContextCompat.getColor(activity, colorId)

inline fun Context.loadColor(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)

inline fun Context.dimensSize(@IntegerRes dimensId: Int) = resources.getDimensionPixelSize(dimensId)

inline fun Context.shortToast(string: String) =
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()