package com.ironelder.androidarchitecture.view.mainview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ironelder.androidarchitecture.R
import com.ironelder.androidarchitecture.common.*
import com.ironelder.androidarchitecture.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(
            this@MainActivity,
            R.layout.activity_main
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this@MainActivity
    }

    inner class PagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int {
            return TAB_MAX_CNT
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                1 -> MainFragment.newInstance(
                    NEWS
                )
                2 -> MainFragment.newInstance(
                    BOOK
                )
                3 -> MainFragment.newInstance(
                    MOVIE
                )
                else -> MainFragment.newInstance(
                    BLOG
                )
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                1 -> getString(R.string.tab_news)
                2 -> getString(R.string.tab_book)
                3 -> getString(R.string.tab_movie)
                else -> getString(R.string.tab_blog)
            }
        }
    }
}

