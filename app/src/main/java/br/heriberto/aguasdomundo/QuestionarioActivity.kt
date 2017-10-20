package br.heriberto.aguasdomundo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import br.heriberto.aguasdomundo.Adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_questionario.*


class QuestionarioActivity : AppCompatActivity() {
    private val CAMERA: Int = 0
    private val WRITE: Int = 1
    private val READ: Int = 2
    private val EXIT: Int = 0
    private var adapter: ViewPagerAdapter? = null
    var cameraFragment: Fragment? = null
    var galeriaFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionario)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    CAMERA)
        }

        cameraFragment = CameraFragment()
        galeriaFragment = GaleriaFragment()

        setupViewPager()
        tabs.setupWithViewPager(viewpager)
    }

    private fun setupViewPager() {
        adapter = ViewPagerAdapter(supportFragmentManager)
        adapter!!.addFragment(cameraFragment!!, "Câmera")
        adapter!!.addFragment(galeriaFragment!!, "Galeria")
        viewpager.adapter = adapter
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<out String>, @NonNull grantResults: IntArray) {
        for (i in 0 until permissions.size) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(findViewById(R.id.viewpager), getString(R.string.permissao_fotos), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.permitir), {
                                ActivityCompat.requestPermissions(this,
                                        arrayOf(Manifest.permission.CAMERA),
                                        CAMERA)
                            })
                            .show()
                }
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(findViewById(R.id.viewpager), getString(R.string.galeria_negada), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.permitir), {
                                ActivityCompat.requestPermissions(this,
                                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                        READ)
                            })
                            .show()
                }
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(findViewById(R.id.viewpager), getString(R.string.permissao_salvar), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.permitir), {
                                ActivityCompat.requestPermissions(this,
                                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                        WRITE)
                            })
                            .show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Unit {
        if (requestCode == EXIT) {
            if (resultCode == Activity.RESULT_OK) {
                this.finish()
            }
        }
    }
}
