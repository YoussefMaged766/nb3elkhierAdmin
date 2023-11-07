package com.devYoussef.nb3elkhieradmin.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(supportFragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(supportFragmentManager,lifecycle) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()
    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int) : Fragment {
        return mFragmentList[position]

    }
    fun addFragment(fragment: Fragment , title: String? = null){
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title.toString())
    }
    fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList[position]
    }

}