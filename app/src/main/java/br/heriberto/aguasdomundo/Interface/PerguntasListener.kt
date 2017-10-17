package br.heriberto.aguasdomundo.Interface

import android.location.Location
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

/**
 * Created by herib on 10/10/2017.
 */
interface PerguntasListener : TransferListener, OnCompleteListener<Location> {
    override fun onComplete(location: Task<Location>)

    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long)

    override fun onStateChanged(id: Int, state: TransferState?)

    override fun onError(id: Int, ex: Exception?)

    fun finish(response: String)
}