package br.heriberto.aguasdomundo

import br.heriberto.aguasdomundo.Models.Analise
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction


/**
 * Created by herib on 07/06/2017.
 */
interface AnalisarInterface {
    @LambdaFunction
    fun EscreverSensor(analise: Analise): String
}
