package br.heriberto.aguasdomundo

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction
import br.heriberto.aguasdomundo.Models.Analise
import br.heriberto.aguasdomundo.Models.Response


/**
 * Created by herib on 07/06/2017.
 */
interface AnalisarInterface {
    @LambdaFunction
    fun EscreverSensor(analise: Analise) : String
}
