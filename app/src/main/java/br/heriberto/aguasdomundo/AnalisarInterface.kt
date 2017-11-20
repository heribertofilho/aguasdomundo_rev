package br.heriberto.aguasdomundo

import br.heriberto.aguasdomundo.Models.Analise
import br.heriberto.aguasdomundo.Models.LatLng
import br.heriberto.aguasdomundo.Models.Media
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction


/**
 * Created by herib on 07/06/2017.
 */
interface AnalisarInterface {
    @LambdaFunction
    fun EscreverSensor(analise: Analise): String

    @LambdaFunction
    fun retrieveData(latitude: Array<out LatLng>): ArrayList<Media>
}
