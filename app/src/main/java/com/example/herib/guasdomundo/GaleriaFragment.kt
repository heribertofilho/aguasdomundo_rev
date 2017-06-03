package com.example.herib.guasdomundo

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore.Images
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.herib.guasdomundo.Adapters.FotoAdapter
import com.example.herib.guasdomundo.Models.Foto
import kotlinx.android.synthetic.main.galeria_layout.view.*
import java.io.File


/**
 * Created by herib on 03/06/2017.
 */

class GaleriaFragment : Fragment() {
    var MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: Int = 1
    var layoutManager: RecyclerView.LayoutManager? = null
    var fotoAdapter: FotoAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater!!.inflate(R.layout.galeria_layout, container, false)
        view.imgClose.setOnClickListener({
            activity.finish()
        })

        if (isExternalStorageReadable()) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
            } else {
                view = readImagens(view)
            }
        }

        return view
    }

    fun readImagens(view: View): View {
        val IMAGES_PROJECTION: Array<out String> = arrayOf(
                Images.ImageColumns._ID,
                Images.ImageColumns.DATA,
                Images.ImageColumns.BUCKET_DISPLAY_NAME, //the album it in
                Images.ImageColumns.DATE_TAKEN,
                Images.ImageColumns.MIME_TYPE
        )
        val cursor = activity.applicationContext.contentResolver.query(Images.Media.EXTERNAL_CONTENT_URI, IMAGES_PROJECTION, null, null, Images.Media.DATE_TAKEN + " DESC")

        if (cursor.moveToFirst()) {
            val imageLocation = cursor.getString(1)
            val imageFile: File = File(imageLocation)
            if (imageFile.exists()) {
                val bm = BitmapFactory.decodeFile(imageLocation)
                view.imageView.setImageBitmap(bm)
            }
        }
        var fotos: ArrayList<Foto> = ArrayList()
        while (cursor.moveToNext()) {
            val imageLocation = cursor.getString(1)
            val imageTitle = cursor.getString(0)
            fotos.add(Foto(BitmapFactory.decodeFile(imageLocation), imageTitle))
        }
        fotoAdapter = FotoAdapter(activity.applicationContext, R.layout.foto_layout, fotos)
        view.recyclerView.adapter = fotoAdapter

        view.recyclerView.setHasFixedSize(true)
        layoutManager = GridLayoutManager(activity.applicationContext, 3)
        view.recyclerView.layoutManager = layoutManager

        return view
    }

    fun isExternalStorageReadable(): Boolean {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true
        }
        return false
    }
}