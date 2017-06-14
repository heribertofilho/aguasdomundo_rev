package com.example.herib.guasdomundo

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction
import com.example.herib.guasdomundo.Models.Analise
import com.example.herib.guasdomundo.Models.Response


/**
 * Created by herib on 07/06/2017.
 */
interface AnalisarInterface {
    @LambdaFunction
    fun EscreverSensor(analise: Analise) : String
}
