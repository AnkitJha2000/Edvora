package com.example.edvora.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.edvora.ui.fragments.NearestRideFragment
import com.example.edvora.ui.fragments.PastRideFragment
import com.example.edvora.ui.fragments.UpcomingRideFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(
    fragmentActivity
) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> NearestRideFragment()
            1 -> UpcomingRideFragment()
            else -> PastRideFragment()
        }



    }


}