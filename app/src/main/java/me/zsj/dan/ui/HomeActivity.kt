package me.zsj.dan.ui

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import butterknife.bindView
import me.zsj.dan.R
import me.zsj.dan.ui.fragment.BoringFragment
import me.zsj.dan.ui.fragment.FreshNewsFragment
import me.zsj.dan.ui.fragment.JokeFragment
import me.zsj.dan.ui.fragment.MeiziFragment

/**
 * @author zsj
 */

class HomeActivity : AppCompatActivity() {

    private val TAG = "HomeActivity"

    private val pager: ViewPager by bindView(R.id.viewpager)
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val tabLayout: TabLayout by bindView(R.id.tabs)

    private var adapter: Adapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)

        setupViewPager()

        tabLayout.setupWithViewPager(pager)

        setupTabClickListener()
    }

    private fun setupViewPager() {
        adapter = Adapter(supportFragmentManager)
        adapter?.addFragment(FreshNewsFragment(), getString(R.string.fresh_news_title))
        adapter?.addFragment(BoringFragment.newInstance(), getString(R.string.boring_pic_title))
        adapter?.addFragment(MeiziFragment.newInstance(), getString(R.string.meizi_title))
        adapter?.addFragment(JokeFragment(), getString(R.string.joke_title))
        pager.adapter = adapter
        pager.offscreenPageLimit = 4
    }

    private fun setupTabClickListener() {
        (tabLayout.getChildAt(0) as LinearLayout).getChildAt(0).setOnClickListener {
            val fragment = adapter?.getItem(0) as FreshNewsFragment
            val recyclerView = fragment.view?.findViewById(R.id.news_list) as RecyclerView
            if (pager.currentItem == 0) {
                recyclerView.scrollToPosition(0)
            }
        }
        (tabLayout.getChildAt(0) as LinearLayout).getChildAt(1).setOnClickListener {
            val fragment = adapter?.getItem(1) as BoringFragment
            val recyclerView = fragment.view?.findViewById(R.id.pic_list) as RecyclerView
            if (pager.currentItem == 1) {
                recyclerView.scrollToPosition(0)
            }
        }
        (tabLayout.getChildAt(0) as LinearLayout).getChildAt(2).setOnClickListener {
            val fragment = adapter?.getItem(2) as MeiziFragment
            val recyclerView = fragment.view?.findViewById(R.id.pic_list) as RecyclerView
            if (pager.currentItem == 2) {
                recyclerView.scrollToPosition(0)
            }
        }
        (tabLayout.getChildAt(0) as LinearLayout).getChildAt(3).setOnClickListener {
            val fragment = adapter?.getItem(3) as JokeFragment
            val recyclerView = fragment.view?.findViewById(R.id.joke_list) as RecyclerView
            if (pager.currentItem == 3) {
                recyclerView.scrollToPosition(0)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.settings) {
            //go to settings activity
        }
        return super.onOptionsItemSelected(item)
    }
}