package com.example.herib.guasdomundo.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.herib.guasdomundo.CameraFragment
import com.example.herib.guasdomundo.GaleriaFragment


/**
 * Created by herib on 03/06/2017.
 */

class ViewPagerAdapter(fm: FragmentManager, var Titles: Array<CharSequence>, var NumbOfTabs: Int) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            val tab1 = CameraFragment()
            return tab1
        } else {
            val tab2 = GaleriaFragment()
            return tab2
        }


    }

    override fun getPageTitle(position: Int): CharSequence {
        return Titles[position]
    }

    override fun getCount(): Int {
        return NumbOfTabs
    }
}