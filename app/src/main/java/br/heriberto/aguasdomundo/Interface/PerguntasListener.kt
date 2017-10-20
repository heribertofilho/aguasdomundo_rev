package br.heriberto.aguasdomundo.Interface

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState

/**
 * Created by herib on 10/10/2017.
 */
interface PerguntasListener : TransferListener {
    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long)

    override fun onStateChanged(id: Int, state: TransferState?)

    override fun onError(id: Int, ex: Exception?)

    fun finish(response: String)

    fun finishData()

    fun placesError()

    fun placesSuccess(pais: String, estado: String, cidade: String, latitude: Double, longitude: Double)
}