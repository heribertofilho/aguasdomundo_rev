package br.heriberto.aguasdomundo.Presenters

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import br.heriberto.aguasdomundo.AnalisarInterface
import br.heriberto.aguasdomundo.Interface.PerguntasListener
import br.heriberto.aguasdomundo.Models.Analise
import br.heriberto.aguasdomundo.PerguntasActivity
import br.heriberto.aguasdomundo.Utils.CountryCodeParser
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.S3ClientOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import java.io.File
import java.text.Normalizer


/**
 * Created by herib on 07/06/2017.
 */

class PerguntasPresenter(private val mListener: PerguntasListener, activity: PerguntasActivity, private val context: Context) : GoogleApiClient.OnConnectionFailedListener {
    private var mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context)
    private val mGoogleApiClient: GoogleApiClient = GoogleApiClient
            .Builder(context)
            .addApi(Places.GEO_DATA_API)
            .addApi(Places.PLACE_DETECTION_API)
            .enableAutoManage(activity, this)
            .build()

    @SuppressLint("MissingPermission")
    fun localizarUsuario() {
        val result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null)
        result.setResultCallback { likelyPlaces ->
            val placeLikelihood = likelyPlaces.first()
            val ccParser = CountryCodeParser()
            val parsed: String = ccParser.parseCountryCode(placeLikelihood.place.locale.country)
            var cityState = placeLikelihood.place.address.split(",")
            cityState = cityState[2].split("-")
            val state = cityState[1].trim()
            val city = stripAccents(cityState[0].trim())
            val latitude = placeLikelihood.place.latLng.latitude
            val longitude = placeLikelihood.place.latLng.longitude
            likelyPlaces.release()
            mListener.placesSuccess(parsed, state, city, latitude, longitude)
        }
    }

    fun stripAccents(s: String): String {
        var s = s
        s = Normalizer.normalize(s, Normalizer.Form.NFD)
        s = s.replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
        return s
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun enviarFoto(file: File, bucket: String) {
        // Create an instance of CognitoCachingCredentialsProvider
        val cognitoProvider = CognitoCachingCredentialsProvider(
                context, "us-west-2:418a0917-25ce-4018-ac3c-9d4266190fb0", Regions.US_WEST_2)
        val s3 = AmazonS3Client(cognitoProvider)
        val transferUtility = TransferUtility(s3, context)

        val observer = transferUtility.upload(
                bucket, /* The bucket to upload to */
                file.name, /* The key for the uploaded object */
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
            AsyncEscrever(analiseInterface, mListener).execute(analise)
        }
        val thread = Thread(runnable)
        thread.start()
    }

    class AsyncEscrever(private val analiseInterface: AnalisarInterface, private val mListener: PerguntasListener) : AsyncTask<Analise, Void, String>() {
        override fun doInBackground(vararg params: Analise): String? {
            try {
                return analiseInterface.EscreverSensor(params[0])
            } catch (lfe: LambdaFunctionException) {
                // Log.e("ERROR", "Failed! " + lfe.details, lfe)
                return null
            }
        }

        override fun onPostExecute(result: String?) {
            if (result == null)
                return
            mListener.finishData()
        }
    }
}