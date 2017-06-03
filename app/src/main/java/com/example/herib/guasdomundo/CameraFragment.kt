package com.example.herib.guasdomundo

import android.hardware.Camera
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.herib.guasdomundo.Utils.CameraPreview
import kotlinx.android.synthetic.main.camera_layout.view.*

/**
 * Created by herib on 03/06/2017.
 */

class CameraFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater!!.inflate(R.layout.camera_layout, container, false)
        val c = getCameraInstance()
        if (c != null) {
            val mPreview = CameraPreview(activity.baseContext, c)
            view.cameraContainer.addView(mPreview)
        }
        view.imgClose.setOnClickListener({
            (MainActivity::updateNavigation)(activity as MainActivity, R.id.home)
        })
        return view
    }

    /** A safe way to get an instance of the Camera object.  */
    fun getCameraInstance(): Camera? {
        var c: Camera? = null
        try {
            c = Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
        }

        return c // returns null if camera is unavailable
    }
}
