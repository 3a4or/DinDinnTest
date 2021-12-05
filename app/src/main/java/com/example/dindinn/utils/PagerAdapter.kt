package com.example.dindinn.utils

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.ArrayList

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var mItems: List<Fragment>

    fun setItems(items: List<Fragment>) {
        mItems = items
        notifyDataSetChanged()
    }

    override fun createFragment(position: Int): Fragment {
        return try {
            mItems[position]
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalStateException("Unexpected position: $position", e)
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    init {
        mItems = ArrayList()
    }
}