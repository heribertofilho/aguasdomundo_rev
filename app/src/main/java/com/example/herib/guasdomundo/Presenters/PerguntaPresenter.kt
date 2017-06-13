package com.example.herib.guasdomundo.Presenters

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import java.io.File
import java.lang.Exception

/**
 * Created by herib on 07/06/2017.
 */

class PerguntasPresenter(private val mListener: PerguntasPresenter.PerguntasPresenterListener, private val context: Context) {
    interface PerguntasPresenterListener : TransferListener {
        override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long)

        override fun onStateChanged(id: Int, state: TransferState?)

        override fun onError(id: Int, ex: Exception?)
    }

    fun enviarFoto(file: File, superficie: Int, dureza: Int, peixe_apatico: Int, latitude: Double, longitude: Double) {
        // Create an instance of CognitoCachingCredentialsProvider
        val cognitoProvider = CognitoCachingCredentialsProvider(
                context, "us-west-2:418a0917-25ce-4018-ac3c-9d4266190fb0", Regions.US_WEST_2)
        val s3 = AmazonS3Client(cognitoProvider)
        val transferUtility = TransferUtility(s3, context)

        val myObjectMetadata = ObjectMetadata()

        val userMetadata = HashMap<String,String>()
        userMetadata.put("superficie", superficie.toString())
        userMetadata.put("dureza", dureza.toString())
        userMetadata.put("peixe_apatico", peixe_apatico.toString())
        userMetadata.put("latitude", latitude.toString())
        userMetadata.put("longitude", longitude.toString())

        myObjectMetadata.userMetadata = userMetadata

        val observer = transferUtility.upload(
                "aguasdomundo", /* The bucket to upload to */
                file.name,      /* The key for the uploaded object */
                file,           /* The file where the data to upload exists */
                myObjectMetadata
        )

        observer.setTransferListener(mListener)
    }
}