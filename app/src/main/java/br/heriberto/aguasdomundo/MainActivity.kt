package br.heriberto.aguasdomundo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 0
    private val MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1
    private var mapsFragment: Fragment? = null
    private var questionarioFragment: Fragment? = null
    private var mSelectedItem: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapsFragment = MapsFragment()

        navigation.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener@ {
            menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    setFragment(menuItem, mapsFragment!!)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        val i = Intent(this, QuestionarioActivity::class.java)
                        startActivity(i)
                    } else {
                        Toast.makeText(this, "Não é possível fazer análise sem a localização do dispositivo.", Toast.LENGTH_LONG).show()
                    }
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
        navigation.selectedItemId = R.id.navigation_home

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    MY_PERMISSIONS_REQUEST_COARSE_LOCATION)

        }
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

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<out String>, @NonNull grantResults: IntArray) {
        for (i in 0 until permissions.size) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(container, getString(R.string.permissao_localizacao), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.permitir), {
                                ActivityCompat.requestPermissions(this,
                                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                                        MY_PERMISSIONS_REQUEST_COARSE_LOCATION)
                            })
                            .show()
                }

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(container, getString(R.string.permissao_localizacao), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.permitir), {
                                ActivityCompat.requestPermissions(this,
                                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                        MY_PERMISSIONS_REQUEST_FINE_LOCATION)
                            })
                            .show()
                }
            } else {
                val i : Intent = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        navigation.selectedItemId = R.id.navigation_home
        val menuSize: Int = navigation!!.menu.size() - 1
        for (i in 0..menuSize) {
            val menuItem = navigation!!.menu.getItem(i)
            menuItem.isChecked = (menuItem.itemId == R.id.navigation_home)
        }
    }

}
