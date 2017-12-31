package br.heriberto.aguasdomundo

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
import br.heriberto.aguasdomundo.Utils.CameraPreview
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
    var cameraOn: Boolean = false
    var fotos: Int = 0
    val data = Date()
    lateinit var aquifero:String
    lateinit var copo:String

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.camera_layout, container, false)

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            getCameraInstance()
            val mPreview = CameraPreview(activity, mCamera)
            view.cameraContainer.addView(mPreview)

            view.cameraButton.setOnClickListener({
                mCamera!!.takePicture(null, null, mPicture)
            })

            cameraOn = true
        }

        view.imgClose.setOnClickListener({
            activity.finish()
        })

        return view
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (!cameraOn)
                ativarCamera()
        }
    }

    private val mPicture = PictureCallback { data, _ ->
        val pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE)
        if (pictureFile == null) {
            Log.d("ERROR", "Error creating media file, check storage permissions.")
            return@PictureCallback
        }

        try {
            val fos = FileOutputStream(pictureFile)
            fos.write(data)
            fos.close()
            fotos++
            if(fotos == 2) {
                copo = pictureFile.toURI().toString()
                val intent = Intent(context, PerguntasActivity::class.java)
                intent.putExtra("fotoUrl", aquifero)
                intent.putExtra("fotoCopoUrl", copo)
                activity.startActivityForResult(intent, REQUEST_EXIT)
                activity.finish()
            } else {
                aquifero = pictureFile.toURI().toString()
            }
            mCamera!!.startPreview()
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

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(data)
        val mediaFile: File
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = File(mediaStorageDir.path + File.separator +
                    "IMG" + fotos + "_" + timeStamp + ".jpg")
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

    private fun getCameraInstance() {
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
            cameraOn = false
            mCamera!!.release()
            mCamera = null
            view!!.cameraContainer.removeAllViews()
        }
    }

    override fun onPause() {
        super.onPause()
        releaseCameraAndPreview()
    }

    override fun onStop() {
        super.onStop()
        releaseCameraAndPreview()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseCameraAndPreview()
    }
}
