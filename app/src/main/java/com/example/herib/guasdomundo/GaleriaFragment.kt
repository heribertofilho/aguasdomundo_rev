package com.example.herib.guasdomundo

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.herib.guasdomundo.Adapters.FotoAdapter
import com.example.herib.guasdomundo.Models.Foto
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.galeria_layout.view.*
import java.io.File


/**
 * Created by herib on 03/06/2017.
 */

class GaleriaFragment : Fragment() {
    val REQUEST_EXIT = 0
    var layoutManager: RecyclerView.LayoutManager? = null
    var fotoAdapter: FotoAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater!!.inflate(R.layout.galeria_layout, container, false)
        view.imgClose.setOnClickListener({
            activity.finish()
        })

        if (isExternalStorageReadable()) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                view = readImagens(view)
            }
        }

        return view
    }

    fun readImagens(view: View): View {
        val IMAGES_PROJECTION: Array<out String> = arrayOf(
                Images.ImageColumns._ID,
                Images.ImageColumns.DATA,
                Images.ImageColumns.BUCKET_DISPLAY_NAME,
                Images.ImageColumns.DATE_TAKEN,
                Images.ImageColumns.MIME_TYPE
        )

        val cursor = activity.applicationContext.contentResolver.query(Images.Media.EXTERNAL_CONTENT_URI, IMAGES_PROJECTION, null, null, Images.Media.DATE_TAKEN + " DESC")

        setImagemGrande(view, cursor)
        val fotos: ArrayList<Foto> = getArrayListFotos(cursor)

        fotoAdapter = FotoAdapter((activity as QuestionarioActivity), activity.applicationContext, getDensityDpiRecyclerView(), fotos)
        view.recyclerView.adapter = fotoAdapter

        view.recyclerView.setHasFixedSize(true)
        layoutManager = GridLayoutManager(activity.applicationContext, 3)
        view.recyclerView.layoutManager = layoutManager

        return view
    }

    fun setImagemGrande(view: View, cursor: Cursor) {
        if (cursor.moveToFirst()) {
            val imageTitle = cursor.getString(0)
            if (imageTitle.isNotEmpty()) {
                val imageURI = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageTitle)
                Picasso.with(context)
                        .load(imageURI)
                        .placeholder(R.color.material_grey_600)
                        .into(view.imageView)
                view.imageView.setOnClickListener({
                    val intent = Intent(context, PerguntasActivity::class.java)
                    intent.putExtra("fotoUrl", imageURI.toString())
                    startActivityForResult(intent, REQUEST_EXIT)
                })
            }
        }
    }

    fun getArrayListFotos(cursor: Cursor): ArrayList<Foto> {
        var fotos: ArrayList<Foto> = ArrayList()
        while (cursor.moveToNext()) {
            val imageLocation = cursor.getString(1)
            val imageTitle = cursor.getString(0)
            fotos.add(Foto(imageLocation, imageTitle))
        }
        return fotos
    }

    fun getDensityDpiRecyclerView(): Int {
        val metrics = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        return metrics.densityDpi - 8
    }

    fun isExternalStorageReadable(): Boolean {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true
        }
        return false
    }
}