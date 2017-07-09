package me.zsj.dan.ui.fragment

import android.os.Bundle

/**
 * @author zsj
 */
class MeiziFragment : PictureFragment() {

    companion object {
        fun newInstance() : MeiziFragment {
            val fragment = MeiziFragment()
            val bundle = Bundle()
            bundle.putString("picture", "meizi")
            fragment.arguments = bundle
            return fragment
        }
    }

}