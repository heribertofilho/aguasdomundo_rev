package com.example.herib.guasdomundo

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.herib.guasdomundo.Adapters.ViewPagerAdapter
import com.example.herib.guasdomundo.Utils.SlidingTabLayout
import kotlinx.android.synthetic.main.activity_questionario.view.*


class QuestionarioFragment : Fragment() {
    var adapter: ViewPagerAdapter? = null
    var tabs: SlidingTabLayout? = null
    var Titles = arrayOf<CharSequence>("Câmera", "Galeria")
    var Numboftabs = 2

    /**
     * The [ViewPager] that will host the section contents.
     */
    private var mViewPager: ViewPager? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.activity_questionario, container, false)

        adapter = ViewPagerAdapter(fragmentManager, Titles, Numboftabs)

        // Assigning ViewPager View and setting the adapter
        view.pagerQuestionario.adapter = adapter

        // Assiging the Sliding Tab Layout View
        view.tabs!!.setDistributeEvenly(true) // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        view.tabs!!.setCustomTabColorizer(SlidingTabLayout.TabColorizer { resources.getColor(R.color.colorAccent) })

        // Setting the ViewPager For the SlidingTabsLayout
        view.tabs!!.setViewPager(view.pagerQuestionario)
        return view
    }

    fun updateNavigation() {
        (MainActivity::updateNavigation)(activity as MainActivity, R.id.home)
    }
}
