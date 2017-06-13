package com.example.herib.guasdomundo

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction
import com.example.herib.guasdomundo.Models.RequestClass


/**
 * Created by herib on 07/06/2017.
 */
interface AnalisarInterface {
    @LambdaFunction
    fun EscreverSensor(requestClass: RequestClass): RequestClass
}
