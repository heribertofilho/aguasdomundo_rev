package com.example.herib.guasdomundo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.herib.guasdomundo.Utils.CameraPreview
import kotlinx.android.synthetic.main.camera_layout.view.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by herib on 03/06/2017.
 */

class CameraFragment : Fragment() {
    val REQUEST_EXIT = 0
    var mCamera: Camera? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.camera_layout, container, false)

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            getCameraInstance()
            val mPreview = CameraPreview(activity, mCamera)
            view.cameraContainer.addView(mPreview)

            view.cameraButton.setOnClickListener({
                mCamera!!.takePicture(null, null, mPicture)
            })
        }

        view.imgClose.setOnClickListener({
            activity.finish()
        })

        return view
    }

    private val mPicture = PictureCallback { data, camera ->
        val pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE)
        if (pictureFile == null) {
            Log.d("ERROR", "Error creating media file, check storage permissions.")
            return@PictureCallback
        }

        try {
            val fos = FileOutputStream(pictureFile)
            fos.write(data)
            fos.close()
            val intent = Intent(context, PerguntasActivity::class.java)
            intent.putExtra("fotoUrl", pictureFile.toURI().toString())
            activity.startActivityForResult(intent, REQUEST_EXIT)
        } catch (e: FileNotFoundException) {
            Log.d("ERROR", "File not found: " + e.message)
        } catch (e: IOException) {
            Log.d("ERROR", "Error accessing file: " + e.message)
        }
    }

    private fun getOutputMediaFile(type: Int): File? {
        val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "AguasDoMundo")

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("AguasDoMundo", "failed to create directory")
                return null
            }
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val mediaFile: File
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg")
        } else {
            return null
        }

        return mediaFile
    }

    fun ativarCamera() {
        getCameraInstance()
        val mPreview = CameraPreview(activity, mCamera)
        view!!.cameraContainer.addView(mPreview)
    }

    fun getCameraInstance() {
        try {
            releaseCameraAndPreview()
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
        } catch (e: Exception) {
            Log.e(getString(R.string.app_name), "failed to open Camera")
            e.printStackTrace()
        }
    }

    private fun releaseCameraAndPreview() {
        if (mCamera != null) {
            mCamera!!.release()
            mCamera = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseCameraAndPreview()
    }
}
