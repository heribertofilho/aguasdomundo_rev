package com.example.herib.guasdomundo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mapsFragment: Fragment? = null
    private var questionarioFragment: Fragment? = null
    private var mSelectedItem: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapsFragment = MapsFragment()
        questionarioFragment = QuestionarioFragment()

        navigation.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener@ {
            menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    setFragment(menuItem, mapsFragment!!)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    setFragment(menuItem, questionarioFragment!!)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
        navigation.selectedItemId = R.id.navigation_dashboard
    }

    fun updateNavigation(id: Int) {
        navigation.selectedItemId = id
    }

    private fun setFragment(item: MenuItem, fragment: Fragment) {
        mSelectedItem = item.itemId
        val menuSize: Int = navigation!!.menu.size() - 1
        for (i in 0..menuSize) {
            val menuItem = navigation!!.menu.getItem(i)
            menuItem.isChecked = (menuItem.itemId == item.itemId)
        }

        val ft = supportFragmentManager.beginTransaction()
        if (supportFragmentManager.findFragmentById(fragment.id) == null)
            ft.add(R.id.container, fragment, fragment.tag)
        else
            ft.show(fragment)
        ft.commit()

        updateToolbar(fragment.id)
    }

    private fun updateToolbar(id: Int) {
        when (id) {
            mapsFragment!!.id -> {
                toolbar_galeria.visibility = GONE
                toolbarMaps.visibility = VISIBLE
                navigation.visibility = VISIBLE
            }
            questionarioFragment!!.id -> {
                navigation.visibility = GONE
                toolbarMaps.visibility = GONE
            }
        }
    }

}
