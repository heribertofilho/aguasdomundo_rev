package com.example.herib.guasdomundo.Presenters

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.AsyncTask
import android.util.Log
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.example.herib.guasdomundo.AnalisarInterface
import com.example.herib.guasdomundo.Models.Analise
import com.example.herib.guasdomundo.Models.Response
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.io.File
import java.lang.Exception


/**
 * Created by herib on 07/06/2017.
 */

class PerguntasPresenter(private val mListener: PerguntasPresenter.PerguntasPresenterListener, private val context: Context) {
    private var mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context)

    interface PerguntasPresenterListener : TransferListener, OnCompleteListener<Location> {
        override fun onComplete(location: Task<Location>)

        override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long)

        override fun onStateChanged(id: Int, state: TransferState?)

        override fun onError(id: Int, ex: Exception?)

        fun finish(response: String)
    }

    fun enviarFoto(file: File) {
        // Create an instance of CognitoCachingCredentialsProvider
        val cognitoProvider = CognitoCachingCredentialsProvider(
                context, "us-west-2:418a0917-25ce-4018-ac3c-9d4266190fb0", Regions.US_WEST_2)
        val s3 = AmazonS3Client(cognitoProvider)
        val transferUtility = TransferUtility(s3, context)

        val observer = transferUtility.upload(
                "aguasdomundo", /* The bucket to upload to */
                file.name,      /* The key for the uploaded object */
                file           /* The file where the data to upload exists */
        )

        observer.setTransferListener(mListener)
    }

    fun enviarDados(analise: Analise) {
        mFusedLocationClient = null
        val cognitoProvider = CognitoCachingCredentialsProvider(
                context, "us-west-2:418a0917-25ce-4018-ac3c-9d4266190fb0", Regions.US_WEST_2)

        val factory = LambdaInvokerFactory(
                this.context,
                Regions.US_WEST_2,
                cognitoProvider)

        val analiseInterface = factory.build(AnalisarInterface::class.java)

        val runnable = Runnable {
            object : AsyncTask<Analise, Void, String>() {
                override fun doInBackground(vararg params: Analise): String? {
                    try {
                        return analiseInterface.EscreverSensor(params[0])
                    } catch (lfe: LambdaFunctionException) {
                        Log.e("ERROR", "Failed! " + lfe.details, lfe)
                        return null
                    }
                }

                override fun onPostExecute(result: String?) {
                    if (result == null)
                        return
                    mListener.finish(result)
                }
            }.execute(analise)
        }
        val thread = Thread(runnable)
        thread.start()
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        mFusedLocationClient.lastLocation
                .addOnCompleteListener(mListener)
    }
}