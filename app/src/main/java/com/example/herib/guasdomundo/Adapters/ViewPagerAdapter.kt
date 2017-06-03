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

    //This method return the fragment for the every position in the View Pager
    override fun getItem(position: Int): Fragment {

        if (position == 0)
        // if the position is 0 we are returning the First tab
        {
            val tab1 = CameraFragment()
            return tab1
        } else
        // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            val tab2 = GaleriaFragment()
            return tab2
        }


    }

    // This method return the titles for the Tabs in the Tab Strip

    override fun getPageTitle(position: Int): CharSequence {
        return Titles[position]
    }

    // This method return the Number of tabs for the tabs Strip

    override fun getCount(): Int {
        return NumbOfTabs
    }
}