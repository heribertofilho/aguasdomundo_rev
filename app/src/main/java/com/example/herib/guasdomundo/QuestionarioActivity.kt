package com.example.herib.guasdomundo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.herib.guasdomundo.Adapters.ViewPagerAdapter
import com.example.herib.guasdomundo.Utils.SlidingTabLayout
import kotlinx.android.synthetic.main.activity_questionario.*


class QuestionarioActivity : AppCompatActivity() {
    var MY_PERMISSIONS_REQUEST_CAMERA: Int = 0
    var adapter: ViewPagerAdapter? = null
    var Titles = arrayOf<CharSequence>("Câmera", "Galeria")
    var Numboftabs = 2

    var cameraFragment: Fragment? = null
    var galeriaFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionario)

        cameraFragment = CameraFragment()
        galeriaFragment = GaleriaFragment()
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

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<out String>, @NonNull grantResults: IntArray) {
        for (i in 0..permissions.size - 1) {
            val permission: String = permissions[i]
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                Snackbar.make(findViewById(R.id.pagerQuestionario), getString(R.string.permissao_fotos), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.permitir), {
                            ActivityCompat.requestPermissions(this,
                                    arrayOf(Manifest.permission.CAMERA),
                                    MY_PERMISSIONS_REQUEST_CAMERA)
                        })
                        .show()
            } else {
                pagerQuestionario.adapter = null
                pagerQuestionario.adapter = adapter
            }
        }
    }
}
