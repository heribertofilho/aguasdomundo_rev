package br.heriberto.aguasdomundo.Interface

import br.heriberto.aguasdomundo.Models.Media
import com.google.android.gms.maps.model.LatLng

/**
 * Created by herib on 16/11/2017.
 */
interface RetrieveDataListener {
    fun finishedRetrieve(medias: ArrayList<Media>)
    fun retrieveData(latLng: LatLng)
}