package com.example.herib.guasdomundo

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.herib.guasdomundo.Utils.CameraPreview
import kotlinx.android.synthetic.main.camera_layout.view.*


/**
 * Created by herib on 03/06/2017.
 */

class CameraFragment : Fragment() {
    var mCamera: Camera? = null
    var MY_PERMISSIONS_REQUEST_CAMERA: Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.camera_layout, container, false)

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_CAMERA)

        } else {
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

        //        val pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE)
//        if (pictureFile == null) {
//            Log.d("ERROR", "Error creating media file, check storage permissions: " + e.getMessage())
//            return@PictureCallback
//        }
//
//        try {
//            val fos = FileOutputStream(pictureFile)
//            fos.write(data)
//            fos.close()
//        } catch (e: FileNotFoundException) {
//            Log.d("ERROR", "File not found: " + e.message)
//        } catch (e: IOException) {
//            Log.d("ERROR", "Error accessing file: " + e.message)
//        }
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
