package me.zsj.dan.ui.fragment

import android.os.Bundle

/**
 * @author zsj
 */
open class BoringFragment : PictureFragment() {

    companion object {
        fun newInstance() : BoringFragment {
            val fragment = BoringFragment()
            val bundle = Bundle()
            bundle.putString("picture", "boring")
            fragment.arguments = bundle
            return fragment
        }
    }

}