package com.base.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * pagerAdapter will be replace FragmentStatePagerAdapter or FragmentStateAdapter
 * support the same recycle view
 * @see <a href="https://developer.android.com/develop/ui/views/animations/vp2-migration">link</a>
 */
abstract class BaseFragmentPagerAdapter : FragmentStateAdapter {

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)
    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(fragmentManager, lifecycle)

    private var fragments = mutableListOf<Fragment>()

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyItemChanged(fragments.size - 1)
    }

    fun addFragments(fragment: List<Fragment>) {
        fragments.addAll(fragment)
        notifyDataSetChanged()
    }

    fun getListFragment() = fragments

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}

