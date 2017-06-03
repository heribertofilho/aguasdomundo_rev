package com.example.herib.guasdomundo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.herib.guasdomundo.Adapters.ViewPagerAdapter
import com.example.herib.guasdomundo.Utils.SlidingTabLayout
import kotlinx.android.synthetic.main.activity_questionario.*


class QuestionarioActivity : AppCompatActivity() {
    var adapter: ViewPagerAdapter? = null
    var Titles = arrayOf<CharSequence>("Câmera", "Galeria")
    var Numboftabs = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionario)
        adapter = ViewPagerAdapter(supportFragmentManager, Titles, Numboftabs)

        // Assigning ViewPager View and setting the adapter
        pagerQuestionario.adapter = adapter

        // Assiging the Sliding Tab Layout View
        tabs!!.setDistributeEvenly(true) // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs!!.setCustomTabColorizer(SlidingTabLayout.TabColorizer { resources.getColor(R.color.colorAccent) })

        // Setting the ViewPager For the SlidingTabsLayout
        tabs!!.setViewPager(pagerQuestionario)

    }
}
