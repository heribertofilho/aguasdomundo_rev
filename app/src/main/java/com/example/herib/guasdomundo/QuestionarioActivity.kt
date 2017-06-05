package com.example.herib.guasdomundo

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.example.herib.guasdomundo.Adapters.ViewPagerAdapter
import com.example.herib.guasdomundo.Utils.SlidingTabLayout
import kotlinx.android.synthetic.main.activity_questionario.*
import java.io.ByteArrayOutputStream


class QuestionarioActivity : AppCompatActivity() {
    val MY_PERMISSIONS_REQUEST_CAMERA: Int = 0
    val MY_PERMISSIONS_REQUEST_WRITE: Int = 1
    val MY_PERMISSIONS_REQUEST_READ: Int = 2
    var adapter: ViewPagerAdapter? = null
    var Titles = arrayOf<CharSequence>("Câmera", "Galeria")
    var Numboftabs = 2

    var cameraFragment: Fragment? = null
    var galeriaFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionario)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_CAMERA)
        }

        cameraFragment = CameraFragment()
        galeriaFragment = GaleriaFragment()
        adapter = ViewPagerAdapter(supportFragmentManager, Titles, Numboftabs)

        pagerQuestionario.adapter = adapter
        tabs!!.setDistributeEvenly(true)
        tabs!!.setCustomTabColorizer(SlidingTabLayout.TabColorizer { resources.getColor(R.color.colorAccent) })
        tabs!!.setViewPager(pagerQuestionario)
    }

    // convert from bitmap to byte array
    fun getBytesFromBitmap(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.JPEG, 70, stream)
        return stream.toByteArray()
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<out String>, @NonNull grantResults: IntArray) {
        for (i in 0..permissions.size - 1) {
            val permission: String = permissions[i]
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(findViewById(R.id.pagerQuestionario), getString(R.string.permissao_fotos), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.permitir), {
                                ActivityCompat.requestPermissions(this,
                                        arrayOf(Manifest.permission.CAMERA),
                                        MY_PERMISSIONS_REQUEST_CAMERA)
                            })
                            .show()
                }
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(findViewById(R.id.pagerQuestionario), getString(R.string.galeria_negada), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.permitir), {
                                ActivityCompat.requestPermissions(this,
                                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                        MY_PERMISSIONS_REQUEST_READ)
                            })
                            .show()
                }
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(findViewById(R.id.pagerQuestionario), getString(R.string.permissao_salvar), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.permitir), {
                                ActivityCompat.requestPermissions(this,
                                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                        MY_PERMISSIONS_REQUEST_WRITE)
                            })
                            .show()
                }
            } else {
                pagerQuestionario.adapter = null
                pagerQuestionario.adapter = adapter
            }
        }
    }
}
