package br.heriberto.aguasdomundo.Presenters

import android.content.Context
import android.os.AsyncTask
import br.heriberto.aguasdomundo.AnalisarInterface
import br.heriberto.aguasdomundo.Interface.RetrieveDataListener
import br.heriberto.aguasdomundo.MainActivity
import br.heriberto.aguasdomundo.Models.Media
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory
import com.amazonaws.regions.Regions
import com.google.android.gms.maps.model.LatLng
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

/**
 * Created by herib on 16/11/2017.
 */
class MainActivityPresenter(private val mListener: RetrieveDataListener, activity: MainActivity, private val context: Context) {
    fun solicitarDados(latlng: LatLng) {
        val latitude: Double = latlng.latitude
        val longitude: Double = latlng.longitude

        val cognitoProvider = CognitoCachingCredentialsProvider(
                context, "us-west-2:418a0917-25ce-4018-ac3c-9d4266190fb0", Regions.US_WEST_2)

        val factory = LambdaInvokerFactory(
                this.context,
                Regions.US_WEST_2,
                cognitoProvider)

        val analiseInterface = factory.build(AnalisarInterface::class.java)
        val laln = br.heriberto.aguasdomundo.Models.LatLng(latitude = latlng.latitude, longitude = latlng.longitude)
        val runnable = Runnable {
            AsyncRetrieve(analiseInterface, mListener).execute(laln)
        }
        val thread = Thread(runnable)
        thread.start()
    }

    class AsyncRetrieve(private val analiseInterface: AnalisarInterface, private val mListener: RetrieveDataListener) : AsyncTask<br.heriberto.aguasdomundo.Models.LatLng, Void, ArrayList<Media>>(), AnkoLogger {
        override fun doInBackground(vararg p0: br.heriberto.aguasdomundo.Models.LatLng): ArrayList<Media>? {
            try {
                return analiseInterface.retrieveData(p0)
            } catch (lfe: LambdaFunctionException) {
                debug("Failed! " + lfe.details)
            }
            return null
        }

        override fun onPostExecute(result: ArrayList<Media>?) {
            if (result == null)
                return
            mListener.finishedRetrieve(result)
        }
    }
}